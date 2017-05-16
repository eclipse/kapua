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
 *
 *******************************************************************************/
package org.eclipse.kapua.commons.util;

import java.util.ArrayList;
import java.util.Date;

import org.eclipse.kapua.KapuaIllegalArgumentException;
import org.eclipse.kapua.KapuaIllegalNullArgumentException;
import org.junit.Assert;
import org.junit.Test;

public class ArgumentValidatorTest extends Assert {

    @Test
    public void testMatchNotNull()
            throws Exception {
        String argVal1 = null;
        String argRegExp1 = "^[a-zA-Z0-9\\-]{3,}$";
        ArgumentValidator.match(argVal1, argRegExp1, "Null_name_test_case");
    }

    @Test(expected = KapuaIllegalArgumentException.class)
    public void testTooShort()
            throws Exception {
        String argVal2 = "ni";
        String argRegExp2 = "^[a-zA-Z0-9\\-]{3,}$";
        ArgumentValidator.match(argVal2, argRegExp2, "Short_name_test_case");
    }

    @Test(expected = AssertionError.class)
    public void testTooLong()
            throws KapuaIllegalArgumentException {
        String argRegExp1 = "^[a-zA-Z0-9\\-]{3,}$";
        String argVal1 = "1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz-" +
                "-1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDE" +
                "FGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRS" +
                "TQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefigh" +
                "ijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstu" +
                "vxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--123456789" +
                "ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLM" +
                "NIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcd" +
                "efighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnop" +
                "rwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234" +
                "567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFG" +
                "HIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVW" +
                "Z-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighij" +
                "klmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz" +
                "--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890A" +
                "BCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRS" +
                "TQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcde" +
                "fighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstu" +
                "vxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--12345" +
                "67890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKL" +
                "MNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ" +
                "-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmno" +
                "prwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz-" +
                "-1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEF" +
                "GHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRST" +
                "QUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighi" +
                "jklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuv" +
                "xyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890" +
                "ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLM" +
                "NIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcd" +
                "efighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnop" +
                "rwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234" +
                "567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFG" +
                "HIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVW" +
                "-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijk" +
                "lmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz-" +
                "-1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890AB" +
                "CDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRST" +
                "QUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdef" +
                "ighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuv" +
                "xyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--123456" +
                "890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMN" +
                "IPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-a" +
                "bcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnopr" +
                "wstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1" +
                "34567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHI" +
                "JKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUX" +
                "VWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijkl" +
                "mnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyz" +
                "z--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCD" +
                "EFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPR" +
                "STQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefig" +
                "hijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwst" +
                "vxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--123456789" +
                "ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLM" +
                "NIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcd" +
                "efighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnop" +
                "rwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234" +
                "567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFG" +
                "HIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVW" +
                "Z-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighij" +
                "klmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz" +
                "--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890A" +
                "BCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRS" +
                "TQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcde" +
                "fighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstu" +
                "vxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--12345" +
                "67890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKL" +
                "MNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ" +
                "-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmno" +
                "prwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz-" +
                "-1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEF" +
                "GHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRST" +
                "QUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighi" +
                "jklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuv" +
                "xyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890" +
                "ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLM" +
                "NIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcd" +
                "efighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnop" +
                "rwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--1234567890ABCDEFGHIJKLMNIPRSTQUXVWZ-abcdefighijklmnoprwstuvxyzzz--";
        ArgumentValidator.match(argVal1, argRegExp1, "Too_long_name_test_case");
        Assert.fail();
    }

