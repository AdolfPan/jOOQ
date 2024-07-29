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

import java.util.List;

import org.jooq.ForeignKey;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.impl.QOM.Aliasable;

/**
 * @author Lukas Eder
 */
abstract class AbstractDelegatingTable<R extends Record>
extends
    AbstractTable<R>
{

    final AbstractTable<R> delegate;

    AbstractDelegatingTable(AbstractTable<R> delegate) {
        super(delegate.getOptions(), delegate.getQualifiedName(), delegate.getSchema());

        this.delegate = delegate;
    }

    abstract <O extends Record> AbstractDelegatingTable<O> construct(AbstractTable<O> newDelegate);

    @Override
    public final boolean declaresTables() {
        return true;
    }

    @Override
    public final Class<? extends R> getRecordType() {
        return delegate.getRecordType();
    }

    @Override
    public final Table<R> as(Name alias) {
        return construct(new TableAlias<>(delegate, alias));
    }

    @Override
    public final Table<R> as(Name alias, Name... fieldAliases) {
        return construct(new TableAlias<>(delegate, alias, fieldAliases));
    }

    @Override
    public final List<ForeignKey<R, ?>> getReferences() {
        return delegate.getReferences();
    }

    @Override
    final FieldsImpl<R> fields0() {
        return delegate.fields0();
    }

    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final Table<R> $aliased() {
        return construct((AbstractTable<R>) ((Aliasable<Table<R>>) delegate).$aliased());
    }

    @Override
    public final Name $alias() {
        return ((Aliasable<Table<R>>) delegate).$alias();
    }
}
