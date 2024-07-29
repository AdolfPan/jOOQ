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

import org.jooq.Converter;
import org.jooq.ConverterContext;
import org.jooq.ContextConverter;

/**
 * A converter that doesn't convert anything.
 *
 * @author Lukas Eder
 */
public final class IdentityConverter<T> implements ContextConverter<T, T> {
    private final Class<T> type;

    public IdentityConverter(Class<T> type) {
        this.type = type;
    }

    @Override
    public final T from(T t, ConverterContext scope) {
        return t;
    }

    @Override
    public final T to(T t, ConverterContext scope) {
        return t;
    }

    @Override
    public final Class<T> fromType() {
        return type;
    }

    @Override
    public final Class<T> toType() {
        return type;
    }

    @Override
    public String toString() {
        return "IdentityConverter [ " + fromType().getName() + " ]";
    }
}