    @Test(expected = KapuaIllegalArgumentException.class)
    public void testIllegalCharacterSimpleName()
            throws KapuaIllegalArgumentException {
        String argRegExpr_SIMPLE_NAME = "^[a-zA-Z0-9\\-]{3,}$";
        String[] listOfCharacters_SIMPLE_NAME = new String[] { "abcdefghijklmnoprsqtuxvwyzABCDEFGHIJKLMNOPRQRSTUVWXYZ01234567890--all-PERMITED_SImbo1s-12309fd", "-da",
                "sh-is-permitted", "!", "\"", "#", "$", "%", "&", "'", "(", ")", "=", "?", "*", "[", "]", "{", "}", "Œ", "^", "[", "|", "}", "Æ", "@", "_permitted_symbol", "¿", ":", "ˇ", ";", "È",
                ">",
                "<", "~", "«", "≈", "ç", "Ç", "√", "◊", "∫", "Ñ", "µ", "¯", "å", "ß", "Å", "Í", "ƒ", "Ï", "©", " ̏", "∆", "Ô", " ̑", "", "¬", "Ò", "œ", "ø", "Ø", "π", "∏", "‹", "›", "€", "ı", "¶",
                "°", "•", "±",
                "//", "\\" };
        int size = listOfCharacters_SIMPLE_NAME.length;
        for (int i = 0; i < size; i++) {
            ArgumentValidator.match(listOfCharacters_SIMPLE_NAME[i], argRegExpr_SIMPLE_NAME, "SIMPLE_NAME_test_case");
        }

    }

    @Test(expected = KapuaIllegalArgumentException.class)
    public void testIllegalCharacterNameRegExp()
            throws KapuaIllegalArgumentException {
        String argRegExpr_NAME_REGEXP = "^[a-zA-Z0-9\\_\\-]{3,}$";
        String[] listOfCharacters_NAME_REGEXP = new String[] { "all-PERMITED______simBOLs-12309fd_", "-dash-is-permitted", "!", "\"", "#", "$", "%", "&", "'", "(", ")", "=", "?",
                "*", "[", "]", "{", "}", "Œ", "^", "[", "|", "}", "Æ", "@", "_symbol", "¿", ":", "ˇ", ";", "È", ">", "<", "~", "«", "≈", "ç", "Ç", "√", "◊", "∫", "Ñ", "µ", "¯", "å", "ß", "Å", "Í",
                "ƒ", "Ï", "©", " ̏", "∆", "Ô", " ̑", "", "¬", "Ò", "œ", "ø", "Ø", "π", "∏", "‹", "›", "€", "ı", "¶", "°", "•", "±", "//", "\\" };
        int size = listOfCharacters_NAME_REGEXP.length;
        for (int i = 0; i < size; i++) {
            ArgumentValidator.match(listOfCharacters_NAME_REGEXP[i], argRegExpr_NAME_REGEXP, "SIMPLE_NAME_test_case");
        }
    }

    @Test(expected = KapuaIllegalArgumentException.class)
    public void testIllegalCharacterNameSpaceRegExp()
            throws KapuaIllegalArgumentException {
        String argRegExpr_NAME_SPACE_REGEXP = "^[a-zA-Z0-9\\ \\_\\-]{3,}$";
        String[] listOfCharacters_NAME_SPACE_REGEXP = new String[] { "all_-PERMITED_   si m b o l s-12309fd_ ", "   space_is_permitted", "-dash-is-permitted", "!", "\"", "#",
                "$", "%", "&", "'", "(", ")", "=", "?", "*", "[", "]", "{", "}", "Œ", "^", "[", "|", "}", "Æ", "@", "_is_permitted", "¿", ":", "ˇ", ";", "È", ">", "<", "~", "«", "≈", "ç", "Ç", "√",
                "◊",
                "∫", "Ñ", "µ", "¯", "å", "ß", "Å", "Í", "ƒ", "Ï", "©", " ̏", "∆", "Ô", " ̑", "", "¬", "Ò", "œ", "ø", "Ø", "π", "∏", "‹", "›", "€", "ı", "¶", "°", "•", "±", "//", "\\" };
        int size = listOfCharacters_NAME_SPACE_REGEXP.length;
        for (int i = 0; i < size; i++) {
            ArgumentValidator.match(listOfCharacters_NAME_SPACE_REGEXP[i], argRegExpr_NAME_SPACE_REGEXP, "NAME_SPACE_test_case");
        }
    }

