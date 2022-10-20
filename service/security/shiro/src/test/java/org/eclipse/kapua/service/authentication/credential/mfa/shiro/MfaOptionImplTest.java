/*******************************************************************************
 * Copyright (c) 2021, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.authentication.credential.mfa.shiro;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.eclipse.kapua.service.authentication.credential.mfa.MfaOption;
import org.eclipse.kapua.service.authentication.credential.mfa.ScratchCode;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.Mockito;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;


@Category(JUnitTests.class)
public class MfaOptionImplTest {

    KapuaId[] scopeIds;
    KapuaEid[] userIds;
    String[] mfaSecretKeys;
    MfaOption mfaOption;
    Date trustExpirationDate, modifiedOn;

    @Before
    public void initialize() {
        scopeIds = new KapuaId[]{null, KapuaId.ONE};
        userIds = new KapuaEid[]{null, new KapuaEid()};
        mfaSecretKeys = new String[]{null, "", "!!mfaSecretKey-1", "#1(newMfa.,/SecretKey)9--99", "!$$ 1-2 key//", "mfa_key(....)<00>"};
        mfaOption = Mockito.mock(MfaOption.class);
        trustExpirationDate = new Date();
        modifiedOn = new Date();
    }

    @Test
    public void mfaOptionImplWithoutParametersTest() {
        MfaOptionImpl mfaOptionImpl = new MfaOptionImpl();
        Assert.assertNull("Null expected.", mfaOptionImpl.getScopeId());
        Assert.assertNull("Null expected.", mfaOptionImpl.getUserId());
        Assert.assertNull("Null expected.", mfaOptionImpl.getMfaSecretKey());
        Assert.assertNull("Null expected.", mfaOptionImpl.getTrustKey());
        Assert.assertNull("Null expected.", mfaOptionImpl.getTrustExpirationDate());
    }

    @Test
    public void mfaOptionImplScopeIdParameterTest() {
        for (KapuaId scopeId : scopeIds) {
            MfaOptionImpl mfaOptionImpl = new MfaOptionImpl(scopeId);
            Assert.assertEquals("Expected and actual values should be the same.", scopeId, mfaOptionImpl.getScopeId());
            Assert.assertNull("Null expected.", mfaOptionImpl.getUserId());
            Assert.assertNull("Null expected.", mfaOptionImpl.getMfaSecretKey());
            Assert.assertNull("Null expected.", mfaOptionImpl.getTrustKey());
            Assert.assertNull("Null expected.", mfaOptionImpl.getTrustExpirationDate());
        }
    }

    @Test
    public void mfaOptionImplScopeIdUserIdMfaSecretKeyParametersTest() {
        for (KapuaId scopeId : scopeIds) {
            for (KapuaId userId : userIds) {
                for (String mfaSecretKey : mfaSecretKeys) {
                    MfaOptionImpl mfaOptionImpl = new MfaOptionImpl(scopeId, userId, mfaSecretKey);
                    Assert.assertEquals("Expected and actual values should be the same.", scopeId, mfaOptionImpl.getScopeId());
                    Assert.assertEquals("Expected and actual values should be the same.", userId, mfaOptionImpl.getUserId());
                    Assert.assertEquals("Expected and actual values should be the same.", mfaSecretKey, mfaOptionImpl.getMfaSecretKey());
                    Assert.assertNull("Null expected.", mfaOptionImpl.getTrustKey());
                    Assert.assertNull("Null expected.", mfaOptionImpl.getTrustExpirationDate());
                }
            }
        }
    }

    @Test
    public void mfaOptionImplMfaOptionParameterTest() throws KapuaException {
        Mockito.when(mfaOption.getScopeId()).thenReturn(KapuaId.ONE);
        Mockito.when(mfaOption.getUserId()).thenReturn(KapuaId.ONE);
        Mockito.when(mfaOption.getMfaSecretKey()).thenReturn("mfa secret key");
        Mockito.when(mfaOption.getTrustKey()).thenReturn("trust key");
        Mockito.when(mfaOption.getTrustExpirationDate()).thenReturn(trustExpirationDate);
        Mockito.when(mfaOption.getModifiedOn()).thenReturn(modifiedOn);
        Mockito.when(mfaOption.getModifiedBy()).thenReturn(KapuaId.ONE);
        Mockito.when(mfaOption.getOptlock()).thenReturn(10);

        MfaOptionImpl mfaOptionImpl = new MfaOptionImpl(mfaOption);

        Assert.assertEquals("Expected and actual values should be the same.", KapuaId.ONE, mfaOptionImpl.getScopeId());
        Assert.assertEquals("Expected and actual values should be the same.", KapuaId.ONE, mfaOptionImpl.getUserId());
        Assert.assertEquals("Expected and actual values should be the same.", "mfa secret key", mfaOptionImpl.getMfaSecretKey());
        Assert.assertEquals("Expected and actual values should be the same.", "trust key", mfaOptionImpl.getTrustKey());
        Assert.assertEquals("Expected and actual values should be the same.", trustExpirationDate, mfaOptionImpl.getTrustExpirationDate());
        Assert.assertEquals("Expected and actual values should be the same.", modifiedOn, mfaOptionImpl.getModifiedOn());
        Assert.assertEquals("Expected and actual values should be the same.", KapuaId.ONE, mfaOptionImpl.getModifiedBy());
        Assert.assertEquals("Expected and actual values should be the same.", 10, mfaOptionImpl.getOptlock());
    }

    @Test(expected = NullPointerException.class)
    public void mfaOptionImplNullMfaOptionParameterTest() throws KapuaException {
        new MfaOptionImpl((MfaOption) null);
    }

    @Test
    public void setAndGetUserIdTest() throws KapuaException {
        KapuaId[] newUserIds = {null, KapuaId.ANY};

        MfaOptionImpl mfaOptionImpl1 = new MfaOptionImpl();
        for (KapuaId newUserId : newUserIds) {
            mfaOptionImpl1.setUserId(newUserId);
            Assert.assertEquals("Expected and actual values should be the same.", newUserId, mfaOptionImpl1.getUserId());
        }

        MfaOptionImpl mfaOptionImpl2 = new MfaOptionImpl(KapuaId.ONE);
        for (KapuaId newUserId : newUserIds) {
            mfaOptionImpl2.setUserId(newUserId);
            Assert.assertEquals("Expected and actual values should be the same.", newUserId, mfaOptionImpl2.getUserId());
        }

        MfaOptionImpl mfaOptionImpl3 = new MfaOptionImpl(KapuaId.ONE, new KapuaEid(), "mfa secret key");
        for (KapuaId newUserId : newUserIds) {
            mfaOptionImpl3.setUserId(newUserId);
            Assert.assertEquals("Expected and actual values should be the same.", newUserId, mfaOptionImpl3.getUserId());
        }

        MfaOptionImpl mfaOptionImpl4 = new MfaOptionImpl(mfaOption);
        for (KapuaId newUserId : newUserIds) {
            mfaOptionImpl4.setUserId(newUserId);
            Assert.assertEquals("Expected and actual values should be the same.", newUserId, mfaOptionImpl4.getUserId());
        }
    }

    @Test
    public void setAndGetMfaSecretKeyTest() throws KapuaException {
        String[] newSecretKeys = {null, "new secret key"};

        MfaOptionImpl mfaOptionImpl1 = new MfaOptionImpl();
        for (String newSecretKey : newSecretKeys) {
            mfaOptionImpl1.setMfaSecretKey(newSecretKey);
            Assert.assertEquals("Expected and actual values should be the same.", newSecretKey, mfaOptionImpl1.getMfaSecretKey());
        }

        MfaOptionImpl mfaOptionImpl2 = new MfaOptionImpl(KapuaId.ONE);
        for (String newSecretKey : newSecretKeys) {
            mfaOptionImpl2.setMfaSecretKey(newSecretKey);
            Assert.assertEquals("Expected and actual values should be the same.", newSecretKey, mfaOptionImpl2.getMfaSecretKey());
        }

        MfaOptionImpl mfaOptionImpl3 = new MfaOptionImpl(KapuaId.ONE, new KapuaEid(), "mfa secret key");
        for (String newSecretKey : newSecretKeys) {
            mfaOptionImpl3.setMfaSecretKey(newSecretKey);
            Assert.assertEquals("Expected and actual values should be the same.", newSecretKey, mfaOptionImpl3.getMfaSecretKey());
        }

        MfaOptionImpl mfaOptionImpl4 = new MfaOptionImpl(mfaOption);
        for (String newSecretKey : newSecretKeys) {
            mfaOptionImpl4.setMfaSecretKey(newSecretKey);
            Assert.assertEquals("Expected and actual values should be the same.", newSecretKey, mfaOptionImpl4.getMfaSecretKey());
        }
    }

    @Test
    public void setAndGetTrustKeyTest() throws KapuaException {
        String[] newTrustKeys = {null, "new trust key"};

        MfaOptionImpl mfaOptionImpl1 = new MfaOptionImpl();
        for (String newTrustKey : newTrustKeys) {
            mfaOptionImpl1.setTrustKey(newTrustKey);
            Assert.assertEquals("Expected and actual values should be the same.", newTrustKey, mfaOptionImpl1.getTrustKey());
        }

        MfaOptionImpl mfaOptionImpl2 = new MfaOptionImpl(KapuaId.ONE);
        for (String newTrustKey : newTrustKeys) {
            mfaOptionImpl2.setTrustKey(newTrustKey);
            Assert.assertEquals("Expected and actual values should be the same.", newTrustKey, mfaOptionImpl2.getTrustKey());
        }

        MfaOptionImpl mfaOptionImpl3 = new MfaOptionImpl(KapuaId.ONE, new KapuaEid(), "mfa secret key");
        for (String newTrustKey : newTrustKeys) {
            mfaOptionImpl3.setTrustKey(newTrustKey);
            Assert.assertEquals("Expected and actual values should be the same.", newTrustKey, mfaOptionImpl3.getTrustKey());
        }

        MfaOptionImpl mfaOptionImpl4 = new MfaOptionImpl(mfaOption);
        for (String newTrustKey : newTrustKeys) {
            mfaOptionImpl4.setTrustKey(newTrustKey);
            Assert.assertEquals("Expected and actual values should be the same.", newTrustKey, mfaOptionImpl4.getTrustKey());
        }
    }

    @Test
    public void setAndGetTrustExpirationDateTest() throws KapuaException {
        Date[] newTrustExpirationDates = {null, new Date()};

        MfaOptionImpl mfaOptionImpl1 = new MfaOptionImpl();
        for (Date newTrustExpirationDate : newTrustExpirationDates) {
            mfaOptionImpl1.setTrustExpirationDate(newTrustExpirationDate);
            Assert.assertEquals("Expected and actual values should be the same.", newTrustExpirationDate, mfaOptionImpl1.getTrustExpirationDate());
        }

        MfaOptionImpl mfaOptionImpl2 = new MfaOptionImpl(KapuaId.ONE);
        for (Date newTrustExpirationDate : newTrustExpirationDates) {
            mfaOptionImpl2.setTrustExpirationDate(newTrustExpirationDate);
            Assert.assertEquals("Expected and actual values should be the same.", newTrustExpirationDate, mfaOptionImpl2.getTrustExpirationDate());
        }

        MfaOptionImpl mfaOptionImpl3 = new MfaOptionImpl(KapuaId.ONE, new KapuaEid(), "mfa secret key");
        for (Date newTrustExpirationDate : newTrustExpirationDates) {
            mfaOptionImpl3.setTrustExpirationDate(newTrustExpirationDate);
            Assert.assertEquals("Expected and actual values should be the same.", newTrustExpirationDate, mfaOptionImpl3.getTrustExpirationDate());
        }

        MfaOptionImpl mfaOptionImpl4 = new MfaOptionImpl(mfaOption);
        for (Date newTrustExpirationDate : newTrustExpirationDates) {
            mfaOptionImpl4.setTrustExpirationDate(newTrustExpirationDate);
            Assert.assertEquals("Expected and actual values should be the same.", newTrustExpirationDate, mfaOptionImpl4.getTrustExpirationDate());
        }
    }

    @Test
    public void setAndGetQRCodeImageTest() throws KapuaException {
        String[] newQrCodeImages = {null, "new qr Code Image"};

        MfaOptionImpl mfaOptionImpl1 = new MfaOptionImpl();
        for (String newQrCodeImage : newQrCodeImages) {
            mfaOptionImpl1.setQRCodeImage(newQrCodeImage);
            Assert.assertEquals("Expected and actual values should be the same.", newQrCodeImage, mfaOptionImpl1.getQRCodeImage());
        }

        MfaOptionImpl mfaOptionImpl2 = new MfaOptionImpl(KapuaId.ONE);
        for (String newQrCodeImage : newQrCodeImages) {
            mfaOptionImpl2.setQRCodeImage(newQrCodeImage);
            Assert.assertEquals("Expected and actual values should be the same.", newQrCodeImage, mfaOptionImpl2.getQRCodeImage());
        }

        MfaOptionImpl mfaOptionImpl3 = new MfaOptionImpl(KapuaId.ONE, new KapuaEid(), "mfa secret key");
        for (String newQrCodeImage : newQrCodeImages) {
            mfaOptionImpl3.setQRCodeImage(newQrCodeImage);
            Assert.assertEquals("Expected and actual values should be the same.", newQrCodeImage, mfaOptionImpl3.getQRCodeImage());
        }

        MfaOptionImpl mfaOptionImpl4 = new MfaOptionImpl(mfaOption);
        for (String newQrCodeImage : newQrCodeImages) {
            mfaOptionImpl4.setQRCodeImage(newQrCodeImage);
            Assert.assertEquals("Expected and actual values should be the same.", newQrCodeImage, mfaOptionImpl4.getQRCodeImage());
        }
    }

    @Test
    public void setAndGetScratchCodesEmptyListTest() throws KapuaException {
        List<ScratchCode> scratchCodeList = new LinkedList<>();

        MfaOptionImpl mfaOptionImpl1 = new MfaOptionImpl();
        mfaOptionImpl1.setScratchCodes(scratchCodeList);
        Assert.assertEquals("Expected and actual values should be the same.", scratchCodeList, mfaOptionImpl1.getScratchCodes());
        Assert.assertTrue("True expected.", mfaOptionImpl1.getScratchCodes().isEmpty());

        MfaOptionImpl mfaOptionImpl2 = new MfaOptionImpl(KapuaId.ONE);
        mfaOptionImpl2.setScratchCodes(scratchCodeList);
        Assert.assertEquals("Expected and actual values should be the same.", scratchCodeList, mfaOptionImpl2.getScratchCodes());
        Assert.assertTrue("True expected.", mfaOptionImpl2.getScratchCodes().isEmpty());

        MfaOptionImpl mfaOptionImpl3 = new MfaOptionImpl(KapuaId.ONE, new KapuaEid(), "mfa secret key");
        mfaOptionImpl3.setScratchCodes(scratchCodeList);
        Assert.assertEquals("Expected and actual values should be the same.", scratchCodeList, mfaOptionImpl3.getScratchCodes());
        Assert.assertTrue("True expected.", mfaOptionImpl3.getScratchCodes().isEmpty());

        MfaOptionImpl mfaOptionImpl4 = new MfaOptionImpl(mfaOption);
        mfaOptionImpl4.setScratchCodes(scratchCodeList);
        Assert.assertEquals("Expected and actual values should be the same.", scratchCodeList, mfaOptionImpl4.getScratchCodes());
        Assert.assertTrue("True expected.", mfaOptionImpl4.getScratchCodes().isEmpty());
    }

    @Test
    public void setAndGetScratchCodesTest() throws KapuaException {
        List<ScratchCode> scratchCodeList = new LinkedList<>();
        ScratchCode scratchCode1 = Mockito.mock(ScratchCode.class);
        ScratchCode scratchCode2 = Mockito.mock(ScratchCode.class);

        scratchCodeList.add(scratchCode1);
        scratchCodeList.add(scratchCode2);

        MfaOptionImpl mfaOptionImpl1 = new MfaOptionImpl();
        mfaOptionImpl1.setScratchCodes(scratchCodeList);
        Assert.assertEquals("Expected and actual values should be the same.", scratchCodeList, mfaOptionImpl1.getScratchCodes());
        Assert.assertFalse("False expected.", mfaOptionImpl1.getScratchCodes().isEmpty());

        MfaOptionImpl mfaOptionImpl2 = new MfaOptionImpl(KapuaId.ONE);
        mfaOptionImpl2.setScratchCodes(scratchCodeList);
        Assert.assertEquals("Expected and actual values should be the same.", scratchCodeList, mfaOptionImpl2.getScratchCodes());
        Assert.assertFalse("False expected.", mfaOptionImpl2.getScratchCodes().isEmpty());

        MfaOptionImpl mfaOptionImpl3 = new MfaOptionImpl(KapuaId.ONE, new KapuaEid(), "mfa secret key");
        mfaOptionImpl3.setScratchCodes(scratchCodeList);
        Assert.assertEquals("Expected and actual values should be the same.", scratchCodeList, mfaOptionImpl3.getScratchCodes());
        Assert.assertFalse("False expected.", mfaOptionImpl3.getScratchCodes().isEmpty());

        MfaOptionImpl mfaOptionImpl4 = new MfaOptionImpl(mfaOption);
        mfaOptionImpl4.setScratchCodes(scratchCodeList);
        Assert.assertEquals("Expected and actual values should be the same.", scratchCodeList, mfaOptionImpl4.getScratchCodes());
        Assert.assertFalse("False expected.", mfaOptionImpl4.getScratchCodes().isEmpty());
    }
}