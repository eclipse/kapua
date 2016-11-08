package org.eclipse.kapua.app.console.client.ui.dialog.entity;

import org.eclipse.kapua.app.console.client.resources.icons.KapuaIcon;
import org.eclipse.kapua.app.console.client.ui.dialog.SimpleDialog;
import org.eclipse.kapua.app.console.shared.model.GwtSession;

import com.google.gwt.user.client.ui.AbstractImagePrototype;

public abstract class EntityAddDialog extends SimpleDialog {

    protected GwtSession currentSession;

    public EntityAddDialog(GwtSession currentSession) {
        this.currentSession = currentSession;
    }

    @Override
    protected void addListeners() {
    }

    @Override
    public AbstractImagePrototype getHeaderIcon() {
        return null;
    }

    @Override
    public KapuaIcon getInfoIcon() {
        return null;
    }
}