    @Test(expected = KapuaIllegalArgumentException.class)
    public void testIllegalCharacterPasswordRegExp()
            throws KapuaIllegalArgumentException {
        String argRegExpr_PASSWORD_REGEXP = "^.*(?=.{12,})(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!\\~\\|]).*$";
        String[] listOfCharacters_PASSWORD_REGEXP = new String[] { "ThisOneW1llN@tFail", "THISoneH@sNoDigit]", "Th1sOneH4sN0S1mb0ls", "Abcd1234@r45e", "1234abcdABCD@",
                "Abcd1234~cdba", "aBcd5678|1234", "Character1234$" };
        int size = listOfCharacters_PASSWORD_REGEXP.length;
        for (int i = 0; i < size; i++) {
            ArgumentValidator.match(listOfCharacters_PASSWORD_REGEXP[i], argRegExpr_PASSWORD_REGEXP, "PASSWORD_test_case");
        }
    }

    @Test(expected = KapuaIllegalArgumentException.class)
    public void testIllegalCharacterEmailRegExp()
            throws KapuaIllegalArgumentException {
        String argRegExpr_EMAIL_REGEXP = "^(\\w+)([-+.][\\w]+)*@(\\w[-\\w]*\\.){1,5}([A-Za-z]){2,4}$";
        String[] listOfCharacters_EMAIL_REGEXP = new String[] { "1@1.2", "1@1.co", "hot-programmer@hot.com", "hot.programmer1@hotty.com", "bad+programmer@angel.uk",
                "sh0r7@sh.co", "o@g.co", "1@g.org", "+@mail.com", ".@SUPER.COM", "-@dash.robo", "sAmpl-e.mail+robot@examp.co", "", "EXTREMELY-long.emailadress-which" +
                        "-does.not.exist-onlyEXIST-FOR.TEsting-purposes+at-kapua.com@stupid.org",
                "name1.kapua@@g.com", "name-.+kapua@gmail.com", "www.my-sample123-email@g.co",
                "123-sam-ple+mail-reawe@h.comp", ".r-r@g.co", "shotr@gm.co", "a@g.c", "B@H.or", "worm-lover@hotmail.com", "hedgehog-hunter@skype.org", "some1@at3.com", "new" +
                        "_email@f.g",
                "newev_email123@f.co", "ONELONGEMAILADDRESSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSNGEMAILADDRESSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS" +
                        "SSSSSSSSSSSSSSSSSSSSSNGEMAILADDRESSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSNGEMAILADDRESSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS" +
                        "SSNGEMAILADDRESSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSNGEMAILADDRESSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSNGEMAILADDRESSSSS" +
                        "SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSNGEMAILADDRESSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSNGEMAILADDRESSSSSSSSSSSSSSSSSSSSSSSS" +
                        "SSSSSSSSSSSSSSSSSSSSSSSSSSSSNGEMAILADDRESSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSNGEMAILADDRESSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS" +
                        "SSSSSSSSSNGEMAILADDRESSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSNGEMAILADDRESSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSNGEMAILADD" +
                        "RESSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSNGEMAILADDRESSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSNGEMAILADDRESSSSSSSSSSSSSSSSS" +
                        "SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSNGEMAILADDRESSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSNGEMAILADDRESSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS" +
                        "SSSSSSSSSSSSSSSSNGEMAILADDRESSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSNGEMAILADDRESSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSNGE" +
                        "MAILADDRESSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSNGEMAILADDRESSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSNGEMAILADDRESSSSSSSSSS" +
                        "SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSNGEMAILADDRESSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSNGEMAILADDRESSSSSSSSSSSSSSSSSSSSSSSSSSSSS" +
                        "SSSSSSSSSSSSSSSSSSSSSSSNGEMAILADDRESSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSNGEMAILADDRESSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS" +
                        "SSSSNGEMAILADDRESSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSNGEMAILADDRESSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSNGEMAILADDRESSS" +
                        "SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSNGEMAILADDRESSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSNGEMAILADDRESSSSSSSSSSSSSSSSSSSSSS" +
                        "SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSNGEMAILADDRESSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSNGEMAILADDRESSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS" +
                        "SSSSSSSSSSSNGEMAILADDRESSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSNGEMAILADDRESSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSNGEMAILA" +
                        "DDRESSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSNGEMAILADDRESSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSNGEMAILADDRESSSSSSSSSSSSSSS" +
                        "SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSNGEMAILADDRESSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSNGEMAILADDRESSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS" +
                        "SSSSSSSSSSSSSSSSSSNGEMAILADDRESSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSNGEMAILADDRESSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSN" +
                        "GEMAILADDRESSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSNGEMAILADDRESSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSNGEMAILADDRESSSSSSSS" +
                        "SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSNGEMAILADDRESSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSNGEMAILADDRESSSSSSSSSSSSSSSSSSSSSSSSSSS" +
                        "SSSSSSSSSSSSSSSSSSSSSSSSSNGEMAILADDRESSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSNGEMAILADDRESSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS" +
                        "SSSSSSNGEMAILADDRESSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSNGEMAILADDRESSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS" +
                        "SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS" +
                        "SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS" +
                        "SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS" +
                        "SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS" +
                        "SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS" +
                        "SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS" +
                        "SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSghfjghjfhgfjfjffjgfgSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS" +
                        "SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS" +
                        "SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS" +
                        "SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS" +
                        "SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSfdfdgdwerergfhgedfdfSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS" +
                        "SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS" +
                        "SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSasgdsg97re68f8a7ftgg3j439ydfdfdgfherSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS" +
                        "SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS" +
                        "SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS" +
                        "SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS" +
                        "SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS" +
                        "SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS" +
                        "SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS" +
                        "SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS" +
                        "SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSjh457634GFYDFGmfkdfjj3437484734jhfdhf8373kjfkdf39483hjhfdfbeke23pfendjknfSSSSSSSSSSSSSSSSSSSSSSSSSSSSS" +
                        "SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS" +
                        "SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS" +
                        "SSSSSSSSSSSSSSSSSSSSSSSSSSSSSEXTREMEEEEEEEMEEEEEELYYYYYYLLLLOOOOOOOOOONGEMAAAAAAILLLLLLLLLLLLLLLSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS" +
                        "SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS" +
                        "SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS" +
                        "SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS" +
                        "SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS" +
                        "SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS" +
                        "SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS" +
                        "SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS@gmail.com" };
        int size = listOfCharacters_EMAIL_REGEXP.length;

        for (int i = 0; i < size; i++) {
            ArgumentValidator.match(listOfCharacters_EMAIL_REGEXP[i], argRegExpr_EMAIL_REGEXP, "EMAIL_test_case");
        }
    }

