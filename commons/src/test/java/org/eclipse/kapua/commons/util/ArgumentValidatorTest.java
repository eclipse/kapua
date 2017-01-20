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
 *
 *******************************************************************************/
package org.eclipse.kapua.commons.util;

import java.util.ArrayList;
import java.util.Date;

import org.eclipse.kapua.KapuaIllegalArgumentException;
import org.eclipse.kapua.KapuaIllegalNullArgumentException;
import org.junit.Ignore;
import org.junit.Test;



public class ArgumentValidatorTest {

    /*********************
     * 
     * @Test: ArgumentValidator.match() 
     * @return: should return an error if name is NULL.
     */
    @Test
    public void testMatchNotNull()
            throws KapuaIllegalArgumentException {
        String argVal1 = null;
        String argRegExp1 = "^[a-zA-Z0-9\\-]{3,}$";
        ArgumentValidator.match(argVal1, argRegExp1, "matchTest1");
    }

    /*********************
     * 
     * @Test: ArgumentValidator.match()
     * @return: should return an error if name too short. 
     */
    @Test(expected=KapuaIllegalArgumentException.class)
    public void testTooShort()
            throws KapuaIllegalArgumentException {
        String argVal2 = "ni";
        String argRegExp2 = "^[a-zA-Z0-9\\-]{3,}$";
        ArgumentValidator.match(argVal2, argRegExp2, "matchTest2");
    }

    /*********************
     * 
     *  @Test: ArgumentValidator.match()
     *  @return: should return an error if name too long.
     * 
     */
    @Test
    public void testTooLong()
            throws KapuaIllegalArgumentException {
        String argVal1 = "1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--";
        String argRegExp1 = "^[a-zA-Z0-9\\-]{3,}$";
        ArgumentValidator.match(argVal1, argRegExp1, "matchTest3");
    }

