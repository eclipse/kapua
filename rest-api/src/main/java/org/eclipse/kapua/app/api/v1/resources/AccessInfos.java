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
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authorization.access.AccessInfo;
import org.eclipse.kapua.service.authorization.access.AccessInfoCreator;
import org.eclipse.kapua.service.authorization.access.AccessInfoFactory;
import org.eclipse.kapua.service.authorization.access.AccessInfoListResult;
import org.eclipse.kapua.service.authorization.access.AccessInfoQuery;
import org.eclipse.kapua.service.authorization.access.AccessInfoService;
import org.eclipse.kapua.service.authorization.access.AccessPermission;
import org.eclipse.kapua.service.authorization.access.AccessPermissionCreator;
import org.eclipse.kapua.service.authorization.access.AccessPermissionFactory;
import org.eclipse.kapua.service.authorization.access.AccessPermissionListResult;
import org.eclipse.kapua.service.authorization.access.AccessPermissionService;
import org.eclipse.kapua.service.authorization.access.AccessRole;
import org.eclipse.kapua.service.authorization.access.AccessRoleCreator;
import org.eclipse.kapua.service.authorization.access.AccessRoleFactory;
import org.eclipse.kapua.service.authorization.access.AccessRoleListResult;
import org.eclipse.kapua.service.authorization.access.AccessRoleService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api("Access Info")
@Path("/accessinfos")
public class AccessInfos extends AbstractKapuaResource {

    private final KapuaLocator locator = KapuaLocator.getInstance();
    private final AccessInfoService accessInfoService = locator.getService(AccessInfoService.class);
    private final AccessInfoFactory accessInfoFactory = locator.getFactory(AccessInfoFactory.class);
    private final AccessPermissionService accessPermissionService = locator.getService(AccessPermissionService.class);
    private final AccessPermissionFactory accessPermissionFactory = locator.getFactory(AccessPermissionFactory.class);
    private final AccessRoleService accessRoleService = locator.getService(AccessRoleService.class);
    private final AccessRoleFactory accessRoleFactory = locator.getFactory(AccessRoleFactory.class);

    /**
     * Returns the list of all the access info for the current account.
     *
     * @return The list of requested AccessInfo objects.
     */
    @ApiOperation(value = "Get the AccessInfos for the current account", notes = "Returns the list of all the access infos available for the current account.", response = AccessInfo.class, responseContainer = "AccessInfoListResult")
    @GET
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public AccessInfoListResult getRoles() {
        AccessInfoListResult accessInfoList = accessInfoFactory.newAccessInfoListResult();
        try {
            KapuaId scopeId = KapuaSecurityUtils.getSession().getScopeId();
            AccessInfoQuery accessInfoQuery = accessInfoFactory.newQuery(scopeId);
            accessInfoList = accessInfoService.query(accessInfoQuery);
        } catch (Throwable t) {
            handleException(t);
        }
        return accessInfoList;
    }

    /**
     * Returns the access info for the given id.
     * 
     * @param id
     *            The {@link AccessInfo} id.
     *
     * @return The requested access info.
     */
    @ApiOperation(value = "Get the Access Info for the given id", notes = "Returns the access info for the given id.", response = AccessInfo.class)
    @GET
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Path("{id}")
    public AccessInfo getAccessInfo(@PathParam("id") String id) {
        AccessInfo accessInfo = null;
        try {
            KapuaId scopeId = KapuaSecurityUtils.getSession().getScopeId();
            KapuaId accessInfoKapuaId = KapuaEid.parseCompactId(id);
            accessInfo = accessInfoService.find(scopeId, accessInfoKapuaId);
        } catch (Throwable t) {
            handleException(t);
        }
        return accessInfo;
    }

    /**
     * Creates a new AccessInfo based on the information provided in AccessInfoCreator parameter.
     *
     * @param accessInfoCreator
     *            Provides the information for the new AccessInfo to be created.
     * @return The newly created AccessInfo object.
     */
    @ApiOperation(value = "Create a AccessInfo", notes = "Creates a new AccessInfo based on the information provided in AccessInfoCreator parameter.", response = AccessInfo.class)
    @POST
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public AccessInfo createAccessInfo(
            @ApiParam(value = "Provides the information for the new AccessInfo to be created", required = true) AccessInfoCreator accessInfoCreator) {
        AccessInfo accessInfo = null;
        try {
            accessInfoCreator.setScopeId(KapuaSecurityUtils.getSession().getScopeId());
            accessInfo = accessInfoService.create(accessInfoCreator);
        } catch (Throwable t) {
            handleException(t);
        }
        return returnNotNullEntity(accessInfo);
    }