    @Test(expected = KapuaIllegalArgumentException.class)
    public void testIllegalCharacterTagNameRegExp()
            throws KapuaIllegalArgumentException {
        String argRegExpr_TAG_NAME_REGEXP = "[A-Za-z0-9-_@#!$%^&*+=?<>]{3,255}";
        String[] listOfCharacters_TAG_NAME_REGEXP = new String[] { "abcdefghijklmnopqrstuvxwyzABCDEFGHIJKLMNOPQRSTUVXYZ1234567890-_@#!$%^&*+=?<>", "Normal_name" +
                "-with_sp@cialS%^&YMBOLS", "SYMBO,L", "tag.name", "[tag]name", "(tag)name", "{tag}name", "Œtagname", "tag^name", "tag√name", "tag~name", "tagÆname", "tag¿name",
                "tag:name", "tagˇname", "tag;name", "tagÈname", "tag>name", "tag<name", "tag~name", "tag«name", "tag≈", "tagç", "tagÇ", "tag√", "123◊", "tag∫", "tagÑ", "tagµ", "tag¯",
                "nameå", "223ß", "tagÅ", "tagÍ", "tagƒ", "tagÏ", "tag©", "tag ̏", "tag∆", "tagÔ", "tag ̑", "tag", "tag¬", "tagÒ", "tagœ", "taøg", "taØg", "tagπ", "tag∏", "tag‹", "›tag",
                "€tag", "ıtag", "¶tag", "°tag", "•tag", "±tag", "//tag", "tag\\", "---", "___", "%%%", "###", "@@@", "!!!", "???", ">>>", "<<<", "+++", "***", "111", "ABC", "123", "abc",
                "^^^", "===", "$$$", "256SYMOBLS-256SYMBOLS-256SYMBOLS-256SYMBOLS-256SYMBOLS-256SYMBOLS-256SYMBOLS-256SYMBOLS-256SYMBOLS-256SYMBOLS-256SYMBOLS-256SYM" +
                        "BOLS-256SYMBOLS-256SYMBOLS-256SYMBOLS-256SYMBOLS-256SYMBOLS-256SYMBOLS-256SYMBOLS-256SYMBOLS-256SYMBOLS-256SYMBOLS-256SYMBOLS----" };
        int size = listOfCharacters_TAG_NAME_REGEXP.length;
        for (int i = 0; i < size; i++) {
            ArgumentValidator.match(listOfCharacters_TAG_NAME_REGEXP[i], argRegExpr_TAG_NAME_REGEXP, "TAGE_NAME_test_case");
        }

    }

