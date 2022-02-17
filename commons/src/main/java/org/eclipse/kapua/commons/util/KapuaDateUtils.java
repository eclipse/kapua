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
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.commons.util;

import java.text.ParseException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
//import java.time.format.ResolverStyle;
import java.util.Date;
import java.util.Locale;

/**
 * Date utilities
 */
public final class KapuaDateUtils {

    private KapuaDateUtils() {
    }

    /**
     * {@literal uuuu} is used instead of {@literal yyyy} for the year because the latter is meant to be used together with the
     * era of the date, while the former is the proleptic year. Also, {@literal X} is used at the end instead of {@literal 'Z'}
     * because with {@literal 'Z'} we were blindly appending a Z at the end without actually relying on the timezone,
     * while with {@literal X} Z is printed only if the time difference is 0 (i.e. the timezone is UTC)
     *
     * @see <a href=https://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html>DateTimeFormatter</a>
     * @see <a href=https://stackoverflow.com/a/29014580/218901>Answer on StackOverflow</a>
     */
    public static final String ISO_DATE_PATTERN = "uuuu-MM-dd'T'HH:mm:ss.SSSX"; // example 2017-01-24T11:22:10.999Z

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter
            .ofPattern(ISO_DATE_PATTERN)
            .withLocale(KapuaDateUtils.getLocale())
            .withZone(getTimeZone());
    /**
     * Get current date
     *
     * @return current date
     */
    public static Instant getKapuaSysDate() {
        return Instant.now();
    }

    public static ZoneId getTimeZone() {
        return ZoneOffset.UTC;
    }

    public static Locale getLocale() {
        return Locale.US;
    }

    /**
     * Parse the provided String using the {@link KapuaDateUtils#ISO_DATE_PATTERN default pattern}
     *
     * @param date
     * @return
     * @throws ParseException
     */
    public static Date parseDate(String date) throws ParseException {
        if (date == null) {
            return null;
        } else {
            return Date.from(Instant.from(FORMATTER.parse(date)));
        }
    }

    /**
     * Format the provided Date using the {@link KapuaDateUtils#ISO_DATE_PATTERN default pattern}
     *
     * @param date
     * @return
     * @throws ParseException
     */
    public static String formatDate(Date date) throws ParseException {
        if (date == null) {
            return null;
        } else {
            return FORMATTER.format(date.toInstant());
        }
    }

}
