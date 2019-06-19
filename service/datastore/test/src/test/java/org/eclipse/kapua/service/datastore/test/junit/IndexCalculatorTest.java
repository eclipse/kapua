/*******************************************************************************
 * Copyright (c) 2017, 2019 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.datastore.test.junit;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.commons.util.KapuaDateUtils;
import org.eclipse.kapua.commons.util.xml.XmlUtil;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.datastore.internal.mediator.DatastoreException;
import org.eclipse.kapua.service.datastore.internal.mediator.DatastoreUtils;

import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

@Category(JUnitTests.class)
public class IndexCalculatorTest extends Assert {

    private static final Logger LOG = LoggerFactory.getLogger(IndexCalculatorTest.class);

    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm Z");

    @BeforeClass
    public static void setUpBeforeClass() {
        XmlUtil.setContextProvider(new DatastoreJAXBContextProvider());
    }

    @Test
    public void testIndex() throws KapuaException, ParseException {
        // performTest(sdf.parse("01/01/2000 13:12 +0100"), sdf.parse("01/01/2020 13:12 +0100"), buildExpectedResult("1", 1, 2000, 1, 2020, new int[] {
        // 53,// 2000 for locale us - 52 for locale "Europe"
        // 52,// 2001
        // 52,// 2002
        // 52,// 2003
        // 53,// 2004
        // 53,// 2005 for locale us - 52 for locale "Europe"
        // 52,// 2006
        // 52,// 2007
        // 52,// 2008
        // 53,// 2009
        // 53,// 2010 for locale us - 52 for locale "Europe"
        // 53,// 2011 for locale us - 52 for locale "Europe"
        // 52,// 2012
        // 52,// 2013
        // 52,// 2014
        // 53,// 2015 for locale us - 52 for locale "Europe"
        // 53,// 2016 for locale us - 52 for locale "Europe"
        // 52,// 2017
        // 52,// 2018
        // 52,// 2019
        // 53// 2020 for locale us - 52 for locale "Europe"
        //
        // }));
        performTest(sdf.parse("02/01/2017 13:12 +0100"), sdf.parse("02/07/2017 13:12 +0100"), buildExpectedResult("1", 2, 2017, 25, 2017, null));
        performTest(sdf.parse("02/01/2017 13:12 +0100"), sdf.parse("01/07/2017 13:12 +0100"), buildExpectedResult("1", 2, 2017, 25, 2017, null));
        performTest(sdf.parse("01/01/2017 13:12 +0100"), sdf.parse("02/07/2017 13:12 +0100"), buildExpectedResult("1", 1, 2017, 25, 2017, null));
        performTest(sdf.parse("31/12/2016 13:12 +0100"), sdf.parse("02/07/2017 13:12 +0100"), buildExpectedResult("1", 1, 2017, 25, 2017, null));
        performTest(sdf.parse("01/01/2017 13:12 +0100"), sdf.parse("01/07/2017 13:12 +0100"), buildExpectedResult("1", 1, 2017, 25, 2017, null));
        performTest(sdf.parse("02/01/2017 13:12 +0100"), sdf.parse("03/07/2017 13:12 +0100"), buildExpectedResult("1", 2, 2017, 26, 2017, null));
        performTest(sdf.parse("02/01/2017 13:12 +0100"), sdf.parse("03/07/2017 13:12 +0100"), buildExpectedResult("1", 2, 2017, 26, 2017, null));
        performTest(sdf.parse("01/01/2017 13:12 +0100"), sdf.parse("03/07/2017 13:12 +0100"), buildExpectedResult("1", 1, 2017, 26, 2017, null));

        performTest(sdf.parse("31/12/2016 13:12 +0100"), sdf.parse("09/01/2017 13:12 +0100"), buildExpectedResult("1", 1, 2017, 1, 2017, null));
        performTest(sdf.parse("01/01/2017 13:12 +0100"), sdf.parse("09/01/2017 13:12 +0100"), buildExpectedResult("1", 1, 2017, 1, 2017, null));
        performTest(sdf.parse("01/01/2017 13:12 +0100"), sdf.parse("06/01/2017 13:12 +0100"), null);
        performTest(sdf.parse("01/01/2017 13:12 +0100"), sdf.parse("08/01/2017 13:12 +0100"), null);
        performTest(sdf.parse("02/01/2017 13:12 +0100"), sdf.parse("08/01/2017 13:12 +0100"), null);
        performTest(sdf.parse("02/01/2017 13:12 +0100"), sdf.parse("09/01/2017 13:12 +0100"), null);
        performTest(sdf.parse("02/01/2017 13:12 +0100"), sdf.parse("09/01/2017 13:12 +0100"), null);

        performTest(null, sdf.parse("09/04/2015 13:12 +0100"), buildExpectedResult("1", 1, 2015, 14, 2015, null));
        performTest(null, sdf.parse("06/04/2015 13:12 +0100"), buildExpectedResult("1", 1, 2015, 14, 2015, null));
        performTest(null, sdf.parse("05/04/2015 13:12 +0100"), buildExpectedResult("1", 1, 2015, 13, 2015, null));
        performTest(null, sdf.parse("13/04/2015 13:12 +0100"), buildExpectedResult("1", 1, 2015, 15, 2015, null));

        performTest(sdf.parse("09/12/2018 13:12 +0100"), null, buildExpectedResult("1", 50, 2018, 52, 2018, null));
        performTest(sdf.parse("10/12/2018 13:12 +0100"), null, buildExpectedResult("1", 51, 2018, 52, 2018, null));
        performTest(sdf.parse("08/12/2018 13:12 +0100"), null, buildExpectedResult("1", 50, 2018, 52, 2018, null));
        performTest(sdf.parse("17/12/2018 13:12 +0100"), null, buildExpectedResult("1", 52, 2018, 52, 2018, null));

        performNullIndexTest(sdf.parse("02/01/2017 13:12 +0100"), sdf.parse("09/01/2017 13:12 +0100"));
    }

    @Test
    public void dataIndexNameByScopeId() {
        assertEquals("1-*", DatastoreUtils.getDataIndexName(KapuaId.ONE));
    }

    @Test
    public void dataIndexNameByScopeIdAndTimestamp() throws KapuaException, ParseException {

        // Index by Week
        String weekIndexName = DatastoreUtils.getDataIndexName(KapuaId.ONE, sdf.parse("02/01/2017 13:12 +0100").getTime(), DatastoreUtils.INDEXING_WINDOW_OPTION_WEEK);
        assertEquals("1-2017-01", weekIndexName);

        // Index by Day
        String dayIndexName = DatastoreUtils.getDataIndexName(KapuaId.ONE, sdf.parse("02/01/2017 13:12 +0100").getTime(), DatastoreUtils.INDEXING_WINDOW_OPTION_DAY);
        assertEquals("1-2017-01-02", dayIndexName);

        // Index by Hour
        String hourIndexName = DatastoreUtils.getDataIndexName(KapuaId.ONE, sdf.parse("02/01/2017 13:12 +0100").getTime(), DatastoreUtils.INDEXING_WINDOW_OPTION_HOUR);
        assertEquals("1-2017-01-02-12", hourIndexName);     // Index Hour is UTC!
    }

    @Test
    public void registryIndexNameByScopeId() {
        assertEquals(".1", DatastoreUtils.getRegistryIndexName(KapuaId.ONE));
    }

    private void performTest(Date startDate, Date endDate, String[] expectedIndexes) throws DatastoreException {
        Calendar calStartDate = null;
        Calendar calEndDate = null;
        if (startDate != null) {
            calStartDate = Calendar.getInstance(TimeZone.getTimeZone("UTC"), KapuaDateUtils.getLocale());
            calStartDate.setTimeInMillis(startDate.getTime());
        }
        if (endDate != null) {
            calEndDate = Calendar.getInstance(TimeZone.getTimeZone("UTC"), KapuaDateUtils.getLocale());
            calEndDate.setTimeInMillis(endDate.getTime());
        }

        LOG.info("StartDate week {} - day {} *** EndDate week {} - day {}",
                calStartDate != null ? calStartDate.get(Calendar.WEEK_OF_YEAR) : "Infinity",
                calStartDate != null ? calStartDate.get(Calendar.DAY_OF_WEEK) : "Infinity",
                calEndDate != null ? calEndDate.get(Calendar.WEEK_OF_YEAR) : "Infinity",
                calEndDate != null ? calEndDate.get(Calendar.DAY_OF_WEEK) : "Infinity");

        String[] index = DatastoreUtils.convertToDataIndexes(getDataIndexesByAccount(KapuaEid.ONE), startDate != null ? startDate.toInstant() : null, endDate != null ? endDate.toInstant() : null);
        compareResult(expectedIndexes, index);
    }

    private void performNullIndexTest(Date startDate, Date endDate) throws DatastoreException {
        Calendar calStartDate = null;
        Calendar calEndDate = null;
        if (startDate != null) {
            calStartDate = Calendar.getInstance(TimeZone.getTimeZone("UTC"), KapuaDateUtils.getLocale());
            calStartDate.setTimeInMillis(startDate.getTime());
        }
        if (endDate != null) {
            calEndDate = Calendar.getInstance(TimeZone.getTimeZone("UTC"), KapuaDateUtils.getLocale());
            calEndDate.setTimeInMillis(endDate.getTime());
        }

        LOG.info("StartDate week {} - day {} *** EndDate week {} - day {}",
                calStartDate != null ? calStartDate.get(Calendar.WEEK_OF_YEAR) : "Infinity",
                calStartDate != null ? calStartDate.get(Calendar.DAY_OF_WEEK) : "Infinity",
                calEndDate != null ? calEndDate.get(Calendar.WEEK_OF_YEAR) : "Infinity",
                calEndDate != null ? calEndDate.get(Calendar.DAY_OF_WEEK) : "Infinity");

        String[] index = DatastoreUtils.convertToDataIndexes(new String[]{ null, null }, startDate != null ? startDate.toInstant() : null, endDate != null ? endDate.toInstant() : null);
        compareResult(null, index);
    }

    private String[] buildExpectedResult(String scopeId, int startWeek, int startYear, int endWeek, int endYear, int[] weekCountByYear) {
        List<String> result = new ArrayList<>();
        for (int i = startYear; i <= endYear; i++) {
            int startWeekForCurrentYear = startWeek;
            if (i != endYear) {
                startWeekForCurrentYear = 1;
            }
            int endWeekForCurrentYear = endWeek;
            if (i != endYear) {
                endWeekForCurrentYear = weekCountByYear[endYear - i];
            }
            for (int j = startWeekForCurrentYear; j <= endWeekForCurrentYear; j++) {
                result.add(String.format("%s-%s-%s", scopeId, i, (j < 10 ? "0" + j : j)));
            }
        }
        return result.toArray(new String[0]);
    }

    private void compareResult(String[] expected, String[] result) {
        if (result != null) {
            assertEquals("Wrong result size!", (expected != null ? expected.length : 0), result.length);
            for (int i = 0; i < result.length; i++) {
                assertEquals(String.format("Wrong result at position {0}!", i), expected[i], result[i]);
            }
        } else {
            assertTrue("Wrong result size!", expected == null || expected.length <= 0);
        }
    }

    private String[] getDataIndexesByAccount(KapuaId scopeId) {
        return buildExpectedResult("1", 1, 2015, 52, 2018, new int[]{ 53, 52, 52, 52 });
    }
}
