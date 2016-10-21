package org.eclipse.kapua.app.console.client.ui.button;

import org.eclipse.kapua.app.console.client.messages.ConsoleMessages;
import org.eclipse.kapua.app.console.client.resources.icons.IconSet;
import org.eclipse.kapua.app.console.client.resources.icons.KapuaIcon;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.google.gwt.core.client.GWT;

public class AddButton extends Button {

    private static final ConsoleMessages MSGS = GWT.create(ConsoleMessages.class);

    public AddButton(SelectionListener<ButtonEvent> listener) {
        super(MSGS.buttonAdd(),
                new KapuaIcon(IconSet.PLUS),
                listener);
    }
}
