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

import org.jooq.Context;
import org.jooq.Keyword;
import org.jooq.Name;
import org.jooq.QueryPart;
import org.jooq.Record;
// ...
import org.jooq.Table;
// ...

import org.jetbrains.annotations.NotNull;

/**
 * @author Lukas Eder
 */
final class HintedTable<R extends Record>
extends
    AbstractDelegatingTable<R>
implements
    QOM.HintedTable<R>
{

    private final Keyword             keywords;
    private final QueryPartList<Name> arguments;

    HintedTable(AbstractTable<R> delegate, String keywords, String... arguments) {
        this(delegate, keywords, new QueryPartList<>(Tools.names(arguments)));
    }

    HintedTable(AbstractTable<R> delegate, String keywords, QueryPartList<Name> arguments) {
        this(delegate, DSL.keyword(keywords), arguments);
    }

    HintedTable(AbstractTable<R> delegate, Keyword keywords, String... arguments) {
        this(delegate, keywords, new QueryPartList<>(Tools.names(arguments)));
    }

    HintedTable(AbstractTable<R> delegate, Keyword keywords, QueryPartList<Name> arguments) {
        super(delegate);

        this.keywords = keywords;
        this.arguments = arguments;
    }

    @Override
    final <O extends Record> HintedTable<O> construct(AbstractTable<O> newDelegate) {
        return new HintedTable<>(newDelegate, keywords, arguments);
    }

    // ------------------------------------------------------------------------
    // XXX: QueryPart API
    // ------------------------------------------------------------------------

    @Override
    public final void accept(Context<?> ctx) {
        ctx.visit(delegate)
            .sql(' ').visit(keywords)
            .sql(" (").visit(arguments)
            .sql(')');
    }

    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------


    @Override
    public final Table<R> $table() {
        return delegate;
    }

    @Override
    public final <O extends Record> HintedTable<O> $table(Table<O> newTable) {
        return construct((AbstractTable<O>) newTable);
    }


















}
