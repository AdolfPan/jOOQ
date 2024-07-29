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
package org.jooq.meta;

import java.util.List;

// ...
import org.jooq.Name;
import org.jooq.impl.QOM.GenerationLocation;
import org.jooq.impl.QOM.GenerationOption;
import org.jooq.meta.jaxb.ForcedType;

import org.jetbrains.annotations.Nullable;

/**
 * A definition for a data type object.
 *
 * @author Lukas Eder
 */
public interface DataTypeDefinition {

    /**
     * The dialect-specific column type.
     */
    String getType();

    /**
     * The generator type that is applied to this data type, or
     * <code>null</code>, if no such generator type is configured.
     */
    @Nullable
    String getGenerator();

    /**
     * The converter type that is applied to this data type, or
     * <code>null</code>, if no such converter type is configured.
     */
    @Nullable
    String getConverter();

    /**
     * The binding type that is applied to this data type, or
     * <code>null</code>, if no such binding type is configured.
     */
    @Nullable
    String getBinding();

    /**
     * The type's length.
     */
    int getLength();

    /**
     * The type's precision.
     */
    int getPrecision();

    /**
     * The type's scale.
     */
    int getScale();

    /**
     * The user type, if applicable.
     */
    String getUserType();

    /**
     * The qualified user type, if applicable.
     */
    Name getQualifiedUserType();

    /**
     * The custom Java type to represent this data type, if applicable.
     * <p>
     * This is the same as calling {@link #getJavaType(JavaTypeResolver)} with a
     * <code>null</code> resolver.
     */
    String getJavaType();

    /**
     * The custom Java type to represent this data type, if applicable.
     */
    String getJavaType(JavaTypeResolver resolver);

    /**
     * Whether this data type is nullable.
     */
    boolean isNullable();

    /**
     * Whether this data type is readonly.
     */
    boolean isReadonly();

    /**
     * Whether this data type is computed.
     */
    boolean isComputed();

    /**
     * The computed column expression.
     */
    String getGeneratedAlwaysAs();

    /**
     * The computed column generation option.
     */
    GenerationOption getGenerationOption();

    /**
     * The XML type definition, if available.
     */
    XMLTypeDefinition getXMLTypeDefinition();

    /**
     * The computed column generation location.
     */
    GenerationLocation getGenerationLocation();

    /**
     * Whether this data type is an identity.
     */
    boolean isIdentity();

    /**
     * Whether this data type is defaultable.
     */
    boolean isDefaulted();

    /**
     * The default value expression.
     */
    String getDefaultValue();

    /**
     * Whether this data type represents a udt.
     */
    boolean isUDT();

    /**
     * Whether this data type represents an array producing an
     * {@link ArrayRecord}.
     */
    boolean isArray();

    /**
     * Whether this data type represents an array producing an
     * {@link ArrayRecord} of udt types.
     */
    boolean isUDTArray();

    /**
     * Whether this data type is a NUMBER type without precision and scale.
     */
    boolean isGenericNumberType();

    /**
     * The underlying database.
     */
    Database getDatabase();

    /**
     * The underlying schema.
     */
    SchemaDefinition getSchema();

    /**
     * The various type names by which this type can be matched by a
     * {@link ForcedType}.
     */
    List<String> getMatchNames();
}
