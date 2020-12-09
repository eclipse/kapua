/*******************************************************************************
 * Copyright (c) 2017, 2020 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.endpoint.internal;

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaEntityUniquenessException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.jpa.EntityManager;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.commons.service.internal.AbstractKapuaService;
import org.eclipse.kapua.commons.util.ArgumentValidator;
import org.eclipse.kapua.commons.util.CommonsValidationRegex;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.domain.Actions;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.model.query.predicate.AndPredicate;
import org.eclipse.kapua.model.query.predicate.AttributePredicate.Operator;
import org.eclipse.kapua.service.account.Account;
import org.eclipse.kapua.service.account.AccountService;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.endpoint.EndpointInfo;
import org.eclipse.kapua.service.endpoint.EndpointInfoAttributes;
import org.eclipse.kapua.service.endpoint.EndpointInfoCreator;
import org.eclipse.kapua.service.endpoint.EndpointInfoDomains;
import org.eclipse.kapua.service.endpoint.EndpointInfoFactory;
import org.eclipse.kapua.service.endpoint.EndpointInfoListResult;
import org.eclipse.kapua.service.endpoint.EndpointInfoQuery;
import org.eclipse.kapua.service.endpoint.EndpointInfoService;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * {@link EndpointInfoService} implementation.
 *
 * @since 1.0.0
 */
