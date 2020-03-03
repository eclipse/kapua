/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.translator.exception;

import org.eclipse.kapua.KapuaErrorCode;

/**
 * {@link org.eclipse.kapua.translator.Translator} {@link KapuaErrorCode}s.
 *
 * @since 1.0.0
 */
public enum TranslatorErrorCodes implements KapuaErrorCode {
    /**
     * @since 1.0.0
     */
    INVALID_MESSAGE,

    /**
     * @since 1.0.0
     */
    INVALID_CHANNEL,
    /**
     * @since 1.0.0
     */
    INVALID_CHANNEL_CLASSIFIER,
    /**
     * @since 1.0.0
     */
    INVALID_CHANNEL_APP_NAME,
    /**
     * @since 1.0.0
     */
    INVALID_CHANNEL_APP_VERSION,

    /**
     * @since 1.0.0
     */
    INVALID_PAYLOAD,

    /**
     * @since 1.0.0
     */
    INVALID_BODY,


    /**
     * @see TranslatorNotFoundException
     * @since 1.2.0
     */
    TRANSLATOR_NOT_FOUND
}
