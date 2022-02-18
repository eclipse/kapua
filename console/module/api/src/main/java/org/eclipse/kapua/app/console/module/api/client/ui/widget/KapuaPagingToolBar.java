/*******************************************************************************
 * Copyright (c) 2017, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.app.console.module.api.client.ui.widget;

import com.extjs.gxt.ui.client.widget.toolbar.PagingToolBar;
import com.google.gwt.core.client.GWT;
import org.eclipse.kapua.app.console.module.api.client.messages.ConsoleMessages;

public class KapuaPagingToolBar extends PagingToolBar {

    private static final ConsoleMessages TOOLBAR_MSGS = GWT.create(ConsoleMessages.class);

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
        sb.append(TOOLBAR_MSGS.pagingToolbarShowingPre())
                .append(" {0} - {1} ")
                .append(TOOLBAR_MSGS.pagingToolbarShowingMid())
                .append(" {2} ")
                .append(kapuaPagingToolbarMessages.pagingToolbarShowingPost());
        pagingToolbarMessages.setDisplayMsg(sb.toString());

        pagingToolbarMessages.setEmptyMsg(kapuaPagingToolbarMessages.pagingToolbarNoResult());
        pagingToolbarMessages.setBeforePageText(TOOLBAR_MSGS.pagingToolbarPage());
        pagingToolbarMessages.setAfterPageText(TOOLBAR_MSGS.pagingToolbarOf().concat("{0}"));
        pagingToolbarMessages.setFirstText(TOOLBAR_MSGS.pagingToolbarFirstPage());
        pagingToolbarMessages.setPrevText(TOOLBAR_MSGS.pagingToolbarPrevPage());
        pagingToolbarMessages.setNextText(TOOLBAR_MSGS.pagingToolbarNextPage());
        pagingToolbarMessages.setLastText(TOOLBAR_MSGS.pagingToolbarLastPage());
        pagingToolbarMessages.setRefreshText(TOOLBAR_MSGS.pagingToolbarRefresh());
    }

}
