/*******************************************************************************
 * Copyright (c) 2017, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.app.console.module.authentication.shared.util;

import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.data.BaseModel;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.kapua.app.console.module.api.shared.util.GwtKapuaCommonsModelConverter;
import org.eclipse.kapua.app.console.module.authentication.shared.model.GwtCredential;
import org.eclipse.kapua.app.console.module.authentication.shared.model.GwtCredentialCreator;
import org.eclipse.kapua.app.console.module.authentication.shared.model.GwtCredentialQuery;
import org.eclipse.kapua.app.console.module.authentication.shared.model.GwtCredentialStatus;
import org.eclipse.kapua.app.console.module.authentication.shared.model.GwtCredentialType;
import org.eclipse.kapua.app.console.module.authentication.shared.model.GwtMfaCredentialOptionsCreator;
import org.eclipse.kapua.model.query.FieldSortCriteria;
import org.eclipse.kapua.model.query.SortOrder;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.KapuaEntity;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.predicate.AndPredicate;
import org.eclipse.kapua.model.query.predicate.AttributePredicate.Operator;
import org.eclipse.kapua.service.authentication.credential.Credential;
import org.eclipse.kapua.service.authentication.credential.CredentialAttributes;
import org.eclipse.kapua.service.authentication.credential.CredentialCreator;
import org.eclipse.kapua.service.authentication.credential.CredentialFactory;
import org.eclipse.kapua.service.authentication.credential.CredentialQuery;
import org.eclipse.kapua.service.authentication.credential.CredentialStatus;
import org.eclipse.kapua.service.authentication.credential.CredentialType;
import org.eclipse.kapua.service.authentication.credential.mfa.MfaOptionCreator;
import org.eclipse.kapua.service.authentication.credential.mfa.MfaOptionFactory;

/**
 * Utility class for convertKapuaId {@link BaseModel}s to {@link KapuaEntity}ies and other Kapua models
 */
public class GwtKapuaAuthenticationModelConverter {

    // Get Services
    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();
    private static final CredentialFactory CREDENTIAL_FACTORY = LOCATOR.getFactory(CredentialFactory.class);
    private static final MfaOptionFactory MFA_OPTION_FACTORY = LOCATOR.getFactory(MfaOptionFactory.class);

    private GwtKapuaAuthenticationModelConverter() {
    }

    /**
     * Converts a {@link GwtCredentialQuery} into a {@link CredentialQuery} object for backend usage
     *
     * @param loadConfig         the load configuration
     * @param gwtCredentialQuery the {@link GwtCredentialQuery} to convertKapuaId
     * @return the converted {@link CredentialQuery}
     */
    public static CredentialQuery convertCredentialQuery(PagingLoadConfig loadConfig, GwtCredentialQuery gwtCredentialQuery) {

        // Convert query
        CredentialQuery query = CREDENTIAL_FACTORY.newQuery(GwtKapuaCommonsModelConverter.convertKapuaId(gwtCredentialQuery.getScopeId()));
        AndPredicate andPredicate = query.andPredicate();

        if (gwtCredentialQuery.getUserId() != null && !gwtCredentialQuery.getUserId().trim().isEmpty()) {
            andPredicate.and(query.attributePredicate(CredentialAttributes.USER_ID, GwtKapuaCommonsModelConverter.convertKapuaId(gwtCredentialQuery.getUserId())));
        }
        if (gwtCredentialQuery.getUsername() != null && !gwtCredentialQuery.getUsername().trim().isEmpty()) {
            // TODO set username predicate
        }
        if (gwtCredentialQuery.getType() != null && gwtCredentialQuery.getType() != GwtCredentialType.ALL) {
            andPredicate.and(query.attributePredicate(CredentialAttributes.CREDENTIAL_TYPE, convertCredentialType(gwtCredentialQuery.getType()), Operator.EQUAL));
        }
        String sortField = StringUtils.isEmpty(loadConfig.getSortField()) ? CredentialAttributes.CREDENTIAL_TYPE : loadConfig.getSortField();
        if (sortField.equals("expirationDateFormatted")) {
            sortField = CredentialAttributes.EXPIRATION_DATE;
        } else if (sortField.equals("modifiedOnFormatted")) {
            sortField = CredentialAttributes.MODIFIED_ON;
        }

        SortOrder sortOrder = loadConfig.getSortDir().equals(SortDir.DESC) ? SortOrder.DESCENDING : SortOrder.ASCENDING;
        FieldSortCriteria sortCriteria = query.fieldSortCriteria(sortField, sortOrder);
        query.setSortCriteria(sortCriteria);
        query.setPredicate(andPredicate);
        query.setOffset(loadConfig.getOffset());
        query.setLimit(loadConfig.getLimit());
        query.setAskTotalCount(gwtCredentialQuery.getAskTotalCount());

        //
        // Return converted
        return query;
    }

