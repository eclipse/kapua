/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
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

@Path("/accounts")
public class Accounts extends AbstractKapuaResource 
{
	private final KapuaLocator locator = KapuaLocator.getInstance();
	private final AccountService accountService = locator.getService(AccountService.class);
	private final AccountFactory accountFactory = locator.getFactory(AccountFactory.class);
	
    /**
     * Returns the list of all the Accounts visible to the currently connected user.
     *
     * @return The list of requested Account objects.
     */
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
     * Returns the Account specified by the "id" path parameter.
     *
     * @param accountId The id of the Account requested.
     * @return The requested Account object.
     */
    @GET
    @Path("{accountId}")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public Account getAccount(@PathParam("accountId") String accountId) {

        Account account = null;
        try {
        	KapuaId id = KapuaEid.parseShortId(accountId);
            account = accountService.find(id);
        } catch (Throwable t) {
            handleException(t);
        }
        return returnNotNullEntity(account);
    }

    /**
     * Returns the Account specified by the "name" query parameter.
     *
     * @param accountName The name of the Account requested.
     * @return The requested Account object.
     */
    @GET
    @Path("findByName")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public Account getAccountByName(@QueryParam("accountName") String accountName) {

        Account account = null;
        try {
            account = accountService.findByName(accountName);
        } catch (Throwable t) {
            handleException(t);
        }
        return returnNotNullEntity(account);
    }

    /**
     * Returns the list of all direct child accounts for the Account specified by the "id" path parameter.
     *
     * @param scopeId The id of the Account requested.
     * @return The requested list of child accounts.
     */
    @GET
    @Path("{scopeId}/childAccounts")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public AccountListResult getChildAccounts(@PathParam("scopeId") String scopeId) {
        AccountListResult accountsResult = accountFactory.newAccountListResult();
        try {
            KapuaId id = KapuaEid.parseShortId(scopeId);
        	accountsResult = (AccountListResult) accountService.findChildsRecursively(id);
        } catch (Throwable t) {
            handleException(t);
        }
        return accountsResult;
    }

    /**
     * Returns the Service Plan for the Account specified by the "id" path parameter.
     *
     * @param accountId The id of the Account for which the Service Plan is requested.
     * @return The requested AccountServicePlan object.
     */
//    @GET
//    @Path("{accountId}/servicePlan")
//    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
//    public AccountServicePlan getAccountServicePlan(@PathParam("accountId") long accountId) {
//
//        AccountServicePlan servicePlan = null;
//        try {
//            ServiceLocator sl = ServiceLocator.getInstance();
//            AccountService as = sl.getAccountService();
//            servicePlan = as.getAccountServicePlan(accountId);
//        } catch (Throwable t) {
//            handleException(t);
//        }
//        return returnNotNullEntity(servicePlan);
//    }

    /**
     * Returns the usage for the Account specified by the "id" path parameter.
     *
     * @param limit Maximum number of entries to be returned.
     * @param offset Starting offset for the entries to be returned.
     * @param startDate Start date of the date range requested. The parameter is expressed as a long counting the number of milliseconds since January 1, 1970, 00:00:00 GMT. The default value of 0
     *            means no start date. Alternatively, the date can be expressed as a string following the ISO 8601 format.
     * @param endDate End date of the date range requested. The parameter is expressed as a long counting the number of milliseconds since January 1, 1970, 00:00:00 GMT. The default value of 0 means
     *            no end date. Alternatively, the date can be expressed as a string following the ISO 8601 format.
     * @param accountId The id of the Account for which the usage is requested.
     * @return The requested UsageResult object.
     */
//    @GET
//    @Path("{accountId}/usageByHour")
//    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
//    public UsageResult getAccountUsageByHour(@PathParam("accountId") long accountId,
//            @QueryParam("limit")     @DefaultValue("50") int limit,
//            @QueryParam("offset")    @DefaultValue("0")  int offset,
//            @QueryParam("startDate") @DefaultValue("0")  EdcTimestamp startDate,
//            @QueryParam("endDate")   @DefaultValue("0")  EdcTimestamp endDate) {
//
//        EdcUsageQuery query = new EdcUsageQuery();
//        int maxLimit = EdcConfig.getInstance().getDataExportMaxLimit();
//        int maxCount = EdcConfig.getInstance().getDataExportMaxCount();
//        if (limit > maxLimit)
//            limit = maxLimit;
//        if (offset > maxCount)
//            offset = maxCount;
//        query.setLimit(limit);
//        query.setIndexOffset(offset);
//        query.setTimetype(1); // HOUR
//        if (startDate.getTimeInMillis() != 0) {
//            query.setStartDate(startDate.getTimeInMillis());
//        }
//        if (endDate.getTimeInMillis() != 0) {
//            query.setEndDate(endDate.getTimeInMillis());
//        }
//        UsageResult usage = new UsageResult();
//        try {
//            ServiceLocator sl = ServiceLocator.getInstance();
//            AccountService as = sl.getAccountService();
//            String accountName = as.getAccountName(accountId);
//            AccountUsageService aus = sl.getAccountUsageService();
//            EdcListResult<EdcUsageInfo> result = aus.findUsageByAccount(accountName, query);
//            usage.setUsage(result);
//        } catch (Throwable t) {
//            handleException(t);
//        }
//        return returnNotNullEntity(usage);
//    }

