/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Other licenses:
 * -----------------------------------------------------------------------------
 * Commercial licenses for this work are available. These replace the above
 * Apache-2.0 license and offer limited warranties, support, maintenance, and
 * commercial database integrations.
 *
 * For more information, please visit: https://www.jooq.org/legal/licensing
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */
package org.jooq.meta.sqlite;

import static org.jooq.conf.ParseWithMetaLookups.THROW_ON_FAILURE;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.lower;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.selectOne;
import static org.jooq.impl.QOM.GenerationOption.STORED;
import static org.jooq.impl.QOM.GenerationOption.VIRTUAL;
import static org.jooq.meta.sqlite.sqlite_master.SQLiteMaster.SQLITE_MASTER;
import static org.jooq.tools.StringUtils.isBlank;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.jooq.Configuration;
import org.jooq.Field;
import org.jooq.Query;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.TableOptions.TableType;
import org.jooq.exception.DataDefinitionException;
import org.jooq.impl.DSL;
import org.jooq.impl.ParserException;
import org.jooq.meta.AbstractTableDefinition;
import org.jooq.meta.ColumnDefinition;
import org.jooq.meta.DefaultColumnDefinition;
import org.jooq.meta.DefaultDataTypeDefinition;
import org.jooq.meta.SchemaDefinition;
import org.jooq.meta.sqlite.sqlite_master.SQLiteMaster;
import org.jooq.tools.JooqLogger;

/**
 * SQLite table definition
 *
 * @author Lukas Eder
 */
public class SQLiteTableDefinition extends AbstractTableDefinition {

    private static final JooqLogger log = JooqLogger.getLogger(SQLiteTableDefinition.class);
    private Table<?>                interpretedTable;

    public SQLiteTableDefinition(SchemaDefinition schema, String name, String comment) {
        super(schema, name, comment);
    }

    public SQLiteTableDefinition(SchemaDefinition schema, String name, String comment, TableType tableType, String source) {
        super(schema, name, comment, tableType, source);
    }

    Table<?> interpretedTable() {
        if (interpretedTable == null) {
            try {
                Configuration c = create().configuration().derive();
                c.settings().withParseWithMetaLookups(THROW_ON_FAILURE);
                Query query = create().parser().parseQuery(getSource());

                for (Table<?> t : create().meta(query).getTables(getInputName()))
                    return interpretedTable = t;
            }
            catch (ParserException e) {
                log.info("Cannot parse SQL: " + getSource(), e);
            }
            catch (DataDefinitionException e) {
                log.info("Cannot interpret SQL: " + getSource(), e);
            }
        }

        return interpretedTable;
    }

    @Override
    public List<ColumnDefinition> getElements0() throws SQLException {
        List<ColumnDefinition> result = new ArrayList<>();

        Field<String> fName = field(name("name"), String.class);
        Field<String> fType = field(name("type"), String.class);
        Field<Boolean> fNotnull = field(name("notnull"), boolean.class);
        Field<String> fDefaultValue = field(name("dflt_value"), String.class);
        Field<Integer> fPk = field(name("pk"), int.class);
        Field<Integer> fHidden = field(name("hidden"), int.class);

        for (Record record : create()
            .select(fName, fType, fNotnull, fDefaultValue, fPk, fHidden)
            .from("pragma_table_xinfo({0})", inline(getName()))
            // 0 = ordinary column
            // 2 = generated column (virtual)
            // 3 = generated column (stored)
            .where("hidden in (0, 2, 3)")
        ) {

            String name = record.get(fName);
            String dataType = record.get(fType)
                                    .replaceAll("\\(\\d+(\\s*,\\s*\\d+)?\\)", "");
            Number precision = parsePrecision(record.get(fType));
            Number scale = parseScale(record.get(fType));

            // SQLite identities are primary keys whose tables are mentioned in
            // sqlite_sequence
            int pk = record.get(fPk);
            int hidden = record.get(fHidden);
            boolean identity = false;
            boolean generated = hidden == 2 || hidden == 3;
            String generator = null;

            // [#8278] [#11172] SQLite doesn't store the data type for all views or virtual tables
            if (isBlank(dataType) || "other".equals(dataType)) {
                Table<?> t = interpretedTable();

                if (t != null) {
                    Field<?> f = t.field(name);

                    if (f != null) {
                        dataType = f.getDataType().getName();
                        precision = f.getDataType().precision();
                        scale = f.getDataType().scale();
                    }
                }
            }

            if (generated) {

                // SQLite's type affinity feature gets in the way of correctly
                // interpreting data types when the column is computed, see
                // https://sqlite.org/forum/forumpost/8ebb993012
                // This is ignoring typos, because things like "generaated aways"
                // also work in SQLite, as long as there's an "as" keyword...
                dataType = dataType.replaceAll("(?i:\\s*generated\\s+always\\s*)", "");











            }

            identityCheck:
            if (pk > 0) {

                // [#14656] Explicit WITHOUT ROWID clauses mean there's no identity
                if (getSource().matches("(?s:\\.*(?i:\\bwithout\\s+rowid\\b).*)"))
                    break identityCheck;

                // [#6854] sqlite_sequence only contains identity information once a table contains records.
                identity |= ((SQLiteDatabase) getDatabase()).existsSqliteSequence() && create()
                    .fetchOne("select count(*) from sqlite_sequence where name = ?", getName())
                    .get(0, Boolean.class);

                // [#6854] If sqlite_sequence didn't contain an entry and the table is empty...
                if (!identity && !create().fetchExists(selectOne().from("{0}", DSL.name(getName())))) {

                    // [#14656] The presence of AUTOINCREMENT means there must be an identity (SQLite rejects it, otherwise)
                    identity = getSource().matches("(?s:.*\\b" + getName() + "\\b[^,]*(?i:\\bautoincrement\\b)[^,]*.*)");
                }

                // [#14656] Finally, a table can be non-empty, not have an autoincrement token, not have an entry in
                //          sqlite_sequence, but still have an identity (!) when there's an INTEGER PRIMARY KEY column.
                //          e.g. CREATE TABLE t (i INTEGER NOT NULL, PRIMARY KEY (i)) And then, there's also the possibility
                //          of running into quirks when using DESC vs ASC, see
                //          https://www.sqlite.org/lang_createtable.html#rowids_and_the_integer_primary_key
                if (!identity) {}
            }

            DefaultDataTypeDefinition type = new DefaultDataTypeDefinition(
                getDatabase(),
                getSchema(),
                dataType,
                precision,
                precision,
                scale,
                !record.get(fNotnull),
                record.get(fDefaultValue)
            )
                .generatedAlwaysAs(generator)
                .generationOption(hidden == 2 ? VIRTUAL : hidden == 3 ? STORED : null);

            result.add(new DefaultColumnDefinition(
                getDatabase().getTable(getSchema(), getName()),
                name,
                result.size() + 1,
                type,
                identity,
                null
            ));
        }

        return result;
    }
}
