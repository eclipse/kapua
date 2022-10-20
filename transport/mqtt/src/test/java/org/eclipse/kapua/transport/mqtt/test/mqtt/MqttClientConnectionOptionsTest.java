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
package org.eclipse.kapua.transport.mqtt.test.mqtt;

import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.eclipse.kapua.transport.mqtt.MqttClientConnectionOptions;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.net.URI;
import java.net.URISyntaxException;


@Category(JUnitTests.class)
public class MqttClientConnectionOptionsTest {

    MqttClientConnectionOptions connectOptions;

    @Before
    public void createInstanceOfClass() {
        connectOptions = new MqttClientConnectionOptions();
    }

    @Test
    public void setAndGetClientIdTest() {
        String[] stringValues = {"", "!#$%&'()=?⁄@‹›€°·‚,.-;:_Èˇ¿<>«‘”’ÉØ∏{}|ÆæÒuF8FFÔÓÌÏÎÅ«»Ç◊Ñˆ¯Èˇ", "regularNaming", "regular Naming", "49", "regularNaming49", "NAMING", "246465494135646120009090049684646496468456468496846464968496844ppooqqqqweqrttskjoijjnbvzbhdsjkpk++adasdascdadserfaolkaiw;leqawoejoaidmn,masdnjokjaduiyqhwidbhnaskjfhaskidhnkauidhkauisdjhdhadnjkahduiqhdeihjoljiaolidjpqdjp;qkd';adkpoakdpoqjwoejqwoejqldfkjlasjf"};
        for (String value : stringValues) {
            connectOptions.setClientId(value);
            Assert.assertEquals("Expected and actual values are not equal!", value, connectOptions.getClientId());
        }
    }

    @Test
    public void setAndGetClientIdNullTest() {
        connectOptions.setClientId(null);
        Assert.assertNull("Null expected!", connectOptions.getClientId());
    }

    @Test
    public void setAndGetUsernameTest() {
        String[] stringValues = {"", "!#$%&'()=?⁄@‹›€°·‚,.-;:_Èˇ¿<>«‘”’ÉØ∏{}|ÆæÒuF8FFÔÓÌÏÎÅ«»Ç◊Ñˆ¯Èˇ", "regularNaming", "regular Naming", "49", "regularNaming49", "NAMING", "246465494135646120009090049684646496468456468496846464968496844ppooqqqqweqrttskjoijjnbvzbhdsjkpk++adasdascdadserfaolkaiw;leqawoejoaidmn,masdnjokjaduiyqhwidbhnaskjfhaskidhnkauidhkauisdjhdhadnjkahduiqhdeihjoljiaolidjpqdjp;qkd';adkpoakdpoqjwoejqwoejqldfkjlasjf"};
        for (String value : stringValues) {
            connectOptions.setUsername(value);
            Assert.assertEquals("Expected and actual values are not equal!", value, connectOptions.getUsername());
        }
    }

    @Test
    public void setAndGetUsernameNullTest() {
        connectOptions.setUsername(null);
        Assert.assertNull("Null expected!", connectOptions.getUsername());
    }

    @Test
    public void setAndGetPasswordTest() {
        String[] stringValues = {"", "!#$%&'()=?⁄@‹›€°·‚,.-;:_Èˇ¿<>«‘”’ÉØ∏{}|ÆæÒuF8FFÔÓÌÏÎÅ«»Ç◊Ñˆ¯Èˇ", "regularNaming", "regular Naming", "49", "regularNaming49", "NAMING", "246465494135646120009090049684646496468456468496846464968496844ppooqqqqweqrttskjoijjnbvzbhdsjkpk++adasdascdadserfaolkaiw;leqawoejoaidmn,masdnjokjaduiyqhwidbhnaskjfhaskidhnkauidhkauisdjhdhadnjkahduiqhdeihjoljiaolidjpqdjp;qkd';adkpoakdpoqjwoejqwoejqldfkjlasjf"};
        for (String value : stringValues) {
            char[] charValue = value.toCharArray();
            connectOptions.setPassword(charValue);
            Assert.assertEquals("Expected and actual values are not equal!", charValue, connectOptions.getPassword());
        }
    }

    @Test
    public void setAndGetPasswordNullTest() {
        connectOptions.setPassword(null);
        Assert.assertNull("Null expected!", connectOptions.getPassword());
    }

    @Test
    public void setAndGetURITest() throws URISyntaxException {
        URI[] uriPermittedFormats = { new URI("https://john.doe@www.example.com:123/forum/questions/?tag=networking&order=newest#top"), new URI("ldap://[2001:db8::7]/c=GB?objectClass?one"), new URI("mailto:Username.example@example.com"), new URI("news:comp.infosystems.www.servers.unix"), new URI("tel:+1-816-555-1212"), new URI("telnet://192.0.2.16:80/"), new URI("urn:oasis:names:specification:docbook:dtd:xml:4.1.2")};
        for (URI value : uriPermittedFormats) {
            connectOptions.setEndpointURI(value);
            Assert.assertEquals("Expected and actual values are not equal!", value, connectOptions.getEndpointURI());
        }
    }

    @Test
    public void setAndGetURINullTest() {
        connectOptions.setEndpointURI(null);
        Assert.assertNull("Null expected!", connectOptions.getEndpointURI());
    }
}