    /**
     * Converts a {@link GwtCredentialCreator} into a {@link CredentialCreator} object for backend usage
     *
     * @param gwtCredentialCreator the {@link GwtCredentialCreator} to convertKapuaId
     * @return the converted {@link CredentialCreator}
     */
    public static CredentialCreator convertCredentialCreator(GwtCredentialCreator gwtCredentialCreator) {

        // Convert scopeId
        KapuaId scopeId = GwtKapuaCommonsModelConverter.convertKapuaId(gwtCredentialCreator.getScopeId());
        CredentialCreator credentialCreator = CREDENTIAL_FACTORY
                .newCreator(scopeId,
                        GwtKapuaCommonsModelConverter.convertKapuaId(gwtCredentialCreator.getUserId()),
                        convertCredentialType(gwtCredentialCreator.getCredentialType()),
                        gwtCredentialCreator.getCredentialPlainKey(),
                        convertCredentialStatus(gwtCredentialCreator.getCredentialStatus()),
                        gwtCredentialCreator.getExpirationDate());
        //
        // Return converted
        return credentialCreator;
    }

    /**
     * Converts a {@link GwtCredential} into a {@link Credential} object for backend usage
     *
     * @param gwtCredential the {@link GwtCredential} to convertKapuaId
     * @return the converted {@link Credential}
     */
    public static Credential convertCredential(GwtCredential gwtCredential) {

        // Convert scopeId
        KapuaId scopeId = GwtKapuaCommonsModelConverter.convertKapuaId(gwtCredential.getScopeId());
        Credential credential = CREDENTIAL_FACTORY.newEntity(scopeId);
        GwtKapuaCommonsModelConverter.convertUpdatableEntity(gwtCredential, credential);
        if (gwtCredential.getId() != null && !gwtCredential.getId().trim().isEmpty()) {
            credential.setId(GwtKapuaCommonsModelConverter.convertKapuaId(gwtCredential.getId()));
        }
        credential.setUserId(GwtKapuaCommonsModelConverter.convertKapuaId(gwtCredential.getUserId()));
        credential.setCredentialType(convertCredentialType(gwtCredential.getCredentialTypeEnum()));
        credential.setCredentialKey(gwtCredential.getCredentialKey());
        credential.setExpirationDate(gwtCredential.getExpirationDate());
        credential.setStatus(convertCredentialStatus(gwtCredential.getCredentialStatusEnum()));
        credential.setLoginFailures(gwtCredential.getLoginFailures());
        credential.setFirstLoginFailure(gwtCredential.getFirstLoginFailure());
        credential.setLoginFailuresReset(gwtCredential.getLoginFailuresReset());
        credential.setLockoutReset(gwtCredential.getLockoutReset());
        //
        // Return converted
        return credential;
    }

    public static CredentialType convertCredentialType(GwtCredentialType gwtCredentialType) {
        return CredentialType.valueOf(gwtCredentialType.toString());
    }

    public static CredentialStatus convertCredentialStatus(GwtCredentialStatus gwtCredentialStatus) {
        return CredentialStatus.valueOf(gwtCredentialStatus.toString());
    }

    public static MfaOptionCreator convertMfaCredentialOptionsCreator(GwtMfaCredentialOptionsCreator gwtMfaCredentialOptionsCreator) {
        KapuaId scopeId = GwtKapuaCommonsModelConverter.convertKapuaId(gwtMfaCredentialOptionsCreator.getScopeId());
        KapuaId userId = GwtKapuaCommonsModelConverter.convertKapuaId(gwtMfaCredentialOptionsCreator.getUserId());
        return MFA_OPTION_FACTORY.newCreator(scopeId, userId, "authKey");
    }

}