@KapuaProvider
public class EndpointInfoServiceImpl
        extends AbstractKapuaService
        implements EndpointInfoService {

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();

    private static final AccountService ACCOUNT_SERVICE = LOCATOR.getService(AccountService.class);

    private static final AuthorizationService AUTHORIZATION_SERVICE = LOCATOR.getService(AuthorizationService.class);
    private static final PermissionFactory PERMISSION_FACTORY = LOCATOR.getFactory(PermissionFactory.class);

    private static final EndpointInfoFactory ENDPOINT_INFO_FACTORY = LOCATOR.getFactory(EndpointInfoFactory.class);

    private static final String ENDPOINT_INFO_CREATOR_SCHEMA = "endpointInfoCreator.schema";
    private static final String ENDPOINT_INFO_CREATOR_DNS = "endpointInfoCreator.dns";
    private static final String ENDPOINT_INFO_SCHEMA = "endpointInfo.schema";
    private static final String ENDPOINT_INFO_DNS = "endpointInfo.dns";

    public EndpointInfoServiceImpl() {
        super(EndpointEntityManagerFactory.getInstance(), null);
    }

    @Override
    public EndpointInfo create(EndpointInfoCreator endpointInfoCreator)
            throws KapuaException {
        ArgumentValidator.notNull(endpointInfoCreator, "endpointInfoCreator");
        ArgumentValidator.notNull(endpointInfoCreator.getScopeId(), "endpointInfoCreator.scopeId");

        ArgumentValidator.notEmptyOrNull(endpointInfoCreator.getSchema(), ENDPOINT_INFO_CREATOR_SCHEMA);
        ArgumentValidator.match(endpointInfoCreator.getSchema(), CommonsValidationRegex.URI_SCHEME, ENDPOINT_INFO_CREATOR_SCHEMA);
        ArgumentValidator.lengthRange(endpointInfoCreator.getSchema(), 1, 64, ENDPOINT_INFO_CREATOR_SCHEMA);

        ArgumentValidator.notEmptyOrNull(endpointInfoCreator.getDns(), ENDPOINT_INFO_CREATOR_DNS);
        ArgumentValidator.match(endpointInfoCreator.getDns(), CommonsValidationRegex.URI_DNS, ENDPOINT_INFO_CREATOR_DNS);
        ArgumentValidator.lengthRange(endpointInfoCreator.getDns(), 1, 1024, ENDPOINT_INFO_CREATOR_DNS);

        ArgumentValidator.notNegative(endpointInfoCreator.getPort(), "endpointInfoCreator.port");
        ArgumentValidator.numRange(endpointInfoCreator.getPort(), 1, 65535, "endpointInfoCreator.port");

        //
        // Check Access
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(EndpointInfoDomains.ENDPOINT_INFO_DOMAIN, Actions.write, null));

        //
        // Check duplicate endpoint
        checkDuplicateEndpointInfo(
                endpointInfoCreator.getScopeId(),
                null,
                endpointInfoCreator.getSchema(),
                endpointInfoCreator.getDns(),
                endpointInfoCreator.getPort());

        //
        // Do create
        return entityManagerSession.doTransactedAction(em -> EndpointInfoDAO.create(em, endpointInfoCreator));
    }

    @Override
    public EndpointInfo update(EndpointInfo endpointInfo) throws KapuaException {
        ArgumentValidator.notNull(endpointInfo, "endpointInfo");
        ArgumentValidator.notNull(endpointInfo.getScopeId(), "endpointInfo.scopeId");

        ArgumentValidator.notEmptyOrNull(endpointInfo.getSchema(), ENDPOINT_INFO_SCHEMA);
        ArgumentValidator.match(endpointInfo.getSchema(), CommonsValidationRegex.URI_SCHEME, ENDPOINT_INFO_SCHEMA);
        ArgumentValidator.lengthRange(endpointInfo.getSchema(), 1, 64, ENDPOINT_INFO_SCHEMA);

        ArgumentValidator.notEmptyOrNull(endpointInfo.getDns(), ENDPOINT_INFO_DNS);
        ArgumentValidator.match(endpointInfo.getDns(), CommonsValidationRegex.URI_DNS, ENDPOINT_INFO_DNS);
        ArgumentValidator.lengthRange(endpointInfo.getDns(), 1, 1024, ENDPOINT_INFO_DNS);

        ArgumentValidator.notNegative(endpointInfo.getPort(), "endpointInfo.port");
        ArgumentValidator.numRange(endpointInfo.getPort(), 1, 65535, "endpointInfo.port");

        //
        // Check Access
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(EndpointInfoDomains.ENDPOINT_INFO_DOMAIN, Actions.write, null));

        //
        // Check duplicate endpoint
        checkDuplicateEndpointInfo(
                endpointInfo.getScopeId(),
                endpointInfo.getId(),
                endpointInfo.getSchema(),
                endpointInfo.getDns(),
                endpointInfo.getPort());

        //
        // Do update
        return entityManagerSession.doTransactedAction(em -> EndpointInfoDAO.update(em, endpointInfo));
    }

    @Override
    public void delete(KapuaId scopeId, KapuaId endpointInfoId) throws KapuaException {
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(endpointInfoId, "endpointInfoId");

        //
        // Check Access
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(EndpointInfoDomains.ENDPOINT_INFO_DOMAIN, Actions.delete, null));

        //
        // Do delete
        entityManagerSession.doTransactedAction(em -> EndpointInfoDAO.delete(em, scopeId, endpointInfoId));
    }

    @Override
    public EndpointInfo find(KapuaId scopeId, KapuaId endpointInfoId)
            throws KapuaException {
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(endpointInfoId, "endpointInfoId");

        //
        // Check Access
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(EndpointInfoDomains.ENDPOINT_INFO_DOMAIN, Actions.read, scopeId));

        //
        // Do find
        return entityManagerSession.doAction(em -> EndpointInfoDAO.find(em, scopeId, endpointInfoId));
    }

    @Override
    public EndpointInfoListResult query(KapuaQuery query)
            throws KapuaException {
        ArgumentValidator.notNull(query, "query");

        //
        // Check Access
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(EndpointInfoDomains.ENDPOINT_INFO_DOMAIN, Actions.read, query.getScopeId()));

        //
        // Do Query
        return entityManagerSession.doAction(em -> {
            EndpointInfoListResult endpointInfoListResult = EndpointInfoDAO.query(em, query);

            if (endpointInfoListResult.isEmpty() && query.getScopeId() != null) {

                // First check if there are any endpoint specified at all
                if (countAllEndpointsInScope(em, query.getScopeId())) {
                    // if there are endpoints (even not matching the query), return the empty list
                    return endpointInfoListResult;
                }

                KapuaId originalScopeId = query.getScopeId();

                do {
                    Account account = KapuaSecurityUtils.doPrivileged(() -> ACCOUNT_SERVICE.find(query.getScopeId()));

                    if (account == null) {
                        throw new KapuaEntityNotFoundException(Account.TYPE, query.getScopeId());
                    }

                    query.setScopeId(account.getScopeId());
                    endpointInfoListResult = EndpointInfoDAO.query(em, query);
                }
                while (query.getScopeId() != null && endpointInfoListResult.isEmpty());

                query.setScopeId(originalScopeId);
            }

            return endpointInfoListResult;
        });
    }

    @Override
    public long count(KapuaQuery query)
            throws KapuaException {
        ArgumentValidator.notNull(query, "query");

        //
        // Check Access
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(EndpointInfoDomains.ENDPOINT_INFO_DOMAIN, Actions.read, query.getScopeId()));

        //
        // Do count
        return entityManagerSession.doAction(em -> {
            long endpointInfoCount = EndpointInfoDAO.count(em, query);

            if (endpointInfoCount == 0 && query.getScopeId() != null) {

                // First check if there are any endpoint specified at all
                if (countAllEndpointsInScope(em, query.getScopeId())) {
                    // if there are endpoints (even not matching the query), return 0
                    return endpointInfoCount;
                }

                KapuaId originalScopeId = query.getScopeId();

                do {
                    Account account = KapuaSecurityUtils.doPrivileged(() -> ACCOUNT_SERVICE.find(query.getScopeId()));

                    if (account == null) {
                        throw new KapuaEntityNotFoundException(Account.TYPE, query.getScopeId());
                    }

                    query.setScopeId(account.getScopeId());
                    endpointInfoCount = EndpointInfoDAO.count(em, query);
                }
                while (query.getScopeId() != null && endpointInfoCount == 0);

                query.setScopeId(originalScopeId);
            }

            return endpointInfoCount;
        });
    }

    //
    // Private methods
    //

    /**
     * Checks whether or not another {@link EndpointInfo} already exists with the given values.
     *
     * @param scopeId  The ScopeId of the {@link EndpointInfo}
     * @param entityId The entity id, if exists. On update you need to exclude the same entity.
     * @param schema   The {@link EndpointInfo#getSchema()}  value.
     * @param dns      The {@link EndpointInfo#getDns()}  value.
     * @param port     The {@link EndpointInfo#getPort()} value.
     * @throws KapuaException if the values provided matches another {@link EndpointInfo}
     * @since 1.0.0
     */
    private void checkDuplicateEndpointInfo(KapuaId scopeId, KapuaId entityId, String schema, String dns, int port) throws KapuaException {

        EndpointInfoQuery query = new EndpointInfoQueryImpl(scopeId);

        AndPredicate andPredicate = query.andPredicate(
                query.attributePredicate(EndpointInfoAttributes.SCHEMA, schema),
                query.attributePredicate(EndpointInfoAttributes.DNS, dns),
                query.attributePredicate(EndpointInfoAttributes.PORT, port)
        );

        if (entityId != null) {
            andPredicate.and(query.attributePredicate(EndpointInfoAttributes.ENTITY_ID, entityId, Operator.NOT_EQUAL));
        }

        query.setPredicate(andPredicate);

        if (count(query) > 0) {
            List<Map.Entry<String, Object>> uniquesFieldValues = new ArrayList<>();
            uniquesFieldValues.add(new AbstractMap.SimpleEntry<>(EndpointInfoAttributes.SCOPE_ID, scopeId));
            uniquesFieldValues.add(new AbstractMap.SimpleEntry<>(EndpointInfoAttributes.SCHEMA, schema));
            uniquesFieldValues.add(new AbstractMap.SimpleEntry<>(EndpointInfoAttributes.DNS, dns));
            uniquesFieldValues.add(new AbstractMap.SimpleEntry<>(EndpointInfoAttributes.PORT, port));

            throw new KapuaEntityUniquenessException(EndpointInfo.TYPE, uniquesFieldValues);
        }
    }

    private boolean countAllEndpointsInScope(EntityManager em, KapuaId scopeId) throws KapuaException {
        EndpointInfoQuery totalQuery = ENDPOINT_INFO_FACTORY.newQuery(scopeId);
        long totalCount = EndpointInfoDAO.count(em, totalQuery);
        return totalCount != 0;
    }
}
