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
package org.eclipse.kapua.app.api.v1.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.commons.security.KapuaSession;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.account.Account;
import org.eclipse.kapua.service.account.AccountCreator;
import org.eclipse.kapua.service.account.AccountFactory;
import org.eclipse.kapua.service.account.AccountListResult;
import org.eclipse.kapua.service.account.AccountService;
import org.eclipse.kapua.service.account.internal.AccountImpl;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api("Accounts")
@Path("/accounts")
public class Accounts extends AbstractKapuaResource {

    private final KapuaLocator locator = KapuaLocator.getInstance();
    private final AccountService accountService = locator.getService(AccountService.class);
    private final AccountFactory accountFactory = locator.getFactory(AccountFactory.class);

    /**
     * Returns the list of all the Accounts visible to the currently connected user.
     *
     * @return The list of requested Account objects.
     */
    @ApiOperation(value = "Get the Accounts list", notes = "Returns the list of all the Accounts visible to the currently connected user.", response = Account.class, responseContainer = "AccountListResult")
    @GET
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public AccountListResult getAccounts() {

        AccountListResult accountsResult = accountFactory.newAccountListResult();

        try {
            KapuaSession session = KapuaSecurityUtils.getSession();
            accountsResult = (AccountListResult) accountService.findChildsRecursively(session.getScopeId());
        } catch (Throwable t) {
            handleException(t);
        }
        return accountsResult;
    }

    /**
     * Returns the Account specified by the "accountId" path parameter.
     *
     * @param accountId
     *            The id of the requested Account.
     * @return The requested Account object.
     */
    @ApiOperation(value = "Get an Account", notes = "Returns the Account specified by the \"accountId\" path parameter.", response = Account.class)
    @GET
    @Path("{accountId}")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public Account getAccount(
            @ApiParam(value = "The id of the requested Account", required = true) @PathParam("accountId") String accountId) {

        Account account = null;
        try {
            KapuaId id = KapuaEid.parseCompactId(accountId);
            account = accountService.find(id);
        } catch (Throwable t) {
            handleException(t);
        }
        return returnNotNullEntity(account);
    }

    /**
     * Returns the Account specified by the "name" query parameter.
     *
     * @param accountName
     *            The name of the requested Account.
     * @return The requested Account object.
     */
    @ApiOperation(value = "Get an Account by name", notes = "Returns the Account specified by the \"name\" query parameter.", response = Account.class)
    @GET
    @Path("findByName")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public Account getAccountByName(
            @ApiParam(value = "The name of the requested Account", required = true) @QueryParam("accountName") String accountName) {

        Account account = null;
        try {
            account = accountService.findByName(accountName);
        } catch (Throwable t) {
            handleException(t);
        }
        return returnNotNullEntity(account);
    }

    /**
     * Returns the list of all direct child accounts for the Account specified by the "scopeId" path parameter.
     *
     * @param scopeId
     *            The id of the requested Account.
     * @return The requested list of child accounts.
     */
    @ApiOperation(value = "Get Children Accounts", notes = "Returns the list of all direct child accounts for the Account specified by the \"scopeId\" path parameter.", response = AccountListResult.class)
    @GET
    @Path("{scopeId}/childAccounts")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public AccountListResult getChildAccounts(
            @ApiParam(value = "The id of the requested Account", required = true) @PathParam("scopeId") String scopeId) {
        AccountListResult accountsResult = accountFactory.newAccountListResult();
        try {
            KapuaId id = KapuaEid.parseCompactId(scopeId);
            accountsResult = (AccountListResult) accountService.findChildsRecursively(id);
        } catch (Throwable t) {
            handleException(t);
        }
        return accountsResult;
    }

    /**
     * Creates a new Account based on the information provided in AccountCreator parameter.
     *
     * @param accountCreator
     *            Provides the information for the new Account to be created.
     * @return The newly created Account object.
     */
    @ApiOperation(value = "Create an Account", notes = "Creates a new Account based on the information provided in AccountCreator parameter.", response = Account.class)
    @POST
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public Account postAccount(
            @ApiParam(value = "Provides the information for the new Account to be created", required = true) AccountCreator accountCreator) {

        Account account = null;
        try {
            accountCreator.setScopeId(KapuaSecurityUtils.getSession().getScopeId());
            account = accountService.create(accountCreator);
        } catch (Throwable t) {
            handleException(t);
        }
        return returnNotNullEntity(account);
    }

    /**
     * Updates an account based on the information provided in Account parameter.
     *
     * @param account
     *            Provides the information to update the account.
     * @return The updated created Account object.
     */
    @ApiOperation(value = "Update an Account", notes = "Updates an account based on the information provided in Account parameter.", response = Account.class)
    @PUT
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public Account updateAccount(
            @ApiParam(value = "Provides the information to update the account", required = true) Account account) {
        Account updatedAccount = null;
        try {
            ((AccountImpl) account).setScopeId(KapuaSecurityUtils.getSession().getScopeId());
            updatedAccount = accountService.update(account);
        } catch (Throwable t) {
            handleException(t);
        }
        return returnNotNullEntity(updatedAccount);
    }

    /**
     * Deletes the Account specified by the "accountId" path parameter.
     *
     * @param accountId
     *            The id of the Account to be deleted.
     */
    @ApiOperation(value = "Delete an Account", notes = "Deletes an account based on the information provided in accountId parameter.")
    @DELETE
    @Path("{accountId}")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public Response deleteAccount(
            @ApiParam(value = "The id of the Account to be delete", required = true) @PathParam("accountId") String accountId) {
        try {
            KapuaId accountKapuaId = KapuaEid.parseCompactId(accountId);
            KapuaId scopeId = KapuaSecurityUtils.getSession().getScopeId();
            accountService.delete(scopeId, accountKapuaId);
        } catch (Throwable t) {
            handleException(t);
        }
        return Response.ok().build();
    }

}
