/*******************************************************************************
 * Copyright (c) 2017 Red Hat Inc and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat Inc - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.console.client.about;

import static org.eclipse.kapua.app.console.client.util.Years.getCurrentYear;

import org.eclipse.kapua.app.console.client.messages.ConsoleAboutMessages;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.VerticalAlignment;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.Text;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.TableData;
import com.extjs.gxt.ui.client.widget.layout.TableLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Image;

public class AboutView extends LayoutContainer {

    private static final ConsoleAboutMessages MSGS = GWT.create(ConsoleAboutMessages.class);

    protected void onRender(final Element parent, int index) {
        super.onRender(parent, index);

        setBorders(false);
        setLayout(new FitLayout());

        // Kapua logo
        final Image kapuaIcon = new Image("img/icon-color.svg");
        kapuaIcon.setHeight("200px");

        // Kapua welcome
        final Text welcomeMessage = new Text();
        welcomeMessage.setText(MSGS.aboutBlurb(Integer.toString(getCurrentYear())));

        final TableLayout tableLayout = new TableLayout(2);
        tableLayout.setWidth("100%");
        tableLayout.setHeight("100%");
        tableLayout.setCellPadding(40);
        tableLayout.setCellHorizontalAlign(HorizontalAlignment.CENTER);
        tableLayout.setCellVerticalAlign(VerticalAlignment.MIDDLE);

        final ContentPanel centerPanel = new ContentPanel(tableLayout);
        centerPanel.setBodyBorder(false);
        centerPanel.setBorders(false);
        centerPanel.setHeaderVisible(false);
        
        centerPanel.add(kapuaIcon, new TableData());
        centerPanel.add(welcomeMessage, new TableData(HorizontalAlignment.LEFT, VerticalAlignment.MIDDLE));

        add(centerPanel);
    }
}
