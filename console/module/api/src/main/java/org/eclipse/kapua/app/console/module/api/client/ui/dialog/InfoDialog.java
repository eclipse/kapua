/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.console.module.api.client.ui.dialog;

import org.eclipse.kapua.app.console.module.api.client.resources.icons.IconSet;
import org.eclipse.kapua.app.console.module.api.client.resources.icons.KapuaIcon;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.google.gwt.resources.client.ImageResource;

public class InfoDialog extends KapuaDialog {

    public enum InfoDialogType {
        SUCCESS, INFO, ERROR;
    }

    private String headerMessage;
    private KapuaIcon infoIcon;
    private String infoMessage;

    private Button submitButton;

    public InfoDialog(InfoDialogType infoDialogType,
            String infoMessage) {
        super();

        switch (infoDialogType) {
        case SUCCESS: {
            this.headerMessage = MSGS.success();
            this.infoIcon = new KapuaIcon(IconSet.CHECK_CIRCLE);
        }
            break;
        case INFO: {
            this.headerMessage = MSGS.information();
            this.infoIcon = new KapuaIcon(IconSet.INFO_CIRCLE);
        }
            break;
        case ERROR: {
            this.headerMessage = MSGS.error();
            this.infoIcon = new KapuaIcon(IconSet.EXCLAMATION_CIRCLE);
        }
            break;
        }

        this.infoMessage = infoMessage;
    }

    public InfoDialog(ImageResource headerIcon,
            String headerMessage,
            ImageResource infoIcon,
            String infoMessage) {
        super();

        this.headerMessage = headerMessage;
        this.infoMessage = infoMessage;

        setWidth(450);
    }

    @Override
    public String getHeaderMessage() {
        return headerMessage;
    }

    @Override
    public KapuaIcon getInfoIcon() {
        return infoIcon;
    }

    @Override
    public String getInfoMessage() {
        return infoMessage;
    }

    @Override
    public void createButtons() {
        super.createButtons();

        submitButton = new Button(MSGS.ok());
        submitButton.setSize(60, 25);
        submitButton.addSelectionListener(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                hide();
            }
        });

        addButton(submitButton);
    }
}
