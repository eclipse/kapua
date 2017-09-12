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
package org.eclipse.kapua.commons.util;

import java.util.Collection;
import java.util.Date;

import org.eclipse.kapua.KapuaIllegalArgumentException;
import org.eclipse.kapua.KapuaIllegalNullArgumentException;

/**
 * Utility class to validate arguments passed in a parameters in service methods.
 *
 * @since 1.0
 *
 */
public class ArgumentValidator {

    public static final String SIMPLE_NAME_REGEXP = "^[a-zA-Z0-9\\-]{3,}$";
    public static final String NAME_REGEXP = "^[a-zA-Z0-9\\_\\-]{3,}$";
    public static final String NAME_SPACE_REGEXP = "^[a-zA-Z0-9\\ \\_\\-]{3,}$";
    public static final String PASSWORD_REGEXP = "^.*(?=.{12,})(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!\\~\\|]).*$";
    public static final String EMAIL_REGEXP = "^(\\w+)([-+.][\\w]+)*@(\\w[-\\w]*\\.){1,5}([A-Za-z]){2,4}$";
    public static final String TAG_NAME_REGEXP = "[A-Za-z0-9-_@#!$%^&*+=?<>]{3,255}";
    public static final String IP_ADDRESS_REGEXP = "(^(http://)|(https://)|())([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])($)";
    public static final String LOCAL_IP_ADDRESS_REGEXP = "(^(http://)|(https://))(((127\\.0\\.0\\.1))|((10\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])))|((172\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])))|((192\\.168\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])))$)";

    // The standard (IEEE 802) format for printing MAC-48 addresses in human-friendly
    public static final String MAC_ADDRESS_REGEXP = "^([0-9A-F]{2}[:-]){5}([0-9A-F]{2})$";

    private ArgumentValidator() {
    }

    /**
     * Throws KapuaIllegalArgumentException if the supplied argValue does not matches specified validation expression.
     *
     * @param argValue
     * @param argRegExp
     * @param argName
     * @throws KapuaIllegalArgumentException
     */
    public static void match(String argValue, String argRegExp, String argName)
            throws KapuaIllegalArgumentException {
        if (argValue != null && !argValue.matches(argRegExp)) {
            throw new KapuaIllegalArgumentException(argName, argValue);
        }
    }

    /**
     * Throws an KapuaIllegalNullArgumentException if the value for the specified argument is null.
     *
     * @param value
     * @param argumentName
     * @throws KapuaIllegalNullArgumentException
     */
    public static void notNull(Object value, String argumentName)
            throws KapuaIllegalNullArgumentException {
        if (value == null) {
            throw new KapuaIllegalNullArgumentException(argumentName);
        }
    }

    /**
     * Throws an KapuaIllegalNullArgumentException if the value for the specified argument is not null.
     *
     * @param value
     * @param argumentName
     * @throws KapuaIllegalNullArgumentException
     */
    public static void isNull(Object value, String argumentName)
            throws KapuaIllegalArgumentException {
        if (value != null) {
            throw new KapuaIllegalArgumentException(argumentName, value.toString());
        }
    }

    /**
     * Throws an KapuaIllegalNullArgumentException if the string value for the specified argument is not empty nor null.
     *
     * @param value
     * @param argumentName
     * @throws KapuaIllegalNullArgumentException
     */
    public static void isEmptyOrNull(String value, String argumentName)
            throws KapuaIllegalArgumentException {
        if (!(value == null || value.trim().length() == 0)) {
            throw new KapuaIllegalArgumentException(argumentName, value);
        }
    }

    /**
     * Throws an KapuaIllegalNullArgumentException if the string value for the specified argument is empty or null.
     *
     * @param value
     * @param argumentName
     * @throws KapuaIllegalNullArgumentException
     */
    public static void notEmptyOrNull(String value, String argumentName)
            throws KapuaIllegalNullArgumentException {
        if (value == null || value.trim().length() == 0) {
            throw new KapuaIllegalNullArgumentException(argumentName);
        }
    }

    /**
     * Throws an KapuaIllegalNullArgumentException if the array for the specified argument is empty or null.
     * 
     * @param value
     * @param argumentName
     * @throws KapuaIllegalNullArgumentException
     */
    public static void notEmptyOrNull(Object[] value, String argumentName)
            throws KapuaIllegalNullArgumentException {
        if (value == null || value.length == 0) {
            throw new KapuaIllegalNullArgumentException(argumentName);
        }
    }

    /**
     * Throws an KapuaIllegalNullArgumentException if the collection for the specified argument is empty or null.
     * 
     * @param value
     * @param argumentName
     * @throws KapuaIllegalNullArgumentException
     */
    public static void notEmptyOrNull(Collection<?> value, String argumentName)
            throws KapuaIllegalNullArgumentException {
        if (value == null || value.isEmpty()) {
            throw new KapuaIllegalNullArgumentException(argumentName);
        }
    }

    /**
     * Throws an KapuaIllegalNullArgumentException if the value for the specified argument is null.
     *
     * @param value
     * @param argumentName
     * @throws KapuaIllegalNullArgumentException
     */
    public static void notNegative(long value, String argumentName)
            throws KapuaIllegalNullArgumentException {
        if (value < 0) {
            throw new KapuaIllegalNullArgumentException(argumentName);
        }
    }

    /**
     * Throws an KapuaIllegalNullArgumentException if StartDate comes after EndDate.
     * 
     * @param startDate
     * @param endDate
     * @throws KapuaIllegalArgumentException
     */
    public static void dateRange(Date startDate, Date endDate)
            throws KapuaIllegalArgumentException {
        dateRange(startDate.getTime(), endDate.getTime());
    }

    /**
     * Throws an KapuaIllegalNullArgumentException if StartDate comes after EndDate.
     * 
     * @param startDate
     * @param endDate
     * @throws KapuaIllegalArgumentException
     */
    public static void dateRange(long startDate, long endDate)
            throws KapuaIllegalArgumentException {

        if (startDate != -1 && endDate != -1 && startDate > endDate) {
            throw new KapuaIllegalArgumentException("Date Range", "Start Date after End Date.");
        }
    }

    /**
     * Throws an KapuaIllegalArgumentException if the value for the specified argument is lower (&lt;) than the minValue given or higher (&gt;) than the maxValue given. Extremes included.
     *
     * @param value
     * @param minValue
     * @param maxValue
     * @param argumentName
     * @throws KapuaIllegalArgumentException
     */
    public static void numRange(long value, long minValue, long maxValue, String argumentName)
            throws KapuaIllegalArgumentException {

        if (value < minValue) {
            throw new KapuaIllegalArgumentException(argumentName, "Value less than allowed min value. Min value is " + minValue);
        }

        if (value > maxValue) {
            throw new KapuaIllegalArgumentException(argumentName, "Value over than allowed max value. Max value is " + maxValue);
        }
    }

}
