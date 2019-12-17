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

import org.eclipse.kapua.KapuaIllegalArgumentException;
import org.eclipse.kapua.KapuaIllegalNullArgumentException;
import org.eclipse.kapua.model.KapuaNamedEntity;
import org.eclipse.kapua.model.KapuaNamedEntityCreator;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Date;
import java.util.Objects;

/**
 * Utility class to validate arguments passed in a parameters in service methods.
 *
 * @since 1.0
 */
public class ArgumentValidator {

    private ArgumentValidator() {
    }

    /**
     * Throws KapuaIllegalArgumentException if the supplied argValue does not matches specified validation expression.
     *
     * @param argValue
     * @param validationRegex
     * @param argName
     * @throws KapuaIllegalArgumentException
     */
    public static void match(String argValue, ValidationRegex validationRegex, String argName) throws KapuaIllegalArgumentException {
        if (argValue != null && !validationRegex.getPattern().matcher(argValue).matches()) {
            throw new KapuaIllegalArgumentException(argName, argValue);
        }
    }

    /**
     * Throws KapuaIllegalArgumentException if the supplied argValue1 is not equal to the supplied argValue2
     *
     * @param actual
     * @param expected
     * @param argName
     * @throws KapuaIllegalArgumentException
     */
    public static void areEqual(String actual, String expected, String argName)
            throws KapuaIllegalArgumentException {
        if (!Objects.equals(actual, expected)) {
            throw new KapuaIllegalArgumentException(argName, actual);
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

    /**
     * Throws a {@link KapuaIllegalArgumentException} if the {@link String} given has {@link String#length()} less than the #minLength given or greater than the #maxLength given.
     *
     * @param value        The {@link String} to check
     * @param minLength    The minimun valid value. If {@code null} it means unbounded.
     * @param maxLength    The maximum valid value. If {@code null} it means unbounded.
     * @param argumentName The argument name with will be used in the exception
     * @throws KapuaIllegalArgumentException If the given {@link String} excedees the given length limits.
     */
    public static void lengthRange(@NotNull String value, @Nullable Integer minLength, @Nullable Integer maxLength, @NotNull String argumentName) throws KapuaIllegalArgumentException {

        if (minLength != null && value.length() < minLength) {
            throw new KapuaIllegalArgumentException(argumentName, "Value less than allowed min length. Min length is " + minLength);
        }

        if (maxLength != null && value.length() > maxLength) {
            throw new KapuaIllegalArgumentException(argumentName, "Value over than allowed max length. Max length is " + maxLength);
        }
    }

    /**
     * Comprehensive validation method for the {@link KapuaNamedEntity#getName()} or {@link KapuaNamedEntityCreator#getName()} fields.
     * <p>
     * Same as {@link #validateEntityName(String, Integer, Integer, String)} but assumes default minimum length of 3 chars and maximum length of 255 chars.
     *
     * @param name         The value to validate. Usually would be the {@link KapuaNamedEntity#getName()} or {@link KapuaNamedEntityCreator#getName()}, but other values could be provided
     * @param argumentName The name of the argumento to bundle with the {@link KapuaIllegalArgumentException}
     * @throws KapuaIllegalNullArgumentException If the given value to validate is {@code null}.
     * @throws KapuaIllegalArgumentException     If other validations fails.
     * @see ArgumentValidator#notEmptyOrNull(String, String)
     * @see ArgumentValidator#lengthRange(String, Integer, Integer, String)
     * @see ArgumentValidator#match(String, ValidationRegex, String)
     * @since 1.2.0
     */
    public static void validateEntityName(@Nullable String name, @NotNull String argumentName) throws KapuaIllegalNullArgumentException, KapuaIllegalArgumentException {
        validateEntityName(name, 3, 255, argumentName);
    }

    /**
     * Comprehensive validation method for the {@link KapuaNamedEntity#getName()} or {@link KapuaNamedEntityCreator#getName()} fields.
     * <p>
     * It invokes in sequence the three {@link ArgumentValidator} validation methods, using the provided parameters.
     * <ul>
     *     <li>{@link #notEmptyOrNull(String, String)}</li>
     *     <li>{@link #lengthRange(String, Integer, Integer, String)}</li>
     *     <li>{@link #match(String, ValidationRegex, String)} with {@link CommonsValidationRegex#NAME_SPACE_REGEXP} </li>
     * </ul>
     *
     * @param name         The value to validate. Usually would be the {@link KapuaNamedEntity#getName()} or {@link KapuaNamedEntityCreator#getName()}, but other values could be provided
     * @param minLength    The minimum length of the field. If {@code null} the minLength validation is skipped
     * @param maxLength    The maximum length of the field. If {@code null} the maxLength validation is skipped
     * @param argumentName The name of the argumento to bundle with the {@link KapuaIllegalArgumentException}
     * @throws KapuaIllegalNullArgumentException If the given value to validate is {@code null}.
     * @throws KapuaIllegalArgumentException     If other validations fails.
     * @see ArgumentValidator#notEmptyOrNull(String, String)
     * @see ArgumentValidator#lengthRange(String, Integer, Integer, String)
     * @see ArgumentValidator#match(String, ValidationRegex, String)
     * @since 1.2.0
     */
    public static void validateEntityName(@Nullable String name, @Nullable Integer minLength, @Nullable Integer maxLength, @NotNull String argumentName) throws KapuaIllegalNullArgumentException, KapuaIllegalArgumentException {
        notEmptyOrNull(name, argumentName);
        lengthRange(name, minLength, maxLength, argumentName);
        match(name, CommonsValidationRegex.NAME_SPACE_REGEXP, argumentName);
    }
}
