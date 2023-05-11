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
import org.eclipse.kapua.model.KapuaEntityAttributes;
import org.eclipse.kapua.model.domain.Actions;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.model.query.predicate.AndPredicate;
import org.eclipse.kapua.model.query.predicate.AttributePredicate.Operator;
import org.eclipse.kapua.model.query.predicate.QueryPredicate;
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
import java.util.function.Predicate;

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
        ArgumentValidator.notNull(endpointInfoCreator.getEndpointType(), "endpointInfoCreator.endpointType");

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
        KapuaId scopeIdPermission = endpointInfoCreator.getEndpointType().equals(EndpointInfo.ENDPOINT_TYPE_CORS) ?
                endpointInfoCreator.getScopeId() : null;
        AUTHORIZATION_SERVICE.checkPermission(
                PERMISSION_FACTORY.newPermission(EndpointInfoDomains.ENDPOINT_INFO_DOMAIN, Actions.write, scopeIdPermission)
        );

        //
        // Check duplicate endpoint
        checkDuplicateEndpointInfo(
                endpointInfoCreator.getScopeId(),
                null,
                endpointInfoCreator.getSchema(),
                endpointInfoCreator.getDns(),
                endpointInfoCreator.getPort(),
                endpointInfoCreator.getEndpointType());

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
        KapuaId scopeIdPermission = endpointInfo.getEndpointType().equals(EndpointInfo.ENDPOINT_TYPE_CORS) ?
                endpointInfo.getScopeId() : null;
        AUTHORIZATION_SERVICE.checkPermission(
                PERMISSION_FACTORY.newPermission(EndpointInfoDomains.ENDPOINT_INFO_DOMAIN, Actions.write, scopeIdPermission)
        );

        //
        // Check duplicate endpoint
        checkDuplicateEndpointInfo(
                endpointInfo.getScopeId(),
                endpointInfo.getId(),
                endpointInfo.getSchema(),
                endpointInfo.getDns(),
                endpointInfo.getPort(),
                endpointInfo.getEndpointType());

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
        EndpointInfo endpointInfoToDelete = entityManagerSession.doAction(em -> EndpointInfoDAO.find(em, scopeId, endpointInfoId));
        KapuaId scopeIdPermission = null;
        if (endpointInfoToDelete != null && endpointInfoToDelete.getEndpointType().equals(EndpointInfo.ENDPOINT_TYPE_CORS)) {
            scopeIdPermission = scopeId;
        }

        AUTHORIZATION_SERVICE.checkPermission(
                PERMISSION_FACTORY.newPermission(EndpointInfoDomains.ENDPOINT_INFO_DOMAIN, Actions.delete, scopeIdPermission)
        );

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

        AUTHORIZATION_SERVICE.checkPermission(
                PERMISSION_FACTORY.newPermission(EndpointInfoDomains.ENDPOINT_INFO_DOMAIN, Actions.read, scopeId)
        );

        EndpointInfo endpointInfoToFind = entityManagerSession.doAction(em -> EndpointInfoDAO.find(em, KapuaId.ANY, endpointInfoId)); // search the endpoint in any scope

        if (endpointInfoToFind == null) {
            throw new KapuaEntityNotFoundException(EndpointInfo.TYPE, endpointInfoId);
        }

        if (endpointInfoToFind.getScopeId().equals(scopeId)) { //found in the specified scope, search finish here
            return endpointInfoToFind;
        }
        //found but in another scope...is defined in the scope of the first Account that has defined endpoints? (proceeding upwards)
        String type = endpointInfoToFind.getEndpointType();
        //now find the endpoints of the search type that I can use (aka, the nearest proceeding upwards in Accounts hierarchy)
        EndpointInfoQuery query = ENDPOINT_INFO_FACTORY.newQuery(scopeId);
        EndpointInfoListResult nearestUsableEndpoints = query(query, type);

        if (nearestUsableEndpoints.isEmpty() || ! nearestUsableEndpoints.getFirstItem().getScopeId().equals(endpointInfoToFind.getScopeId())) { //the second condition is equivalent to verify if the searched endpoint is in this list
            throw new KapuaEntityNotFoundException(EndpointInfo.TYPE, endpointInfoId);
        } else {
            return endpointInfoToFind;
        }
    }

    /**
     * Traverse the account hierarchy bottom-up to search for {@link EndpointInfo} respecting the given query,
     * performing for each layer the given operationToPerform until the given emptinessPredicate dictates to stop OR when endpoints of the same section are found in one layer
     * In other terms, this method applies a given function to the "nearest usable endpoints", aka the ones that I see in a given scopeID
     *
     * @param query  The query to filter the {@link EndpointInfo}s.
     * @param section section of {@link EndpointInfo} where we want to search the information
     * @param operationToPerform  function to apply at each layer
     * @param emptinessPredicate  predicate that dictates to stop the traversal when false
     */
    public Object traverse(KapuaQuery query, String section, myBifunction<EntityManager, KapuaQuery, Object, KapuaException> operationToPerform, Predicate<Object> emptinessPredicate)
            throws KapuaException {
        ArgumentValidator.notNull(query, "query");

        //
        // Check Access
        AUTHORIZATION_SERVICE.checkPermission(
                PERMISSION_FACTORY.newPermission(EndpointInfoDomains.ENDPOINT_INFO_DOMAIN, Actions.read, query.getScopeId())
        );
        addSectionToPredicate(query, section);

        //
        // Do Query
        return entityManagerSession.doAction(em -> {
            Object res = operationToPerform.apply(em, query);

            if (emptinessPredicate.test(res) && query.getScopeId() != null) { //query did not find results

                KapuaId originalScopeId = query.getScopeId();

                do {
                    // First check if there are any endpoint AT ALL specified in this scope/layer
                    if (countAllEndpointsInScope(em, query.getScopeId(), section)) {
                        // There are endpoints (even not matching the query), exit because I found the "nearest usable" endpoints which don't have what I'm searching
                        break;
                    }
                    Account account = KapuaSecurityUtils.doPrivileged(() -> ACCOUNT_SERVICE.find(query.getScopeId()));

                    if (account == null) {
                        throw new KapuaEntityNotFoundException(Account.TYPE, query.getScopeId());
                    }
                    if (account.getScopeId() == null) {
                        // Query was originally on root account, and querying on parent scope id would mean querying in null,
                        // i.e. querying on all accounts. Since that's not what we want, break away.
                        break;
                    }
                    query.setScopeId(account.getScopeId());
                    res = operationToPerform.apply(em, query);
                }
                while (query.getScopeId() != null && emptinessPredicate.test(res));

                query.setScopeId(originalScopeId);
            }

            return res;
        });
    }

    @Override
    public long count(KapuaQuery query) throws KapuaException {
        return count(query, EndpointInfo.ENDPOINT_TYPE_RESOURCE);
    }

    @Override
    public long count(KapuaQuery query, String section) throws KapuaException {
        return (long) traverse(query, section, EndpointInfoDAO::count, a -> (long)a == 0);
    }

    @Override
    public EndpointInfoListResult query(KapuaQuery query) throws KapuaException {
        return query(query, EndpointInfo.ENDPOINT_TYPE_RESOURCE);
    }

    public EndpointInfoListResult query(KapuaQuery query, String section) throws KapuaException {
        return (EndpointInfoListResult) traverse(query, section, EndpointInfoDAO::query, a -> ((EndpointInfoListResult)a).isEmpty());
    }

    //
    // Private methods and interfaces
    //

    //this interface is created solely because java complains if you pass lambdas throwing checked exception
    //to overcome this, I created this interface which is a custom form of a Bifunction throwing the checked KapuaException
    @FunctionalInterface
    private interface myBifunction<A, B, C, E extends KapuaException> {
        C apply(A input1, B input2) throws E;
    }

    /**
     * Checks whether another {@link EndpointInfo} already exists with the given values.
     *
     * @param scopeId  The ScopeId of the {@link EndpointInfo}
     * @param entityId The entity id, if exists. On update you need to exclude the same entity.
     * @param schema   The {@link EndpointInfo#getSchema()}  value.
     * @param dns      The {@link EndpointInfo#getDns()}  value.
     * @param port     The {@link EndpointInfo#getPort()} value.
     * @param type     The {@link EndpointInfo#getEndpointType()} value.
     * @throws KapuaException if the values provided matches another {@link EndpointInfo}
     * @since 1.0.0
     */
    private void checkDuplicateEndpointInfo(KapuaId scopeId, KapuaId entityId, String schema, String dns, int port, String type) throws KapuaException {

        EndpointInfoQuery query = new EndpointInfoQueryImpl(scopeId);

        AndPredicate andPredicate = query.andPredicate(
                query.attributePredicate(EndpointInfoAttributes.SCHEMA, schema),
                query.attributePredicate(EndpointInfoAttributes.DNS, dns),
                query.attributePredicate(EndpointInfoAttributes.PORT, port)
                                                      );

        if (entityId != null) {
            andPredicate.and(query.attributePredicate(KapuaEntityAttributes.ENTITY_ID, entityId, Operator.NOT_EQUAL));
        }

        query.setPredicate(andPredicate);

        if (count(query,type) > 0) {
            List<Map.Entry<String, Object>> uniquesFieldValues = new ArrayList<>();
            uniquesFieldValues.add(new AbstractMap.SimpleEntry<>(KapuaEntityAttributes.SCOPE_ID, scopeId));
            uniquesFieldValues.add(new AbstractMap.SimpleEntry<>(EndpointInfoAttributes.SCHEMA, schema));
            uniquesFieldValues.add(new AbstractMap.SimpleEntry<>(EndpointInfoAttributes.DNS, dns));
            uniquesFieldValues.add(new AbstractMap.SimpleEntry<>(EndpointInfoAttributes.PORT, port));

            throw new KapuaEntityUniquenessException(EndpointInfo.TYPE, uniquesFieldValues);
        }
    }

    private boolean countAllEndpointsInScope(EntityManager em, KapuaId scopeId, String section) throws KapuaException {
        EndpointInfoQuery totalQuery = ENDPOINT_INFO_FACTORY.newQuery(scopeId);
        addSectionToPredicate(totalQuery, section);
        long totalCount = EndpointInfoDAO.count(em, totalQuery);
        return totalCount != 0;
    }

    private void addSectionToPredicate(KapuaQuery query, String section) {
        QueryPredicate sectionPredicate = query.attributePredicate(EndpointInfoAttributes.ENDPOINT_TYPE, section);
        QueryPredicate currentPredicate = query.getPredicate();
        query.setPredicate(currentPredicate == null ? sectionPredicate : query.andPredicate(currentPredicate, sectionPredicate));
    }

}
