/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.app.api.resources.v1.resources;

import com.google.common.base.Strings;
import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.app.api.core.resources.AbstractKapuaResource;
import org.eclipse.kapua.app.api.core.model.CountResult;
import org.eclipse.kapua.app.api.core.model.EntityId;
import org.eclipse.kapua.app.api.core.model.ScopeId;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.KapuaNamedEntityAttributes;
import org.eclipse.kapua.model.query.SortOrder;
import org.eclipse.kapua.model.query.predicate.AndPredicate;
import org.eclipse.kapua.service.KapuaService;
import org.eclipse.kapua.service.account.Account;
import org.eclipse.kapua.service.account.AccountCreator;
import org.eclipse.kapua.service.account.AccountFactory;
import org.eclipse.kapua.service.account.AccountListResult;
import org.eclipse.kapua.service.account.AccountQuery;
import org.eclipse.kapua.service.account.AccountService;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("{scopeId}/accounts")
public class Accounts extends AbstractKapuaResource {

    private final KapuaLocator locator = KapuaLocator.getInstance();
    private final AccountService accountService = locator.getService(AccountService.class);
    private final AccountFactory accountFactory = locator.getFactory(AccountFactory.class);

    /**
     * Gets the {@link Account} list in the scope.
     *
     * @param scopeId       The {@link ScopeId} in which to search results.
     * @param name          The {@link Account} name in which to search results.
     * @param recursive     The {@link Account} name in which to search results.
     * @param sortParam     The name of the parameter that will be used as a sorting key
     * @param sortDir       The sort direction. Can be ASCENDING (default), DESCENDING. Case-insensitive.
     * @param offset        The result set offset.
     * @param limit         The result set limit.
     * @return              The {@link AccountListResult} of all the accounts associated to the current selected scope.
     * @throws              KapuaException Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public AccountListResult simpleQuery(@PathParam("scopeId") ScopeId scopeId, //
                                         @QueryParam("name") String name, //
                                         @QueryParam("recursive") boolean recursive, //
                                         @QueryParam("sortParam") String sortParam,
                                         @QueryParam("sortDir") @DefaultValue("ASCENDING") SortOrder sortDir,
                                         @QueryParam("offset") @DefaultValue("0") int offset, //
                                         @QueryParam("limit") @DefaultValue("50") int limit) throws KapuaException {

        if (recursive) {
            return accountService.findChildrenRecursively(scopeId);
        }

        AccountQuery query = accountFactory.newQuery(scopeId);

        AndPredicate andPredicate = query.andPredicate();
        if (!Strings.isNullOrEmpty(name)) {
            andPredicate.and(query.attributePredicate(KapuaNamedEntityAttributes.NAME, name));
        }
        if (!Strings.isNullOrEmpty(sortParam)) {
            query.setSortCriteria(query.fieldSortCriteria(sortParam, sortDir));
        }
        query.setPredicate(andPredicate);

        query.setOffset(offset);
        query.setLimit(limit);

        return query(scopeId, query);
    }

    /**
     * Queries the results with the given {@link AccountQuery} parameter.
     *
     * @param scopeId The {@link ScopeId} in which to search results.
     * @param query   The {@link AccountQuery} to use to filter results.
     * @return The {@link AccountListResult} of all the result matching the given {@link AccountQuery} parameter.
     * @throws KapuaException Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    //
    @POST
    @Path("_query")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public AccountListResult query(
            @PathParam("scopeId") ScopeId scopeId, //
            AccountQuery query) throws KapuaException {
        query.setScopeId(scopeId);

        return accountService.query(query);
    }

    /**
     * Counts the results with the given {@link AccountQuery} parameter.
     *
     * @param scopeId The {@link ScopeId} in which to count results.
     * @param query   The {@link AccountQuery} to use to filter results.
     * @return The count of all the result matching the given {@link AccountQuery} parameter.
     * @throws KapuaException Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */

    @POST
    @Path("_count")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public CountResult count(
            @PathParam("scopeId") ScopeId scopeId, //
            AccountQuery query) throws KapuaException {
        query.setScopeId(scopeId);

        return new CountResult(accountService.count(query));
    }

    /**
     * Creates a new Account based on the information provided in AccountCreator
     * parameter.
     *
     * @param scopeId        The {@link ScopeId} in which to create the {@link Account}
     * @param accountCreator Provides the information for the new {@link Account} to be created.
     * @return The newly created {@link Account} object.
     * @throws KapuaException Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */

    @POST
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response create(
            @PathParam("scopeId") ScopeId scopeId, //
            AccountCreator accountCreator) throws KapuaException {
        accountCreator.setScopeId(scopeId);

        return returnCreated(accountService.create(accountCreator));
    }

    /**
     * Returns the Account specified by the "accountId" path parameter.
     *
     * @param scopeId   The {@link ScopeId} of the requested {@link Account}.
     * @param accountId The id of the requested Account.
     * @return The requested Account object.
     * @throws KapuaException Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */

    @GET
    @Path("{accountId}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Account find(
            @PathParam("scopeId") ScopeId scopeId, //
            @PathParam("accountId") EntityId accountId) throws KapuaException {
        Account account = accountService.find(scopeId, accountId);

        if (account == null) {
            throw new KapuaEntityNotFoundException(Account.TYPE, accountId);
        }

        return account;
    }

    /**
     * Updates the Account based on the information provided in the Account parameter.
     *
     * @param scopeId   The ScopeId of the requested {@link Account}.
     * @param accountId The id of the requested {@link Account}
     * @param account   The modified Account whose attributed need to be updated.
     * @return The updated account.
     * @throws KapuaException Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */

    @PUT
    @Path("{accountId}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Account update(
            @PathParam("scopeId") ScopeId scopeId, //
            @PathParam("accountId") EntityId accountId, //
            Account account) throws KapuaException {
        account.setScopeId(scopeId);
        account.setId(accountId);

        return accountService.update(account);
    }

    /**
     * Deletes the Account specified by the "accountId" path parameter.
     *
     * @param scopeId   The ScopeId of the requested {@link Account}.
     * @param accountId The id of the Account to be deleted.
     * @return HTTP 200 if operation has completed successfully.
     * @throws KapuaException Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */

    @DELETE
    @Path("{accountId}")
    public Response deleteAccount(
            @PathParam("scopeId") ScopeId scopeId, //
            @PathParam("accountId") EntityId accountId) throws KapuaException {
        accountService.delete(scopeId, accountId);

        return returnNoContent();
    }

}
