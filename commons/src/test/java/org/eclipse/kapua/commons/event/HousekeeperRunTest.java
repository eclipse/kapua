/*******************************************************************************
 * Copyright (c) 2020, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.commons.event;

import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Category(JUnitTests.class)
public class HousekeeperRunTest extends Assert {

    DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
    DateFormat format1 = new SimpleDateFormat("yyyy/mm/dd");
    DateFormat format2 = new SimpleDateFormat("MM-dd-yyyy");
    DateFormat format3 = new SimpleDateFormat("dd-M-yyyy");
    DateFormat format4 = new SimpleDateFormat("E, dd MMM yyyy");

    Date date, date1, date2, date3, date4;
    {
        try {
            date = format.parse("29/5/2020");
            date1 = format1.parse("2020/6/24");
            date2 = format2.parse("06-24-2020");
            date3 = format3.parse("24-6-2020");
            date4 = format4.parse("Wed, 24 Jun 2020");
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    HousekeeperRun houseKeeperRun;

    @Before
    public void createInstanceOfClass() {
        houseKeeperRun = new HousekeeperRun();
    }

    @Test
    public void setAndGetServiceNullTest() {
        houseKeeperRun.setService(null);
        assertNull("Null expected!", houseKeeperRun.getService());
    }

    @Test
    public void setAndGetServiceTest() {
        String[] permittedValues = {"", "!#$%&'()=?⁄@‹›€°·‚,.-;:_Èˇ¿<>«‘”’ÉØ∏{}|ÆæÒuF8FFÔÓÌÏÎÅ«»Ç◊Ñˆ¯Èˇ", "regularNaming", "regular Naming", "49", "regularNaming49", "NAMING", "246465494135646120009090049684646496468456468496846464968496844"};
        for (String value : permittedValues) {
            houseKeeperRun.setService(value);
            assertEquals("Expected and actual values should be the same!", value, houseKeeperRun.getService());
        }
    }

    @Test
    public void setAndGetLastRunOnNullTest() {
        houseKeeperRun.setLastRunOn(null);
        assertNull("Null expected!", houseKeeperRun.getLastRunOn());
    }

    @Test
    public void setAndGetLastRunOnDateFormatTest() {
        houseKeeperRun.setLastRunOn(date);
        assertEquals("Expected and actual values should be the same!", houseKeeperRun.getLastRunOn(), date);
    }

    @Test
    public void setAndGetLastRunOnDateFormat1Test() {
        houseKeeperRun.setLastRunOn(date1);
        assertEquals("Expected and actual values should be the same!", houseKeeperRun.getLastRunOn(), date1);
    }

    @Test
    public void setAndGetLastRunOnDateFormat2Test() {
        houseKeeperRun.setLastRunOn(date2);
        assertEquals("Expected and actual values should be the same!", houseKeeperRun.getLastRunOn(), date2);
    }

    @Test
    public void setAndGetLastRunOnDateFormat3Test() {
        houseKeeperRun.setLastRunOn(date3);
        assertEquals("Expected and actual values should be the same!", houseKeeperRun.getLastRunOn(), date3);
    }

    @Test
    public void setAndGetLastRunOnDateFormat4Test() {
        houseKeeperRun.setLastRunOn(date4);
        assertEquals("Expected and actual values should be the same!", houseKeeperRun.getLastRunOn(), date4);
    }

    @Test
    public void setAndGetLastRunByNullTest() {
        houseKeeperRun.setLastRunBy(null);
        assertNull("Null expected!", houseKeeperRun.getLastRunBy());
    }

    @Test
    public void setAndGetLastRunByTest() {
        String[] permittedValues = {"", "!#$%&'()=?⁄@‹›€°·‚,.-;:_Èˇ¿<>«‘”’ÉØ∏{}|ÆæÒuF8FFÔÓÌÏÎÅ«»Ç◊Ñˆ¯Èˇ", "regularNaming", "regular Naming", "49", "regularNaming49", "NAMING", "246465494135646120009090049684646496468456468496846464968496844"};
        for (String value : permittedValues) {
            houseKeeperRun.setLastRunBy(value);
            assertEquals("Expected and actual values should be the same!", value, houseKeeperRun.getLastRunBy());
        }
    }

    @Test
    public void setAndGetVersionNullTest() {
        houseKeeperRun.setVersion(null);
        assertNull("Null expected!", houseKeeperRun.getVersion());
    }

    @Test
    public void setAndGetVersionTest() {
        Long[] permittedValues = {-9223372036854775808L, 9223372036854775807L, 12345678910L, 1234L, 88928L, 1L};
        for(Long value : permittedValues) {
            houseKeeperRun.setVersion(value);
            assertEquals("Expected and actual values should be the same!", value, houseKeeperRun.getVersion());
        }
    }
}
