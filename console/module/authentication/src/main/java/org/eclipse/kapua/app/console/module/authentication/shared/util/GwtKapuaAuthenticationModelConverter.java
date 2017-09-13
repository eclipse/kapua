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
package org.eclipse.kapua.app.console.module.authentication.shared.util;

import com.extjs.gxt.ui.client.data.BaseModel;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import org.eclipse.kapua.app.console.module.api.shared.util.GwtKapuaCommonsModelConverter;
import org.eclipse.kapua.app.console.module.authentication.shared.model.GwtCredential;
import org.eclipse.kapua.app.console.module.authentication.shared.model.GwtCredentialCreator;
import org.eclipse.kapua.app.console.module.authentication.shared.model.GwtCredentialQuery;
import org.eclipse.kapua.app.console.module.authentication.shared.model.GwtCredentialStatus;
import org.eclipse.kapua.app.console.module.authentication.shared.model.GwtCredentialType;
import org.eclipse.kapua.commons.model.query.predicate.AndPredicate;
import org.eclipse.kapua.commons.model.query.predicate.AttributePredicate;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.KapuaEntity;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.predicate.KapuaAttributePredicate.Operator;
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
        AndPredicate andPredicate = new AndPredicate();
        if (gwtCredentialQuery.getUserId() != null && !gwtCredentialQuery.getUserId().trim().isEmpty()) {
            andPredicate.and(new AttributePredicate<KapuaId>(CredentialPredicates.USER_ID, GwtKapuaCommonsModelConverter.convertKapuaId(gwtCredentialQuery.getUserId())));
        }
        if (gwtCredentialQuery.getUsername() != null && !gwtCredentialQuery.getUsername().trim().isEmpty()) {
            // TODO set username predicate
        }
        if (gwtCredentialQuery.getType() != null && gwtCredentialQuery.getType() != GwtCredentialType.ALL) {
            andPredicate.and(new AttributePredicate<CredentialType>(CredentialPredicates.CREDENTIAL_TYPE, convertCredentialType(gwtCredentialQuery.getType()), Operator.EQUAL));
        }
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
