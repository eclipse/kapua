/*******************************************************************************
 * Copyright (c) 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.console.module.data.client;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.kapua.app.console.module.api.client.messages.ConsoleMessages;
import org.eclipse.kapua.app.console.module.api.client.util.FailureHandler;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSession;
import org.eclipse.kapua.app.console.module.data.client.messages.ConsoleDataMessages;
import org.eclipse.kapua.app.console.module.data.shared.model.GwtDatastoreDevice;
import org.eclipse.kapua.app.console.module.data.shared.service.GwtDataService;
import org.eclipse.kapua.app.console.module.data.shared.service.GwtDataServiceAsync;

import com.extjs.gxt.ui.client.event.IconButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.Text;
import com.extjs.gxt.ui.client.widget.button.ToolButton;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class DeviceTimestampCellRenderer implements GridCellRenderer<GwtDatastoreDevice> {

    private static final GwtDataServiceAsync DATA_SERVICE = GWT.create(GwtDataService.class);
    private static final ConsoleMessages MSGS = GWT.create(ConsoleMessages.class);
    private static final ConsoleDataMessages DATA_MSGS = GWT.create(ConsoleDataMessages.class);

    private final GwtSession currentSession;

    public DeviceTimestampCellRenderer(GwtSession currentSession) {
        this.currentSession = currentSession;
    }

    @Override
    public Object render(final GwtDatastoreDevice gwtDevice, String s, ColumnData columnData, int i, int i1, ListStore<GwtDatastoreDevice> listStore, final Grid<GwtDatastoreDevice> grid) {
        final HorizontalPanel hp = new HorizontalPanel();
        hp.setHeight(15);
        // Timestamp text
        final Text cellText = new Text(gwtDevice.getTimestamp() != null ? gwtDevice.getTimestampFormatted() : DATA_MSGS.topicInfoTableCalculatingLastPostDate());
        cellText.setStyleName("x-grid3-cell");
        hp.add(cellText);

        // Refresh button
        final ToolButton refreshButton = new ToolButton("x-tool-refresh", new SelectionListener<IconButtonEvent>() {

            @Override
            public void componentSelected(IconButtonEvent ce) {
                refreshTimestamp(gwtDevice, cellText, grid);
            }
        });
        hp.add(refreshButton);
        refreshButton.hide();

        // Show refresh button on mouse over
        hp.addDomHandler(new MouseOverHandler() {
            @Override
            public void onMouseOver(MouseOverEvent arg0) {
                refreshButton.show();
            }
        }, MouseOverEvent.getType());

        // Hide refresh button on mouse out
        hp.addDomHandler(new MouseOutHandler() {
            @Override
            public void onMouseOut(MouseOutEvent arg0) {
                refreshButton.hide();
            }
        }, MouseOutEvent.getType());

        return hp;
    }

    private void refreshTimestamp(final GwtDatastoreDevice gwtDevice, final Text cellText, final Grid<GwtDatastoreDevice> grid) {
        cellText.mask(DATA_MSGS.topicInfoTableCalculatingLastPostDate());

        List<GwtDatastoreDevice> gwtDevices = new ArrayList<GwtDatastoreDevice>();
        gwtDevices.add(gwtDevice);
        // Refresh timestamp for selected topic
        DATA_SERVICE.updateDeviceTimestamps(currentSession.getSelectedAccountId(), gwtDevices, new AsyncCallback<List<GwtDatastoreDevice>>() {

            @Override
            public void onFailure(Throwable caught) {
                cellText.unmask();
                FailureHandler.handle(caught);
            }

            @Override
            public void onSuccess(List<GwtDatastoreDevice> gwtDatastoreDevices) {
                if (gwtDatastoreDevices != null && gwtDatastoreDevices.size() == 1) {
                    gwtDevice.setTimestamp(gwtDatastoreDevices.get(0).getTimestamp());
                } else {
                    cellText.setText(DATA_MSGS.topicInfoTableNoLastPostDate());
                }
                grid.getView().refresh(false);
                cellText.unmask();
            }
        });
    }
}
