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

import org.eclipse.kapua.KapuaIllegalArgumentException;
import org.eclipse.kapua.KapuaIllegalNullArgumentException;
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
            try{
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
}