    /*********************
     * 
     *  @Test: should return an error if illegal character present. 
     *  This test checks for illegal characters in certain strings (simple name, tag name, e-mail, password...).
     *  If illegal characters are in checked string, exception should pop-up.
     * 
     */
    @Test
    public void testIllegalCharacter()
            throws KapuaIllegalArgumentException {
        // Strings of Regular Expressions, which contain permitted symbols. 
        // For a valid simple name u must enter at least three characters. They must be letters a-z or A-Z, digits 0-9 or "dash" symbol (-).
        String argRegExpr_SIMPLE_NAME = "^[a-zA-Z0-9\\-]{3,}$";
        // For a valid Name u must enter at least three characters. They must be letters a-z or A-Z, digits 0-9,"_" or "dash" symbol (-).
        String argRegExpr_NAME_REGEXP = "^[a-zA-Z0-9\\_\\-]{3,}$";
        // For a valid Name Space u must enter at least three characters. They must be letters a-z or A-Z, digits 0-9,"_", space character or "dash" symbol (-).
        String argRegExpr_NAME_SPACE_REGEXP = "^[a-zA-Z0-9\\ \\_\\-]{3,}$";
        // For valid password you have to enter at least 12 characters, it has to contain digits, small and capital letters and at least one special symbol: [@#$%^&+=!\\~\\|]
        String argRegExpr_PASSWORD_REGEXP = "^.*(?=.{12,})(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!\\~\\|]).*$";
        // For a valid e-mail address u must enter at least one word character [a-zA-Z0-9] or number, then AT symbol(@), then at least one word character [a-zA-Z0-9] and 2,3 
        // or 4 character ending (no digits).
        String argRegExpr_EMAIL_REGEXP = "^(\\w+)([-+.][\\w]+)*@(\\w[-\\w]*\\.){1,5}([A-Za-z]){2,4}$";
        // TAG name has to be between 3 and 255 character long and can contain symbols listed below. 
        String argRegExpr_TAG_NAME_REGEXP = "[A-Za-z0-9-_@#!$%^&*+=?<>]{3,255}";
        // IP address has should be with http:/, https:// or just numbers; numbers should be in range from 000.000.000.000 to 255.255.255.255
        String argRegExpr_IP_ADDRESS_REGEXP = "(^(http://)|(https://)|())([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])((.*)$)";
        String argRegExpr_LOCAL_IP_ADDRESS_REGEXP = "(^(http://)|(https://))((127\\.0\\.0\\.1)|(10(\\.[0-9]+){3,3})|(172(\\.[0-9]+){3,3})|(192\\.168(\\.[0-9]+){2,2}))((.*)$)";
        // what MAC address should be like: "xx:xx:xx:xx:xx:xx"
        String argRegExpr_MAC_ADDRESS_REGEXP    = "^([0-9A-F]{2}[:-]){5}([0-9A-F]{2})$";
        
        // List of strings which contain as much symbols as possible (numbers and letters not included). Those who are permitted are 
        //"tagged" (e.g. "#_is_permitted"), because otherwise they would be too short (minimum length is 3) and test would fail.  
        String[] listOfCharacters_SIMPLE_NAME = new String[]{"abcdefghijklmnoprsqtuxvwyzABCDEFGHIJKLMNOPRQRSTUVWXYZ01234567890--all-PERMITED_SImbo1s-12309fd","-dash-is-permitted","!","\"","#","$","%","&","'","(",")","=","?","*", "[","]","{","}","Œ","^","[","|","}","Æ", "@","_permitted_symbol","¿",":","ˇ",";","È",">","<","~","«","≈","ç","Ç","√","◊","∫","Ñ","µ","¯","å","ß","Å","Í","ƒ","Ï","©"," ̏","∆","Ô"," ̑","","¬","Ò","œ","ø","Ø","π","∏","‹","›","€","ı","¶","°","•","±","//","\\"};
        String[] listOfCharacters_NAME_REGEXP = new String[]{"all-PERMITED______simBOLs-12309fd_","-dash-is-permitted","!","\"","#","$","%","&","'","(",")","=","?","*", "[","]","{","}","Œ","^","[","|","}","Æ", "@","_symbol","¿",":","ˇ",";","È",">","<","~","«","≈","ç","Ç","√","◊","∫","Ñ","µ","¯","å","ß","Å","Í","ƒ","Ï","©"," ̏","∆","Ô"," ̑","","¬","Ò","œ","ø","Ø","π","∏","‹","›","€","ı","¶","°","•","±","//","\\" };
        String[] listOfCharacters_NAME_SPACE_REGEXP = new String[]{"all_-PERMITED_   si m b o l s-12309fd_ ","   space_is_permitted","-dash-is-permitted","!","\"","#","$","%","&","'","(",")","=","?","*", "[","]","{","}","Œ","^","[","|","}","Æ", "@","_is_permitted","¿",":","ˇ",";","È",">","<","~","«","≈","ç","Ç","√","◊","∫","Ñ","µ","¯","å","ß","Å","Í","ƒ","Ï","©"," ̏","∆","Ô"," ̑","","¬","Ò","œ","ø","Ø","π","∏","‹","›","€","ı","¶","°","•","±","//","\\" };
        String[] listOfCharacters_PASSWORD_REGEXP = new String[]{"ThisOneW1llN@tFail","THISoneH@sNoDigit]","Th1sOneH4sN0S1mb0ls","Abcd1234@r45e","1234abcdABCD@","Abcd1234~cdba","aBcd5678|1234","Character1234$"};
        String[] listOfCharacters_EMAIL_REGEXP = new String[]{"1@1.2","1@1.co","hot-programmer@hot.com","hot.programmer1@hotty.com","bad+programmer@angel.uk","sh0r7@sh.co","o@g.co","1@g.org","+@mail.com",".@SUPER.COM","-@dash.robo","sAmpl-e.mail+robot@examp.co","","EXTREMELY-long.emailadress-which-does.not.exist-onlyEXIST-FOR.TEsting-purposes+at-kapua.com@stupid.org","name1.kapua@@g.com","name-.+kapua@gmail.com","www.my-sample123-email@g.co","123-sam-ple+mail-reawe@h.comp",".r-r@g.co","shotr@gm.co","a@g.c","B@H.or","worm-lover@hotmail.com","hedgehog-hunter@skype.org","some1@at3.com","new_email@f.g","newev_email123@f.co","ONELONGEMAILADDRESSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSNGEMAILADDRESSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSNGEMAILADDRESSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSNGEMAILADDRESSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSNGEMAILADDRESSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSNGEMAILADDRESSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSNGEMAILADDRESSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSNGEMAILADDRESSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSNGEMAILADDRESSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSNGEMAILADDRESSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSNGEMAILADDRESSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSNGEMAILADDRESSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSNGEMAILADDRESSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSNGEMAILADDRESSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSNGEMAILADDRESSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSNGEMAILADDRESSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSNGEMAILADDRESSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSNGEMAILADDRESSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSNGEMAILADDRESSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSNGEMAILADDRESSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSNGEMAILADDRESSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSNGEMAILADDRESSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSNGEMAILADDRESSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSNGEMAILADDRESSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSNGEMAILADDRESSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSNGEMAILADDRESSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSNGEMAILADDRESSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSNGEMAILADDRESSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSNGEMAILADDRESSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSNGEMAILADDRESSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSNGEMAILADDRESSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSNGEMAILADDRESSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSNGEMAILADDRESSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSNGEMAILADDRESSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSNGEMAILADDRESSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSNGEMAILADDRESSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSNGEMAILADDRESSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSNGEMAILADDRESSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSNGEMAILADDRESSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSNGEMAILADDRESSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSNGEMAILADDRESSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSNGEMAILADDRESSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSNGEMAILADDRESSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSNGEMAILADDRESSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSNGEMAILADDRESSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSNGEMAILADDRESSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSNGEMAILADDRESSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSNGEMAILADDRESSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSNGEMAILADDRESSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSNGEMAILADDRESSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSNGEMAILADDRESSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSNGEMAILADDRESSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS@gmail.com"};
        String[] listOfCharacters_TAG_NAME_REGEXP = new String[]{"abcdefghijklmnopqrstuvxwyzABCDEFGHIJKLMNOPQRSTUVXYZ1234567890-_@#!$%^&*+=?<>","Normal_name-with_sp@cialS%^&YMBOLS","SYMBO,L","tag.name","[tag]name","(tag)name","{tag}name","Œtagname","tag^name","tag√name","tag~name","tagÆname","tag¿name","tag:name","tagˇname","tag;name","tagÈname","tag>name","tag<name","tag~name","tag«name","tag≈","tagç","tagÇ","tag√","123◊","tag∫","tagÑ","tagµ","tag¯","nameå","223ß","tagÅ","tagÍ","tagƒ","tagÏ","tag©","tag ̏","tag∆","tagÔ","tag ̑","tag","tag¬","tagÒ","tagœ","taøg","taØg","tagπ","tag∏","tag‹","›tag","€tag","ıtag","¶tag","°tag","•tag","±tag","//tag","tag\\","---","___","%%%","###","@@@","!!!","???",">>>","<<<","+++","***","111","ABC","123","abc","^^^","===","$$$","256SYMOBLS-256SYMBOLS-256SYMBOLS-256SYMBOLS-256SYMBOLS-256SYMBOLS-256SYMBOLS-256SYMBOLS-256SYMBOLS-256SYMBOLS-256SYMBOLS-256SYMBOLS-256SYMBOLS-256SYMBOLS-256SYMBOLS-256SYMBOLS-256SYMBOLS-256SYMBOLS-256SYMBOLS-256SYMBOLS-256SYMBOLS-256SYMBOLS-256SYMBOLS----"};
        String[] listOfCharacters_LOCAL_IP_ADDRESS_REGEXP = new String[]{"000.000.000.000","255.255.255.255","http://000.000.000.000","http://255.255.255.255","https://000.000.000.000","https://255.255.255.255","10.10.10.10","http://10.10.10.10", "https://10.10.10.10","192.168.0.1","http://192.168.0.1","https://192.168.0.1","172.0.0.0","http://172.0.0.0","https://172.0.0.0","123.123.123.123","http://123.123.123.123","https://123.123.123.123","255.255.255.256","http://255.255.255.256","https://255.255.255.256","290.290.290.290","http://290.290.290.290", "https://290.290.290.290","255.255.255.255.255", "http://255.255.255.255.255", "https://255.255.255.255.255"};
        String[] listOfCharacters_IP_ADDRESS_REGEXP = new String[]{"000.000.000.000","255.255.255.255","http://000.000.000.000","http://255.255.255.255","https://000.000.000.000","https://255.255.255.255","10.10.10.10","http://10.10.10.10", "https://10.10.10.10","192.168.0.1","http://192.168.0.1","https://192.168.0.1","172.0.0.0","http://172.0.0.0","https://172.0.0.0","123.123.123.123","http://123.123.123.123","https://123.123.123.123","255.255.255.256","http://255.255.255.256","https://255.255.255.256","290.290.290.290","http://290.290.290.290", "https://290.290.290.290","255.255.255.255.255", "http://255.255.255.255.255", "https://255.255.255.255.255"};      
        String[] listOfCharacters_MAC_ADDRESS_REGEXP = new String[]{"00:00:00:00:00:00","AA:AA:AA:AA:AA:AA","AB:CD:EF:01:23:45","10:20:90:99:91:92","99:99:99:99:99:99","FF:FF:FF:FF:FF:FF", "00:11:22:33:44:55:66","AB:CD:EF:GF:FF:FF","01.23.34.FF"};

        
        // testing SIMPLE_NAME_REGEXP expression for illegal characters
        // for output uncomment "System.out.println(...)" line
        int size = listOfCharacters_SIMPLE_NAME.length;
        int i = 0;
        //System.out.println("SIMPLE_NAME_REGEXP: ");
        //System.out.println("Number of symbols:" + (size-1));
        for(i=0;i<size;i++) {
           try {
               ArgumentValidator.match(listOfCharacters_SIMPLE_NAME[i], argRegExpr_SIMPLE_NAME, "SIMPLE_NAME_test_case");
           } 
           catch(Exception KapuaIllegalArgumentException) {
               //System.out.println("character at " + i + ":" + listOfCharacters_SIMPLE_NAME[i] + " not permitted");
           }
           
           // testing NAME_REGEXP expression for illegal characters
           int size2 = listOfCharacters_NAME_REGEXP.length;
           int i2 = 0;
           //System.out.println("NAME_REGEXP: ");
           for(i2=0;i2<size2;i2++) {
               try {
                   ArgumentValidator.match(listOfCharacters_NAME_REGEXP[i2], argRegExpr_NAME_REGEXP, "SIMPLE_NAME_test_case");
               } 
               catch(Exception KapuaIllegalArgumentException) {
                   //System.out.println("character at " + i2 + ":" + listOfCharacters_NAME_REGEXP[i2] + " not permitted");
               }
               
        }
           
           // testing NAME_SPACE_REGEXP expression for illegal characters
           // for output uncomment "System.out.println(...)" line
           int size3= listOfCharacters_NAME_SPACE_REGEXP.length;
           //System.out.println("NAME_SPACE_REGEXP");
           for(int i3=0;i3<size3;i3++) 
           {
               try 
               {
                   ArgumentValidator.match(listOfCharacters_NAME_SPACE_REGEXP[i3], argRegExpr_NAME_SPACE_REGEXP, "NAME_SPACE_test_case");
               } 
               catch(Exception KapuaIllegalArgumentException) 
               {
                   //System.out.println("character at " + i3 + ":" + listOfCharacters_NAME_SPACE_REGEXP[i3] + " not permitted");
               }
             
           }
           
           // testing PASSWORD_REGEXP expression for illegal characters
           // password has to contain at least 12 characters with digits, small and capital letters and at least one symbol.
           // for output uncomment "System.out.println(...)" line
           int size4= listOfCharacters_PASSWORD_REGEXP.length;
           //System.out.println("PASSWORD_REGEXP");
           for(int i4=0;i4<size4;i4++) 
           {
               try 
               {
                   ArgumentValidator.match(listOfCharacters_PASSWORD_REGEXP[i4], argRegExpr_PASSWORD_REGEXP, "PASSWORD_test_case");
               } 
               catch(Exception KapuaIllegalArgumentException) 
               {
                   //System.out.println("character at " + i4 + ":" + listOfCharacters_PASSWORD_REGEXP[i4] + " does not contain a digit, small and capital letter, special character or pass. not 12 charac. long");
               }
             
           }
           
           // testing EMAIL_REGEXP expression for illegal characters
           // for valid e-mail addres it has to begin with a character, it can contain numbers, letters, dot (.), dash(-) and plus sign (+). it can contain only one AT sign (@) and at least one
           // character for mail provider name and 2,3 or 4 characters for domain name (.co,.com,.uk).
           // for output uncomment "System.out.println(...)" line
           int size8 = listOfCharacters_EMAIL_REGEXP.length;
           //System.out.println("EMAIL ADDRESS: ");
           //System.out.println("Number of symbols:" + (size8-1));
           for(int i8=0;i8<size8;i8++) {
              try {
                  ArgumentValidator.match(listOfCharacters_EMAIL_REGEXP[i8], argRegExpr_EMAIL_REGEXP, "EMAIL_test_case");
              } 
              catch(Exception KapuaIllegalArgumentException) {
                  //System.out.println("character at " + i8 + ":" + listOfCharacters_EMAIL_REGEXP[i8] + " email address not accurate format. Please check again");
              }
        }
           
           // testing TAG_NAME expression for illegal characters
           // for output uncomment "System.out.println(...)" line
           int size5 = listOfCharacters_TAG_NAME_REGEXP.length;
           int i5 = 0;
           //System.out.println("TAG_NAME: ");
           //System.out.println("Number of symbols:" + (size5-1));
           for(i5=0;i5<size5;i5++) {
              try {
                  ArgumentValidator.match(listOfCharacters_TAG_NAME_REGEXP[i5], argRegExpr_TAG_NAME_REGEXP, "TAGE_NAME_test_case");
              } 
              catch(Exception KapuaIllegalArgumentException) {
                 //System.out.println("character at " + i5 + ":" + listOfCharacters_TAG_NAME_REGEXP[i5] + " TAG NAME not OK, please check again!");
              }
        }
           // testing LOCAL_IP_ADDRESS_REGEXP expression for illegal characters
           // for output uncomment "System.out.println(...)" line
           int size6 = listOfCharacters_LOCAL_IP_ADDRESS_REGEXP.length;
           //System.out.println(" LOCAL_IP_ADDRESS ");
           //System.out.println("Number of symbols:" + (size6-1));
           for(int i6=0;i6<size6;i6++) {
              try {
                  ArgumentValidator.match(listOfCharacters_LOCAL_IP_ADDRESS_REGEXP[i6], argRegExpr_LOCAL_IP_ADDRESS_REGEXP, "LOCAL_IP_ADDRESS_test_case");
              } 
              catch(Exception KapuaIllegalArgumentException) {
                  //System.out.println("character at " + i6 + ":" + listOfCharacters_LOCAL_IP_ADDRESS_REGEXP[i6] + " not permitted");
              }
        }
           
           // testing IP_ADDRESS_REGEXP expression for illegal characters
           // for output uncomment "System.out.println(...)" line
           int size7 = listOfCharacters_IP_ADDRESS_REGEXP.length;
           //System.out.println("IP_ADDRESS ");
           //System.out.println("Number of symbols:" + (size7-1));
           for(int i7=0;i7<size7;i7++) {
              try {
                  ArgumentValidator.match(listOfCharacters_IP_ADDRESS_REGEXP[i7], argRegExpr_IP_ADDRESS_REGEXP, "IP_ADDRESS_test_case");
              } 
              catch(Exception KapuaIllegalArgumentException) {
                  //System.out.println("character at " + i7 + ":" + listOfCharacters_IP_ADDRESS_REGEXP[i7] + " not permitted");
              }
        }
           
           // testing MAC_ADDRESS_REGEXP expression for illegal characters
           // for output uncomment "System.out.println(...)" line
           int size9 = listOfCharacters_MAC_ADDRESS_REGEXP.length;
           //System.out.println("MAC_ADDRESS ");
           //System.out.println("Number of symbols:" + (size9-1));
           for(int i9=0;i9<size9;i9++) {
              try {
                  ArgumentValidator.match(listOfCharacters_MAC_ADDRESS_REGEXP[i9], argRegExpr_MAC_ADDRESS_REGEXP, "MAC_ADDRESS_test_case");
              } 
              catch(Exception KapuaIllegalArgumentException) {
                  //System.out.println("character at " + i9 + ":" + listOfCharacters_MAC_ADDRESS_REGEXP[i9] + "  MAC address not ok! Please check again!");
              }
        }
    }
    }

