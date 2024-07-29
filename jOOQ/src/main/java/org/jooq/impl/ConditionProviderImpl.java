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

import static org.jooq.impl.DSL.noCondition;
import static org.jooq.impl.Names.N_CONDITION;

import java.util.Arrays;
import java.util.Collection;

import org.jooq.Condition;
import org.jooq.ConditionProvider;
import org.jooq.Context;
import org.jooq.Field;
import org.jooq.Operator;
import org.jooq.QueryPart;
import org.jooq.SQL;
import org.jooq.Select;
import org.jooq.impl.QOM.UProxy;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Lukas Eder
 */
final class ConditionProviderImpl extends AbstractField<Boolean> implements ConditionProvider, Condition, UProxy<Condition> {

    private Condition condition;

    ConditionProviderImpl() {
        this(null);
    }

    ConditionProviderImpl(Condition condition) {
        super(N_CONDITION, SQLDataType.BOOLEAN);

        this.condition = condition;
    }

    static final Condition extractCondition(Condition c) {

        // join(..).on(..).and(..) uses some identity tricks to keep the right
        // reference to the mutable ConditionProviderImpl instance, which we
        // must take into account here.
        return c instanceof ConditionProviderImpl cp ? cp.getWhere() : c;
    }


    @Nullable
    final Condition getWhereOrNull() {
        return hasWhere() ? condition : null;
    }

    @NotNull
    final Condition getWhere() {
        return hasWhere() ? extractCondition(condition) : noCondition();
    }

    final void setWhere(Condition newCondition) {
        this.condition = newCondition;
    }

    final boolean hasWhere() {
        return condition != null && !(condition instanceof NoCondition);
    }

    // -------------------------------------------------------------------------
    // ConditionProvider API
    // -------------------------------------------------------------------------

    @Override
    public final void addConditions(Condition conditions) {
        addConditions(Operator.AND, conditions);
    }

    @Override
    public final void addConditions(Condition... conditions) {
        addConditions(Operator.AND, conditions);
    }

    @Override
    public final void addConditions(Collection<? extends Condition> conditions) {
        addConditions(Operator.AND, conditions);
    }

    @Override
    public final void addConditions(Operator operator, Condition conditions) {
        if (hasWhere())
            setWhere(DSL.condition(operator, getWhere(), conditions));
        else
            setWhere(conditions);
    }

    @Override
    public final void addConditions(Operator operator, Condition... conditions) {
        addConditions(operator, Arrays.asList(conditions));
    }

    @Override
    public final void addConditions(Operator operator, Collection<? extends Condition> conditions) {
        if (!conditions.isEmpty()) {
            Condition c;

            if (conditions.size() == 1)
                c = conditions.iterator().next();
            else
                c = DSL.condition(operator, conditions);

            addConditions(operator, c);
        }
    }

    @Override
    public final void accept(Context<?> ctx) {
        ctx.visit(getWhere());
    }

    // -------------------------------------------------------------------------
    // Condition API
    // -------------------------------------------------------------------------

    @Override
    public final Condition and(Condition other) {
        return getWhere().and(other);
    }

    @Override
    public final Condition and(Field<Boolean> other) {
        return getWhere().and(other);
    }

    @Override
    public final Condition and(SQL sql) {
        return getWhere().and(sql);
    }

    @Override
    public final Condition and(String sql) {
        return getWhere().and(sql);
    }

    @Override
    public final Condition and(String sql, Object... bindings) {
        return getWhere().and(sql, bindings);
    }

    @Override
    public final Condition and(String sql, QueryPart... parts) {
        return getWhere().and(sql, parts);
    }

    @Override
    public final Condition andNot(Condition other) {
        return getWhere().andNot(other);
    }

    @Override
    public final Condition andNot(Field<Boolean> other) {
        return getWhere().andNot(other);
    }

    @Override
    public final Condition andExists(Select<?> select) {
        return getWhere().andExists(select);
    }

    @Override
    public final Condition andNotExists(Select<?> select) {
        return getWhere().andNotExists(select);
    }

    @Override
    public final Condition or(Condition other) {
        return getWhere().or(other);
    }

    @Override
    public final Condition or(Field<Boolean> other) {
        return getWhere().or(other);
    }

    @Override
    public final Condition or(SQL sql) {
        return getWhere().or(sql);
    }

    @Override
    public final Condition or(String sql) {
        return getWhere().or(sql);
    }

    @Override
    public final Condition or(String sql, Object... bindings) {
        return getWhere().or(sql, bindings);
    }

    @Override
    public final Condition or(String sql, QueryPart... parts) {
        return getWhere().or(sql, parts);
    }

    @Override
    public final Condition orNot(Condition other) {
        return getWhere().orNot(other);
    }

    @Override
    public final Condition orNot(Field<Boolean> other) {
        return getWhere().orNot(other);
    }

    @Override
    public final Condition orExists(Select<?> select) {
        return getWhere().orExists(select);
    }

    @Override
    public final Condition orNotExists(Select<?> select) {
        return getWhere().orNotExists(select);
    }

    @Override
    public final Condition xor(Condition other) {
        return getWhere().xor(other);
    }

    @Override
    public final Condition xor(Field<Boolean> other) {
        return getWhere().xor(other);
    }

    @Override
    public final Condition xor(SQL sql) {
        return getWhere().xor(sql);
    }

    @Override
    public final Condition xor(String sql) {
        return getWhere().xor(sql);
    }

    @Override
    public final Condition xor(String sql, Object... bindings) {
        return getWhere().xor(sql, bindings);
    }

    @Override
    public final Condition xor(String sql, QueryPart... parts) {
        return getWhere().xor(sql, parts);
    }

    @Override
    public final Condition xorNot(Condition other) {
        return getWhere().xorNot(other);
    }

    @Override
    public final Condition xorNot(Field<Boolean> other) {
        return getWhere().xorNot(other);
    }

    @Override
    public final Condition xorExists(Select<?> select) {
        return getWhere().xorExists(select);
    }

    @Override
    public final Condition xorNotExists(Select<?> select) {
        return getWhere().xorNotExists(select);
    }

    @Override
    public final Condition not() {
        return getWhere().not();
    }

    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final Condition $delegate() {
        return getWhere();
    }
}