    @Test(expected = KapuaIllegalArgumentException.class)
    public void testIllegalCharacterAddressRegExp()
            throws KapuaIllegalArgumentException {
        String argRegExpr_IP_ADDRESS_REGEXP = "(^(http://)|(https://)|())([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|" +
                "2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])((.*)$)";
        String[] listOfCharacters_IP_ADDRESS_REGEXP = new String[] { "000.000.000.000", "255.255.255.255", "http://000.000.000.000", "http://255.255.255.255",
                "https://000.000.000.000", "https://255.255.255.255", "10.10.10.10", "http://10.10.10.10", "https://10.10.10.10", "192.168.0.1", "http://192.168.0.1",
                "https://192.168.0.1", "172.0.0.0", "http://172.0.0.0", "https://172.0.0.0", "123.123.123.123", "http://123.123.123.123", "https://123.123.123.123",
                "255.255.255.256", "http://255.255.255.256", "https://255.255.255.256", "290.290.290.290", "http://290.290.290.290", "https://290.290.290.290",
                "255.255.255.255.255", "http://255.255.255.255.255", "https://255.255.255.255.255" };
        int size = listOfCharacters_IP_ADDRESS_REGEXP.length;
        for (int i = 0; i < size; i++) {
            ArgumentValidator.match(listOfCharacters_IP_ADDRESS_REGEXP[i], argRegExpr_IP_ADDRESS_REGEXP, "IP_ADDRESS_test_case");
        }
    }

    @Test(expected = KapuaIllegalArgumentException.class)
    public void testIllegalCharacterLocalIpAddressRegExp()
            throws KapuaIllegalArgumentException {
        String argRegExpr_LOCAL_IP_ADDRESS_REGEXP = "(^(http://)|(https://))((127\\.0\\.0\\.1)|(10(\\.[0-9]+){3,3})|(172(\\.[0-9]+){3,3})|(192\\.168(\\.[0-9]+){2,2}))((.*)$)";
        String[] listOfCharacters_LOCAL_IP_ADDRESS_REGEXP = new String[] { "000.000.000.000", "255.255.255.255", "http://000.000.000.000", "http://255.255.255.255",
                "https://000.000.000.000", "https://255.255.255.255", "10.10.10.10", "http://10.10.10.10", "https://10.10.10.10", "192.168.0.1", "http://192.168.0.1",
                "https://192.168.0.1", "172.0.0.0", "http://172.0.0.0", "https://172.0.0.0", "123.123.123.123", "http://123.123.123.123", "https://123.123.123.123",
                "255.255.255.256", "http://255.255.255.256", "https://255.255.255.256", "290.290.290.290", "http://290.290.290.290", "https://290.290.290.290",
                "255.255.255.255.255", "http://255.255.255.255.255", "https://255.255.255.255.255" };
        int size = listOfCharacters_LOCAL_IP_ADDRESS_REGEXP.length;
        for (int i = 0; i < size; i++) {
            ArgumentValidator.match(listOfCharacters_LOCAL_IP_ADDRESS_REGEXP[i], argRegExpr_LOCAL_IP_ADDRESS_REGEXP, "LOCAL_IP_ADDRESS_test_case");
        }
    }

