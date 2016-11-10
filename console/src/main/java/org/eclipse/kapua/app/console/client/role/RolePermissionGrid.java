package org.eclipse.kapua.app.console.client.role;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.kapua.app.console.client.messages.ConsoleRoleMessages;
import org.eclipse.kapua.app.console.client.ui.grid.EntityGrid;
import org.eclipse.kapua.app.console.client.ui.view.EntityView;
import org.eclipse.kapua.app.console.client.ui.widget.EntityCRUDToolbar;
import org.eclipse.kapua.app.console.shared.model.GwtSession;
import org.eclipse.kapua.app.console.shared.model.authorization.GwtRole;
import org.eclipse.kapua.app.console.shared.model.authorization.GwtRolePermission;
import org.eclipse.kapua.app.console.shared.service.GwtRoleService;
import org.eclipse.kapua.app.console.shared.service.GwtRoleServiceAsync;

import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class RolePermissionGrid extends EntityGrid<GwtRolePermission> {

    private final static ConsoleRoleMessages MSGS = GWT.create(ConsoleRoleMessages.class);

    private static final GwtRoleServiceAsync gwtRoleService = GWT.create(GwtRoleService.class);

    private GwtRole selectedRole;

    protected RolePermissionGrid(EntityView<GwtRolePermission> entityView, GwtSession currentSession) {
        super(entityView, currentSession);
    }

    @Override
    protected EntityCRUDToolbar<GwtRolePermission> getToolbar() {
        EntityCRUDToolbar<GwtRolePermission> toolbar = super.getToolbar();

        toolbar.setAddButtonVisible(false);
        toolbar.setEditButtonVisible(false);
        toolbar.setDeleteButtonVisible(false);

        return toolbar;
    }

    @Override
    protected RpcProxy<PagingLoadResult<GwtRolePermission>> getDataProxy() {
        return new RpcProxy<PagingLoadResult<GwtRolePermission>>() {

            @Override
            protected void load(Object loadConfig, AsyncCallback<PagingLoadResult<GwtRolePermission>> callback) {
                if (selectedRole != null) {
                    gwtRoleService.getRolePermissions((PagingLoadConfig) loadConfig, selectedRole.getScopeId(), selectedRole.getId(), callback);
                } else {
                    callback.onSuccess(new BasePagingLoadResult<GwtRolePermission>(new ArrayList<GwtRolePermission>()));
                }
            }
        };
    }

    @Override
    protected List<ColumnConfig> getColumns() {
        List<ColumnConfig> columnConfigs = new ArrayList<ColumnConfig>();

        ColumnConfig columnConfig = new ColumnConfig("id", MSGS.gridRolePermissionColumnHeaderId(), 100);
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnConfig("domain", MSGS.gridRolePermissionColumnHeaderDomain(), 100);
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnConfig("action", MSGS.gridRolePermissionColumnHeaderAction(), 100);
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnConfig("targetScopeId", MSGS.gridRolePermissionColumnHeaderTargetScopeId(), 100);
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnConfig("createdBy", MSGS.gridRolePermissionColumnHeaderCreatedBy(), 100);
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnConfig("createdOnFormatted", MSGS.gridRolePermissionColumnHeaderCreatedOn(), 100);
        columnConfigs.add(columnConfig);

        return columnConfigs;
    }

    public void setSelectedRole(GwtRole gwtRole) {
        selectedRole = gwtRole;
    }

    public void clear() {
        entityStore.removeAll();
    }

}