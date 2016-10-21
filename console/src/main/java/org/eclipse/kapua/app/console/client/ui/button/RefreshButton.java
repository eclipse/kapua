package org.eclipse.kapua.app.console.client.ui.button;

import org.eclipse.kapua.app.console.client.messages.ConsoleMessages;
import org.eclipse.kapua.app.console.client.resources.icons.IconSet;
import org.eclipse.kapua.app.console.client.resources.icons.KapuaIcon;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.google.gwt.core.client.GWT;

public class RefreshButton extends Button {

    private static final ConsoleMessages MSGS = GWT.create(ConsoleMessages.class);

    public RefreshButton(SelectionListener<ButtonEvent> listener) {
        super(MSGS.buttonRefresh(),
                new KapuaIcon(IconSet.REFRESH),
                listener);
    }
}
