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
package org.eclipse.kapua.commons.util;

import java.util.Random;

import org.eclipse.kapua.KapuaIllegalArgumentException;
import org.junit.Assert;
import org.junit.Test;

public class ArgumentValidatorTest extends Assert {

    @Test
    public void testMatchNotNull() throws KapuaIllegalArgumentException {
        String argRegExp = "^[a-zA-Z0-9\\-]{3,}$";
        String argVal = null;
        try {
            ArgumentValidator.match(argVal, argRegExp, "Null_test_case");
        } catch (Exception ex) {
            fail("No exception expected for: " + argVal);
        }
    }

    @Test
    public void testIllegalCharacterSimpleName() throws Exception {
        String argRegExprSimpleName = "^[a-zA-Z0-9\\-]{3,}$";
        String permittedSymbols = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ01234567890-";
        String[] listOfFalseStringsSimpleName = new String[] {"abc!","abc\\", "abc#","abc$","abc%","abc&","abc'","abc(",
                "abc)","abc=","abc?","abc*","abc_","abc:","abc;","abc>","abc<","abc.","abc,","abc¡", "abc™","abc£",
                "abc¢","abc∞","abc§","abc¶","abc•","abcª","abcº","abc≠","abcœ","abc∑","abc´","abc®", "abc†","abc—",
                "abc¨","abc^","abcø","abcπ","abc[","abc]","abcå","abcß","abc∂","abcƒ","abc©","abc ","̏abc",  "abc∆","abc¬",
                "abc…","abc^","abc\\","abcΩ","abc≈","abcç","abc√","abc∫","abc~","abcµ","abc≤","abc≥","abc÷","abc⁄","abc@",
                "abc‹","abc›","abc€","abcı","abc°","abc·","abc‚","abc_","abc±","abcŒ","abc„","abc‘", "abc”","abc’","abcÉ",
                "abcØ","abc∏","abc{","abc}","abcÅ","abcÍ","abcÔ","abc","abcÒ","abcæ","abcÆ","abc|", "abc«","abc◊","abcÑ",
                "abc¯","abcÈ","abcˇ","abc¿","","a","ab"};
        int sizeOfFalseStrings = listOfFalseStringsSimpleName.length;
        String[] listOfPermittedStringsSimpleName = new String[] { permittedSymbols, "abc", "ABC", "123", "-123", "-1aB2", "---" };
        int sizeOfPermittedStrings = listOfPermittedStringsSimpleName.length;
        // Negative tests
        for (int i = 0; i < sizeOfFalseStrings; i++) {
            try {
                ArgumentValidator.match(listOfFalseStringsSimpleName[i], argRegExprSimpleName, "SIMPLE_NAME_test_case");
                fail("Exception expected for: " + listOfFalseStringsSimpleName[i]);
            } catch (KapuaIllegalArgumentException e) {
                // Expected
            }
        }
        // Positive tests
        for (int i = 0; i < sizeOfPermittedStrings; i++) {
            try {
                ArgumentValidator.match(listOfPermittedStringsSimpleName[i], argRegExprSimpleName, "SIMPLE_NAME_test_case");
            } catch (Exception ex) {
                fail("No exception expected for: " + listOfPermittedStringsSimpleName[i]);
            }
        }
    }
    
    
    @Test
    public void testIllegalCharacterNameRegExp() throws Exception {
        String argRegExprName = "^[a-zA-Z0-9\\_\\-]{3,}$";
        String permittedSymbols="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ01234567890-_";
        String[] listOfFalseStringsName = new String[] {"abc!","abc\\", "abc#","abc$","abc%","abc&","abc'","abc(",
                "abc)","abc=","abc?","abc*","abc:","abc;","abc>","abc<","abc.","abc,","abc¡", "abc™","abc£",
                "abc¢","abc∞","abc§","abc¶","abc•","abcª","abcº","abc≠","abcœ","abc∑","abc´","abc®", "abc†","abc—",
                "abc¨","abc^","abcø","abcπ","abc[","abc]","abcå","abcß","abc∂","abcƒ","abc©","abc ","̏abc",  "abc∆","abc¬",
                "abc…","abc^","abc\\","abcΩ","abc≈","abcç","abc√","abc∫","abc~","abcµ","abc≤","abc≥","abc÷","abc⁄","abc@",
                "abc‹","abc›","abc€","abcı","abc–","abc°","abc·","abc‚","abc±","abcŒ","abc„","abc‘", "abc”","abc’",
                "abcÉ","abcØ","abc∏","abc{","abc}","abcÅ","abcÍ","abcÔ","abc","abcÒ","abcæ","abcÆ","abc|", "abc«","abc◊",
                "abcÑ","abc¯","abcÈ","abcˇ","abc¿","","a","ab"};
        int sizeOfFalseStrings = listOfFalseStringsName.length;
        String[] listOfPermittedStringsName = new String[]{permittedSymbols,"aBc1-_","acbd","1234","-_-_-"};
        int sizeOfPermittedStrings = listOfPermittedStringsName.length;
        // Negative tests
        for(int i=0;i<sizeOfFalseStrings;i++){
            try{
                ArgumentValidator.match(listOfFalseStringsName[i], argRegExprName, "NAME_REGEXP_test_case");
                fail("Exception expected for: " + listOfFalseStringsName[i]);
            } catch (KapuaIllegalArgumentException e) {
                // Expected
            }
        }
        // Positive tests
        for(int i=0; i<sizeOfPermittedStrings; i++) {
            try{
                ArgumentValidator.match(listOfPermittedStringsName[i], argRegExprName, "NAME_REGEXP_test_case");
            }
            catch(Exception ex){
                fail("No exception expected for: " + listOfPermittedStringsName[i]);
            }
        }
    }
    

