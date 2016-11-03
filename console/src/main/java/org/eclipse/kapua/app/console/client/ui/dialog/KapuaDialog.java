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

import org.eclipse.kapua.app.console.client.messages.ConsoleMessages;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.Style.VerticalAlignment;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.Text;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.extjs.gxt.ui.client.widget.layout.TableData;
import com.extjs.gxt.ui.client.widget.layout.TableLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.Image;

public abstract class KapuaDialog extends Dialog {

    protected static final ConsoleMessages MSGS = GWT.create(ConsoleMessages.class);

    private ContentPanel infoPanel;

    public KapuaDialog() {
        super();

        setClosable(false);
        setHideOnButtonClick(false);
        setModal(true);
        setResizable(false);

        setButtonAlign(HorizontalAlignment.CENTER);
    }

    @Override
    protected void onRender(Element parent, int pos) {
        super.onRender(parent, pos);

        //
        // Dialog layout setup
        setLayout(new RowLayout(Orientation.VERTICAL));
        setBodyBorder(false);
        setButtons("");
        setScrollMode(Scroll.NONE);

        //
        // Info setup
        infoPanel = new ContentPanel();
        infoPanel.setBorders(false);
        infoPanel.setBodyBorder(false);
        infoPanel.setHeaderVisible(false);
        infoPanel.setLayout(new TableLayout(2));
        infoPanel.setStyleAttribute("background-color", "#F0F0F0");
        infoPanel.setBodyStyle("background-color:transparent");
        add(infoPanel);

        // Heading Icon (if implementation needs one
        AbstractImagePrototype headerIcon = getHeaderIcon();
        if (headerIcon != null) {
            getHeader().setIcon(headerIcon);
        }

        // Heading Message (if implementation needs one
        String headingMessage = getHeaderMessage();
        if (headingMessage == null) {
            headingMessage = "";
        }
        setHeading(headingMessage);

        // Icon (if implementation needs one)
        Image infoIcon = getInfoIcon();
        if (infoIcon != null) {
            TableData tableData = new TableData();
            tableData.setWidth("50px");
            tableData.setHorizontalAlign(HorizontalAlignment.CENTER);
            tableData.setVerticalAlign(VerticalAlignment.MIDDLE);
            tableData.setPadding(5);

            infoPanel.add(infoIcon, tableData);
        }

        // Message (if implementation needs one)
        String infoMessage = getInfoMessage();
        if (infoMessage != null) {
            TableData tableData = new TableData();
            tableData.setHorizontalAlign(HorizontalAlignment.LEFT);
            tableData.setVerticalAlign(VerticalAlignment.MIDDLE);

            Text dialogTextInfo = new Text(infoMessage);
            dialogTextInfo.setStyleName("kapua-info-text");
            dialogTextInfo.setStyleAttribute("padding", "5px");

            infoPanel.add(dialogTextInfo, tableData);
        }

        //
        // Buttons setup
        createButtons();
    }

    public abstract AbstractImagePrototype getHeaderIcon();

    public abstract String getHeaderMessage();

    public abstract Image getInfoIcon();

    public abstract String getInfoMessage();

    public void createButtons() {
        super.createButtons();
    }
}
