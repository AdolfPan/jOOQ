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
package org.jooq;

// ...
// ...
// ...
import static org.jooq.SQLDialect.CUBRID;
// ...
import static org.jooq.SQLDialect.DUCKDB;
// ...
import static org.jooq.SQLDialect.FIREBIRD;
import static org.jooq.SQLDialect.H2;
// ...
// ...
import static org.jooq.SQLDialect.MARIADB;
// ...
import static org.jooq.SQLDialect.MYSQL;
// ...
import static org.jooq.SQLDialect.POSTGRES;
// ...
// ...
// ...
import static org.jooq.SQLDialect.SQLITE;
// ...
// ...
// ...
import static org.jooq.SQLDialect.TRINO;
// ...
import static org.jooq.SQLDialect.YUGABYTEDB;

import java.util.Collection;

import org.jetbrains.annotations.NotNull;

/**
 * This type is used for the window function DSL API.
 * <p>
 * Example: <pre><code>
 * field.firstValue()
 *      .ignoreNulls()
 *      .over()
 *      .partitionBy(AUTHOR_ID)
 *      .orderBy(PUBLISHED_IN.asc())
 *      .rowsBetweenUnboundedPreceding()
 *      .andUnboundedFollowing()
 * </code></pre>
 *
 * @param <T> The function return type
 * @author Lukas Eder
 */
public interface WindowPartitionByStep<T> extends WindowOrderByStep<T> {

    /**
     * Add a <code>PARTITION BY</code> clause to the window functions.
     */
    @NotNull
    @Support({ CUBRID, DUCKDB, FIREBIRD, H2, MARIADB, MYSQL, POSTGRES, SQLITE, TRINO, YUGABYTEDB })
    WindowOrderByStep<T> partitionBy(GroupField... fields);

    /**
     * Add a <code>PARTITION BY</code> clause to the window functions.
     */
    @NotNull
    @Support({ CUBRID, DUCKDB, FIREBIRD, H2, MARIADB, MYSQL, POSTGRES, SQLITE, TRINO, YUGABYTEDB })
    WindowOrderByStep<T> partitionBy(Collection<? extends GroupField> fields);

    /**
     * Add a <code>PARTITION BY 1</code> clause to the window functions, where
     * such a clause is required by the syntax of an RDBMS.
     *
     * @deprecated - 3.10 - [#6427] - This synthetic clause is no longer
     *             supported, use {@link #partitionBy(GroupField...)} instead, or
     *             omit the clause entirely.
     */
    @Deprecated(forRemoval = true, since = "3.10")
    @NotNull
    @Support({ CUBRID, DUCKDB, FIREBIRD, H2, MARIADB, MYSQL, POSTGRES, SQLITE, TRINO, YUGABYTEDB })
    WindowOrderByStep<T> partitionByOne();

}
