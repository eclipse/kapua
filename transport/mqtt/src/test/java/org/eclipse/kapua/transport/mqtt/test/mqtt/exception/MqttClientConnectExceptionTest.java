/*******************************************************************************
 * Copyright (c) 2020 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.transport.mqtt.test.mqtt.exception;

import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.eclipse.kapua.transport.mqtt.exception.MqttClientConnectException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.net.URI;
import java.net.URISyntaxException;

@Category(JUnitTests.class)
public class MqttClientConnectExceptionTest extends Assert {

    MqttClientConnectException exception1, exception2;
    Throwable cause;

    private String clientID = "clientName";
    private String username = "username";

    @Before
    public void createInstancesOfClasses() throws URISyntaxException {
        exception1 = new MqttClientConnectException(clientID, username, new URI("https://john.doe@www.example.com:123/forum/questions/?tag=networking&order=newest#top"));
        exception2 = new MqttClientConnectException(cause, clientID, username, new URI("https://john.doe@www.example.com:123/forum/questions/?tag=networking&order=newest#top"));
        cause = new Throwable();
    }


    @Test
    public void constructorValidTest() throws URISyntaxException {
        URI[] uriPermittedFormats = { new URI("https://john.doe@www.example.com:123/forum/questions/?tag=networking&order=newest#top"), new URI("ldap://[2001:db8::7]/c=GB?objectClass?one"), new URI("mailto:Username.example@example.com"), new URI("news:comp.infosystems.www.servers.unix"), new URI("tel:+1-816-555-1212"), new URI("telnet://192.0.2.16:80/"), new URI("urn:oasis:names:specification:docbook:dtd:xml:4.1.2")};
        for (URI value : uriPermittedFormats) {
            MqttClientConnectException exception = new MqttClientConnectException(clientID, username, value);
            assertEquals("Expected and actual values should be the same!", clientID, exception.getClientId());
            assertEquals("Expected and actual values should be the same!", username, exception.getUsername());
            assertEquals("Expected and actual values should be the same!", value, exception.getUri());
        }
    }

    @Test
    public void constructorURINullTest() {
        try {
            MqttClientConnectException exception = new MqttClientConnectException(clientID, username, null);
            assertEquals("Expected and actual values should be the same!", clientID, exception.getClientId());
            assertEquals("Expected and actual values should be the same!", username, exception.getUsername());
            assertNull("Null expected!", exception.getUri());
        } catch (Exception ex) {
            fail("No exception expected!");
        }
    }

    @Test
    public void constructorUsernameNullTest() throws URISyntaxException {
        URI[] uriPermittedFormats = { new URI("https://john.doe@www.example.com:123/forum/questions/?tag=networking&order=newest#top"), new URI("ldap://[2001:db8::7]/c=GB?objectClass?one"), new URI("mailto:Username.example@example.com"), new URI("news:comp.infosystems.www.servers.unix"), new URI("tel:+1-816-555-1212"), new URI("telnet://192.0.2.16:80/"), new URI("urn:oasis:names:specification:docbook:dtd:xml:4.1.2")};
        for (URI value : uriPermittedFormats) {
            try {
                MqttClientConnectException exception = new MqttClientConnectException(clientID, null, value);
                assertEquals("Expected and actual values should be the same!", clientID, exception.getClientId());
                assertNull("Null expected!", exception.getUsername());
                assertEquals("Expected and actual values should be the same!", value, exception.getUri());
            } catch (Exception ex) {
                fail("No exception expected!");
            }
        }
    }

    @Test
    public void constructorClientIdNullTest() throws URISyntaxException {
        URI[] uriPermittedFormats = { new URI("https://john.doe@www.example.com:123/forum/questions/?tag=networking&order=newest#top"), new URI("ldap://[2001:db8::7]/c=GB?objectClass?one"), new URI("mailto:Username.example@example.com"), new URI("news:comp.infosystems.www.servers.unix"), new URI("tel:+1-816-555-1212"), new URI("telnet://192.0.2.16:80/"), new URI("urn:oasis:names:specification:docbook:dtd:xml:4.1.2")};
        for (URI value : uriPermittedFormats) {
            try {
                MqttClientConnectException exception = new MqttClientConnectException(null, username, value);
                assertNull("Null expected!", exception.getClientId());
                assertEquals("Expected and actual values should be the same!", username, exception.getUsername());
                assertEquals("Expected and actual values should be the same!", value, exception.getUri());
            } catch (Exception ex) {
                fail("No exception expected!");
            }
        }
    }

    @Test
    public void constructorAllNullTest() {
        try {
            MqttClientConnectException exception = new MqttClientConnectException(null, null, null);
            assertNull("Null expected!", exception.getClientId());
            assertNull("Null expected!", exception.getUsername());
            assertNull("Null expected!", exception.getUri());
        } catch (Exception ex) {
            fail("No exception expected!");
        }
    }

    @Test (expected = MqttClientConnectException.class)
    public void throwingExceptionUsingFirstConstructorTest() throws MqttClientConnectException {
        throw exception1;
    }

    @Test
    public void secondConstructorValidTest() throws URISyntaxException {
        URI[] uriPermittedFormats = { new URI("https://john.doe@www.example.com:123/forum/questions/?tag=networking&order=newest#top"), new URI("ldap://[2001:db8::7]/c=GB?objectClass?one"), new URI("mailto:Username.example@example.com"), new URI("news:comp.infosystems.www.servers.unix"), new URI("tel:+1-816-555-1212"), new URI("telnet://192.0.2.16:80/"), new URI("urn:oasis:names:specification:docbook:dtd:xml:4.1.2")};
        for (URI value : uriPermittedFormats) {
            try {
                MqttClientConnectException exception = new MqttClientConnectException(cause, clientID, username, value);
                assertEquals("Expected and actual values should be the same!", clientID, exception.getClientId());
                assertEquals("Expected and actual values should be the same!", username, exception.getUsername());
                assertEquals("Expected and actual values should be the same!", cause, exception.getCause());
                assertEquals("Expected and actual values should be the same!", value, exception.getUri());
                assertEquals("Expected and actual values should be the same!", "CONNECT_ERROR", exception.getCode().toString());
            } catch (Exception ex) {
                fail("No exception expected!");
            }
        }
    }

    @Test
    public void secondConstructorThrowableNullTest() throws URISyntaxException {
        URI[] uriPermittedFormats = { new URI("https://john.doe@www.example.com:123/forum/questions/?tag=networking&order=newest#top"), new URI("ldap://[2001:db8::7]/c=GB?objectClass?one"), new URI("mailto:Username.example@example.com"), new URI("news:comp.infosystems.www.servers.unix"), new URI("tel:+1-816-555-1212"), new URI("telnet://192.0.2.16:80/"), new URI("urn:oasis:names:specification:docbook:dtd:xml:4.1.2")};
        for (URI value : uriPermittedFormats) {
            try {
                MqttClientConnectException exception = new MqttClientConnectException(null, clientID, username, value);
                assertEquals("Expected and actual values should be the same!", clientID, exception.getClientId());
                assertEquals("Expected and actual values should be the same!", username, exception.getUsername());
                assertNull("Null expected!", exception.getCause());
                assertEquals("Expected and actual values should be the same!", value, exception.getUri());
                assertEquals("Expected and actual values should be the same!", "CONNECT_ERROR", exception.getCode().toString());
            } catch (Exception ex) {
                fail("No exception expected!");
            }
        }
    }

    @Test
    public void secondConstructorClientIdNullTest() throws URISyntaxException {
        URI[] uriPermittedFormats = { new URI("https://john.doe@www.example.com:123/forum/questions/?tag=networking&order=newest#top"), new URI("ldap://[2001:db8::7]/c=GB?objectClass?one"), new URI("mailto:Username.example@example.com"), new URI("news:comp.infosystems.www.servers.unix"), new URI("tel:+1-816-555-1212"), new URI("telnet://192.0.2.16:80/"), new URI("urn:oasis:names:specification:docbook:dtd:xml:4.1.2")};
        for (URI value : uriPermittedFormats) {
            try {
                MqttClientConnectException exception = new MqttClientConnectException(cause, null, username, value);
                assertEquals("Expected and actual values should be the same!", cause, exception.getCause());
                assertNull("Null expected!", exception.getClientId());
                assertEquals("Expected and actual values should be the same!", username, exception.getUsername());
                assertEquals("Expected and actual values should be the same!", value, exception.getUri());
                assertEquals("Expected and actual values should be the same!", "CONNECT_ERROR", exception.getCode().toString());
            } catch (Exception ex) {
                fail("No exception expected!");
            }
        }
    }

    @Test
    public void secondConstructorUsernameNullTest() throws URISyntaxException {
        URI[] uriPermittedFormats = { new URI("https://john.doe@www.example.com:123/forum/questions/?tag=networking&order=newest#top"), new URI("ldap://[2001:db8::7]/c=GB?objectClass?one"), new URI("mailto:Username.example@example.com"), new URI("news:comp.infosystems.www.servers.unix"), new URI("tel:+1-816-555-1212"), new URI("telnet://192.0.2.16:80/"), new URI("urn:oasis:names:specification:docbook:dtd:xml:4.1.2")};
        for (URI value : uriPermittedFormats) {
            try {
                MqttClientConnectException exception = new MqttClientConnectException(cause, clientID, null, value);
                assertEquals("Expected and actual values should be the same!", cause, exception.getCause());
                assertEquals("Expected and actual values should be the same!", clientID, exception.getClientId());
                assertNull("Null expected!", exception.getUsername());
                assertEquals("Expected and actual values should be the same!", value, exception.getUri());
                assertEquals("Expected and actual values should be the same!", "CONNECT_ERROR", exception.getCode().toString());
            } catch (Exception ex) {
                fail("No exception expected!");
            }
        }
    }

    @Test
    public void secondConstructorURINullTest() {
        try {
            MqttClientConnectException exception = new MqttClientConnectException(cause, clientID, username, null);
            assertEquals("Expected and actual values should be the same!", cause, exception.getCause());
            assertEquals("Expected and actual values should be the same!", clientID, exception.getClientId());
            assertEquals("Expected and actual values should be the same!", username, exception.getUsername());
            assertNull("Null expected!", exception.getUri());
            assertEquals("Expected and actual values should be the same!", "CONNECT_ERROR", exception.getCode().toString());
        } catch (Exception ex) {
            fail("No exception expected!");
        }
    }

    @Test
    public void secondConstructorAllNullTest() {
        try {
            MqttClientConnectException exception = new MqttClientConnectException(null, null, null, null);
            assertNull("Null expected!", exception.getCause());
            assertNull("Null expected!", exception.getClientId());
            assertNull("Null expected!", exception.getUsername());
            assertNull("Null expected!", exception.getUri());
            assertEquals("Expected and actual values should be the same!", "CONNECT_ERROR", exception.getCode().toString());
        } catch (Exception ex) {
            fail("No exception expected!");
        }
    }

    @Test (expected = MqttClientConnectException.class)
    public void throwingExceptionUsingSecondConstructorTest() throws MqttClientConnectException {
        throw exception2;
    }
}