    @Test(expected = KapuaIllegalArgumentException.class)
    public void testIllegalCharacterMacAddressRegExp()
            throws KapuaIllegalArgumentException {
        String argRegExpr_MAC_ADDRESS_REGEXP = "^([0-9A-F]{2}[:-]){5}([0-9A-F]{2})$";
        String[] listOfCharacters_MAC_ADDRESS_REGEXP = new String[] { "00:00:00:00:00:00", "AA:AA:AA:AA:AA:AA", "AB:CD:EF:01:23:45", "10:20:90:99:91:92", "99:99:99:99:99:99",
                "FF:FF:FF:FF:FF:FF", "00:11:22:33:44:55:66", "AB:CD:EF:GF:FF:FF", "01.23.34.FF" };
        int size = listOfCharacters_MAC_ADDRESS_REGEXP.length;

        for (int i = 0; i < size; i++) {
            ArgumentValidator.match(listOfCharacters_MAC_ADDRESS_REGEXP[i], argRegExpr_MAC_ADDRESS_REGEXP, "MAC_ADDRESS_test_case");
        }
    }

    @Test(expected = KapuaIllegalNullArgumentException.class)
    public void testNotNull()
            throws KapuaIllegalNullArgumentException {
        Object[] listOfCharacters_NULL_TEST_REGEXP = new Object[] { 1, 2, 3, 123, "1", "kapua", "a", "abcdefghijklmnopqrstuvxyz", "1234567890", "ndgw9gKFOEF95jfe13@€ı–‹€–‹ı™&#", null };
        int size = listOfCharacters_NULL_TEST_REGEXP.length;
        for (int i = 0; i < size; i++) {
            ArgumentValidator.notNull(listOfCharacters_NULL_TEST_REGEXP[i], "testForNull");
        }
    }

    @Test(expected = KapuaIllegalNullArgumentException.class)
    public void testNotEmptyOrNullString()
            throws KapuaIllegalNullArgumentException {
        String[] listOfChoices_NOT_EMPTY_OR_NULL_TEST_REGEXP = new String[] { "string", "", null };
        int size = listOfChoices_NOT_EMPTY_OR_NULL_TEST_REGEXP.length;
        for (int i = 0; i < size; i++) {
            ArgumentValidator.notEmptyOrNull(listOfChoices_NOT_EMPTY_OR_NULL_TEST_REGEXP[i], "notEmptyOrNullTest");
        }
    }
    
    @Test(expected = KapuaIllegalNullArgumentException.class)
    public void testNotEmptyOrNullString2()
            throws KapuaIllegalNullArgumentException {
        String var1 = null;
        ArgumentValidator.notEmptyOrNull(var1, "string");
    }

    @Test(expected = KapuaIllegalNullArgumentException.class)
    public void testNotEmptyOrNullObject()
            throws KapuaIllegalNullArgumentException {
        Object[] listOfChoices_empty = new Object[] {};
        Object[] listOfChoices = new Object[] {1, "string", null};
        int size = listOfChoices.length;
        for(int i=0; i<size; i++){
            ArgumentValidator.notEmptyOrNull(listOfChoices, "test");
        }
        ArgumentValidator.notEmptyOrNull(listOfChoices_empty, "notEmptyOrNullTest");
    }
    