    /**
     * Deletes the AccessInfo specified by the "accessInfoId" path parameter.
     *
     * @param accessInfoId
     *            The id of the AccessInfo to be deleted.
     * @return HTTP 200 if operation has completed successfully
     */
    @ApiOperation(value = "Delete an AccessInfo", notes = "Deletes an access info based on the information provided in accessInfoId parameter.")
    @DELETE
    @Path("{accessInfoId}")
    public Response deleteAccessInfo(
            @ApiParam(value = "The id of the AccessInfo to be deleted", required = true) @PathParam("accessInfoId") String accessInfoId) {
        try {
            KapuaId accessInfoKapuaId = KapuaEid.parseCompactId(accessInfoId);
            KapuaId scopeId = KapuaSecurityUtils.getSession().getScopeId();
            accessInfoService.delete(scopeId, accessInfoKapuaId);
        } catch (Throwable t) {
            handleException(t);
        }
        return Response.ok().build();
    }

    /**
     * Returns the list of all the access permissions for the given access info.
     *
     * @param accessInfoId
     *            The {@link AccessInfo} id.
     * @return The list of requested {@link AccessPermission}s objects.
     */

    @ApiOperation(value = "Get the AccessPermissions list for the given AccessInfo", notes = "Returns the list of all the access permissions available for the given access info.", response = AccessPermission.class, responseContainer = "AccessPermissionListResult")
    @GET
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Path("{accessInfoId}/permission")
    public AccessPermissionListResult getAccessPermissions(@PathParam("accessInfoId") String accessInfoId) {
        AccessPermissionListResult accessPermissionsList = accessPermissionFactory.newAccessPermissionListResult();
        try {
            KapuaId scopeId = KapuaSecurityUtils.getSession().getScopeId();
            KapuaId accessInfoKapuaId = KapuaEid.parseCompactId(accessInfoId);
            accessPermissionsList = accessPermissionService.findByAccessInfoId(scopeId, accessInfoKapuaId);
        } catch (Throwable t) {
            handleException(t);
        }
        return accessPermissionsList;
    }

    /**
     * Returns the access permission for the given id.
     *
     * @param accessPermissionId
     *            The {@link AccessPermission} id
     * @return The access permission for the given id.
     */
    @ApiOperation(value = "Get the AccessPermission for the given id", notes = "Returns the access permission for the given id.", response = AccessPermission.class)
    @GET
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Path("{accessInfoId}/permission/{accessPermissionId}")
    public AccessPermission getAccessPermission(@PathParam("accessPermissionId") String accessPermissionId) {
        AccessPermission accessPermission = null;
        try {
            KapuaId scopeId = KapuaSecurityUtils.getSession().getScopeId();
            KapuaId accessPermissionKapuaId = KapuaEid.parseCompactId(accessPermissionId);
            accessPermission = accessPermissionService.find(scopeId, accessPermissionKapuaId);
        } catch (Throwable t) {
            handleException(t);
        }
        return accessPermission;
    }

    /**
     * Deletes the AccessPermission specified by the "accessPermissionId" path parameter.
     *
     * @param accessPermissionId
     *            The id of the AccessPermission to be deleted.
     * @return HTTP 200 if operation has completed successfully.
     */
    @ApiOperation(value = "Delete an AccessPermission", notes = "Deletes an access permission based on the information provided in accessPermissionId parameter.")
    @DELETE
    @Path("{accessInfoId}/permission/{accessPermissionId}")
    public Response deleteAccessPermission(
            @ApiParam(value = "The id of the AccessPermission to be deleted", required = true) @PathParam("accessPermissionId") String accessPermissionId) {
        try {
            KapuaId accessPermissionKapuaId = KapuaEid.parseCompactId(accessPermissionId);
            KapuaId scopeId = KapuaSecurityUtils.getSession().getScopeId();
            accessPermissionService.delete(scopeId, accessPermissionKapuaId);
        } catch (Throwable t) {
            handleException(t);
        }
        return Response.ok().build();
    }