    /*********************
     * 
     * @Tests : ArgumentValidator.notNull(Object value, String argumentName)
     * @return: Return exception if passed argument is Null.
     */
    @Test
    public void testNotNull()
            throws KapuaIllegalNullArgumentException{
        Object[] listOfCharacters_NULL_TEST_REGEXP = new Object []{1,2,3,123,"1","kapua","a","abcdefghijklmnopqrstuvxyz","1234567890", "ndgw9gKFOEF95jfe13@€ı–‹€–‹ı™&#",null};
        int size11 = listOfCharacters_NULL_TEST_REGEXP.length;
        for(int i11=0;i11<size11;i11++) {
            try {
                ArgumentValidator.notNull(listOfCharacters_NULL_TEST_REGEXP[i11], "test for Null");
            } 
            catch(Exception KapuaIllegalNullArgumentException) {
                System.out.println("problem at:  " + i11 + ":" + listOfCharacters_NULL_TEST_REGEXP[i11] + "  Argument is null! ");
            }
            
        }
    }

    
    /*********************
     * 
     * @Tests : ArgumentValidator.notEmptyOrNull(String value, String argumentName).
     * @return: Return an exception if argument empty or null.
     */
    @Test
    public void testNotEmptyOrNull()
            throws KapuaIllegalNullArgumentException {
        String[] listOfChoices_NOT_EMPTY_OR_NULL_TEST_REGEXP = new String[]{null, "","notNull","a","kapua rocks!", null};
        
        int size12 = listOfChoices_NOT_EMPTY_OR_NULL_TEST_REGEXP.length;
        //System.out.println("notNull test");
        //System.out.println("Number of choices: " + (size12 -1));
        for(int i12=0;i12<size12;i12++){
            try{
                ArgumentValidator.notEmptyOrNull(listOfChoices_NOT_EMPTY_OR_NULL_TEST_REGEXP[i12], "not null or empty test");
            }
            catch(Exception KapuaIllegalNUllArgumentException){
                System.out.println("problem at:  " + i12 + ":" + listOfChoices_NOT_EMPTY_OR_NULL_TEST_REGEXP[i12] + "not ok");
            }
        }
    }

