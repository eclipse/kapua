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
 *
 *******************************************************************************/
package org.eclipse.kapua.app.console.client.ui.dialog;

import org.eclipse.kapua.app.console.client.resources.Resources;
import org.eclipse.kapua.app.console.client.resources.icons.IconSet;
import org.eclipse.kapua.app.console.client.resources.icons.KapuaIcon;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

public class InfoDialog extends KapuaDialog {

    public enum InfoDialogType {
        SUCCESS, INFO, ERROR;
    }

    private ImageResource m_headerIcon;
    private String m_headerMessage;
    private KapuaIcon m_infoIcon;
    private String m_infoMessage;

    private Button m_submitButton;

    public InfoDialog(InfoDialogType infoDialogType,
            String infoMessage) {
        super();

        switch (infoDialogType) {
        case SUCCESS: {
            m_headerIcon = Resources.INSTANCE.accept16();
            m_headerMessage = MSGS.success();
            m_infoIcon = new KapuaIcon(IconSet.CHECK_CIRCLE);
        }
            break;
        case INFO: {
            m_headerIcon = Resources.INSTANCE.info16();
            m_headerMessage = MSGS.information();
            m_infoIcon = new KapuaIcon(IconSet.INFO_CIRCLE);
        }
            break;
        case ERROR: {
            m_headerIcon = Resources.INSTANCE.warn16();
            m_headerMessage = MSGS.error();
            m_infoIcon = new KapuaIcon(IconSet.EXCLAMATION_CIRCLE);
        }
            break;
        }

        m_infoMessage = infoMessage;
    }

    public InfoDialog(ImageResource headerIcon,
            String headerMessage,
            ImageResource infoIcon,
            String infoMessage) {
        super();

        m_headerIcon = headerIcon;
        m_headerMessage = headerMessage;
        m_infoMessage = infoMessage;

        setWidth(450);
    }

    @Override
    public AbstractImagePrototype getHeaderIcon() {
        return AbstractImagePrototype.create(m_headerIcon);
    }

    @Override
    public String getHeaderMessage() {
        return m_headerMessage;
    }

    @Override
    public KapuaIcon getInfoIcon() {
        return m_infoIcon;
    }

    @Override
    public String getInfoMessage() {
        return m_infoMessage;
    }

    @Override
    public void createButtons() {
        super.createButtons();

        m_submitButton = new Button(MSGS.ok());
        m_submitButton.setSize(60, 25);
        m_submitButton.addSelectionListener(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                hide();
            }
        });

        addButton(m_submitButton);
    }
}
