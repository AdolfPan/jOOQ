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


/**
 * @author Lukas Eder
 */
enum DDLStatementType {
    ALTER_DATABASE,
    ALTER_DOMAIN,
    ALTER_FUNCTION,
    ALTER_INDEX,
    ALTER_PROCEDURE,
    ALTER_SCHEMA,
    ALTER_SEQUENCE,
    ALTER_TABLE,
    ALTER_TRIGGER,
    ALTER_TYPE,
    ALTER_VIEW,

    CREATE_DATABASE,
    CREATE_DOMAIN,
    CREATE_FUNCTION,
    CREATE_INDEX,
    CREATE_PROCEDURE,
    CREATE_SCHEMA,
    CREATE_SEQUENCE,
    CREATE_TABLE,
    CREATE_TRIGGER,
    CREATE_TYPE,
    CREATE_VIEW,

    DROP_DATABASE,
    DROP_DOMAIN,
    DROP_FUNCTION,
    DROP_INDEX,
    DROP_PROCEDURE,
    DROP_SCHEMA,
    DROP_SEQUENCE,
    DROP_TABLE,
    DROP_TRIGGER,
    DROP_TYPE,
    DROP_VIEW,
}