    @Test
    public void testIllegalCharacterNameSpaceRegExp() throws Exception {
        String argRegExprNameSpace = "^[a-zA-Z0-9\\ \\_\\-]{3,}$";
        String permittedSymbols="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ01234567890-_ ";
        String[] listOfFalseStringsNameSpace = new String[] {"abc!","abc\\", "abc#","abc$","abc%","abc&","abc'",
                "abc(","abc)","abc=","abc?","abc*","abc:","abc;","abc>","abc<","abc.","abc,","abc¡", "abc™",
                "abc£","abc¢","abc∞","abc§","abc¶","abc•","abcª","abcº","abc≠","abcœ","abc∑","abc´","abc®", "abc†",
                "abc—","abc¨","abc^","abcø","abcπ","abc[","abc]","abcå","abcß","abc∂","abcƒ","abc©","abc ","̏abc",  "abc∆",
                "abc…","abc^","abc\\","abcΩ","abc≈","abcç","abc√","abc∫","abc~","abcµ","abc≤","abc≥","abc÷","abc⁄","abc@",
                "abc‹","abc›","abc€","abcı","abc–","abc°","abc·","abc‚","abc±","abcŒ","abc„","abc‘", "abc”","abc’",
                "abcÉ","abcØ","abc∏","abc{","abc}","abcÅ","abcÍ","abcÔ","abc","abcÒ","abcæ","abcÆ","abc|", "abc«","abc◊",
                "abcÑ","abc¯","abcÈ","abcˇ","abc¿","","a","ab", "abc¬",};
        int sizeOfFalseStrings = listOfFalseStringsNameSpace.length;
        String[] listOfPermittedStringsNameSpace = new String[] {permittedSymbols,"abc","123","ab1","1ab","ABC",
                "A1B","A b", "A-ab1","A_1","ab-","___","---","   ", "_- ","ab ","12 ","12_","2-1","abcd1234-_ "};
        int sizeOfPermittedStrings = listOfPermittedStringsNameSpace.length;
        for (int i = 0; i < sizeOfFalseStrings; i++) {
            try {
                ArgumentValidator.match(listOfFalseStringsNameSpace[i], argRegExprNameSpace, "NAME_SPACE_REGEXP_test_case");
                fail("Exception expected for: " + listOfFalseStringsNameSpace[i]);
            } catch (KapuaIllegalArgumentException e) {
                // Expected
            }
        }
        for (int i = 0; i < sizeOfPermittedStrings; i++) {
            try {
                ArgumentValidator.match(listOfPermittedStringsNameSpace[i], argRegExprNameSpace, "NAME_SPACE_REGEXP_test_case");
            } catch (Exception ex) {
                fail("No exception expected for: " + listOfPermittedStringsNameSpace[i]);
            }
        }
    }

