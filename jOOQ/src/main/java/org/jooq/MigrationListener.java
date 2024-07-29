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
package org.jooq;

import java.util.EventListener;

import org.jetbrains.annotations.ApiStatus.Experimental;

/**
 * A listener for {@link Migration} lifecycles.
 * <p>
 * This is EXPERIMENTAL functionality and subject to change in future jOOQ
 * versions.
 *
 * @author Lukas Eder
 */
@Experimental
public interface MigrationListener extends EventListener {

    /**
     * Invoked at the start of a {@link Migration}.
     *
     * @param ctx The context containing information about the migration.
     */
    default void migrationStart(MigrationContext ctx) {}

    /**
     * Invoked at the end of a {@link Migration}.
     *
     * @param ctx The context containing information about the migration.
     */
    default void migrationEnd(MigrationContext ctx) {}

    /**
     * Invoked at the start of a set of {@link Queries} that describe a single
     * version increment.
     *
     * @param ctx The context containing information about the migration.
     */
    default void queriesStart(MigrationContext ctx) {}

    /**
     * Invoked at the end of a set of {@link Queries} that describe a single
     * version increment.
     *
     * @param ctx The context containing information about the migration.
     */
    default void queriesEnd(MigrationContext ctx) {}

    /**
     * Invoked at the start of an individual {@link Query}.
     *
     * @param ctx The context containing information about the migration.
     */
    default void queryStart(MigrationContext ctx) {}

    /**
     * Invoked at the start of an individual {@link Query}.
     *
     * @param ctx The context containing information about the migration.
     */
    default void queryEnd(MigrationContext ctx) {}
}
