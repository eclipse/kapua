package org.eclipse.kapua.app.console.client.role;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.kapua.app.console.client.ui.grid.EntityGrid;
import org.eclipse.kapua.app.console.client.ui.panel.EntityFilterPanel;
import org.eclipse.kapua.app.console.client.ui.tab.KapuaTabItem;
import org.eclipse.kapua.app.console.client.ui.view.EntityView;
import org.eclipse.kapua.app.console.shared.model.GwtSession;
import org.eclipse.kapua.app.console.shared.model.authorization.GwtRole;

public class RoleView extends EntityView<GwtRole> {

    public RoleView(GwtSession gwtSession) {
        super(gwtSession);
    }

    @Override
    public List<KapuaTabItem<GwtRole>> getTabs(EntityView<GwtRole> entityView, GwtSession currentSession) {
        List<KapuaTabItem<GwtRole>> tabs = new ArrayList<KapuaTabItem<GwtRole>>();
        tabs.add(new RoleTabDescription());
        tabs.add(new RoleTabPermissionGrid());
        return tabs;
    }

    @Override
    public EntityGrid<GwtRole> getEntityGrid(EntityView<GwtRole> entityView, GwtSession currentSession) {
        return new RoleGrid(entityView, currentSession);
    }

    @Override
    public EntityFilterPanel getEntityFilterPanel(EntityView<GwtRole> entityView, GwtSession currentSession2) {
        // TODO Auto-generated method stub
        return null;
    }

}