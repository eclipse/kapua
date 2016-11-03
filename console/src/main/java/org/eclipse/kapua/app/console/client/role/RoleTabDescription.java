package org.eclipse.kapua.app.console.client.role;

import org.eclipse.kapua.app.console.client.ui.tab.EntityDescriptionTabItem;
import org.eclipse.kapua.app.console.shared.model.GwtGroupedNVPair;
import org.eclipse.kapua.app.console.shared.model.authorization.GwtRole;
import org.eclipse.kapua.app.console.shared.service.GwtRoleService;
import org.eclipse.kapua.app.console.shared.service.GwtRoleServiceAsync;

import com.extjs.gxt.ui.client.data.ListLoadResult;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class RoleTabDescription extends EntityDescriptionTabItem<GwtRole> {

    private static final GwtRoleServiceAsync gwtRoleService = GWT.create(GwtRoleService.class);

    @Override
    protected RpcProxy<ListLoadResult<GwtGroupedNVPair>> getDataProxy() {

        return new RpcProxy<ListLoadResult<GwtGroupedNVPair>>() {

            @Override
            protected void load(Object loadConfig, final AsyncCallback<ListLoadResult<GwtGroupedNVPair>> callback) {
                gwtRoleService.getRoleDescription(selectedEntity.getScopeId(), selectedEntity.getId(), callback);
            }
        };
    }
}