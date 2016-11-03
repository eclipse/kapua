package org.eclipse.kapua.app.console.shared.service;

import org.eclipse.kapua.app.console.shared.GwtKapuaException;
import org.eclipse.kapua.app.console.shared.model.GwtXSRFToken;
import org.eclipse.kapua.app.console.shared.model.role.GwtRole;
import org.eclipse.kapua.app.console.shared.model.role.GwtRoleCreator;
import org.eclipse.kapua.app.console.shared.model.role.GwtRoleQuery;

import com.extjs.gxt.ui.client.data.ListLoadResult;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("role")
public interface GwtRoleService extends RemoteService {

    public GwtRole create(GwtXSRFToken gwtXsrfToken, GwtRoleCreator gwtRoleCreator)
            throws GwtKapuaException;

    public GwtRole update(GwtXSRFToken gwtXsrfToken, GwtRole gwtRole)
            throws GwtKapuaException;

    public ListLoadResult<GwtRole> query(PagingLoadConfig loadConfig, GwtRoleQuery gwtRoleQuery)
            throws GwtKapuaException;

    public void delete(GwtXSRFToken gwtXsrfToken, String scopeShortId, String roleShortId)
            throws GwtKapuaException;
}