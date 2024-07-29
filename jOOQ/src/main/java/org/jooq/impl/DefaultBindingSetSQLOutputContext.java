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

import static org.jooq.ContextConverter.scoped;

import java.sql.SQLOutput;

import org.jooq.BindingSetSQLOutputContext;
import org.jooq.Converter;
import org.jooq.ExecuteContext;

/**
 * @author Lukas Eder
 */
class DefaultBindingSetSQLOutputContext<U> extends AbstractExecuteScope implements BindingSetSQLOutputContext<U>, ResourceManagingScopeTrait {

    private final SQLOutput output;
    private final U         value;

    DefaultBindingSetSQLOutputContext(ExecuteContext ctx, SQLOutput output, U value) {
        super(ctx);

        this.output = output;
        this.value = value;
    }

    @Override
    public final SQLOutput output() {
        return output;
    }

    @Override
    public final U value() {
        return value;
    }

    @Override
    public final <T> BindingSetSQLOutputContext<T> convert(Converter<? extends T, ? super U> converter) {
        return new DefaultBindingSetSQLOutputContext<>(ctx, output, scoped(converter).to(value, converterContext()));
    }

    @Override
    public String toString() {
        return "DefaultBindingSetSQLOutputContext [value=" + value + "]";
    }
}
