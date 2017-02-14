package org.eclipse.kapua.app.console.client.role;

import org.eclipse.kapua.app.console.client.resources.icons.IconSet;
import org.eclipse.kapua.app.console.client.resources.icons.KapuaIcon;
import org.eclipse.kapua.app.console.client.ui.tab.KapuaTabItem;
import org.eclipse.kapua.app.console.shared.model.GwtSession;
import org.eclipse.kapua.app.console.shared.model.authorization.GwtRole;
import com.google.gwt.user.client.Element;

public class RoleTabSubjectGrid extends KapuaTabItem<GwtRole> {

    RoleSubjectGrid rolePermissionGrid;

    public RoleTabSubjectGrid(GwtSession gwtSession) {
        super("Subject", new KapuaIcon(IconSet.SUPPORT));
        rolePermissionGrid = new RoleSubjectGrid(null, gwtSession);

    }

    @Override
    protected void doRefresh() {
        if (selectedEntity != null) {
            rolePermissionGrid.refresh();
        } else {
            rolePermissionGrid.refresh();
        }

    }

    @Override
    public void setEntity(GwtRole t) {
        super.setEntity(t);
        rolePermissionGrid.setEntity(t);

    }

    @Override
    protected void onRender(Element parent, int index) {
        super.onRender(parent, index);
        add(rolePermissionGrid);
    }

}
