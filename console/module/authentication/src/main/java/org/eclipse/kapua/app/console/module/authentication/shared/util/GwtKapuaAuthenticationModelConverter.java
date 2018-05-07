/*******************************************************************************
 * Copyright (c) 2017, 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
import org.eclipse.kapua.commons.model.query.FieldSortCriteria;
import org.eclipse.kapua.commons.model.query.FieldSortCriteria.SortOrder;
import org.eclipse.kapua.commons.model.query.predicate.AndPredicateImpl;
import org.eclipse.kapua.commons.model.query.predicate.AttributePredicateImpl;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.KapuaEntity;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.predicate.AttributePredicate.Operator;
import org.eclipse.kapua.service.authentication.credential.Credential;
import org.eclipse.kapua.service.authentication.credential.CredentialCreator;
import org.eclipse.kapua.service.authentication.credential.CredentialFactory;
import org.eclipse.kapua.service.authentication.credential.CredentialPredicates;
import org.eclipse.kapua.service.authentication.credential.CredentialQuery;
import org.eclipse.kapua.service.authentication.credential.CredentialStatus;
import org.eclipse.kapua.service.authentication.credential.CredentialType;

/**
 * Utility class for convertKapuaId {@link BaseModel}s to {@link KapuaEntity}ies and other Kapua models
 */
public class GwtKapuaAuthenticationModelConverter {

    private GwtKapuaAuthenticationModelConverter(){
    }

    /**
     * Converts a {@link GwtCredentialQuery} into a {@link CredentialQuery} object for backend usage
     *
     * @param loadConfig
     *            the load configuration
     * @param gwtCredentialQuery
     *            the {@link GwtCredentialQuery} to convertKapuaId
     * @return the converted {@link CredentialQuery}
     */
    public static CredentialQuery convertCredentialQuery(PagingLoadConfig loadConfig, GwtCredentialQuery gwtCredentialQuery) {

        // Get Services
        KapuaLocator locator = KapuaLocator.getInstance();
        CredentialFactory credentialFactory = locator.getFactory(CredentialFactory.class);

        // Convert query
        CredentialQuery credentialQuery = credentialFactory.newQuery(GwtKapuaCommonsModelConverter.convertKapuaId(gwtCredentialQuery.getScopeId()));
        AndPredicateImpl andPredicate = new AndPredicateImpl();
        if (gwtCredentialQuery.getUserId() != null && !gwtCredentialQuery.getUserId().trim().isEmpty()) {
            andPredicate.and(new AttributePredicateImpl<KapuaId>(CredentialPredicates.USER_ID, GwtKapuaCommonsModelConverter.convertKapuaId(gwtCredentialQuery.getUserId())));
        }
        if (gwtCredentialQuery.getUsername() != null && !gwtCredentialQuery.getUsername().trim().isEmpty()) {
            // TODO set username predicate
        }
        if (gwtCredentialQuery.getType() != null && gwtCredentialQuery.getType() != GwtCredentialType.ALL) {
            andPredicate.and(new AttributePredicateImpl<CredentialType>(CredentialPredicates.CREDENTIAL_TYPE, convertCredentialType(gwtCredentialQuery.getType()), Operator.EQUAL));
        }
        String sortField = StringUtils.isEmpty(loadConfig.getSortField()) ? CredentialPredicates.CREDENTIAL_TYPE : loadConfig.getSortField();
        if (sortField.equals("expirationDateFormatted")) {
            sortField = CredentialPredicates.EXPIRATION_DATE;
        }
        SortOrder sortOrder = loadConfig.getSortDir().equals(SortDir.DESC) ? SortOrder.DESCENDING : SortOrder.ASCENDING;
        FieldSortCriteria sortCriteria = new FieldSortCriteria(sortField, sortOrder);
        credentialQuery.setSortCriteria(sortCriteria);
        credentialQuery.setPredicate(andPredicate);
        credentialQuery.setOffset(loadConfig.getOffset());
        credentialQuery.setLimit(loadConfig.getLimit());

        //
        // Return converted
        return credentialQuery;
    }

    /**
     * Converts a {@link GwtCredentialCreator} into a {@link CredentialCreator} object for backend usage
     *
     * @param gwtCredentialCreator
     *            the {@link GwtCredentialCreator} to convertKapuaId
     * @return the converted {@link CredentialCreator}
     */
    public static CredentialCreator convertCredentialCreator(GwtCredentialCreator gwtCredentialCreator) {

        // Get Services
        KapuaLocator locator = KapuaLocator.getInstance();
        CredentialFactory credentialFactory = locator.getFactory(CredentialFactory.class);

        // Convert scopeId
        KapuaId scopeId = GwtKapuaCommonsModelConverter.convertKapuaId(gwtCredentialCreator.getScopeId());
        CredentialCreator credentialCreator = credentialFactory
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
     * @param gwtCredential
     *            the {@link GwtCredential} to convertKapuaId
     * @return the converted {@link Credential}
     */
    public static Credential convertCredential(GwtCredential gwtCredential) {

        // Get Services
        KapuaLocator locator = KapuaLocator.getInstance();
        CredentialFactory credentialFactory = locator.getFactory(CredentialFactory.class);

        // Convert scopeId
        KapuaId scopeId = GwtKapuaCommonsModelConverter.convertKapuaId(gwtCredential.getScopeId());
        Credential credential = credentialFactory.newEntity(scopeId);
        GwtKapuaCommonsModelConverter.convertUpdatableEntity(gwtCredential, credential);
        if (gwtCredential.getId() != null && !gwtCredential.getId().trim().isEmpty()) {
            credential.setId(GwtKapuaCommonsModelConverter.convertKapuaId(gwtCredential.getId()));
        }
        credential.setUserId(GwtKapuaCommonsModelConverter.convertKapuaId(gwtCredential.getUserId()));
        credential.setCredentialType(convertCredentialType(gwtCredential.getCredentialTypeEnum()));
        credential.setCredentialKey(gwtCredential.getCredentialKey());
        credential.setExpirationDate(gwtCredential.getExpirationDate());
        credential.setCredentialStatus(convertCredentialStatus(gwtCredential.getCredentialStatusEnum()));
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

}
