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

import org.jooq.Case;
import org.jooq.CaseConditionStep;
import org.jooq.CaseValueStep;
import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Select;
import org.jooq.impl.QOM.UTransient;

/**
 * A transient step in the construction of <code>CASE</code> statements.
 *
 * @author Lukas Eder
 */
final class CaseImpl implements Case, UTransient {

    CaseImpl() {}

    @Override
    public final <V> CaseValueStep<V> value(V value) {
        return value(Tools.field(value));
    }

    @Override
    public final <V> CaseValueStep<V> value(Field<V> value) {
        return new CaseSimpleValueStep<>(value);
    }

    @Override
    public final <T> CaseConditionStep<T> when(Condition condition, T result) {
        return when(condition, Tools.field(result));
    }

    @Override
    public final <T> CaseConditionStep<T> when(Condition condition, Field<T> result) {
        return new CaseSearched<>(condition, result);
    }

    @Override
    public final <T> CaseConditionStep<T> when(Condition condition, Select<? extends Record1<T>> result) {
        return when(condition, DSL.field(result));
    }

    @Override
    public final <T> CaseConditionStep<T> when(Field<Boolean> condition, T result) {
        return when(DSL.condition(condition), result);
    }

    @Override
    public final <T> CaseConditionStep<T> when(Field<Boolean> condition, Field<T> result) {
        return when(DSL.condition(condition), result);
    }

    @Override
    public final <T> CaseConditionStep<T> when(Field<Boolean> condition, Select<? extends Record1<T>> result) {
        return when(DSL.condition(condition), result);
    }
}