    @Test
    public void testIllegalCharacterTagNameRegExp() throws Exception {
        String argRegExprTagNameRegExp = "[A-Za-z0-9-_@#!$%^&*+=?<>]{3,255}";
        StringBuilder tagName = new StringBuilder();
        StringBuilder tagTooLong = new StringBuilder();
        Random random = new Random();
        String permittedSymbols = "abcdefghijklmnopqrstuvxwyzABCDEFGHIJKLMNOPQRSTUVXYZ1234567890-_@#!$%^&*+=?<>";
        for (int i = 0; i < 254; i++) {
            tagName.append(permittedSymbols.charAt(random.nextInt(permittedSymbols.length())));
        }
        for (int i = 0; i < 256; i++) {
            tagTooLong.append(permittedSymbols.charAt(random.nextInt(permittedSymbols.length())));
        }
        String tagNameOk = tagName.toString();
        String tagNameTooLong = tagTooLong.toString();
        String[] listOfFalseStringsTagNameRegExp = new String[] {"abc_ABC123'","abc_ABC123(","abc_ABC123)",
                "abc_ABC123;","abc_ABC123:","abc_ABC123,","abc_ABC123.","abc_ABC123¡","abc_ABC123™","abc_ABC123£","abc_ABC123¢",
                "abc_ABC123∞","abc_ABC123§","abc_ABC123¶","abc_ABC123•","abc_ABC123º","abc_ABC123≠","abc_ABC123œ","abc_ABC123∑",
                "abc_ABC123´","abc_ABC123®","abc_ABC123†","abc_ABC123¨","abc_ABC123ø","abc_ABC123π","abc_ABC123[","abc_ABC123]",
                "abc_ABC123å","abc_ABC123ß","abc_ABC123∂","abc_ABC123ƒ","abc_ABC123©","abc_ABC123 ̏","abc_ABC123∆","abc_ABC123¬",
                "abc_ABC123…","abc_ABC123\"","abc_ABC123`","abc_ABC123Ω","abc_ABC123≈","abc_ABC123¿","abc_ABC123ç","abc_ABC123√",
                "abc_ABC123∫","abc_ABC123~","abc_ABC123µ","abc_ABC123≤","abc_ABC123≥","abc_ABC123÷","abc_ABC123⁄","abc_ABC123‹",
                "abc_ABC123›","abc_ABC123€","abc_ABC123ı","abc_ABC123–","abc_ABC123°","abc_ABC123±","abc_ABC123Œ","abc_ABC123„",
                "abc_ABC123‰","abc_ABC123“","abc_ABC123‘","abc_ABC123 ","abc_ABC123”","abc_ABC123’","abc_ABC123É","abc_ABC123Ø",
                "abc_ABC123∏","abc_ABC123{","abc_ABC123}","abc_ABC123Å","abc_ABC123Í","abc_ABC123Î","abc_ABC123Ï","abc_ABC123Ì",
                "abc_ABC123Ó","abc_ABC123Ô","abc_ABC123","abc_ABC123Ò","abc_ABC123æ","abc_ABC123Æ","abc_ABC123|","abc_ABC123~",
                "abc_ABC123«","abc_ABC123»","abc_ABC123Ç","abc_ABC123◊","abc_ABC123Ñ","abc_ABC123ˆ","abc_ABC123¯","abc_ABC123È",
                "abc_ABC123ˇ", tagNameTooLong};
        int sizeOfFalseStrings = listOfFalseStringsTagNameRegExp.length;
        String[] listOfPermittedStringTagNameRegExp = new String[]{tagNameOk,"abc_ABC123-","abc_ABC123_","abc_ABC123@","abc_ABC123#",
                "abc_ABC123!","abc_ABC123$","abc_ABC123%","abc_ABC123^","abc_ABC123&","abc_ABC123*","abc_ABC123+","abc_ABC123=","abc_ABC123?",
                "abc_ABC123<","abc_ABC123>","abc","ABC","123","abc_ABC123","%&$&!","&av231j","123"};
        int sizeOfPermittedStrings = listOfPermittedStringTagNameRegExp.length;
        for (int i = 0; i < sizeOfFalseStrings; i++) {
            try {
                ArgumentValidator.match(listOfFalseStringsTagNameRegExp[i], argRegExprTagNameRegExp, "TAGE_NAME_test_case");
                fail("Exception expected for: " + listOfFalseStringsTagNameRegExp[i]);
            } catch (KapuaIllegalArgumentException ex) {
                // Expected
            }
        }
        for (int i = 0; i < sizeOfPermittedStrings; i++) {
            try {
                ArgumentValidator.match(listOfPermittedStringTagNameRegExp[i], argRegExprTagNameRegExp, "TAGE_NAME_test_case");
            } catch (Exception ex) {
                fail("No exception expected for: " + listOfPermittedStringTagNameRegExp[i]);
            }
        }
    }

