/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
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

import org.eclipse.kapua.KapuaErrorCode;

/**
 * {@link org.eclipse.kapua.translator.Translator} {@link KapuaErrorCode}s.
 *
 * @since 1.0.0
 */
public enum TranslatorErrorCodes implements KapuaErrorCode {

    /**
     * @see InvalidMessageException
     * @since 1.0.0
     */
    INVALID_MESSAGE,

    /**
     * @see InvalidChannelException
     * @since 1.0.0
     */
    INVALID_CHANNEL,

    /**
     * @see InvalidPayloadException
     * @since 1.0.0
     */
    INVALID_PAYLOAD,

    /**
     * @see InvalidBodyEncodingException
     * @since 1.5.0
     */
    INVALID_BODY_ENCODING,

    /**
     * @see InvalidBodyContentException
     * @since 1.5.0
     */
    INVALID_BODY_CONTENT,

    /**
     * @see TranslatorNotFoundException
     * @since 1.2.0
     */
    TRANSLATOR_NOT_FOUND,

    //
    // Deprecated
    //

    /**
     * @since 1.0.0
     * @deprecated Since 1.2.0. Moved to implementation module
     */
    @Deprecated
    INVALID_CHANNEL_CLASSIFIER,

    /**
     * @since 1.0.0
     * @deprecated Since 1.2.0. Moved to implementation module
     */
    @Deprecated
    INVALID_CHANNEL_APP_NAME,

    /**
     * @since 1.0.0
     * @deprecated Since 1.2.0. Moved to implementation module
     */
    @Deprecated
    INVALID_CHANNEL_APP_VERSION,

    /**
     * @since 1.0.0
     * @deprecated Since 1.2.0. Moved to implementation module
     */
    @Deprecated
    INVALID_BODY
}
