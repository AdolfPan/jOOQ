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

import java.sql.Time;
import java.time.LocalTime;
import java.util.function.Function;

import org.jooq.Converter;
import org.jooq.ConverterContext;

/**
 * @author Lukas Eder
 * @deprecated - 3.15.0 - [#11505] - Use
 *             {@link Converter#ofNullable(Class, Class, Function, Function)}
 *             instead, e.g.
 *             <code>Converter.ofNullable(Time.class, LocalTime.class, Time::toLocalTime, Time::valueOf)</code>.
 */
@Deprecated
public final class TimeToLocalTimeConverter extends AbstractContextConverter<Time, LocalTime> {

    public TimeToLocalTimeConverter() {
        super(Time.class, LocalTime.class);
    }

    @Override
    public final LocalTime from(Time t, ConverterContext scope) {
        return t == null ? null : t.toLocalTime();
    }

    @Override
    public final Time to(LocalTime u, ConverterContext scope) {
        return u == null ? null : Time.valueOf(u);
    }
}