    /*********************
     * 
     * @Test : ArgumentValidator.notEmptyOrNull(Object value, String argumentName).
     * @Return: Return an exception if argument empty or null (argument should be an Object).
     */ 
    @Test
    public void testNotEmptyOrNullObject()
        throws KapuaIllegalNullArgumentException
        {
            Object[] listOfChoices_NOT_EMPTY_OR_NULL_OBJECT_TEST_REGEXP = new Object[]{null,"", "not empty", "not null",123};
            int size13 = listOfChoices_NOT_EMPTY_OR_NULL_OBJECT_TEST_REGEXP.length;
            System.out.println("notEmptyOrNull Object test");
            System.out.println("Number of choices: " + size13 );
            for(int i13=0;i13<size13;i13++){
                try{
                    ArgumentValidator.notEmptyOrNull(listOfChoices_NOT_EMPTY_OR_NULL_OBJECT_TEST_REGEXP, "test1");
                }
                catch(Exception KapuaIllegalNUllArgumentException){
                    System.out.println("problems!" );
                }
            }
        }

    /*********************
     * 
     * @Tests : ArgumentValidator.notEmptyOrNull(Collection<?> value, String argumentName) 
     * @Return: Return an exception if argument empty or null (argument should be a Collection).
     */
    @Test(expected=KapuaIllegalNullArgumentException.class)
    public void testNotEmptyOrNullCollection()
            throws KapuaIllegalNullArgumentException {
        ArrayList<String> list1 = null;
        ArgumentValidator.notEmptyOrNull(list1, "notEmptyOrNullTest1");
    }

