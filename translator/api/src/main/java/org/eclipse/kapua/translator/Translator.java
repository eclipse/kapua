/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *     Red Hat Inc
 *
 *******************************************************************************/
package org.eclipse.kapua.translator;

import java.util.Objects;
import java.util.ServiceLoader;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.KapuaRuntimeErrorCodes;
import org.eclipse.kapua.KapuaRuntimeException;
import org.eclipse.kapua.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Translator base class. Translators are used to allow heterogeneous systems to exchange messages through layered messages domain.
 * Kapua platform has 3 different layer:<br>
 * <br>
 * <ul>
 * <li>Kapua level</li>
 * <li>Application level (ie Kura)</li>
 * <li>Transport level (ie jms, mqtt, ...)</li>
 * </ul>
 * 
 * @param <FROM_M>
 *            message from type
 * @param <TO_M>
 *            message to type
 * 
 * @since 1.0
 * 
 */
@SuppressWarnings("rawtypes")
public abstract class Translator<FROM_M extends Message, TO_M extends Message> {

    private static final Logger logger = LoggerFactory.getLogger(Translator.class);

    private static final ServiceLoader<Translator> translators = ServiceLoader.load(Translator.class);

    /**
     * Return a translator for the given messages classes.
     * <br>
     * This method will lookup instances of Translator through {@link java.util.ServiceLoader}
     * 
     * @param fromMessageClass
     *            message from type
     * @param toMessageClass
     *            message to type
     * @return
     * @throws KapuaException
     */
    @SuppressWarnings("unchecked")
    public static synchronized <FROM_M extends Message, TO_M extends Message, T extends Translator<FROM_M, TO_M>> T getTranslatorFor(Class<? extends FROM_M> fromMessageClass,
            Class<? extends TO_M> toMessageClass)
            throws KapuaException {

        Objects.requireNonNull(fromMessageClass);
        Objects.requireNonNull(toMessageClass);

        for (Translator translator : translators) {
            if ((fromMessageClass.isAssignableFrom(translator.getClassFrom())) &&
                    toMessageClass.isAssignableFrom(translator.getClassTo())) {
                return (T) translator;
            }
        }

        logger.error("Cannot find translator from: {}- to: {}", fromMessageClass.getName(), toMessageClass.getName());
        throw new KapuaRuntimeException(KapuaRuntimeErrorCodes.TRANSLATOR_NOT_FOUND,
                null,
                new Object[] {
                        translators,
                        fromMessageClass.getName(),
                        toMessageClass.getName(),
                });
    }

    /**
     * Translate message from the domain FROM_M to the domain TO_M
     * 
     * @param message
     *            the message to translate
     * @return the translated message
     * @throws KapuaException
     */
    public abstract TO_M translate(FROM_M message) throws KapuaException;

    /**
     * Return the FROM_M message type
     * 
     * @return
     */
    public abstract Class<FROM_M> getClassFrom();

    /**
     * Return the TO_M message type
     * 
     * @return
     */
    public abstract Class<TO_M> getClassTo();
}
