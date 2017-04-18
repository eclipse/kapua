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

import org.eclipse.kapua.KapuaException;

/**
 * Translator Exception
 * 
 * @since 1.0
 * 
 */
public class TranslatorException extends KapuaException
{
    private static final long   serialVersionUID     = -6207605695086240243L;

    private static final String TRANSLATOR_ERROR_MESSAGES = "translator-error-messages";

    /**
     * Construct a TranslatorException
     * 
     * @param code error code
     */
    public TranslatorException(TranslatorErrorCodes code)
    {
        super(code);
    }

    /**
     * Construct a TranslatorException
     * 
     * @param code error code
     * @param arguments additional arguments
     */
    public TranslatorException(TranslatorErrorCodes code, Object... arguments)
    {
        super(code, arguments);
    }

    /**
     * Construct a TranslatorException
     * 
     * @param code error code
     * @param cause error cause
     * @param arguments additional arguments
     */
    public TranslatorException(TranslatorErrorCodes code, Throwable cause, Object... arguments)
    {
        super(code, cause, arguments);
    }

    protected String getKapuaErrorMessagesBundle()
    {
        return TRANSLATOR_ERROR_MESSAGES;
    }
}
