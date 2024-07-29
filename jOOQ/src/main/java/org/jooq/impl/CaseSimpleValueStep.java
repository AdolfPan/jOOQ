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

import java.util.LinkedHashMap;
import java.util.Map;

import org.jooq.CaseValueStep;
import org.jooq.CaseWhenStep;
import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Select;
import org.jooq.impl.QOM.UTransient;

/**
 * A transient step in the construction of <code>CASE</code> statements.
 *
 * @author Lukas Eder
 */
final class CaseSimpleValueStep<V> implements CaseValueStep<V>, UTransient {

    private final Field<V> value;

    CaseSimpleValueStep(Field<V> value) {
        this.value = value;
    }

    @Override
    public final <T> CaseWhenStep<V, T> when(V compareValue, T result) {
        return when(Tools.field(compareValue, value), Tools.field(result));
    }

    @Override
    public final <T> CaseWhenStep<V, T> when(V compareValue, Field<T> result) {
        return when(Tools.field(compareValue, value), result);
    }

    @Override
    public final <T> CaseWhenStep<V, T> when(V compareValue, Select<? extends Record1<T>> result) {
        return when(Tools.field(compareValue, value), DSL.field(result));
    }

    @Override
    public final <T> CaseWhenStep<V, T> when(Field<V> compareValue, T result) {
        return when(compareValue, Tools.field(result));
    }

    @Override
    public final <T> CaseWhenStep<V, T> when(Field<V> compareValue, Field<T> result) {
        return new CaseSimple<>(value, compareValue, result);
    }

    @Override
    public final <T> CaseWhenStep<V, T> when(Field<V> compareValue, Select<? extends Record1<T>> result) {
        return when(compareValue, DSL.field(result));
    }

    @Override
    public final <T> CaseWhenStep<V, T> mapValues(Map<V, T> values) {
        Map<Field<V>, Field<T>> fields = new LinkedHashMap<>();
        values.forEach((k, v) -> fields.put(Tools.field(k, value), Tools.field(v)));
        return mapFields(fields);
    }

    @Override
    public final <T> CaseWhenStep<V, T> mapFields(Map<? extends Field<V>, ? extends Field<T>> fields) {
        return new CaseSimple<>(value, fields);
    }
}
