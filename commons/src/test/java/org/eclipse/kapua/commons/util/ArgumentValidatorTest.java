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
        for (int i = 0; i < sizeOfFalseStrings; i++) {
            try {
                ArgumentValidator.match(listOfFalseStringsName[i], argRegExprName, "NAME_REGEXP_test_case");
                fail("Exception expected for: " + listOfFalseStringsName[i]);
            } catch (KapuaIllegalArgumentException e) {
                // Expected
            }
        }
        // Positive tests
        for (int i = 0; i < sizeOfPermittedStrings; i++) {
            try {
                ArgumentValidator.match(listOfPermittedStringsName[i], argRegExprName, "NAME_REGEXP_test_case");
            } catch (Exception ex) {
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
            try{
                ArgumentValidator.match(listOfFalseStringsNameSpace[i], argRegExprNameSpace, "NAME_SPACE_REGEXP_test_case");
                fail("Exception expected for: " + listOfFalseStringsNameSpace[i]);
            } catch (KapuaIllegalArgumentException e) {
                // Expected
            }
        }
        for(int i=0;i<sizeOfPermittedStrings;i++){
            try{
                ArgumentValidator.match(listOfPermittedStringsNameSpace[i], argRegExprNameSpace, "NAME_SPACE_REGEXP_test_case");
            }
            catch(Exception ex){
                fail("No exception expected for: " + listOfPermittedStringsNameSpace[i]);
            }
        }
    }
    
    @Test
    public void testIllegalCharacterEmailRegExp() throws Exception {
        String argRegExprEmailRegExp = "^(\\w+)([-+.][\\w]+)*@(\\w[-\\w]*\\.){1,5}([A-Za-z]){2,4}$";
        StringBuilder namePart = new StringBuilder();
        Random random = new Random();
        String permittedSymbols = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ01234567890";
        for (int i = 0; i < 500; i++) {
            namePart.append(permittedSymbols.charAt(random.nextInt(permittedSymbols.length())));
        }
        String longname = namePart.toString();
        String[] listOfFalseStringsEmailRegExp = new String[] {"abc!.ABC_123@co.co","abc\".ABC_123@co.co",
                "abc#.ABC_123@co.co","abc$.ABC_123@co.co","abc%.ABC_123@co.co","abc&.ABC_123@co.co","abc'.ABC_123@co.co",
                "abc(.ABC_123@co.co","abc).ABC_123@co.co","abc=.ABC_123@co.co","abc?.ABC_123@co.co","abc*.ABC_123@co.co",
                "abc:.ABC_123@co.co","abc;.ABC_123@co.co","abc¶.ABC_123@co.co","abc•.ABC_123@co.co","abcº.ABC_123@co.co",
                "abc<.ABC_123@co.co","abc>.ABC_123@co.co","abc¡.ABC_123@co.co","abc™.ABC_123@co.co","abc[.ABC_123@co.co",
                "abc].ABC_123@co.co","abc{.ABC_123@co.co","abc}.ABC_123@co.co","abc^.ABC_123@co.co","abcÈ.ABC_123@co.co",
                "abc~.ABC_123@co.co","abc±.ABC_123@co.co","abc=.ABC_123@co.co","abc£.ABC_123@co.co","abc¢.ABC_123@co.co",
                "abc∞.ABC_123@co.co","abc≤.ABC_123@co.co","abc≥.ABC_123@co.co","abc§.ABC_123@co.co","abc•.ABC_123@co.co",
                "abc.ABC_123@co.co","abcÒ.ABC_123@co.co","abc«.ABC_123@co.co","abc◊.ABC_123@co.co","abcÑ.ABC_123@co.co",
                "abcˆ.ABC_123@co.co","abc¯.ABC_123@co.co","abcÉ.ABC_123@co.co","abcØ.ABC_123@co.co","abc∏.ABC_123@co.co",
                "abcÅ.ABC_123@co.co","abcÍ.ABC_123@co.co","abcÔ.ABC_123@co.co","abc¿.ABC_123@co.co","abcŒ.ABC_123@co.co",
                "abc\\.ABC_123@co.co","abc„.ABC_123@co.co","abc‘.ABC_123@co.co","abc’.ABC_123@co.co","abc‚.ABC_123@co.co",
                "abcæ.ABC_123@co.co","abcÆ.ABC_123@co.co","abc|.ABC_123@co.co","abcÈ.ABC_123@co.co","abcˇ.ABC_123@co.co",
                "abc‹.ABC_123@co.co","abc›.ABC_123@co.co","abc€.ABC_123@co.co","abcı.ABC_123@co.co","abc–.ABC_123@co.co",
                "abc°.ABC_123@co.co","abc·.ABC_123@co.co","abc.ABC_123@a.B._.1.-.C.co","abc@to.12.TO._-.ab.to.co",
                "abc@com","abc@co.c","@co.co","@com.com","ab.@co.co","abc.ABC_123@abc.toolong"};
        int sizeOfFalseStrings = listOfFalseStringsEmailRegExp.length;
        String[] listOfPermittedStringsEmailRegExp = new String[] {"a@co.co","ab@co.co","abc@co.co","a.a@co.co","ab.ab@co.co",
                "abc.abc@co.co", "1@co.co", "12@co.co", "123@co.co", "1.1@co.co", "12.12@co.co", "123.123@co.co", "a.1@co.co",
                "abc.123@co.co", "abc.ABC-123+_@co.co", "ab.AB+12-_@12.co", "ab.AB+12-_@co12.co", "ab.AB+12-_@1.co", "ab.AB+12-_@12.co",
                "ab.AB+12-_@123.co", "ab.AB+12-_@aB123.co", "ab.AB+12-_@ab-12.co", "abc123ABC@ABcde.coms", "aA1-_.aA1-_@a.A.1._1aB.co",
                longname + "@co.co", "a.a.a.a.a.a.a.a.a.a@co.co", "abc.ABC_123@A12bc_.ab.12c.1234abc_.co", "abc.ABC_123@a.ab.co", };
        int sizeOPermittedStrings = listOfPermittedStringsEmailRegExp.length;
        for (int i = 0; i < sizeOfFalseStrings; i++) {
            try {
                ArgumentValidator.match(listOfFalseStringsEmailRegExp[i], argRegExprEmailRegExp, "EMAIL_test_case");
                fail("Exception expected for: " + listOfFalseStringsEmailRegExp[i]);
            } catch (KapuaIllegalArgumentException ex) {
                // Expected
            }
        }
        for (int i = 0; i < sizeOPermittedStrings; i++) {
            try {
                ArgumentValidator.match(listOfPermittedStringsEmailRegExp[i], argRegExprEmailRegExp, "EMAIL_test_case");
            } catch (Exception ex) {
                fail("No exception expected for: " + listOfPermittedStringsEmailRegExp[i]);
            }
        }
    }
}