    @Test(expected = KapuaIllegalNullArgumentException.class)
    public void testNotEmptyOrNullCollection()
            throws KapuaIllegalNullArgumentException {
        ArrayList<String> listOfChoices_NOT_EMPTY_OR_NULL_TEST_REGEXP_string = new ArrayList<>();
        ArrayList<Integer> listOfChoices_NOT_EMPTY_OR_NULL_TEST_REGEXP_int_null = new ArrayList<>();
        ArrayList<Integer> listOfChoices_NOT_EMPTY_OR_NULL_TEST_REGEXP_int_full = new ArrayList<>();
        ArrayList<Boolean> listOfChoices_NOT_EMPTY_OR_NULL_TEST_REGEXP_bool_full = new ArrayList<>();
        ArrayList<Boolean> listOfChoices_NOT_EMPTY_OR_NULL_TEST_REGEXP_bool_null = new ArrayList<>();
        ArrayList<Long> listOfChoices_NOT_EMPTY_OR_NULL_TEST_REGEXP_long = new ArrayList<>();
        ArrayList<Long> listOfChoices_NOT_EMPTY_OR_NULL_TEST_REGEXP_null = new ArrayList<>();

        listOfChoices_NOT_EMPTY_OR_NULL_TEST_REGEXP_null.add(null);
        listOfChoices_NOT_EMPTY_OR_NULL_TEST_REGEXP_string.add("string");
        listOfChoices_NOT_EMPTY_OR_NULL_TEST_REGEXP_string.add(null);
        listOfChoices_NOT_EMPTY_OR_NULL_TEST_REGEXP_string.add("");
        listOfChoices_NOT_EMPTY_OR_NULL_TEST_REGEXP_int_full.add(12);
        listOfChoices_NOT_EMPTY_OR_NULL_TEST_REGEXP_bool_full.add(true);
        listOfChoices_NOT_EMPTY_OR_NULL_TEST_REGEXP_bool_full.add(false);
        listOfChoices_NOT_EMPTY_OR_NULL_TEST_REGEXP_long.add((long) 123456789);

        ArgumentValidator.notEmptyOrNull(listOfChoices_NOT_EMPTY_OR_NULL_TEST_REGEXP_string, "notEmptyOrNullTest");
        
        ArgumentValidator.notEmptyOrNull(listOfChoices_NOT_EMPTY_OR_NULL_TEST_REGEXP_int_null, "notEmptyOrNullTest");
        ArgumentValidator.notEmptyOrNull(listOfChoices_NOT_EMPTY_OR_NULL_TEST_REGEXP_int_full, "notEmptyOrNullTest");
        ArgumentValidator.notEmptyOrNull(listOfChoices_NOT_EMPTY_OR_NULL_TEST_REGEXP_bool_null, "notEmptyOrNullTest");
        ArgumentValidator.notEmptyOrNull(listOfChoices_NOT_EMPTY_OR_NULL_TEST_REGEXP_bool_full, "notEmptyOrNullTest");
        ArgumentValidator.notEmptyOrNull(listOfChoices_NOT_EMPTY_OR_NULL_TEST_REGEXP_long, "notEmptyOrNullTest");
        
    }

    @Test(expected = KapuaIllegalArgumentException.class)
    public void testIsNull()
            throws KapuaIllegalArgumentException {
        Object[] list1 = new Object[] {null,1};
        int size = list1.length;
        for (int i = 0; i < size; i++) {
            ArgumentValidator.isNull(list1[i], "isNullTest");
        }
    }

    @Test(expected = AssertionError.class)
    public void testIsEmptyOrNull()
            throws KapuaIllegalArgumentException {
        ArgumentValidator.isEmptyOrNull(null, "test");
        Assert.fail();
    }

    @Test(expected = AssertionError.class)
    public void testIsEmptyOrNull2()
            throws KapuaIllegalArgumentException {
        ArgumentValidator.isEmptyOrNull("", "test");
        Assert.fail();
    }
    
