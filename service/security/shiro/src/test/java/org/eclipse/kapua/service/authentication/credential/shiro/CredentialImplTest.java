/*******************************************************************************
 * Copyright (c) 2021 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.authentication.credential.shiro;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.qa.markers.Categories;
import org.eclipse.kapua.service.authentication.credential.Credential;
import org.eclipse.kapua.service.authentication.credential.CredentialStatus;
import org.eclipse.kapua.service.authentication.credential.CredentialType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.Mockito;
import java.util.Date;

@Category(Categories.junitTests.class)
public class CredentialImplTest extends Assert {

    KapuaId[] scopeIds;
    KapuaEid[] userIds;
    String[] credentialKeys;
    CredentialType[] credentialTypes;
    CredentialStatus[] credentialStatuses;
    Date[] dates;
    Date modifiedOn, expirationDate, loginFailuresReset, lockoutReset;
    Credential credential;
    CredentialImpl credentialImpl1, credentialImpl2, credentialImpl3, credentialImpl4;

    @Before
    public void initialize() throws KapuaException {
        scopeIds = new KapuaId[]{null, KapuaId.ONE};
        userIds = new KapuaEid[]{null, new KapuaEid(KapuaId.ONE)};
        credentialKeys = new String[]{null, "", "!!credentialKey-1", "#1(credentialKey.,/Key)9--99", "!$$ 1-2 key//", "credential_K_ey(....)<00>"};
        credentialTypes = new CredentialType[]{null, CredentialType.PASSWORD, CredentialType.API_KEY, CredentialType.JWT};
        credentialStatuses = new CredentialStatus[]{null, CredentialStatus.ENABLED, CredentialStatus.DISABLED};
        dates = new Date[]{null, new Date()};
        modifiedOn = new Date();
        expirationDate = new Date();
        loginFailuresReset = new Date();
        lockoutReset = new Date();
        credential = Mockito.mock(Credential.class);

        Mockito.when(credential.getScopeId()).thenReturn(KapuaId.ONE);
        Mockito.when(credential.getModifiedOn()).thenReturn(modifiedOn);
        Mockito.when(credential.getModifiedBy()).thenReturn(KapuaId.ONE);
        Mockito.when(credential.getOptlock()).thenReturn(10);
        Mockito.when(credential.getUserId()).thenReturn(KapuaId.ONE);
        Mockito.when(credential.getCredentialType()).thenReturn(CredentialType.PASSWORD);
        Mockito.when(credential.getCredentialKey()).thenReturn("key");
        Mockito.when(credential.getExpirationDate()).thenReturn(expirationDate);
        Mockito.when(credential.getStatus()).thenReturn(CredentialStatus.ENABLED);
        Mockito.when(credential.getLoginFailures()).thenReturn(2);
        Mockito.when(credential.getLoginFailuresReset()).thenReturn(loginFailuresReset);
        Mockito.when(credential.getLockoutReset()).thenReturn(lockoutReset);

        credentialImpl1 = new CredentialImpl();
        credentialImpl2 = new CredentialImpl(KapuaId.ONE);
        credentialImpl3 = new CredentialImpl(KapuaId.ONE, new KapuaEid(KapuaId.ONE), CredentialType.PASSWORD, "key", CredentialStatus.ENABLED, new Date());
        credentialImpl4 = new CredentialImpl(credential);
    }

    @Test
    public void credentialImplWithoutParametersTest() {
        CredentialImpl credentialImpl = new CredentialImpl();
        assertNull("Null expected.", credentialImpl.getScopeId());
        assertNull("Null expected.", credentialImpl.getUserId());
        assertNull("Null expected.", credentialImpl.getCredentialType());
        assertNull("Null expected.", credentialImpl.getCredentialKey());
        assertNull("Null expected.", credentialImpl.getStatus());
        assertNull("Null expected.", credentialImpl.getExpirationDate());
    }

    @Test
    public void credentialImplScopeIdParameterTest() {
        for (KapuaId scopeId : scopeIds) {
            CredentialImpl credentialImpl = new CredentialImpl(scopeId);
            assertEquals("Expected and actual values should be the same.", scopeId, credentialImpl.getScopeId());
            assertNull("Null expected.", credentialImpl.getUserId());
            assertNull("Null expected.", credentialImpl.getCredentialType());
            assertNull("Null expected.", credentialImpl.getCredentialKey());
            assertNull("Null expected.", credentialImpl.getStatus());
            assertNull("Null expected.", credentialImpl.getExpirationDate());
        }
    }

    @Test
    public void credentialImplMultipleParametersTest() {
        for (KapuaId scopeId : scopeIds) {
            for (KapuaEid userId : userIds) {
                for (CredentialType credentialType : credentialTypes) {
                    for (String credentialKey : credentialKeys) {
                        for (CredentialStatus credentialStatus : credentialStatuses) {
                            for (Date date : dates) {
                                CredentialImpl credentialImpl = new CredentialImpl(scopeId, userId, credentialType, credentialKey, credentialStatus, date);
                                assertEquals("Expected and actual values should be the same.", scopeId, credentialImpl.getScopeId());
                                assertEquals("Expected and actual values should be the same.", userId, credentialImpl.getUserId());
                                assertEquals("Expected and actual values should be the same.", credentialType, credentialImpl.getCredentialType());
                                assertEquals("Expected and actual values should be the same.", credentialKey, credentialImpl.getCredentialKey());
                                assertEquals("Expected and actual values should be the same.", credentialStatus, credentialImpl.getStatus());
                                assertEquals("Expected and actual values should be the same.", date, credentialImpl.getExpirationDate());
                            }
                        }
                    }
                }
            }
        }
    }

    @Test
    public void credentialImplCredentialParameterTest() throws KapuaException {
        CredentialImpl credentialImpl = new CredentialImpl(credential);
        assertEquals("Expected and actual values should be the same.", KapuaId.ONE, credentialImpl.getScopeId());
        assertEquals("Expected and actual values should be the same.", modifiedOn, credentialImpl.getModifiedOn());
        assertEquals("Expected and actual values should be the same.", KapuaId.ONE, credentialImpl.getModifiedBy());
        assertEquals("Expected and actual values should be the same.", 10, credentialImpl.getOptlock());
        assertEquals("Expected and actual values should be the same.", KapuaId.ONE, credentialImpl.getUserId());
        assertEquals("Expected and actual values should be the same.", CredentialType.PASSWORD, credentialImpl.getCredentialType());
        assertEquals("Expected and actual values should be the same.", "key", credentialImpl.getCredentialKey());
        assertEquals("Expected and actual values should be the same.", expirationDate, credentialImpl.getExpirationDate());
        assertEquals("Expected and actual values should be the same.", CredentialStatus.ENABLED, credentialImpl.getStatus());
        assertEquals("Expected and actual values should be the same.", 2, credentialImpl.getLoginFailures());
        assertEquals("Expected and actual values should be the same.", loginFailuresReset, credentialImpl.getLoginFailuresReset());
        assertEquals("Expected and actual values should be the same.", lockoutReset, credentialImpl.getLockoutReset());
    }

    @Test
    public void setAndGetUserIdTest() {
        KapuaId[] newUserIds = {null, KapuaId.ONE};
        KapuaEid[] newUserEids = {null, new KapuaEid(KapuaId.ONE)};

        for (KapuaId newUserId : newUserIds) {
            credentialImpl1.setUserId(newUserId);
            credentialImpl2.setUserId(newUserId);
            credentialImpl3.setUserId(newUserId);
            credentialImpl4.setUserId(newUserId);

            assertEquals("Expected and actual values should be the same.", newUserId, credentialImpl1.getUserId());
            assertEquals("Expected and actual values should be the same.", newUserId, credentialImpl2.getUserId());
            assertEquals("Expected and actual values should be the same.", newUserId, credentialImpl3.getUserId());
            assertEquals("Expected and actual values should be the same.", newUserId, credentialImpl4.getUserId());
        }

        for (KapuaEid newUserEid : newUserEids) {
            credentialImpl1.setUserId(newUserEid);
            credentialImpl2.setUserId(newUserEid);
            credentialImpl3.setUserId(newUserEid);
            credentialImpl4.setUserId(newUserEid);

            assertEquals("Expected and actual values should be the same.", newUserEid, credentialImpl1.getUserId());
            assertEquals("Expected and actual values should be the same.", newUserEid, credentialImpl2.getUserId());
            assertEquals("Expected and actual values should be the same.", newUserEid, credentialImpl3.getUserId());
            assertEquals("Expected and actual values should be the same.", newUserEid, credentialImpl4.getUserId());
        }
    }

    @Test
    public void setAndGetCredentialTypeTest() {
        CredentialType[] newCredentialTypes = {null, CredentialType.PASSWORD, CredentialType.API_KEY, CredentialType.JWT};

        for (CredentialType newCredentialType : newCredentialTypes) {
            credentialImpl1.setCredentialType(newCredentialType);
            credentialImpl2.setCredentialType(newCredentialType);
            credentialImpl3.setCredentialType(newCredentialType);
            credentialImpl4.setCredentialType(newCredentialType);

            assertEquals("Expected and actual values should be the same.", newCredentialType, credentialImpl1.getCredentialType());
            assertEquals("Expected and actual values should be the same.", newCredentialType, credentialImpl2.getCredentialType());
            assertEquals("Expected and actual values should be the same.", newCredentialType, credentialImpl3.getCredentialType());
            assertEquals("Expected and actual values should be the same.", newCredentialType, credentialImpl4.getCredentialType());
        }
    }

    @Test
    public void setAndGetCredentialKeyTest() {
        String[] newCredentialKeys = {null, "", "!!new credentialKey-1", "#1(credentialKey.,/NEWKey)9--99", "New credential!$$ 1-2 key//", "credential_K_ey NEW KEY(....)<00>"};

        for (String newCredentialKey : newCredentialKeys) {
            credentialImpl1.setCredentialKey(newCredentialKey);
            credentialImpl2.setCredentialKey(newCredentialKey);
            credentialImpl3.setCredentialKey(newCredentialKey);
            credentialImpl4.setCredentialKey(newCredentialKey);

            assertEquals("Expected and actual values should be the same.", newCredentialKey, credentialImpl1.getCredentialKey());
            assertEquals("Expected and actual values should be the same.", newCredentialKey, credentialImpl2.getCredentialKey());
            assertEquals("Expected and actual values should be the same.", newCredentialKey, credentialImpl3.getCredentialKey());
            assertEquals("Expected and actual values should be the same.", newCredentialKey, credentialImpl4.getCredentialKey());
        }
    }

    @Test
    public void setAndGetStatusTest() {
        CredentialStatus[] newCredentialStatuses = {null, CredentialStatus.ENABLED, CredentialStatus.DISABLED};

        for (CredentialStatus newCredentialStatus : newCredentialStatuses) {
            credentialImpl1.setStatus(newCredentialStatus);
            credentialImpl2.setStatus(newCredentialStatus);
            credentialImpl3.setStatus(newCredentialStatus);
            credentialImpl4.setStatus(newCredentialStatus);

            assertEquals("Expected and actual values should be the same.", newCredentialStatus, credentialImpl1.getStatus());
            assertEquals("Expected and actual values should be the same.", newCredentialStatus, credentialImpl2.getStatus());
            assertEquals("Expected and actual values should be the same.", newCredentialStatus, credentialImpl3.getStatus());
            assertEquals("Expected and actual values should be the same.", newCredentialStatus, credentialImpl4.getStatus());
        }
    }

    @Test
    public void setAndGetExpirationDateTest() {
        Date[] newExpirationDates = {null, new Date()};

        for (Date newExpirationDate : newExpirationDates) {
            credentialImpl1.setExpirationDate(newExpirationDate);
            credentialImpl2.setExpirationDate(newExpirationDate);
            credentialImpl3.setExpirationDate(newExpirationDate);
            credentialImpl4.setExpirationDate(newExpirationDate);

            assertEquals("Expected and actual values should be the same.", newExpirationDate, credentialImpl1.getExpirationDate());
            assertEquals("Expected and actual values should be the same.", newExpirationDate, credentialImpl2.getExpirationDate());
            assertEquals("Expected and actual values should be the same.", newExpirationDate, credentialImpl3.getExpirationDate());
            assertEquals("Expected and actual values should be the same.", newExpirationDate, credentialImpl4.getExpirationDate());
        }
    }

    @Test
    public void setAndGetLoginFailuresTest() {
        int[] newLoginFailures = {-1000000000, -999999, -10000, -10, -1, 0, 1, 10, 10000, 1000000000};

        for (int newLoginFailure : newLoginFailures) {
            credentialImpl1.setLoginFailures(newLoginFailure);
            credentialImpl2.setLoginFailures(newLoginFailure);
            credentialImpl3.setLoginFailures(newLoginFailure);
            credentialImpl4.setLoginFailures(newLoginFailure);

            assertEquals("Expected and actual values should be the same.", newLoginFailure, credentialImpl1.getLoginFailures());
            assertEquals("Expected and actual values should be the same.", newLoginFailure, credentialImpl2.getLoginFailures());
            assertEquals("Expected and actual values should be the same.", newLoginFailure, credentialImpl3.getLoginFailures());
            assertEquals("Expected and actual values should be the same.", newLoginFailure, credentialImpl4.getLoginFailures());
        }
    }

    @Test
    public void setAndGetFirstLoginFailureTest() {
        Date[] newFirstLoginFailures = {null, new Date()};

        for (Date newFirstLoginFailure : newFirstLoginFailures) {
            credentialImpl1.setFirstLoginFailure(newFirstLoginFailure);
            credentialImpl2.setFirstLoginFailure(newFirstLoginFailure);
            credentialImpl3.setFirstLoginFailure(newFirstLoginFailure);
            credentialImpl4.setFirstLoginFailure(newFirstLoginFailure);

            assertEquals("Expected and actual values should be the same.", newFirstLoginFailure, credentialImpl1.getFirstLoginFailure());
            assertEquals("Expected and actual values should be the same.", newFirstLoginFailure, credentialImpl2.getFirstLoginFailure());
            assertEquals("Expected and actual values should be the same.", newFirstLoginFailure, credentialImpl3.getFirstLoginFailure());
            assertEquals("Expected and actual values should be the same.", newFirstLoginFailure, credentialImpl4.getFirstLoginFailure());
        }
    }

    @Test
    public void setAndLoginFailuresResetTest() {
        Date[] newLoginFailuresResets = {null, new Date()};

        for (Date newLoginFailuresReset : newLoginFailuresResets) {
            credentialImpl1.setLoginFailuresReset(newLoginFailuresReset);
            credentialImpl2.setLoginFailuresReset(newLoginFailuresReset);
            credentialImpl3.setLoginFailuresReset(newLoginFailuresReset);
            credentialImpl4.setLoginFailuresReset(newLoginFailuresReset);

            assertEquals("Expected and actual values should be the same.", newLoginFailuresReset, credentialImpl1.getLoginFailuresReset());
            assertEquals("Expected and actual values should be the same.", newLoginFailuresReset, credentialImpl2.getLoginFailuresReset());
            assertEquals("Expected and actual values should be the same.", newLoginFailuresReset, credentialImpl3.getLoginFailuresReset());
            assertEquals("Expected and actual values should be the same.", newLoginFailuresReset, credentialImpl4.getLoginFailuresReset());
        }
    }

    @Test
    public void setAndGetLockoutResetTest() {
        Date[] newLockoutResets = {null, new Date()};

        for (Date newLockoutReset : newLockoutResets) {
            credentialImpl1.setLockoutReset(newLockoutReset);
            credentialImpl2.setLockoutReset(newLockoutReset);
            credentialImpl3.setLockoutReset(newLockoutReset);
            credentialImpl4.setLockoutReset(newLockoutReset);

            assertEquals("Expected and actual values should be the same.", newLockoutReset, credentialImpl1.getLockoutReset());
            assertEquals("Expected and actual values should be the same.", newLockoutReset, credentialImpl2.getLockoutReset());
            assertEquals("Expected and actual values should be the same.", newLockoutReset, credentialImpl3.getLockoutReset());
            assertEquals("Expected and actual values should be the same.", newLockoutReset, credentialImpl4.getLockoutReset());
        }
    }
}