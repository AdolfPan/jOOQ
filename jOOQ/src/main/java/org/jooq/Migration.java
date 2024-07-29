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

import org.jooq.exception.DataMigrationException;
import org.jooq.exception.DataMigrationVerificationException;

import org.jetbrains.annotations.Blocking;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.ApiStatus.Experimental;

/**
 * An executable migration between two {@link Commit} instances.
 * <p>
 * This type is a {@link Scope} with independent lifecycle and its own
 * {@link #data()} map.
 * <p>
 * This is EXPERIMENTAL functionality and subject to change in future jOOQ
 * versions.
 *
 * @author Lukas Eder
 */
@Experimental
public interface Migration extends Scope {

    /**
     * The version that is being migrated from.
     */
    Commit from();

    /**
     * The version that is being migrated to.
     */
    Commit to();

    /**
     * The queries that are executed by the migration.
     */
    @NotNull
    Queries queries();

    /**
     * Verify the correctness of a migration.
     *
     * @throws DataMigrationVerificationException When something went wrong during
     *             the verification of the migration.
     */
    void verify() throws DataMigrationVerificationException;

    /**
     * Apply the migration.
     *
     * @throws DataMigrationException When something went wrong during the
     *             application of the migration.
     */
    @Blocking
    void execute() throws DataMigrationException;
}