    @Test(expected=KapuaIllegalNullArgumentException.class)
    public void testNotEmptyOrNullCollection2()
            throws KapuaIllegalNullArgumentException {
        ArrayList<String> list2 = new ArrayList<String>();
        ArgumentValidator.notEmptyOrNull(list2, "notEmptyOrNullTest2");
    }

    @Test
    public void testNotEmptyOrNullCollection3()
            throws KapuaIllegalNullArgumentException {
        ArrayList<String> list3 = new ArrayList<String>();
        list3.add("leo1");
        list3.add("kapua1");
        ArgumentValidator.notEmptyOrNull(list3, "notEmptyOrNullTest3");
    }

    /*********************
     * 
     * @Tests : ArgumentValidator.notNegative(long value, String argumentName).
     * @return: Return an exception if argument is negative.
     */
    @Test
    public void testNotNegative()
        throws KapuaIllegalNullArgumentException
        {
            long[] listOfChoices_NOT_NEGATIVE_TEST_REGEXP = new long[]{0,1,2,-1,13,-13,-45, -0, 133232, 450, -999999999, 859897928, -859897928};
            int size14 = listOfChoices_NOT_NEGATIVE_TEST_REGEXP.length;
            System.out.println("NOT NEGATIVE test");
            System.out.println("Number of choices: " + (size14-1) );
            for(int i14=0;i14<size14;i14++){
                try{
                    ArgumentValidator.notNegative(listOfChoices_NOT_NEGATIVE_TEST_REGEXP[i14], "not null test");
                }
                catch(Exception KapuaIllegalNUllArgumentException){
                    System.out.println("problem at:  " + i14 + ":" + listOfChoices_NOT_NEGATIVE_TEST_REGEXP[i14] + " IS NEGATIVE!");
                }
            }
        }

    
    /*********************
     * 
     * @Tests : ArgumentValidator.dateRange(Date startDate, Date endDate).
     * @return: Return an exception if start date is after end date or either of them is negative. 
     */
    @Test
    public void testDateRange()
        throws KapuaIllegalArgumentException
        {
            Date startTime1 = null;
            Date endTime1 = null; 

            Date startTime2 = new Date(97,1,23);
            Date endTime2 = new Date(97,1,32);
            
            Date startTime3 = new Date(97,1,23);
            Date endTime3 = new Date(94,1,31);
            
            Date startTime4 = new Date(2016, -1, 30);
            Date endTime4 = new Date(2016, -1, 31);
            
            Date startTime5 = new Date(2016,1,30);
            Date endTime5 = new Date(2016,-1,31);
            
            Date startTime6 = new Date(2015,10,12);
            Date endTime6 = new Date(2014,-1,-1);
            
            Date startTime7 = new Date(2016,-12,31);
            Date endTime7 = new Date(2017,1,1);
            
            Date startTime8 = new Date(2013,-1,10);
            Date endTime8 = new Date(2012,1,1);
            
            Date startTime9 = new Date(-2011,2,2);
            Date endTime9 = new Date(2010,-1,2);
        
            
            Date[] startTime = new Date[]{startTime1,startTime2,startTime3,startTime4,startTime5,startTime6, startTime7, startTime8, startTime9};
            Date[] endTime = new Date[]{endTime1,endTime2,endTime3,endTime4,endTime5,endTime6,endTime7,endTime8,endTime9};
            
            
            System.out.println("NOT NEGATIVE test");
            System.out.println("Number of choices: " + startTime.length);
            for(int i15=0;i15<startTime.length;i15++){
                try{
                    ArgumentValidator.dateRange(startTime[i15], endTime[i15]);
                }
                catch(Exception KapuaIllegalArgumentException){
                    System.out.println("problem at:  " + i15 + ":" + " Date NOT OK!");
                }
            }
        }
    
