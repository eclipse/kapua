/*******************************************************************************
 * Copyright (c) 2022 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.console.module.device.client.device.inventory.buttons;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.google.gwt.core.client.GWT;
import org.eclipse.kapua.app.console.module.api.client.resources.icons.IconSet;
import org.eclipse.kapua.app.console.module.api.client.resources.icons.KapuaIcon;
import org.eclipse.kapua.app.console.module.api.client.ui.button.KapuaButton;
import org.eclipse.kapua.app.console.module.device.client.messages.ConsoleDeviceMessages;

public class ContainerStopButton extends KapuaButton {

    private static final ConsoleDeviceMessages MSGS = GWT.create(ConsoleDeviceMessages.class);

    public ContainerStopButton(SelectionListener<ButtonEvent> listener) {
        super("Stop",
                new KapuaIcon(IconSet.STOP),
                listener);
    }
}
