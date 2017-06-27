/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.console.commons.client.ui.button;

import org.eclipse.kapua.app.console.commons.client.messages.ConsoleMessages;
import org.eclipse.kapua.app.console.commons.client.resources.icons.IconSet;
import org.eclipse.kapua.app.console.commons.client.resources.icons.KapuaIcon;

import com.google.gwt.core.client.GWT;

public class ExportButton extends SplitButton {

    private static final ConsoleMessages MSGS = GWT.create(ConsoleMessages.class);

    public ExportButton() {
        super(MSGS.buttonExport(), new KapuaIcon(IconSet.DOWNLOAD));
    }
}
