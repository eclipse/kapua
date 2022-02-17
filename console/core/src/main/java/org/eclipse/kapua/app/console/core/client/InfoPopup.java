/*******************************************************************************
 * Copyright (c) 2018, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.app.console.core.client;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Text;
import com.extjs.gxt.ui.client.widget.layout.TableData;
import com.extjs.gxt.ui.client.widget.layout.TableLayout;
import com.google.gwt.user.client.ui.PopupPanel;

/**
 * Info pop-up panel that blocks UI and displays message to user.
 * Show and hide functionality should be handled by PopupInfo creator.
 */
public class InfoPopup extends PopupPanel {

    private ContentPanel infoPanel;

    public InfoPopup(String message) {
        // Popup auto-hide is set to false, user can not close this popup.
        super(false, true);
        setGlassEnabled(true);
        setStylePrimaryName("kapua-popupPanel");

        // Info setup
        infoPanel = new ContentPanel();
        infoPanel.setBorders(false);
        infoPanel.setBodyBorder(false);
        infoPanel.setHeaderVisible(false);
        infoPanel.setLayout(new TableLayout(2));
        infoPanel.setStyleAttribute("background-color", "#F0F0F0");
        infoPanel.setBodyStyle("background-color: #F0F0F0");
        setWidget(infoPanel);
        // Message
        TableData tableData = new TableData();
        tableData.setHorizontalAlign(Style.HorizontalAlignment.LEFT);
        tableData.setVerticalAlign(Style.VerticalAlignment.MIDDLE);
        Text infoText = new Text(message);
        infoText.setStyleName("kapua-info-text");
        infoText.setStyleAttribute("padding", "5px");
        infoPanel.add(infoText, tableData);
    }

}
