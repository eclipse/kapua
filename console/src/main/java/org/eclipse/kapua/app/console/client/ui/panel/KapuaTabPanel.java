package org.eclipse.kapua.app.console.client.ui.panel;

import org.eclipse.kapua.app.console.client.ui.tab.KapuaTabItem;
import org.eclipse.kapua.app.console.shared.model.GwtEntityModel;

import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.TabPanel;

public class KapuaTabPanel<M extends GwtEntityModel> extends TabPanel {

    public KapuaTabPanel() {
        setPlain(true);
        setBorders(false);
        setBodyBorder(false);
    }

    @SuppressWarnings("unchecked")
    public void setEntity(M entity) {
        for (TabItem t : getItems()) {
            ((KapuaTabItem<M>) t).setEntity(entity);
        }

        if (getSelectedItem() != null) {
            ((KapuaTabItem<M>) getSelectedItem()).refresh();
        }
    }

}
