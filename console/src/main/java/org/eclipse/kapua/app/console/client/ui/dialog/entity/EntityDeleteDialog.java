package org.eclipse.kapua.app.console.client.ui.dialog.entity;

import org.eclipse.kapua.app.console.client.resources.icons.IconSet;
import org.eclipse.kapua.app.console.client.resources.icons.KapuaIcon;
import org.eclipse.kapua.app.console.client.ui.dialog.SimpleDialog;

import com.google.gwt.user.client.ui.AbstractImagePrototype;

public abstract class EntityDeleteDialog extends SimpleDialog {

    @Override
    public void createBody() {
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
        return new KapuaIcon(IconSet.WARNING);
    }

}
