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

import static java.util.Collections.nCopies;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * A stack to register elements that are visible to a certain scope.
 *
 * @author Lukas Eder
 */
final class ScopeStack<K, V> implements Iterable<V> {

    private int                        scopeLevel = -1;
    private Map<K, List<V>>            stack;
    private final ObjIntFunction<K, V> constructor;

    ScopeStack() {
        this((ObjIntFunction<K, V>) null);
    }

    ScopeStack(V defaultValue) {
        this((part, scopeLevel) -> defaultValue);
    }

    ScopeStack(ObjIntFunction<K, V> constructor) {
        this.constructor = constructor;
    }

    private final Map<K, List<V>> stack() {
        if (stack == null)
            stack = new LinkedHashMap<>();

        return stack;
    }

    private final void trim() {
        int l = scopeLevel + 1;
        if (l >= 0) {
            int size;
            for (Iterator<Map.Entry<K, List<V>>> it = stack().entrySet().iterator(); it.hasNext(); ) {
                Map.Entry<K, List<V>> entry = it.next();
                List<V> list = entry.getValue();
                while ((size = list.size()) > l || size > 0 && list.get(size - 1) == null)
                    list.remove(size - 1);
                if (list.isEmpty())
                    it.remove();
            }
        }
    }

    final boolean isEmpty() {
        return !iterator().hasNext();
    }

    final Iterable<Value<V>> valueIterable() {
        return () -> new ScopeStackIterator<Value<V>>(e -> Value.lastOf(e.getValue()), e -> true);
    }

    @Override
    public final Iterator<V> iterator() {
        return iterable(e -> true).iterator();
    }

    final Iterable<V> iterableAtScopeLevel() {
        return () -> new ScopeStackIterator<>((k, v) -> v.size() == scopeLevel + 1 ? v.get(scopeLevel) : null, e -> true);
    }

    final Iterable<V> iterable(Predicate<? super V> filter) {
        return () -> new ScopeStackIterator<>((k, v) -> v.get(v.size() - 1), filter);
    }

    final Iterable<K> keyIterableAtScopeLevel() {
        return () -> new ScopeStackIterator<>((k, v) -> v.size() == scopeLevel + 1 ? k : null, e -> true);
    }

    final Iterable<K> keyIterable(Predicate<? super K> filter) {
        return () -> new ScopeStackIterator<>((k, v) -> k, filter);
    }

    static final record Value<V>(int scopeLevel, V value) {
        static <V> Value<V> of(int scopeLevel, V value) {
            return value == null ? null : new Value<>(scopeLevel, value);
        }

        static <V> Value<V> lastOf(List<V> list) {
            int size = list.size();
            V value = list.get(size - 1);
            return of(size - 1, value);
        }
    }

    private final class ScopeStackIterator<U> implements Iterator<U> {
        final Iterator<Entry<K, List<V>>>    it = stack().entrySet().iterator();
        final Function<Entry<K, List<V>>, U> valueExtractor;
        final Predicate<? super U>           filter;
        U                                    next;

        ScopeStackIterator(BiFunction<K, List<V>, U> valueExtractor, Predicate<? super U> filter) {
            this(e -> valueExtractor.apply(e.getKey(), e.getValue()), filter);
        }

        ScopeStackIterator(Function<Entry<K, List<V>>, U> valueExtractor, Predicate<? super U> filter) {
            this.valueExtractor = valueExtractor;
            this.filter = filter;
        }

        @Override
        public boolean hasNext() {
            return move() != null;
        }

        @Override
        public U next() {
            if (next == null) {
                return move();
            }
            else {
                U result = next;
                next = null;
                return result;
            }
        }

        private U move() {
            for (
                Entry<K, List<V>> e;
                it.hasNext() && ((e = it.next()).getValue().isEmpty() || ((next = valueExtractor.apply(e)) == null) || !filter.test(next));
                next = null
            );

            return next;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("remove");
        }
    }

    final void setAll(V value) {
        for (K key : stack().keySet())
            set(key, value);
    }

    final void set(K key, V value) {
        set0(list(key), value);
    }

    private final V get0(List<V> list) {
        int i;

        if (list == null)
            return null;
        else if ((i = list.size()) == 0)
            return null;
        else
            return list.get(i - 1);
    }

    private final V getCurrentScope0(List<V> list) {
        int i;

        if (list == null)
            return null;
        else if ((i = list.size()) == 0)
            return null;
        else if (scopeLevel >= i)
            return null;
        else
            return list.get(i - 1);
    }

    final V get(K key) {
        return get0(listOrNull(key));
    }

    final V getCurrentScope(K key) {
        return getCurrentScope0(listOrNull(key));
    }

    final <T extends Throwable> V getOrThrow(K key, Supplier<T> exception) throws T {
        V result = get(key);
        if (result == null)
            throw exception.get();
        return result;
    }

    final V getOrCreate(K key) {
        List<V> list = list(key);
        V result = get0(list);
        return result != null ? result : create0(key, list);
    }

    final V create(K key) {
        return create0(key, list(key));
    }

    private final V create0(K key, List<V> list) {
        V result = constructor.apply(key, scopeLevel);
        set0(list, result);
        return result;
    }

    private final void set0(List<V> list, V value) {
        int l = scopeLevel + 1;
        int size = list.size();
        if (size < l) {
            int nulls = l - size;
            for (int i = 0; i < nulls; i++)
                list.add(null);
        }
        list.set(scopeLevel, value);
    }

    private final List<V> listOrNull(K key) {
        return stack().get(key);
    }

    private final List<V> list(K key) {
        return stack().computeIfAbsent(key, k -> new ArrayList<>());
    }

    final boolean inScope() {
        return scopeLevel > -1;
    }

    final int scopeLevel() {
        return scopeLevel;
    }

    final void scopeStart() {
        scopeLevel++;
    }

    final void scopeEnd() {
        scopeLevel--;
        trim();
    }

    @Override
    public String toString() {
        return stack().toString();
    }
}
