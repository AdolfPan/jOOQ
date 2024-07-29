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

import static org.jooq.tools.StringUtils.defaultIfNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.jooq.Configuration;
import org.jooq.Node;
import org.jooq.exception.DataDefinitionException;

/**
 * @author Lukas Eder
 */
abstract class AbstractNode<N extends Node<N>> extends AbstractLazyScope implements Node<N> {

    final N      root;
    final String id;
    final String message;

    @SuppressWarnings("unchecked")
    AbstractNode(Configuration configuration, String id, String message, N root) {
        super(configuration);

        this.root = root != null ? root : (N) this;
        this.id = id;
        this.message = defaultIfNull(message, "");
    }

    @Override
    public final String id() {
        return id;
    }

    @Override
    public final String message() {
        return message;
    }

    @Override
    public final N root() {
        return root;
    }

    @SuppressWarnings("unchecked")
    final N commonAncestor(N other) {
        if (this.id().equals(other.id()))
            return (N) this;

        // TODO: Find a better solution than the brute force one
        // See e.g. https://en.wikipedia.org/wiki/Lowest_common_ancestor

        Map<N, Integer> a1 = ancestors((N) this, new HashMap<>(), 1);
        Map<N, Integer> a2 = ancestors(other, new HashMap<>(), 1);

        N node = null;
        Integer distance = null;

        for (Entry<N, Integer> entry : a1.entrySet()) {
            if (a2.containsKey(entry.getKey())) {

                // TODO: What if there are several conflicting paths?
                if (distance == null || distance > entry.getValue()) {
                    node = entry.getKey();
                    distance = entry.getValue();
                }
            }
        }

        if (node == null)
            throw new DataDefinitionException("Versions " + this.id() + " and " + other.id() + " do not have a common ancestor");

        return node;
    }

    private Map<N, Integer> ancestors(N node, Map<N, Integer> result, int distance) {
        Integer previous = result.get(node);

        if (previous == null || previous > distance) {
            result.put(node, distance);

            for (N parent : node.parents())
                ancestors(parent, result, distance + 1);
        }

        return result;
    }
}
