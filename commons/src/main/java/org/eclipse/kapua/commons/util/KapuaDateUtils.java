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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Date utilities
 * 
 * @since 1.0
 *
 */
public class KapuaDateUtils {

    public static final String DEFAULT_DATE_PATTERN = "MM/dd/yyyy h:mm a"; // example 24/01/2017 11:22 AM
	
    public static final long    SEC_MILLIS       = 1000;
    public static final long    MIN_SECS         = 60;
    public static final long    HOUR_MINS        = 60;
    public static final long    DAY_HOURS        = 24;
    public static final long    DAY_SECS         = DAY_HOURS * HOUR_MINS * MIN_SECS;
    public static final long    DAY_MILLIS       = DAY_SECS * SEC_MILLIS;

    /**
     * Get the Kapua {@link Locale}
     * 
     * @return
     */
	public static Locale getKapuaLocale() {
		return Locale.US;
	}

    /**
     * Get the Kapua {@link TimeZone}
     * 
     * @return
     */
	public static TimeZone getKapuaTimeZone() {
		return TimeZone.getTimeZone("UTC");
	}
	
    /**
     * Get the Kapua {@link Calendar}. It's a {@link Calendar} instance instantiated with Kapua Locale and Kapua TimeZone
     * 
     * @return
     */
	public static Calendar getKapuaCalendar() {
		Calendar cal = Calendar.getInstance(getKapuaTimeZone(), getKapuaLocale());
		return cal;
	}
	
	/**
     * Get current date
     * 
     * @return current date GMT
     */
	public static Date getKapuaSysDate() {
		Calendar cal = KapuaDateUtils.getKapuaCalendar();
		return cal.getTime();
	}
	
	/**
     * Get a date from the millis value (unix millis)
     * 
     * @param millis
     * @return date GMT
     */
	public static Date getDateFromMillis(long millis) {
		Calendar cal = KapuaDateUtils.getKapuaCalendar();
		cal.setTimeInMillis(millis);
		return cal.getTime();
	}
	
    /**
     * Get the week of year for the provided date
     * 
     * @param date
     * @return
     */
	public static int weekOfTheYear(Date date) {
		Calendar cal = KapuaDateUtils.getKapuaCalendar();
		cal.setTime(date);
		return cal.get(Calendar.WEEK_OF_YEAR);
	}	
	
    /**
     * Get the first week day date from the provided date
     * 
     * @param date
     * @return
     */
	public static Date weekByFirstDay(Date date) {
		Calendar cal = KapuaDateUtils.getKapuaCalendar();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.clear(Calendar.MINUTE);
		cal.clear(Calendar.SECOND);
		cal.clear(Calendar.MILLISECOND);

		// get start of this week in milliseconds
		cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
		return cal.getTime();
	}
	
    /**
     * Get the start week year date for the provided year
     * 
     * @param year
     * @param weekOfYear
     * @return
     */
	public static Date getStartOfWeek(int year, int weekOfYear) {
		Calendar cal = KapuaDateUtils.getKapuaCalendar();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.WEEK_OF_YEAR, weekOfYear);
		cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
		cal.set(Calendar.HOUR_OF_DAY, cal.getMinimum(Calendar.HOUR_OF_DAY));
		cal.set(Calendar.MINUTE, cal.getMinimum(Calendar.MINUTE));
		cal.set(Calendar.SECOND, cal.getMinimum(Calendar.SECOND));
		cal.set(Calendar.MILLISECOND, cal.getMinimum(Calendar.MILLISECOND));
		return cal.getTime();
	}

    /**
     * Parse the provided String using the default pattern {@value #DEFAULT_DATE_PATTERN} and the default locale {@link #getKapuaLocale() getKapuaLocale}
     * 
     * @param date
     * @return
     * @throws ParseException
     */
    public static Date parseDate(String date) throws ParseException
    {
        DateFormat df = new SimpleDateFormat(DEFAULT_DATE_PATTERN, getKapuaLocale());
        return df.parse(date);
    }

}
