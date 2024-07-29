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

import org.jetbrains.annotations.ApiStatus.Experimental;
import org.jetbrains.annotations.NotNull;

/**
 * The experimental migrations API.
 * <p>
 * This is EXPERIMENTAL functionality and subject to change in future jOOQ
 * versions.
 * @author Lukas Eder
 */
@Experimental
public interface Migrations extends Scope {

    /**
     * Initialise a {@link File}
     * <p>
     * This is EXPERIMENTAL functionality and subject to change in future jOOQ
     * versions.
     */
    @Experimental
    @NotNull
    File file(String path, String content, ContentType type);

    /**
     * Initialise a {@link Version}.
     * <p>
     * This is EXPERIMENTAL functionality and subject to change in future jOOQ
     * versions.
     */
    @Experimental
    @NotNull
    Version version(String id);

    /**
     * Initialise an empty {@link Commits} graph.
     * <p>
     * This is EXPERIMENTAL functionality and subject to change in future jOOQ
     * versions.
     */
    @Experimental
    @NotNull
    Commits commits();

    /**
     * Retrieve the {@link History} of previously installed {@link Version}
     * objects from the database.
     * <p>
     * This is EXPERIMENTAL functionality and subject to change in future jOOQ
     * versions.
     */
    @Experimental
    @NotNull
    History history();

    /**
     * Create a migration from the currently installed version to a new version.
     * <p>
     * This is EXPERIMENTAL functionality and subject to change in future jOOQ
     * versions.
     */
    @Experimental
    @NotNull
    Migration migrateTo(Commit to);

}
