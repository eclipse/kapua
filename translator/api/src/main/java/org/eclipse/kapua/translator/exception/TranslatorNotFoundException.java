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
package org.eclipse.kapua.translator.exception;

/**
 * {@link TranslatorRuntimeException} to {@code throw} when no {@link org.eclipse.kapua.translator.Translator} are available for the given {@link org.eclipse.kapua.message.Message} classes.
 *
 * @since 1.2.0
 */
public class TranslatorNotFoundException extends TranslatorRuntimeException {

    private final Class<?> fromMessageClass;
    private final Class<?> toMessageClass;

    /**
     * Constructor.
     *
     * @param fromMessageClass The {@link org.eclipse.kapua.message.Message} type for which the {@link org.eclipse.kapua.translator.Translator} was from.
     * @param toMessageClass   The {@link org.eclipse.kapua.message.Message} type for which the {@link org.eclipse.kapua.translator.Translator} was to.
     * @since 1.2.0
     */
    public TranslatorNotFoundException(Class<?> fromMessageClass, Class<?> toMessageClass) {
        super(TranslatorErrorCodes.TRANSLATOR_NOT_FOUND, fromMessageClass, toMessageClass);

        this.fromMessageClass = fromMessageClass;
        this.toMessageClass = toMessageClass;
    }

    /**
     * Gets the {@link org.eclipse.kapua.message.Message} type for which the {@link org.eclipse.kapua.translator.Translator} was from.
     *
     * @return The {@link org.eclipse.kapua.message.Message} type for which the {@link org.eclipse.kapua.translator.Translator} was from.
     * @since 1.2.0
     */
    public Class<?> getFromMessageClass() {
        return fromMessageClass;
    }

    /**
     * Gets the {@link org.eclipse.kapua.message.Message} type for which the {@link org.eclipse.kapua.translator.Translator} was to.
     *
     * @return The {@link org.eclipse.kapua.message.Message} type for which the {@link org.eclipse.kapua.translator.Translator} was to.
     * @since 1.2.0
     */
    public Class<?> getToMessageClass() {
        return toMessageClass;
    }
}