    @Test(expected = KapuaIllegalArgumentException.class)
    public void testIsEmptyOrNull3()
            throws KapuaIllegalArgumentException {
        ArgumentValidator.isEmptyOrNull("Not_empty_string", "test");
    }
    
    @Test(expected = KapuaIllegalNullArgumentException.class)
    public void testNotNegative()
            throws KapuaIllegalNullArgumentException {
        long[] listOfChoices_NOT_NEGATIVE_TEST_REGEXP = new long[] { 0, 1, 2, -1, 13, -13, -45, -0, 133232, 450, -999999999, 859897928, -859897928 };
        int size = listOfChoices_NOT_NEGATIVE_TEST_REGEXP.length;
        for (int i = 0; i < size; i++) {
            ArgumentValidator.notNegative(listOfChoices_NOT_NEGATIVE_TEST_REGEXP[i], "not null test");
        }
    }
    @SuppressWarnings("deprecation")
    @Test(expected = KapuaIllegalArgumentException.class)
    public void testDateRange()
            throws KapuaIllegalArgumentException {
        Date startTime0 = new Date(2017, 1, 1);
        Date endTime0 = new Date(2018, 1, 1);

        Date startTime1 = new Date(97, 1, 23);
        Date endTime1 = new Date(94, 1, 31);

        Date startTime2 = new Date(2016, 1, 30);
        Date endTime2 = new Date(2016, -1, 31);
        
        Date startTime3 = new Date(2016, -12, 31);
        Date endTime3 = new Date(2017, 1, 1);

        Date[] startTime = new Date[] { startTime0, startTime1, startTime2,  startTime3};
        Date[] endTime = new Date[] { endTime0,  endTime1,  endTime2, endTime3};

        for (int i = 0; i < startTime.length; i++) {
            ArgumentValidator.dateRange(startTime[i], endTime[i]);
        }
    }

    @Test(expected = KapuaIllegalArgumentException.class)
    public void testDateRangeLong()
            throws KapuaIllegalArgumentException {
        long startTime1 = 123456789L;
        long endTime1 = 123456789999L;

        long startTime2 = 123456789L;
        long endTime2 = 123456789L;
        
        long startTime3 = 1234567890123456789L;
        long endTime3 = 123456789012345L;

        long startTime4 = 1234567890123459L;
        long endTime4 = -1234567890123456712L;

        long startTime5 = 123456789012L;
        long endTime5 = -123456678L;

        long startTime6 = -123456789L;
        long endTime6 = 123456788901234L;

        long startTime7 = -123456780123L;
        long endTime7 = 1234567L;

        long startTime8 = -1234567L;
        long endTime8 = -12345678901234L;

        long startTime9 = -123456789L;
        long endTime9 = -123456L;

        long[] startTime = new long[] {startTime1, startTime2, startTime3, startTime4, startTime5, startTime6, startTime7, startTime8, startTime9 };
        long[] endTime = new long[] {endTime1, endTime2, endTime3, endTime4, endTime5, endTime6, endTime7, endTime8, endTime9 };

        for (int i = 0; i < startTime.length; i++) {
            ArgumentValidator.dateRange(startTime[i], endTime[i]);
        }
    }

    @Test(expected = KapuaIllegalArgumentException.class)
    public void testNumRange()
            throws KapuaIllegalArgumentException {
        long minValue = 12;
        long maxValue = 12345;
        long numRangeCollection[] = new long[] { 15, 12, 12345, 1, 123456 };
        for (long element : numRangeCollection) {
            ArgumentValidator.numRange(element, minValue, maxValue, "numRange test");
        }
    }

    @Test(expected = KapuaIllegalArgumentException.class)
    public void testNumRange2()
            throws KapuaIllegalArgumentException {
        long minValue = 12;
        long maxValue = 12345;
        long numRangeCollection[] = new long[] { 15, 12345, 123456 };
        for (long element : numRangeCollection) {
            ArgumentValidator.numRange(element, minValue, maxValue, "numRange test");
        }
    }
}
