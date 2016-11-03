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

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.Image;

public class InfoDialog extends KapuaDialog
{
    public enum InfoDialogType
    {
        SUCCESS,
        INFO,
        ERROR;
    }

    private ImageResource m_headerIcon;
    private String        m_headerMessage;
    private ImageResource m_infoIcon;
    private String        m_infoMessage;

    private Button        m_submitButton;

    public InfoDialog(InfoDialogType infoDialogType,
                      String infoMessage)
    {
        super();

        switch (infoDialogType) {
            case SUCCESS:
            {
                m_headerIcon = Resources.INSTANCE.accept16();
                m_headerMessage = MSGS.success();
                m_infoIcon = Resources.INSTANCE.accept32();
            }
                break;
            case INFO:
            {
                m_headerIcon = Resources.INSTANCE.info16();
                m_headerMessage = MSGS.information();
                m_infoIcon = Resources.INSTANCE.info32();
            }
                break;
            case ERROR:
            {
                m_headerIcon = Resources.INSTANCE.warn16();
                m_headerMessage = MSGS.error();
                m_infoIcon = Resources.INSTANCE.warn32();
            }
                break;
        }

        m_infoMessage = infoMessage;
    }

    public InfoDialog(ImageResource headerIcon,
                      String headerMessage,
                      ImageResource infoIcon,
                      String infoMessage)
    {
        super();

        m_headerIcon = headerIcon;
        m_headerMessage = headerMessage;
        m_infoIcon = infoIcon;
        m_infoMessage = infoMessage;

        setWidth(450);
    }

    @Override
    public AbstractImagePrototype getHeaderIcon()
    {
        return AbstractImagePrototype.create(m_headerIcon);
    }

    @Override
    public String getHeaderMessage()
    {
        return m_headerMessage;
    }

    @Override
    public Image getInfoIcon()
    {
        return new Image(m_infoIcon);
    }

    @Override
    public String getInfoMessage()
    {
        return m_infoMessage;
    }

    @Override
    public void createButtons()
    {
        super.createButtons();

        m_submitButton = new Button(MSGS.ok());
        m_submitButton.setSize(60, 25);
        m_submitButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce)
            {
                hide();
            }
        });

        addButton(m_submitButton);
    }
}
