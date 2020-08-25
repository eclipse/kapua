/*******************************************************************************
 * Copyright (c) 2017, 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.console.module.api.client.ui.widget;

import com.extjs.gxt.ui.client.widget.toolbar.PagingToolBar;
import com.google.gwt.core.client.GWT;
import org.eclipse.kapua.app.console.module.api.client.messages.ConsoleMessages;

public class KapuaPagingToolBar extends PagingToolBar {

    private static final ConsoleMessages MSGS = GWT.create(ConsoleMessages.class);

    /**
     * Max length of user input field for page number.
     */
    public static final int MAX_TEXT_LEN = 4;
    private KapuaPagingToolbarMessages kapuaPagingToolbarMessages;

    public KapuaPagingToolBar(int pageSize) {
        super(pageSize);
        this.pageText.setMaxLength(MAX_TEXT_LEN);
        if (rendered) {
            this.pageText.getElement().setAttribute("maxLength", String.valueOf(MAX_TEXT_LEN));
        }

        remove(refresh);
    }

    public void setKapuaPagingToolbarMessages(KapuaPagingToolbarMessages kapuaPagingToolbarMessages) {
        this.kapuaPagingToolbarMessages = kapuaPagingToolbarMessages;

        PagingToolBarMessages pagingToolbarMessages = getMessages();
        StringBuilder sb = new StringBuilder();
        sb.append(MSGS.pagingToolbarShowingPre())
                .append(" {0} - {1} ")
                .append(MSGS.pagingToolbarShowingMid())
                .append(" {2} ")
                .append(kapuaPagingToolbarMessages.pagingToolbarShowingPost());
        pagingToolbarMessages.setDisplayMsg(sb.toString());

        pagingToolbarMessages.setEmptyMsg(kapuaPagingToolbarMessages.pagingToolbarNoResult());
        pagingToolbarMessages.setBeforePageText(MSGS.pagingToolbarPage());
        pagingToolbarMessages.setAfterPageText(MSGS.pagingToolbarOf().concat("{0}"));
        pagingToolbarMessages.setFirstText(MSGS.pagingToolbarFirstPage());
        pagingToolbarMessages.setPrevText(MSGS.pagingToolbarPrevPage());
        pagingToolbarMessages.setNextText(MSGS.pagingToolbarNextPage());
        pagingToolbarMessages.setLastText(MSGS.pagingToolbarLastPage());
        pagingToolbarMessages.setRefreshText(MSGS.pagingToolbarRefresh());
    }

}
