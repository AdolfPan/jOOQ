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

import static org.jooq.SQLDialect.MARIADB;
import static org.jooq.SQLDialect.MYSQL;
// ...
import static org.jooq.SQLDialect.SQLITE;
import static org.jooq.SQLDialect.TRINO;
import static org.jooq.impl.Keywords.K_ABSENT;
import static org.jooq.impl.Keywords.K_NULL;
import static org.jooq.impl.Keywords.K_ON;
import static org.jooq.impl.QOM.JSONOnNull.ABSENT_ON_NULL;
import static org.jooq.impl.QOM.JSONOnNull.NULL_ON_NULL;

import java.util.Set;

import org.jooq.Context;
import org.jooq.SQLDialect;
import org.jooq.impl.QOM.JSONOnNull;
import org.jooq.impl.QOM.UTransient;

/**
 * @author Lukas Eder
 */
final class JSONNull extends AbstractQueryPart implements SimpleQueryPart, UTransient {
    static final Set<SQLDialect> NO_SUPPORT_ABSENT_ON_NULL = SQLDialect.supportedBy(MARIADB, MYSQL, SQLITE);
    static final Set<SQLDialect> NO_SUPPORT_NULL_ON_EMPTY  = SQLDialect.supportedBy(TRINO);

    final JSONOnNull             type;

    JSONNull(JSONOnNull type) {
        this.type = type;
    }

    @Override
    public final boolean rendersContent(Context<?> ctx) {
        return !NO_SUPPORT_ABSENT_ON_NULL.contains(ctx.dialect()) && type != null;
    }

    @Override
    public final void accept(Context<?> ctx) {
        switch (ctx.family()) {










            default:
                if (!NO_SUPPORT_ABSENT_ON_NULL.contains(ctx.dialect()))
                    if (type == NULL_ON_NULL)
                        ctx.visit(K_NULL).sql(' ').visit(K_ON).sql(' ').visit(K_NULL);
                    else if (type == ABSENT_ON_NULL)
                        ctx.visit(K_ABSENT).sql(' ').visit(K_ON).sql(' ').visit(K_NULL);

                break;
        }
    }
}
