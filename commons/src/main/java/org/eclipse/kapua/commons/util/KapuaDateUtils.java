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
 *******************************************************************************/
package org.eclipse.kapua.commons.util;

import java.text.ParseException;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.kapua.commons.setting.system.SystemSetting;
import org.eclipse.kapua.commons.setting.system.SystemSettingKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Date utilities
 */
public final class KapuaDateUtils {

    private static final Logger logger = LoggerFactory.getLogger(KapuaDateUtils.class);

    private final static ZoneId ZONE_ID;
    private final static Locale LOCALE;
    private final static int MINIMAL_DAYS_IN_FIRST_WEEK;
    private final static DayOfWeek FIRST_DAY_OF_THE_WEEK;

    static {
        String zoneId = SystemSetting.getInstance().getString(SystemSettingKey.LOCALE_ZONE_ID);
        if (StringUtils.isEmpty(zoneId)) {
            zoneId = "UTC";
            logger.warn("Cannot find {} parameter. Using default value UTC!", SystemSettingKey.LOCALE_ZONE_ID.key());
        }
        logger.info("Locale: Set zone id to {}", zoneId);
        ZONE_ID = ZoneId.of(zoneId);

        String locale = SystemSetting.getInstance().getString(SystemSettingKey.LOCALE_ID);
        if (StringUtils.isEmpty(locale)) {
            locale = "en_US";
            logger.warn("Cannot find {} parameter. Using default value en_US!", SystemSettingKey.LOCALE_ID.key());
        }
        logger.info("Locale: Set locale to {}", locale);
        LOCALE = new Locale(locale);

        String minDaysInFirstWeekString = SystemSetting.getInstance().getString(SystemSettingKey.LOCALE_MINIMAL_DAYS_IN_FIRST_WEEK);
        int minDaysInFirstWeek = 0;
        try {
            minDaysInFirstWeek = Integer.parseInt(minDaysInFirstWeekString);
        }
        catch (NumberFormatException e) {
            //do nothing
            logger.warn("Cannot parse {} parameter (value {})", SystemSettingKey.LOCALE_MINIMAL_DAYS_IN_FIRST_WEEK.key(), minDaysInFirstWeekString);
        }
        if (minDaysInFirstWeek<=0 || minDaysInFirstWeek>7) {
            minDaysInFirstWeek = 7;
            logger.warn("Cannot find {} parameter. Using default value 7!", SystemSettingKey.LOCALE_MINIMAL_DAYS_IN_FIRST_WEEK.key());
        }
        logger.info("Locale: Set minimum days in first week to {}", minDaysInFirstWeek);
        MINIMAL_DAYS_IN_FIRST_WEEK = minDaysInFirstWeek;

        String firstDayOfTheWeekString = SystemSetting.getInstance().getString(SystemSettingKey.LOCALE_FIRST_DAY_OF_THE_WEEK);
        int firstDayOfTheWeek = 0;
        try {
            firstDayOfTheWeek = Integer.parseInt(firstDayOfTheWeekString);
        }
        catch (NumberFormatException e) {
            //do nothing
            logger.warn("Cannot parse {} parameter (value {})", SystemSettingKey.LOCALE_FIRST_DAY_OF_THE_WEEK.key(), firstDayOfTheWeekString);
        }
        if (firstDayOfTheWeek<=0 || firstDayOfTheWeek>7) {
            firstDayOfTheWeek = 1;
            logger.warn("Cannot find {} parameter. Using default value 1!", SystemSettingKey.LOCALE_FIRST_DAY_OF_THE_WEEK.key());
        }
        logger.info("Locale: Set first day of the week to {} (1 MONDAY - 7 SUNDAY)", firstDayOfTheWeek);
        //1 MONDAY - 7 SUNDAY
        FIRST_DAY_OF_THE_WEEK = DayOfWeek.of(firstDayOfTheWeek);
    }

    private KapuaDateUtils() {
    }

    public static final String ISO_DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"; // example 24/01/2017T11:22:10.999Z

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter
            .ofPattern(ISO_DATE_PATTERN)
            .withLocale(KapuaDateUtils.getLocale())
            .withZone(KapuaDateUtils.getTimeZone());
    /**
     * Get current date
     *
     * @return current date
     */
    public static Instant getKapuaSysDate() {
        return Instant.now();
    }

    public static ZoneId getTimeZone() {
        return ZONE_ID;
    }

    public static Locale getLocale() {
        return LOCALE;
    }

    public static Integer getMinimalDaysInFirstWeek() {
        return MINIMAL_DAYS_IN_FIRST_WEEK;
    }

    public static DayOfWeek getFirstDayOfTheWeek() {
        return FIRST_DAY_OF_THE_WEEK;
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
