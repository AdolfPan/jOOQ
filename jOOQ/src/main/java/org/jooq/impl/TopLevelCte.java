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

import static org.jooq.impl.CommonTableExpressionList.markTopLevelCteAndAccept;
import static org.jooq.impl.Tools.SimpleDataKey.DATA_TOP_LEVEL_CTE;

import java.util.function.Predicate;
import java.util.function.Supplier;

import org.jooq.Context;
// ...
import org.jooq.QueryPart;
import org.jooq.impl.ScopeMarker.ScopeContent;

/**
 * [#3607] [#8522] CTEs that need to be added to the top level CTE section.
 */
final class TopLevelCte extends QueryPartList<QueryPart> implements ScopeContent {

    boolean recursive;

    @Override
    public void accept(Context<?> ctx) {
        markTopLevelCteAndAccept(ctx, c -> super.accept(c));
    }

    @Override
    public final boolean declaresCTE() {
        return true;
    }






















}
