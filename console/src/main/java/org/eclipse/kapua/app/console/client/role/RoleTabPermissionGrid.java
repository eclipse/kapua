package org.eclipse.kapua.app.console.client.role;

import org.eclipse.kapua.app.console.client.resources.icons.IconSet;
import org.eclipse.kapua.app.console.client.resources.icons.KapuaIcon;
import org.eclipse.kapua.app.console.client.ui.tab.KapuaTabItem;
import org.eclipse.kapua.app.console.shared.model.authorization.GwtRole;

import com.google.gwt.user.client.Element;

public class RoleTabPermissionGrid extends KapuaTabItem<GwtRole> {

    RolePermissionGrid rolePermissionGrid = new RolePermissionGrid(null, null);

    public RoleTabPermissionGrid() {
        super("Permission", new KapuaIcon(IconSet.TASKS));
    }

    @Override
    protected void onRender(Element parent, int index) {
        super.onRender(parent, index);

        add(rolePermissionGrid);
    }

    @Override
    public void setEntity(GwtRole gwtRole) {
        super.setEntity(gwtRole);

        rolePermissionGrid.setSelectedRole(gwtRole);
    }

    @Override
    protected void doRefresh() {
        if (selectedEntity != null) {
            rolePermissionGrid.refresh();
        } else {
            rolePermissionGrid.clear();
        }
    }

}