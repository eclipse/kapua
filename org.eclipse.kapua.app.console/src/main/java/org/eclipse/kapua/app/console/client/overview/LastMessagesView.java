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
package org.eclipse.kapua.app.console.client.overview;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.kapua.app.console.client.messages.ConsoleMessages;
import org.eclipse.kapua.app.console.shared.model.GwtMessage;
import org.eclipse.kapua.app.console.shared.model.GwtSession;
import org.eclipse.kapua.app.console.shared.service.GwtDataService;
import org.eclipse.kapua.app.console.shared.service.GwtDataServiceAsync;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Element;

public class LastMessagesView extends LayoutContainer
{

    private static final ConsoleMessages MSGS               = GWT.create(ConsoleMessages.class);
    private static final int             LAST_MESSAGE_LIMIT = 25;

    private final GwtDataServiceAsync    gwtDataService     = GWT.create(GwtDataService.class);
    private GwtSession                   m_currentSession;

    private ListStore<GwtMessage>        m_lastMessageStore;
    private Grid<GwtMessage>             m_grid;

    public LastMessagesView(GwtSession currentSession)
    {
        m_currentSession = currentSession;
    }

    protected void onRender(final Element parent, int index)
    {
        super.onRender(parent, index);

        setLayout(new FitLayout());

        ContentPanel gridPanel = new ContentPanel();
        gridPanel.setBorders(false);
        gridPanel.setBodyBorder(true);
        gridPanel.setHeading(MSGS.dashboardDataLastMessageTitle());
        gridPanel.setLayout(new FitLayout());

        if (m_currentSession.hasDataReadPermission()) {
            List<ColumnConfig> columns = new ArrayList<ColumnConfig>();
            ColumnConfig date = new ColumnConfig("timestamp", MSGS.dashboardDataTableTimestampColumn(), 140);
            date.setDateTimeFormat(DateTimeFormat.getFormat("MM/dd/yyyy HH:mm:ss.SSS"));
            columns.add(date);

            ColumnConfig column = new ColumnConfig("asset", MSGS.dashboardDataTableDeviceColumn(), 130);
            column.setAlignment(HorizontalAlignment.CENTER);
            columns.add(column);

            column = new ColumnConfig("topic", MSGS.dashboardDataTableTopicColumn(), 130);
            column.setAlignment(HorizontalAlignment.LEFT);
            columns.add(column);

            m_lastMessageStore = new ListStore<GwtMessage>();

            m_grid = new Grid<GwtMessage>(m_lastMessageStore, new ColumnModel(columns));
            m_grid.setBorders(false);
            m_grid.setAutoExpandColumn("topic");
            m_grid.getView().setEmptyText(MSGS.noResults());
            m_grid.getView().setAutoFill(true);

            gridPanel.add(m_grid);
        }
        else {
            gridPanel.add(new ForbiddenPanelView());
        }

        add(gridPanel);

        refresh();
    }

    public void refresh()
    {
        if (rendered && m_currentSession.hasDataReadPermission()) {
            m_grid.mask(MSGS.loading());

            // gwtDataService.findLastMessageByTopic(m_currentSession.getSelectedAccount().getName(), LAST_MESSAGE_LIMIT, new AsyncCallback<List<GwtMessage>>() {
            //
            // @Override
            // public void onFailure(Throwable caught)
            // {
            // FailureHandler.handle(caught);
            // m_grid.unmask();
            // }
            //
            // @Override
            // public void onSuccess(List<GwtMessage> lastMessages)
            // {
            // m_lastMessageStore.removeAll();
            // m_lastMessageStore.add(lastMessages);
            // m_grid.unmask();
            // }
            // });
        }
    }
}