    @Test
    public void testIllegalCharacterMacAddressRegExp() throws Exception {
        String argRegExprMACaddress = "^([0-9A-F]{2}[:-]){5}([0-9A-F]{2})$";
        String[] listOfFalseStringsMACaddressRegExp = new String[]{"00:11:22:33:44","00:11:22:33:44:55:66",
                "00-11-22-33-44","00-11-22-33-44-55-66","00:11:22:33:44:55:","00-11-22-33-44-55-","0:11:22:33:44:55","00:1:22:33:44:55",
                "00:11:2:33:44:55","00:11:22:3:44:55","00:11:22:33:4:55","00:11:22:33:44:5","0-11-22-33-44-55","00-1-22-33-44-55",
                "00-11-2-33-44-55","00-11-22-3-44-55","00-11-22-33-4-55","00-11-22-33-44-5","01:23:45:6:78:90","34:56:78:90:A:BC",
                "000:11:22:33:44:55","00:111:AB:CD:EF:34","00:11:ABC:DE:F1:34","00:11:AB:CDE:F1:34","00:11:AB:CD:EF1:34","00:11:AB:CD:EF1:34",
                "00:11:AB:CD:EF:345","000-11-AB-CD-EF-34","00-111-AB-CD-EF-34","00-11-ABC-DE-F1-34","00-AA-AB-CDE-F1-34","00-11-AB-CD-EF1-34",
                "00-11-AB-CD-EF-345","AB:CD:eF.AB.CD.EF","56:78:90:aB:CD:EF","12:34:56:78:G1:12","H1:00:12:23:AB:CD","12:34-56-:78:AB:CD",
                ":00:11:22:33:44:55","-00-11-22-33-44-55","12-34-56-78_90-AB","12.34.56.78.90.AB","AV:CD:EF:12:34:56","AB:CS:EF-12-34-45"};
        int sizeOfFalseStringsMACaddress = listOfFalseStringsMACaddressRegExp.length;
        String[] listOfPermittedStringsMACaddressRegExp = new String[]{"00:11:22:33:44:55","00-11-22-33-44-55","0A:BC:DE:12:34:56",
                "0A-BC-DE-12-34-56","01:23:45:67:89:AB","00:00:00:00:00:00","FF:FF:FF:FF:FF:FF","01-23:45-67:89:A0","00:13:25:AF:AF:AF",
                "00-11-22-33-44-55","0A-2B-3C-4D-5F-1E"};
        int sizeOfPermittedStringsMACaddress = listOfPermittedStringsMACaddressRegExp.length;
        for (int i = 0; i < sizeOfFalseStringsMACaddress; i++) {
            try {
                ArgumentValidator.match(listOfFalseStringsMACaddressRegExp[i], argRegExprMACaddress, "MAC_ADDRESS_test_case");
                fail("Exception expected for: " + listOfFalseStringsMACaddressRegExp[i]);
            } catch (KapuaIllegalArgumentException ex) {
                // Expected
            }
        }
        for (int i = 0; i < sizeOfPermittedStringsMACaddress; i++) {
            try {
                ArgumentValidator.match(listOfPermittedStringsMACaddressRegExp[i], argRegExprMACaddress, "MAC_ADDRESS_test_case");
            } catch (Exception ex) {
                fail("No exception expected for: " + listOfPermittedStringsMACaddressRegExp[i]);
            }
        }
    }
}