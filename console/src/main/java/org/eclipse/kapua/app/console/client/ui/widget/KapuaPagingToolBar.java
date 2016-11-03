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
package org.eclipse.kapua.app.console.client.ui.widget;

import org.eclipse.kapua.app.console.client.messages.ConsoleMessages;

import com.extjs.gxt.ui.client.data.LoadEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.toolbar.PagingToolBar;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.ValueBoxBase.TextAlignment;

public class KapuaPagingToolBar extends PagingToolBar {

    private static final ConsoleMessages MSGS = GWT.create(ConsoleMessages.class);
    private boolean triggeredRemoveElements = false;

    public KapuaPagingToolBar(int pageSize) {
        super(pageSize);
    }

    public void removeElements() {
        try {
            last.removeFromParent(); // Remove go to last page button
            afterText.removeFromParent(); // Remove total number of page text
            pageText.setAlignment(TextAlignment.CENTER);
            beforePage.setStyleName("kapua-paging-text"); // Change font-family and size of text
            pageText.setStyleName("kapua-paging-text"); // Change font-family and size of text
            pageText.setTitle(MSGS.pagingToolbarPage());
            triggeredRemoveElements = true;
        } catch (NullPointerException npe) {
            // Do nothing
        }

    }

    @Override
    protected void onRender(Element target, int index) {
        super.onRender(target, index);
        if (!triggeredRemoveElements) {
            removeElements();
        }
    }

    @Override
    protected void onLoad(LoadEvent event) {
        super.onLoad(event);

        int temp = activePage == pages ? totalLength : start + pageSize;

        StringBuilder sb = new StringBuilder();

        sb.append(MSGS.pagingToolbarSummaryText(String.valueOf(start + 1), String.valueOf(temp)));

        String msg = sb.toString();
        if (totalLength == 0) {
            msg = msgs.getEmptyMsg();
        }
        displayText.setLabel(msg);
    }

    public void addUpdateButtonListener(SelectionListener<ButtonEvent> listener) {
        refresh.removeAllListeners();
        refresh.addSelectionListener(listener);
    }

}