    /*********************
     * 
     * @Tests : ArgumentValidator.dateRange(long startDate, long endDate).
     * @return: Return an exception if start date is after end date or either of them is negative. 
     */
    @Test
    public void testDateRangeLong()
        throws KapuaIllegalArgumentException
        {

            long startTime2 = 123456789L;
            long endTime2 = 123456789999L;
            
            long startTime3 = 1234567890123456789L;
            long endTime3 = 123456789012345L;
            
            long startTime4 = 1234567890123459L;
            long endTime4 =  -1234567890123456712L;
            
            long startTime5 = 123456789012L;
            long endTime5 =  -123456678L;
            
            long startTime6 = -123456789L;
            long endTime6 = 123456788901234L;
            
            long startTime7 = -123456780123L;
            long endTime7 = 1234567L;
            
            long startTime8 = -1234567L;
            long endTime8 = -12345678901234L; 
            
            long startTime9 = -123456789L; 
            long endTime9 =  -123456L;
        
            
            long[] startTime = new long[]{startTime2,startTime3,startTime4,startTime5,startTime6, startTime7, startTime8, startTime9};
            long[] endTime = new long[]{endTime2,endTime3,endTime4,endTime5,endTime6,endTime7,endTime8,endTime9};
            
            
            System.out.println("NOT NEGATIVE LONG test");
            System.out.println("Number of choices: " + startTime.length);
            for(int i16=0;i16<startTime.length;i16++){
                try{
                    ArgumentValidator.dateRange(startTime[i16], endTime[i16]);
                }
                catch(Exception KapuaIllegalArgumentException){
                    System.out.println("problem at:  " + i16 + ":" + " Date NOT OK!");
                }
            }
        }

    
    /*********************
     * 
     * @Tests : ArgumentValidator.numRange(long value, long minValue, long maxValue, String argumentName).
     * @Return: Return an exception if argument value is lower than min or higher than max. 
     */
    @Test
    public void testNumrange()
        throws KapuaIllegalArgumentException
        {
            long val1 = 123456;
            long val1Min = 123;
            long val1Max = 123456789;;
            long val2Min = 1234567;
            long val2Max = 12345678;
            long val3Min = 1234;
            long val3Max = 12345;
            long val4Min = 12345567;
            long val4Max = 12;
                
            long[] minimalValues = new long []{val1Min,val2Min,val3Min, val4Min};
            long[] maximalValues = new long[]{val1Max,val2Max,val3Max,val4Max};
            
            System.out.println("Number Range test");
            for(int i17=0;i17<minimalValues.length;i17++){
                try{
                    ArgumentValidator.numRange(val1, minimalValues[i17], maximalValues[i17],"argumentName");
                }
                catch(Exception KapuaIllegalArgumentException){
                    System.out.println("problem at:  " + i17 + ":" + " Date NOT OK!");
                }
            }
        }
}


