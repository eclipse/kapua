/*******************************************************************************
 * Copyright (c) 2016, 2021 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.StringJoiner;

/**
 * Utilities to print localized error messages for {@link KapuaException}s.
 *
 * @since 1.1.0
 */
public class ExceptionMessageUtils {

    private static final Logger LOG = LoggerFactory.getLogger(ExceptionMessageUtils.class);

    private static final String KAPUA_GENERIC_MESSAGE = "Error: {0}";

    private ExceptionMessageUtils() {
    }

    /**
     * Gets the {@link Locale}ised message for the given {@link KapuaErrorCode}.
     * <p>
     * If the {@link ResourceBundle} or the {@link KapuaErrorCode} are not found, a generic message is printed.
     *
     * @param resourceBundleName The {@link ResourceBundle} name from which to search the {@link KapuaErrorCode}.
     * @param locale             The {@link Locale} of the {@link ResourceBundle}.
     * @param code               The {@link KapuaErrorCode} to search in the {@link ResourceBundle}.
     * @param args               The arguments to use to populate the message pattern.
     * @return The localized message for the given {@link KapuaErrorCode}.
     * @since 1.0.0
     */
    public static String getLocalizedMessage(String resourceBundleName, Locale locale, KapuaErrorCode code, Object[] args) {

        String pattern = getMessagePattern(resourceBundleName, locale, code);
        if (pattern != null) {
            return MessageFormat.format(pattern, args);
        } else {
            StringJoiner joiner = new StringJoiner(", ");
            if (args != null && args.length > 0) {
                for (Object arg : args) {
                    if (arg instanceof Iterable) {
                        joiner.add(arg.toString());
                    } else if (arg != null && arg.getClass().isArray()) {
                        joiner.add(Arrays.toString((Object[]) arg));
                    } else {
                        joiner.add(String.valueOf(arg));
                    }
                }
            }

            return MessageFormat.format(KAPUA_GENERIC_MESSAGE, joiner.toString());
        }
    }

    /**
     * Loads the message pattern from the resource bundle.
     *
     * @param resourceBundleName The {@link ResourceBundle} name from which to search the {@link KapuaErrorCode}.
     * @param locale             The {@link Locale} of the {@link ResourceBundle}.
     * @param code               The {@link KapuaErrorCode} to search in the {@link ResourceBundle}.
     * @return The message pattern of the given {@link KapuaErrorCode} if found, {@code null} otherwise.
     * @since 1.0.0
     */
    private static String getMessagePattern(@NotNull String resourceBundleName, @NotNull Locale locale, @NotNull KapuaErrorCode code) {

        String messagePattern = null;
        ResourceBundle resourceBundle;
        try {
            resourceBundle = ResourceBundle.getBundle(resourceBundleName, locale);
        } catch (MissingResourceException mre) {
            LOG.warn("Could not load exception messages for resource: {} in locale: {}. A generic error message will be printed.", resourceBundleName, locale);
            return null;
        }
        try {
            messagePattern = resourceBundle.getString(code.name());
        } catch (MissingResourceException mre) {
            LOG.warn("Could not load exception messages for code: {}. A generic error message will be printed.", code.name());
        }

        return messagePattern;
    }

}
