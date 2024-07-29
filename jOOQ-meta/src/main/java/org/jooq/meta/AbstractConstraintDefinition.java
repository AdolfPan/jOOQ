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
package org.jooq.meta;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Lukas Eder
 */
public class AbstractConstraintDefinition extends AbstractDefinition implements ConstraintDefinition {

    private final TableDefinition table;
    private final boolean         enforced;

    public AbstractConstraintDefinition(SchemaDefinition schema, TableDefinition table, String name, boolean enforced) {
        this(schema, table, name, enforced, null, null);
    }

    public AbstractConstraintDefinition(SchemaDefinition schema, TableDefinition table, String name, boolean enforced, String comment, String source) {
        super(schema.getDatabase(), schema, null, name, comment, null, source);

        this.table = table;
        this.enforced = enforced;
    }

    @Override
    public List<Definition> getDefinitionPath() {
        List<Definition> result = new ArrayList<>();

        result.addAll(getSchema().getDefinitionPath());
        result.add(this);

        return result;
    }

    @Override
    public TableDefinition getTable() {
        return table;
    }

    @Override
    public boolean enforced() {
        return enforced;
    }
}
