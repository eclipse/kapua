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
package org.eclipse.kapua.app.console.module.data.client;

import com.extjs.gxt.ui.client.event.IconButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
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
import com.google.gwt.user.client.ui.FlowPanel;
import org.eclipse.kapua.app.console.module.api.client.util.FailureHandler;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSession;
import org.eclipse.kapua.app.console.module.data.client.messages.ConsoleDataMessages;
import org.eclipse.kapua.app.console.module.data.shared.model.GwtDatastoreDevice;
import org.eclipse.kapua.app.console.module.data.shared.service.GwtDataService;
import org.eclipse.kapua.app.console.module.data.shared.service.GwtDataServiceAsync;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DeviceTimestampCellRenderer implements GridCellRenderer<GwtDatastoreDevice> {

    private static final ConsoleDataMessages DATA_MSGS = GWT.create(ConsoleDataMessages.class);

    private static final GwtDataServiceAsync DATA_SERVICE = GWT.create(GwtDataService.class);

    private final GwtSession currentSession;

    public DeviceTimestampCellRenderer(GwtSession currentSession) {
        this.currentSession = currentSession;
    }

    @Override
    public Object render(final GwtDatastoreDevice gwtDevice, String s, ColumnData columnData, int i, int i1, ListStore<GwtDatastoreDevice> listStore, final Grid<GwtDatastoreDevice> grid) {
        final FlowPanel flowPanel = new FlowPanel();
        flowPanel.setHeight("15px");

        // Timestamp text
        final Text cellText = new Text();
        cellText.setStyleName("x-grid3-cell");
        cellText.setStyleAttribute("float", "left");

        if (new Date(GwtDatastoreDevice.NO_TIMESTAMP).equals(gwtDevice.getTimestamp())) {
            cellText.setText(DATA_MSGS.topicInfoTableNoLastPostDate());
        } else {
            cellText.setText(gwtDevice.getTimestamp() != null ? gwtDevice.getTimestampFormatted() : DATA_MSGS.topicInfoTableCalculatingLastPostDate());
        }

        flowPanel.add(cellText);

        // Refresh button
        final ToolButton refreshButton = new ToolButton("x-tool-refresh", new SelectionListener<IconButtonEvent>() {

            @Override
            public void componentSelected(IconButtonEvent ce) {
                refreshTimestamp(gwtDevice, cellText, grid);
            }
        });
        refreshButton.setStyleAttribute("float", "left");
        flowPanel.add(refreshButton);
        refreshButton.hide();

        // Show refresh button on mouse over
        flowPanel.addDomHandler(new MouseOverHandler() {

            @Override
            public void onMouseOver(MouseOverEvent arg0) {
                refreshButton.show();
            }
        }, MouseOverEvent.getType());

        // Hide refresh button on mouse out
        flowPanel.addDomHandler(new MouseOutHandler() {

            @Override
            public void onMouseOut(MouseOutEvent arg0) {
                refreshButton.hide();
            }
        }, MouseOutEvent.getType());

        return flowPanel;
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
            public void onSuccess(List<GwtDatastoreDevice> list) {
                if (!list.isEmpty() && list.get(0).getTimestamp() != null) {
                    gwtDevice.setTimestamp(list.get(0).getTimestamp());
                } else {
                    gwtDevice.setTimestamp(new Date(GwtDatastoreDevice.NO_TIMESTAMP));
                }

                grid.getView().refresh(false);
                cellText.unmask();
            }
        });
    }
}
