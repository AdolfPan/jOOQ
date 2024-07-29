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

import java.time.Instant;
import java.util.Map;

import org.jooq.conf.Settings;
import org.jooq.impl.DSL;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Scope implementations provide access to a variety of objects that are
 * available from a given scope.
 * <p>
 * The scope of the various objects contained in this type (e.g.
 * {@link #configuration()}, {@link #settings()}, etc.) are implementation
 * dependent and will be specified by the concrete subtype of
 * <code>Scope</code>. Examples of such scope types are:
 * <ul>
 * <li>{@link BindingScope}: A scope used for a single {@link Binding}
 * operation.</li>
 * <li>{@link Context}: Used for a single traversal of a {@link QueryPart}
 * expression tree to produce a SQL string and / or a list of bind
 * variables.</li>
 * <li>{@link ConverterContext}: A scope that covers a single
 * {@link ContextConverter#from(Object, ConverterContext)} or
 * {@link ContextConverter#to(Object, ConverterContext)} call.</li>
 * <li>{@link DSLContext}: The {@link DSL} API that creates {@link Query}
 * instances in the context of a {@link Configuration}. It shares the wrapped
 * {@link Configuration}'s lifecycle.</li>
 * <li>{@link ExecuteContext}: Used for a single execution of a {@link Query},
 * containing JDBC resources and other execution relevant objects. Can be
 * accessed by the {@link ExecuteListener} SPI.</li>
 * <li>{@link ExecuteScope}: A scope that is used for operations that are
 * {@link ExecuteContext} aware, but may have a more narrow scope, such as e.g.
 * {@link BindingScope}.</li>
 * <li>{@link GeneratorContext}: A scope that is used for client side computed
 * column expression generation.</li>
 * <li>{@link ParseContext}: Used for a single parse call. Can be accessed by
 * the {@link ParseListener} SPI.</li>
 * <li>{@link RecordContext}: Used a single record operation, such as
 * {@link UpdatableRecord#store()}. Can be accessed by the
 * {@link RecordListener} SPI.</li>
 * <li>{@link TransactionContext}: A scope that covers the execution (or
 * nesting) of a single transaction. Can be accessed by the
 * {@link TransactionListener} SPI.</li>
 * <li>{@link VisitContext}: A scope that that covers a single traversal of a
 * {@link QueryPart} expression tree (just like {@link Context}). Can be
 * accessed by the {@link VisitListener} SPI.</li>
 * </ul>
 * <p>
 * One of <code>Scope</code>'s most interesting features for client code
 * implementing any SPI is the {@link #data()} map, which provides access to a
 * {@link Map} where client code can register user-defined values for the entire
 * lifetime of a scope. For instance, in an {@link ExecuteListener}
 * implementation that measures time for fetching data, it is perfectly possible
 * to store timestamps in that map:
 * <p>
 *
 * <pre>
 * <code>
 * class FetchTimeMeasuringListener extends DefaultExecuteListener {
 *     &#64;Override
 *     public void fetchStart(ExecuteContext ctx) {
 *
 *         // Put any arbitrary object in this map:
 *         ctx.data("org.jooq.example.fetch-start-time", System.nanoTime());
 *     }
 *
 *     &#64;Override
 *     public void fetchEnd(ExecuteContext ctx) {
 *
 *         // Retrieve that object again in a later step:
 *         Long startTime = (Long) ctx.data("org.jooq.example.fetch-start-time");
 *         System.out.println("Time taken: " + (System.nanoTime() - startTime) / 1000 / 1000.0 + " ms");
 *     }
 * }
 * </code>
 * </pre>
 *
 * @author Lukas Eder
 */
public interface Scope {

    /**
     * The time, according to {@link Configuration#clock()}, when this
     * {@link Scope} was created.
     */
    @NotNull
    Instant creationTime();

    /**
     * The configuration of the current scope.
     */
    @NotNull
    Configuration configuration();

    /**
     * Wrap the {@link #configuration()} in a {@link DSLContext}, providing
     * access to the configuration-contextual DSL to construct executable
     * queries.
     */
    @NotNull
    DSLContext dsl();

    /**
     * The settings wrapped by this context.
     * <p>
     * This method is a convenient way of accessing
     * <code>configuration().settings()</code>.
     */
    @NotNull
    Settings settings();

    /**
     * The {@link SQLDialect} wrapped by this context.
     * <p>
     * This method is a convenient way of accessing
     * <code>configuration().dialect()</code>.
     */
    @NotNull
    SQLDialect dialect();

    /**
     * The {@link SQLDialect#family()} wrapped by this context.
     * <p>
     * This method is a convenient way of accessing
     * <code>configuration().family()</code>.
     */
    @NotNull
    SQLDialect family();

    /**
     * Get all custom data from this <code>Scope</code>.
     * <p>
     * This is custom data that was previously set to the context using
     * {@link #data(Object, Object)}. Use custom data if you want to pass data
     * to {@link QueryPart} objects for a given {@link Scope}.
     *
     * @return The custom data. This is never <code>null</code>
     */
    @NotNull
    Map<Object, Object> data();

    /**
     * Get some custom data from this <code>Scope</code>.
     * <p>
     * This is custom data that was previously set to the context using
     * {@link #data(Object, Object)}. Use custom data if you want to pass data
     * to {@link QueryPart} objects for a given {@link Scope}
     *
     * @param key A key to identify the custom data
     * @return The custom data or <code>null</code> if no such data is contained
     *         in this <code>Scope</code>
     */
    @Nullable
    Object data(Object key);

    /**
     * Set some custom data to this <code>Scope</code>.
     * <p>
     * This is custom data that was previously set to the context using
     * {@link #data(Object, Object)}. Use custom data if you want to pass data
     * to {@link QueryPart} objects for a given {@link Scope}.
     *
     * @param key A key to identify the custom data
     * @param value The custom data
     * @return The previously set custom data or <code>null</code> if no data
     *         was previously set for the given key
     */
    @Nullable
    Object data(Object key, Object value);
}
