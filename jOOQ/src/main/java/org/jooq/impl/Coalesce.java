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
 * Apache-2.0 and offer limited warranties, support, maintenance, and commercial
 * database integrations.
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

import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.Names.N_COALESCE;
import static org.jooq.impl.SQLDataType.OTHER;
import static org.jooq.impl.Tools.EMPTY_FIELD;
import static org.jooq.impl.Tools.anyNotNull;

import java.util.Collection;

import org.jooq.Context;
import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.Function1;
import org.jooq.impl.QOM.UnmodifiableList;

/**
 * @author Lukas Eder
 */
final class Coalesce<T> extends AbstractField<T> implements QOM.Coalesce<T> {

    private final Field<T>[] fields;

    Coalesce(Collection<? extends Field<?>> fields) {
        this(fields.toArray(EMPTY_FIELD));
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    Coalesce(Field<?>[] fields) {
        this(fields, anyNotNull((DataType) OTHER, fields));
    }

    @SuppressWarnings({ "unchecked" })
    Coalesce(Field<?>[] fields, DataType<T> type) {
        super(N_COALESCE, type);

        this.fields = (Field[]) fields;
    }

    @Override
    public final void accept(Context<?> ctx) {
        if (fields.length == 0) {
            ctx.visit(inline(null, getDataType()));
        }
        else if (fields.length == 1) {
            ctx.visit(fields[0]);
        }
        else {
            switch (ctx.family()) {













                case DERBY: {
                    // [#13601] Workaround for https://issues.apache.org/jira/browse/DERBY-7139
                    ctx.visit(DSL.function(N_COALESCE, getDataType(),
                        Tools.map(fields, f -> f.getDataType().isBoolean() ? new ParenthesisedField<>(f) : f, Field[]::new)
                    ));
                    break;
                }

                default: {
                    ctx.visit(DSL.function(N_COALESCE, getDataType(), fields));
                    break;
                }
            }
        }
    }

    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final UnmodifiableList<? extends Field<T>> $arg1() {
        return QOM.unmodifiable(fields);
    }

    @Override
    public final Function1<? super UnmodifiableList<? extends Field<T>>, ? extends QOM.Coalesce<T>> $constructor() {
        return l -> l.isEmpty()
            ? new Coalesce<>(EMPTY_FIELD, getDataType())
            : new Coalesce<>(l.toArray(EMPTY_FIELD));
    }
}
