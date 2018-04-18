/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.console.module.api.client.util;

import java.util.Date;

import com.google.gwt.i18n.client.DateTimeFormat;

public class DateUtils {

//    private static final ConsoleMessages MSGS = GWT.create(ConsoleMessages.class);

    private static final String NONE = "None";
    private static final String FORMAT = "dd MMM yyyy HH:mm:ss ZZZZ";

    private DateUtils() {
    }

    /**
     * formatDate takes a date an return its string representation with date and time
     */
    public static String formatDateTime(Date d) {
        if (d == null) {
            return NONE;
        }

        return DateTimeFormat.getFormat(FORMAT).format(d);
    }

    public static int getYear(Date date) {
        return Integer.parseInt(DateTimeFormat.getFormat("yyyy").format(date));
    }

    public static int getMonth(Date date) {
        // NB: The month in DateTimeFormat is not zero based [unlike Date() & Calendar()] so we need to subtract one when getting !!!
        return Integer.parseInt(DateTimeFormat.getFormat("MM").format(date)) - 1;
    }

    public static Date setYear(Date date, int year) {
        String dateString = DateTimeFormat.getFormat("yyyy.MM.dd.HH.mm.ss.SSS").format(date);

        StringBuilder sb = new StringBuilder();
        sb.append(pad(Integer.toString(year), 4));
        sb.append(dateString.substring(4));

        return DateTimeFormat.getFormat("yyyy.MM.dd.HH.mm.ss.SSS").parse(sb.toString());
    }

    public static Date setMonth(Date date, int month) {
        String dateString = DateTimeFormat.getFormat("yyyy.MM.dd.HH.mm.ss.SSS").format(date);

        StringBuilder sb = new StringBuilder();
        sb.append(dateString.substring(0, 5));
        // NB: The month in DateTimeFormat is not zero based [unlike Date() & Calendar()] so we need to add one when setting !!!
        sb.append(pad(Integer.toString(month + 1), 2));
        sb.append(dateString.substring(7));

        return DateTimeFormat.getFormat("yyyy.MM.dd.HH.mm.ss.SSS").parse(sb.toString());
    }

    public static Date setDayOfMonth(Date date, int dayOfMonth) {
        String dateString = DateTimeFormat.getFormat("yyyy.MM.dd.HH.mm.ss.SSS").format(date);

        StringBuilder sb = new StringBuilder();
        sb.append(dateString.substring(0, 8));
        sb.append(pad(Integer.toString(dayOfMonth), 2));
        sb.append(dateString.substring(10));

        return DateTimeFormat.getFormat("yyyy.MM.dd.HH.mm.ss.SSS").parse(sb.toString());
    }

    public static Date setToLastDayOfMonth(Date date) {
        String dateString = DateTimeFormat.getFormat("yyyy.MM.dd.HH.mm.ss.SSS").format(date);

        StringBuilder sb = new StringBuilder();
        sb.append(dateString.substring(0, 8));

        switch (getMonth(date)) {
        case 0: // Jan
            sb.append("31");
            break;
        case 1: // Feb
            if (isLeapYear(getYear(date))) {
                sb.append("29");
            } else {
                sb.append("28");
            }
            break;
        case 2: // Mar
            sb.append("31");
            break;
        case 3: // Apr
            sb.append("30");
            break;
        case 4: // May
            sb.append("31");
            break;
        case 5: // June
            sb.append("30");
            break;
        case 6: // July
            sb.append("31");
            break;
        case 7: // August
            sb.append("31");
            break;
        case 8: // Sept
            sb.append("30");
            break;
        case 9: // Oct
            sb.append("31");
            break;
        case 10: // Nov
            sb.append("30");
            break;
        case 11: // Dec
            sb.append("31");
            break;
        default:
            break;
        }

        sb.append(dateString.substring(10));

        return DateTimeFormat.getFormat("yyyy.MM.dd.HH.mm.ss.SSS").parse(sb.toString());
    }

    public static Date setHour(Date date, int hour) {
        String dateString = DateTimeFormat.getFormat("yyyy.MM.dd.HH.mm.ss.SSS").format(date);

        StringBuilder sb = new StringBuilder();
        sb.append(dateString.substring(0, 11));
        sb.append(pad(Integer.toString(hour), 2));
        sb.append(dateString.substring(13));

        return DateTimeFormat.getFormat("yyyy.MM.dd.HH.mm.ss.SSS").parse(sb.toString());
    }

    public static Date setMinute(Date date, int minute) {
        String dateString = DateTimeFormat.getFormat("yyyy.MM.dd.HH.mm.ss.SSS").format(date);

        StringBuilder sb = new StringBuilder();
        sb.append(dateString.substring(0, 14));
        sb.append(pad(Integer.toString(minute), 2));
        sb.append(dateString.substring(16));

        return DateTimeFormat.getFormat("yyyy.MM.dd.HH.mm.ss.SSS").parse(sb.toString());
    }

    public static Date setSecond(Date date, int second) {
        String dateString = DateTimeFormat.getFormat("yyyy.MM.dd.HH.mm.ss.SSS").format(date);

        StringBuilder sb = new StringBuilder();
        sb.append(dateString.substring(0, 17));
        sb.append(pad(Integer.toString(second), 2));
        sb.append(dateString.substring(19));

        return DateTimeFormat.getFormat("yyyy.MM.dd.HH.mm.ss.SSS").parse(sb.toString());
    }

    public static Date setMillisecond(Date date, int millisecond) {
        String dateString = DateTimeFormat.getFormat("yyyy.MM.dd.HH.mm.ss.SSS").format(date);

        StringBuilder sb = new StringBuilder();
        sb.append(dateString.substring(0, 20));
        sb.append(pad(Integer.toString(millisecond), 3));

        return DateTimeFormat.getFormat("yyyy.MM.dd.HH.mm.ss.SSS").parse(sb.toString());
    }

    private static String pad(String data, int size) {
        if (data.length() < size) {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < (size - data.length()); i++) {
                sb.append("0");
            }
            sb.append(data);
            return sb.toString();
        }

        return data;
    }

    private static boolean isLeapYear(int year) {
        return year % 4 == 0;
    }
}
