package org.eclipse.kapua.app.console.client.ui.button;

import org.eclipse.kapua.app.console.client.messages.ConsoleMessages;
import org.eclipse.kapua.app.console.client.resources.icons.IconSet;
import org.eclipse.kapua.app.console.client.resources.icons.KapuaIcon;

import com.google.gwt.core.client.GWT;

public class ExportButton extends SplitButton {

    private static final ConsoleMessages MSGS = GWT.create(ConsoleMessages.class);

    public ExportButton() {
        super(MSGS.buttonExport(), new KapuaIcon(IconSet.DOWNLOAD));
    }
}
