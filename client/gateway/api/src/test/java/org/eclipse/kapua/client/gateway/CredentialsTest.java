/*******************************************************************************
 * Copyright (c) 2017, 2020 Red Hat Inc and others.
 *
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Red Hat Inc - initial API and implementation
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.client.gateway;

import org.eclipse.kapua.client.gateway.Credentials.UserAndPassword;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

@Category(JUnitTests.class)
public class CredentialsTest extends Assert {

    String specialSymbolsAndNumbers = "1234567890~!@$%^&*()_+, .?-/[];':<>";
    String[] stringLengths = new String[]{"", "a", "abc", "qwertyuiopasdfghjklzxcvbnmqwertyuiopasdfghjklzxcvbnmqwertyuiopasdfghjklzxcvbnmqwertyuiopasdfghjklzxcvbnmqwertyuiopasdfghjklzxcvbnmqwertyuiopasdfghjklzxcvbnmqwertyuiopasdfghjklzxcvbnmqwertyuiopasdfghjklzxcvbnmqwertyuiopasdfghjklzxcvbnmqwertyuiopasdfghjqwer", "qwertyuiopasdfghjklzxcvbnmqwertyuiopasdfghjklzxcvbnmqwertyuiopasdfghjklzxcvbnmqwertyuiopasdfghjklzxcvbnmqwertyuiopasdfghjklzxcvbnmqwertyuiopasdfghjklzxcvbnmqwertyuiopasdfghjklzxcvbnmqwertyuiopasdfghjklzxcvbnmqwertyuiopasdfghjklzxcvbnmqwertyuiopasdfghjqwerqwertyuiopasdfghjklzxcvbnmqwertyuiopasdfghjklzxcvbnmqwertyuiopasdfghjklzxcvbnmqwertyuiopasdfghjklzxcvbnmqwertyuiopasdfghjklzxcvbnmqwertyuiopasdfghjklzxcvbnmqwertyuiopasdfghjklzxcvbnmqwertyuiopasdfghjklzxcvbnmqwertyuiopasdfghjklzxcvbnmqwertyuiopasdfghjqwer"};

    @Test
    public void privateConstructorTest() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor<Credentials> credentialsConstructor = Credentials.class.getDeclaredConstructor();
        credentialsConstructor.setAccessible(true);
        Credentials credentials = credentialsConstructor.newInstance();
        assertThat("Instance of Credentials.java expected", credentials, IsInstanceOf.instanceOf(Credentials.class));
    }

    @Test
    public void firstUserAndPasswordValidTest() {
        UserAndPassword userAndPassword = Credentials.userAndPassword("username", "password");
        assertEquals("Expected and actual values should be the same!", "username", userAndPassword.getUsername());
        assertEquals("Expected and actual values should be the same!", "password", userAndPassword.getPasswordAsString());
        assertNotNull("The value should not be null!", userAndPassword.getPassword());
    }

    @Test
    public void firstUserAndPasswordAllNullTest() {
        UserAndPassword userAndPassword = Credentials.userAndPassword(null, (String) null);
        assertNull("Null expected!", userAndPassword.getUsername());
        assertNull("Null expected!", userAndPassword.getPassword());
    }

    @Test
    public void usernameNullTest() {
        UserAndPassword userAndPassword = Credentials.userAndPassword(null, "password");
        assertNull("Null expected!", userAndPassword.getUsername());
        assertEquals("Expected and actual values should be the same!", "password", userAndPassword.getPasswordAsString());
        assertNotNull("The value should not be null!", userAndPassword.getPassword());
    }

    @Test
    public void passwordNullTest() {
        UserAndPassword userAndPassword = Credentials.userAndPassword("username", (String) null);
        assertEquals("Expected and actual values should be the same!", "username", userAndPassword.getUsername());
        assertNull("Null expected!", userAndPassword.getPassword());
        assertNull("Null expected!", userAndPassword.getPasswordAsString());

    }

    @Test
    public void firstUserAndPasswordCharacterTest() {
        for (int i=0; i<specialSymbolsAndNumbers.length(); i++) {
            UserAndPassword userAndPassword = Credentials.userAndPassword("username" + specialSymbolsAndNumbers.charAt(i), "password" + specialSymbolsAndNumbers.charAt(i));
            assertEquals("Expected and actual values should be the same!", "username" + specialSymbolsAndNumbers.charAt(i), userAndPassword.getUsername());
            assertEquals("Expected and actual values should be the same!", "password" + specialSymbolsAndNumbers.charAt(i), userAndPassword.getPasswordAsString());
        }
    }

    @Test
    public void firstUserAndPasswordStringLengthTest() {
        for(String value : stringLengths) {
            UserAndPassword userAndPassword = Credentials.userAndPassword(value, value);
            assertEquals("Expected and actual values should be the same!", value, userAndPassword.getUsername());
            assertEquals("Expected and actual values should be the same!", value, userAndPassword.getPasswordAsString());
        }
    }

    @Test
    public void secondUserAndPasswordValidTest() {
        UserAndPassword userAndPassword = Credentials.userAndPassword("user", "password".toCharArray());
        assertEquals("Expected and actual values should be the same!", "user", userAndPassword.getUsername());
        assertNotNull("The value should not be null!", userAndPassword.getPassword());
        assertEquals("Expected and actual values should be the same!", "password", userAndPassword.getPasswordAsString());
    }

    @Test
    public void secondUserAndPasswordAllNullTest() {
        UserAndPassword userAndPassword = Credentials.userAndPassword(null, (char[]) null);
        assertNull("Null expected!", userAndPassword.getUsername());
        assertNull("Null expected!", userAndPassword.getPassword());
        assertNull("Null expected!", userAndPassword.getPasswordAsString());
    }

    @Test
    public void usernameNullTest2() {
        UserAndPassword userAndPassword = Credentials.userAndPassword(null, "password".toCharArray());
        assertNull("Null expected!", userAndPassword.getUsername());
        assertNotNull("The value should not be null!", userAndPassword.getPassword());
        assertEquals("Expected and actual values should be the same!", "password", userAndPassword.getPasswordAsString());
    }

    @Test
    public void passwordNullTest2() {
        UserAndPassword userAndPassword = Credentials.userAndPassword("user", (char[]) null);
        assertEquals("Expected and actual values should be the same!", "user", userAndPassword.getUsername());
        assertNull("Null expected!", userAndPassword.getPassword());
        assertNull("Null expected!", userAndPassword.getPasswordAsString());
    }

    @Test
    public void secondUserAndPasswordCharacterTest() {
        for (int i=0; i<specialSymbolsAndNumbers.length(); i++) {
            UserAndPassword userAndPassword = Credentials.userAndPassword("username" + specialSymbolsAndNumbers.charAt(i), "password".toCharArray());
            assertEquals("Expected and actual values should be the same!", "username" + specialSymbolsAndNumbers.charAt(i), userAndPassword.getUsername());
            assertEquals("Expected and actual values should be the same!", "password", userAndPassword.getPasswordAsString());
            assertNotNull("The value should not be null!", userAndPassword.getPassword());
        }
    }

    @Test
    public void secondUserAndPasswordCharacterLengthTest() {
        for(String value : stringLengths) {
            UserAndPassword userAndPassword = Credentials.userAndPassword(value, value.toCharArray());
            assertEquals("Expected and actual values should be the same!", value, userAndPassword.getUsername());
            assertEquals("Expected and actual values should be the same!", value, userAndPassword.getPasswordAsString());
            assertNotNull("The value should not be null!", userAndPassword.getPassword());
        }
    }
}