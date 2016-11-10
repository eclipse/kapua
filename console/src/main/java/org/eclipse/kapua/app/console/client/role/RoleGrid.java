package org.eclipse.kapua.app.console.client.role;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.kapua.app.console.client.messages.ConsoleRoleMessages;
import org.eclipse.kapua.app.console.client.ui.grid.EntityGrid;
import org.eclipse.kapua.app.console.client.ui.view.EntityView;
import org.eclipse.kapua.app.console.shared.model.GwtSession;
import org.eclipse.kapua.app.console.shared.model.authorization.GwtRole;
import org.eclipse.kapua.app.console.shared.model.authorization.GwtRoleQuery;
import org.eclipse.kapua.app.console.shared.service.GwtRoleService;
import org.eclipse.kapua.app.console.shared.service.GwtRoleServiceAsync;

import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class RoleGrid extends EntityGrid<GwtRole> {

    private final static ConsoleRoleMessages MSGS = GWT.create(ConsoleRoleMessages.class);

    private static final GwtRoleServiceAsync gwtRoleService = GWT.create(GwtRoleService.class);

    public RoleGrid(EntityView<GwtRole> entityView, GwtSession currentSession) {
        super(entityView, currentSession);
    }

    @Override
    protected RoleToolbarGrid getToolbar() {
        return new RoleToolbarGrid(currentSession);
    }

    @Override
    protected RpcProxy<PagingLoadResult<GwtRole>> getDataProxy() {
        return new RpcProxy<PagingLoadResult<GwtRole>>() {

            @Override
            protected void load(Object loadConfig, AsyncCallback<PagingLoadResult<GwtRole>> callback) {
                GwtRoleQuery query = new GwtRoleQuery();
                query.setScopeId(currentSession.getSelectedAccount().getId());

                gwtRoleService.query((PagingLoadConfig) loadConfig,
                        query,
                        callback);
            }
        };
    }

    @Override
    protected List<ColumnConfig> getColumns() {
        List<ColumnConfig> columnConfigs = new ArrayList<ColumnConfig>();

        ColumnConfig columnConfig = new ColumnConfig("id", MSGS.gridRoleColumnHeaderId(), 100);
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnConfig("name", MSGS.gridRoleColumnHeaderName(), 400);
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnConfig("createdBy", MSGS.gridRoleColumnHeaderCreatedBy(), 200);
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnConfig("createdOnFormatted", MSGS.gridRoleColumnHeaderCreatedOn(), 200);
        columnConfigs.add(columnConfig);

        return columnConfigs;
    }

}