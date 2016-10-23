package org.eclipse.kapua.app.console.client.device.button;

import org.eclipse.kapua.app.console.client.messages.ConsoleMessages;
import org.eclipse.kapua.app.console.client.resources.icons.IconSet;
import org.eclipse.kapua.app.console.client.resources.icons.KapuaIcon;
import org.eclipse.kapua.app.console.client.ui.button.Button;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.google.gwt.core.client.GWT;

public class BundleStopButton extends Button {

    private static final ConsoleMessages MSGS = GWT.create(ConsoleMessages.class);

    public BundleStopButton(SelectionListener<ButtonEvent> listener) {
        super(MSGS.buttonBundleStop(),
                new KapuaIcon(IconSet.STOP),
                listener);
    }
}
