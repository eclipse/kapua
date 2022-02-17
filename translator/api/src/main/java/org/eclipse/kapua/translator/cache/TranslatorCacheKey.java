/*******************************************************************************
 * Copyright (c) 2020, 2022 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.translator.cache;

import com.google.common.base.Objects;

/**
 * {@link TranslatorCache} key {@link Object}.
 * <p>
 * This combines the {@code from} and {@code to} to create a unique reference for the {@link org.eclipse.kapua.translator.Translator}
 * {@link #equals(Object)} and {@link #hashCode()} are {@link Override}n to use only {@link #fromClassName} and {@link #toClassName}
 *
 * @since 1.2.0
 */
class TranslatorCacheKey {
    final String fromClassName;
    final String toClassName;
    final String toString;

    /**
     * Constructor.
     *
     * @param fromClass The {@link org.eclipse.kapua.message.Message} type from which Translate.
     * @param toClass   The {@link org.eclipse.kapua.message.Message} type to which Translate.
     * @since 1.2.0
     */
    public TranslatorCacheKey(Class<?> fromClass, Class<?> toClass) {
        this.fromClassName = fromClass.getName();
        this.toClassName = toClass.getName();
        this.toString = fromClass.getSimpleName() + " -> " + toClass.getSimpleName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TranslatorCacheKey that = (TranslatorCacheKey) o;
        return Objects.equal(fromClassName, that.fromClassName) &&
                Objects.equal(toClassName, that.toClassName);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(fromClassName, toClassName);
    }

    @Override
    public String toString() {
        return toString;
    }
}