    /**
     * Returns the daily usage for the Account specified by the "id" path parameter.
     *
     * @param limit Maximum number of entries to be returned.
     * @param offset Starting offset for the entries to be returned.
     * @param startDate Start date of the date range requested. The parameter is expressed as a long counting the number of milliseconds since January 1, 1970, 00:00:00 GMT. The default value of 0
     *            means no start date. Alternatively, the date can be expressed as a string following the ISO 8601 format.
     * @param endDate End date of the date range requested. The parameter is expressed as a long counting the number of milliseconds since January 1, 1970, 00:00:00 GMT. The default value of 0 means
     *            no end date. Alternatively, the date can be expressed as a string following the ISO 8601 format.
     * @param accountId The id of the Account for which the usage is requested.
     * @return The requested UsageResult object.
     */
//    @GET
//    @Path("{accountId}/usageByDay")
//    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
//    public UsageResult getAccountUsageByDay(@PathParam("accountId") long accountId,
//                                            @QueryParam("limit")     @DefaultValue("50") int limit,
//                                            @QueryParam("offset")    @DefaultValue("0")  int offset,
//                                            @QueryParam("startDate") @DefaultValue("0")  EdcTimestamp startDate,
//                                            @QueryParam("endDate")   @DefaultValue("0")  EdcTimestamp endDate) {
//
//        EdcUsageQuery query = new EdcUsageQuery();
//        int maxLimit = EdcConfig.getInstance().getDataExportMaxLimit();
//        int maxCount = EdcConfig.getInstance().getDataExportMaxCount();
//        if (limit > maxLimit)
//            limit = maxLimit;
//        if (offset > maxCount)
//            offset = maxCount;
//        query.setLimit(limit);
//        query.setIndexOffset(offset);
//        query.setTimetype(2); // DAY
//        if (startDate.getTimeInMillis() != 0) {
//            query.setStartDate(startDate.getTimeInMillis());
//        }
//        if (endDate.getTimeInMillis() != 0) {
//            query.setEndDate(endDate.getTimeInMillis());
//        }
//        UsageResult usage = new UsageResult();
//        try {
//            ServiceLocator sl = ServiceLocator.getInstance();
//            AccountService as = sl.getAccountService();
//            String accountName = as.getAccountName(accountId);
//            AccountUsageService aus = sl.getAccountUsageService();
//            EdcListResult<EdcUsageInfo> result = aus.findUsageByAccount(accountName, query);
//            usage.setUsage(result);
//        } catch (Throwable t) {
//            handleException(t);
//        }
//        return returnNotNullEntity(usage);
//    }

    /**
     * Returns the number of currently connected devices for the specified account.
     *
     * @param accountId The id of the Account.
     * @return The requested DeviceCountResult object.
     */
//    @GET
//    @Path("{accountId}/devices/count")
//    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
//    public CountResult getDeviceCount(@PathParam("accountId") long accountId) {
//
//        CountResult dcr = new CountResult();
//        try {
//            ServiceLocator sl = ServiceLocator.getInstance();
//            DeviceRegistryService ds = sl.getDeviceRegistryService();
//            long result = ds.getDeviceCount(accountId);
//            dcr.setCount(result);
//        } catch (Throwable t) {
//            handleException(t);
//        }
//        return returnNotNullEntity(dcr);
//    }

    /**
     * Creates a new Account based on the information provided in AccountCreator parameter.
     *
     * @param accountCreator Provides the information for the new Account to be created.
     * @return The newly created Account object.
     */
    @POST
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Account postAccount(AccountCreator accountCreator) {

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
     * @param account Provides the information to update the account.
     * @return The updated created Account object.
     */
    @PUT
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Account updateAccount(Account account) {
        try {
            ((AccountImpl)account).setScopeId(KapuaSecurityUtils.getSession().getScopeId());
            account = accountService.update(account);
        } catch (Throwable t) {
            handleException(t);
        }
        return returnNotNullEntity(account);
    }
    
    /**
     * Deletes an account based on the information provided in Account parameter.
     *
     * @param accountId Provides the information to update the account.
     * @return The updated created Account object.
     */
    @DELETE
    @Path("{accountId}")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public Response deleteAccount(@PathParam("accountId") String accountId) {
        try {
            KapuaId accountKapuaId = KapuaEid.parseShortId(accountId);
            KapuaId scopeId = KapuaSecurityUtils.getSession().getScopeId();
            accountService.delete(scopeId, accountKapuaId);
        } catch (Throwable t) {
            handleException(t);
        }
        return Response.ok().build();
    }

}
