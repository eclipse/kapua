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
 *******************************************************************************/
package org.eclipse.kapua.message.internal;

import java.io.StringWriter;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.eclipse.kapua.commons.util.xml.XmlUtil;
import org.eclipse.kapua.message.KapuaPosition;
import org.eclipse.kapua.model.xml.DateXmlAdapter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class KapuaPositionTest extends Assert {

    private static final String NEWLINE = System.lineSeparator();

    private static final String POSITION_DISPLAY_STR = "^latitude=.*~~longitude=.*~~altitude=.*" +
            "~~precision=.*~~heading=.*~~speed=.*~~timestamp=.*~~satellites=.*~~status=.*$";

    private static final String DISPLAY_STR = "^\\{\"longitude\":.*, \"latitude\":.*, \"altitude\":.*" +
            ", \"precision\":.*, \"heading\":.*, \"speed\":.*, \"timestamp\":.*, \"satellites\":.*, \"status\":.*\\}$";

    private static ZonedDateTime referenceDate = ZonedDateTime.of(2017, 1, 18, 12, 10, 46, 238000000, ZoneId.of(DateXmlAdapter.TIME_ZONE_UTC));

    private static final String POSITION_XML_STR = //
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + NEWLINE +
                    "<position>" + NEWLINE +
                    "   <longitude>45.1111</longitude>" + NEWLINE +
                    "   <latitude>15.3333</latitude>" + NEWLINE +
                    "   <altitude>430.3</altitude>" + NEWLINE +
                    "   <precision>12.0</precision>" + NEWLINE +
                    "   <heading>280.0</heading>" + NEWLINE +
                    "   <speed>60.2</speed>" + NEWLINE +
                    "   <timestamp>2017-01-18T12:10:46.238Z</timestamp>" + NEWLINE +
                    "   <satellites>5</satellites>" + NEWLINE +
                    "   <status>4</status>" + NEWLINE +
                    "</position>" + NEWLINE;

    @Before
    public void before() throws Exception {
        XmlUtil.setContextProvider(new MessageJAXBContextProvider());
    }

    @Test
    public void positionGetterSetters() throws Exception {
        KapuaPosition position = new KapuaPositionImpl();
        KapuaMessageUtil.populatePosition(position, referenceDate);

        assertEquals(Double.valueOf("45.1111"), position.getLongitude());
        assertEquals(Double.valueOf("15.3333"), position.getLatitude());
        assertEquals(Double.valueOf("430.3"), position.getAltitude());
        assertEquals(Double.valueOf("12.0"), position.getPrecision());
        assertEquals(Double.valueOf("280.0"), position.getHeading());
        assertEquals(Double.valueOf("60.2"), position.getSpeed());
        assertNotNull(position.getTimestamp());
        assertEquals(Integer.valueOf(5), position.getSatellites());
        assertEquals(Integer.valueOf(4), position.getStatus());
    }

    @Test
    public void displayString() throws Exception {
        KapuaPosition position = new KapuaPositionImpl();
        KapuaMessageUtil.populatePosition(position, referenceDate);

        String displayStr = position.toDisplayString();
        assertTrue("\nExpected: " + POSITION_DISPLAY_STR +
                "\nActual:   " + displayStr,
                displayStr.matches(POSITION_DISPLAY_STR));
    }

    @Test
    public void displayEmptyPositionString() throws Exception {
        KapuaPosition position = new KapuaPositionImpl();

        String displayStr = position.toDisplayString();
        assertNull(displayStr);
    }

    @Test
    public void toStringValue() throws Exception {
        KapuaPosition position = new KapuaPositionImpl();
        KapuaMessageUtil.populatePosition(position, referenceDate);

        String toStr = position.toString();
        assertTrue("\nExpected: " + DISPLAY_STR +
                "\nActual:   " + toStr,
                toStr.matches(DISPLAY_STR));
    }

    @Test
    public void marshallPosition() throws Exception {
        KapuaPosition position = new KapuaPositionImpl();
        KapuaMessageUtil.populatePosition(position, referenceDate);

        StringWriter strWriter = new StringWriter();
        XmlUtil.marshal(position, strWriter);
        assertEquals(POSITION_XML_STR, strWriter.toString());
    }
}
