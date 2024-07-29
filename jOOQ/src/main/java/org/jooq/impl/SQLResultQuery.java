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
package org.jooq.impl;

import static org.jooq.impl.Tools.EMPTY_FIELD;
import static org.jooq.impl.Tools.isEmpty;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Collection;

import org.jooq.Clause;
import org.jooq.Configuration;
import org.jooq.Context;
import org.jooq.Field;
import org.jooq.QueryPart;
import org.jooq.QueryPartInternal;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.SQL;
import org.jooq.Table;
import org.jooq.impl.QOM.UEmpty;
import org.jooq.impl.QOM.UEmptyQuery;

/**
 * A plain SQL query that returns results
 *
 * @author Lukas Eder
 */
final class SQLResultQuery extends AbstractResultQuery<Record> implements UEmptyQuery {

    private final SQL delegate;

    SQLResultQuery(Configuration configuration, SQL delegate) {
        super(configuration);

        this.delegate = delegate;
    }

    // ------------------------------------------------------------------------
    // ResultQuery API
    // ------------------------------------------------------------------------

    @Override
    public final void accept(Context<?> ctx) {
        switch (ctx.family()) {






            default:
                ctx.visit(delegate);
                break;
        }
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        if (delegate instanceof QueryPartInternal q)
            return q.clauses(ctx);

        return null;
    }

    @Override
    final Table<? extends Record1<Integer>> getTable0() {
        return null;
    }

    @Override
    public final Class<? extends Record> getRecordType0() {
        return RecordImplN.class;
    }

    @Override
    public final Field<?>[] getFields(ThrowingSupplier<? extends ResultSetMetaData, SQLException> rs) throws SQLException {
        Field<?>[] result = getFields();

        if (!isEmpty(result))
            return result;
        else
            return new MetaDataFieldProvider(configuration(), rs.get()).getFields();
    }

    @Override
    public final Field<?>[] getFields() {
        Collection<? extends Field<?>> coerce = coerce();

        if (!isEmpty(coerce))
            return coerce.toArray(EMPTY_FIELD);
        else
            return EMPTY_FIELD;
    }
}