    /**
     * Creates a new AccessPermission based on the information provided in AccessPermissionCreator parameter.
     *
     * @param accessPermissionCreator
     *            Provides the information for the new AccessPermission to be created.
     * @return The newly created AccessPermission object.
     */
    @ApiOperation(value = "Create a AccessPermission", notes = "Creates a new AccessPermission based on the information provided in AccessPermissionCreator parameter.", response = AccessPermission.class)
    @POST
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Path("{accessInfoId}/permission")
    public AccessPermission createAccessPermission(
            @ApiParam(value = "Provides the information for the new AccessPermission to be created", required = true) AccessPermissionCreator accessPermissionCreator) {
        AccessPermission accessPermission = null;
        try {
            accessPermissionCreator.setScopeId(KapuaSecurityUtils.getSession().getScopeId());
            accessPermission = accessPermissionService.create(accessPermissionCreator);
        } catch (Throwable t) {
            handleException(t);
        }
        return returnNotNullEntity(accessPermission);
    }

    /**
     * Returns the list of all the access roles for the given access info.
     *
     * @param accessInfoId
     *            The access info id
     * @return The list of requested AccessRoles objects.
     */
    @ApiOperation(value = "Get the AccessRoles list for the given AccessInfo", notes = "Returns the list of all the access roles available for the given access info.", response = AccessRole.class, responseContainer = "AccessRoleListResult")
    @GET
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Path("{accessInfoId}/role")
    public AccessRoleListResult getAccessRoless(@PathParam("accessInfoId") String accessInfoId) {
        AccessRoleListResult accessRolesList = accessRoleFactory.newAccessRoleListResult();
        try {
            KapuaId scopeId = KapuaSecurityUtils.getSession().getScopeId();
            KapuaId accessInfoKapuaId = KapuaEid.parseCompactId(accessInfoId);
            accessRolesList = accessRoleService.findByAccessInfoId(scopeId, accessInfoKapuaId);
        } catch (Throwable t) {
            handleException(t);
        }
        return accessRolesList;
    }

    /**
     * Returns the access role for the given id.
     *
     * @param accessRoleId
     *            The access role id
     * @return The access role for the given id.
     */
    @ApiOperation(value = "Get the AccessRole for the given id", notes = "Returns the access role for the given id.", response = AccessRole.class)
    @GET
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Path("{accessInfoId}/role/{accessRoleId}")
    public AccessRole getAccessRole(@PathParam("accessRoleId") String accessRoleId) {
        AccessRole accessRole = null;
        try {
            KapuaId scopeId = KapuaSecurityUtils.getSession().getScopeId();
            KapuaId accessRoleKapuaId = KapuaEid.parseCompactId(accessRoleId);
            accessRole = accessRoleService.find(scopeId, accessRoleKapuaId);
        } catch (Throwable t) {
            handleException(t);
        }
        return accessRole;
    }

    /**
     * Deletes the AccessRole specified by the "accessRoleId" path parameter.
     *
     * @param accessRoleId
     *            The id of the AccessRoleto be deleted.
     * @return HTTP 200 if operation has completed successfully
     */
    @ApiOperation(value = "Delete an AccessRole", notes = "Deletes an access role based on the information provided in accessRoleId parameter.")
    @DELETE
    @Path("{accessInfoId}/role/{accessRoleId}")
    public Response deleteAccessRole(
            @ApiParam(value = "The id of the AccessRole to be deleted", required = true) @PathParam("accessRoleId") String accessRoleId) {
        try {
            KapuaId accessRoleKapuaId = KapuaEid.parseCompactId(accessRoleId);
            KapuaId scopeId = KapuaSecurityUtils.getSession().getScopeId();
            accessRoleService.delete(scopeId, accessRoleKapuaId);
        } catch (Throwable t) {
            handleException(t);
        }
        return Response.ok().build();
    }

    /**
     * Creates a new AccessRole based on the information provided in AccessRoleCreator parameter.
     *
     * @param accessRoleCreator
     *            Provides the information for the new AccessRole to be created.
     * @return The newly created AccessRole object.
     */
    @ApiOperation(value = "Create a AccessRole", notes = "Creates a new AccessRole based on the information provided in AccessRoleCreator parameter.", response = AccessRole.class)
    @POST
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Path("{accessInfoId}/role")
    public AccessRole createAccessRole(
            @ApiParam(value = "Provides the information for the new Accessrole to be created", required = true) AccessRoleCreator accessRoleCreator) {
        AccessRole accessRole = null;
        try {
            accessRoleCreator.setScopeId(KapuaSecurityUtils.getSession().getScopeId());
            accessRole = accessRoleService.create(accessRoleCreator);
        } catch (Throwable t) {
            handleException(t);
        }
        return returnNotNullEntity(accessRole);
    }
}
