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
 *
 *******************************************************************************/
package org.eclipse.kapua;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.StringJoiner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Kapua runtime exception.
 * 
 * @since 1.0
 *
 */
public class KapuaRuntimeException extends RuntimeException
{
    private static final long   serialVersionUID      = 5389531827567100733L;

    private static final String KAPUA_ERROR_MESSAGES  = "kapua-service-error-messages";
    private static final String KAPUA_GENERIC_MESSAGE = "Error: {0}";

    private static final Logger s_logger              = LoggerFactory.getLogger(KapuaException.class);

    protected KapuaErrorCode    code;
    protected Object[]          args;

    /**
     * Constructor
     */
    @SuppressWarnings("unused")
    private KapuaRuntimeException()
    {
        super();
    }

    /**
     * Constructor
     * 
     * @param message
     */
    @SuppressWarnings("unused")
    private KapuaRuntimeException(String message)
    {
        this(message, null);
    }

    /**
     * Constructor
     * 
     * @param throwable
     */
    @SuppressWarnings("unused")
    private KapuaRuntimeException(Throwable throwable)
    {
        this(null, throwable);
    }

    /**
     * Constructor
     * 
     * @param message
     * @param throwable
     */
    private KapuaRuntimeException(String message, Throwable throwable)
    {
        super(message, throwable);
    }

    /**
     * Builds a new KapuaRuntimeException instance based on the supplied KapuaErrorCode.
     * 
     * @param code
     */
    public KapuaRuntimeException(KapuaErrorCode code)
    {
        this(code, (Object[]) null);
    }

    /**
     * Builds a new KapuaRuntimeException instance based on the supplied KapuaErrorCode
     * and optional arguments for the associated exception message.
     * 
     * @param code
     * @param arguments
     */
    public KapuaRuntimeException(KapuaErrorCode code, Object... arguments)
    {
        this(code, null, arguments);
    }

    /**
     * Builds a new KapuaRuntimeException instance based on the supplied KapuaErrorCode,
     * an Throwable cause, and optional arguments for the associated exception message.
     * 
     * @param code
     * @param cause
     * @param arguments
     */
    public KapuaRuntimeException(KapuaErrorCode code, Throwable cause, Object... arguments)
    {
        super(cause);
        this.code = code;
        this.args = arguments;
    }

    /**
     * Factory method to build an KapuaRuntimeException with the KapuaErrorCode.INTERNAL_ERROR,
     * an Throwable cause, and optional arguments for the associated exception message.
     * 
     * @param cause
     * @param message
     * @return
     */
    public static KapuaRuntimeException internalError(Throwable cause, String message)
    {
        return new KapuaRuntimeException(KapuaErrorCodes.INTERNAL_ERROR, cause, message);
    }

    /**
     * Factory method to build an KapuaException with the KapuaErrorCode.INTERNAL_ERROR,
     * and an Throwable cause.
     * 
     * @param cause
     * @return
     */
    public static KapuaRuntimeException internalError(Throwable cause)
    {
        String arg = cause.getMessage();
        if (arg == null) {
            arg = cause.getClass().getName();
        }
        return new KapuaRuntimeException(KapuaErrorCodes.INTERNAL_ERROR, cause, arg);
    }

    /**
     * Factory method to build an KapuaException with the KapuaErrorCode.INTERNAL_ERROR,
     * and optional arguments for the associated exception message.
     * 
     * @param message
     * @return
     */
    public static KapuaRuntimeException internalError(String message)
    {
        return new KapuaRuntimeException(KapuaErrorCodes.INTERNAL_ERROR, null, message);
    }

    /**
     * Get the error code
     * 
     * @return
     */
    public KapuaErrorCode getCode()
    {
        return code;
    }

    /**
     * Get message
     */
    public String getMessage()
    {
        return getLocalizedMessage(Locale.US);
    }

    /**
     * Get localized message
     */
    public String getLocalizedMessage()
    {
        return getLocalizedMessage(Locale.getDefault());
    }

    protected String getKapuaErrorMessagesBundle()
    {
        return KAPUA_ERROR_MESSAGES;
    }

    protected String getLocalizedMessage(Locale locale)
    {
        String pattern = getMessagePattern(locale, code);
        if (pattern != null) {
            return MessageFormat.format(pattern, args);
        }
        else {
            // use the generic message by concatenating all args in one string
            StringJoiner joiner = new StringJoiner(",");
            if (args != null && args.length > 0) {
                for (Object arg : args)
                    joiner.add(arg.toString());
            }
            return MessageFormat.format(KAPUA_GENERIC_MESSAGE, joiner.toString());
        }
    }

    protected String getMessagePattern(Locale locale, KapuaErrorCode code)
    {
        //
        // Load the message pattern from the bundle
        String messagePattern = null;
        ResourceBundle resourceBundle = null;
        try {
            resourceBundle = ResourceBundle.getBundle(getKapuaErrorMessagesBundle(), locale);
            messagePattern = resourceBundle.getString(code.name());
        }
        catch (MissingResourceException mre) {
            // log the failure to load a message bundle
            s_logger.warn("Could not load Exception Messages Bundle for Locale {}", locale);
        }

        return messagePattern;
    }
}
