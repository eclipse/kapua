/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.console.module.device.client.device.tag;

import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.eclipse.kapua.app.console.module.api.client.ui.dialog.entity.EntityAddEditDialog;
import org.eclipse.kapua.app.console.module.api.client.ui.panel.FormPanel;
import org.eclipse.kapua.app.console.module.api.client.util.DialogUtils;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtSession;
import org.eclipse.kapua.app.console.module.device.client.messages.ConsoleDeviceMessages;
import org.eclipse.kapua.app.console.module.device.shared.model.GwtDevice;
import org.eclipse.kapua.app.console.module.device.shared.service.GwtDeviceService;
import org.eclipse.kapua.app.console.module.device.shared.service.GwtDeviceServiceAsync;
import org.eclipse.kapua.app.console.module.tag.shared.model.GwtTag;
import org.eclipse.kapua.app.console.module.tag.shared.service.GwtTagService;
import org.eclipse.kapua.app.console.module.tag.shared.service.GwtTagServiceAsync;

import java.util.List;

public class DeviceTagAddDialog extends EntityAddEditDialog {

    private final static ConsoleDeviceMessages MSGS = GWT.create(ConsoleDeviceMessages.class);

    private final static GwtTagServiceAsync GWT_TAG_SERVICE = GWT.create(GwtTagService.class);
    private final static GwtDeviceServiceAsync GWT_DEVICE_SERVICE = GWT.create(GwtDeviceService.class);

    private ComboBox<GwtTag> tagsCombo;

    private GwtDevice selectedDevice;

    public DeviceTagAddDialog(GwtSession currentSession, GwtDevice selectedDevice) {
        super(currentSession);

        this.selectedDevice = selectedDevice;

        DialogUtils.resizeDialog(this, 400, 200);
    }

    @Override
    public void submit() {

        String tagId = tagsCombo.getValue().getId();

        GWT_DEVICE_SERVICE.addDeviceTag(xsrfToken, selectedDevice.getScopeId(), selectedDevice.getId(), tagId, new AsyncCallback<Void>() {

            @Override
            public void onSuccess(Void gwtAccessPermission) {
                exitStatus = true;
                exitMessage = MSGS.dialogDeviceTagAddConfirmation();
                hide();
            }

            @Override
            public void onFailure(Throwable cause) {
                unmask();
                submitButton.enable();
                cancelButton.enable();
                status.hide();
                exitStatus = false;
                exitMessage = MSGS.dialogDeviceTagAddError(cause.getLocalizedMessage());

                hide();
            }
        });
    }

    @Override
    public String getHeaderMessage() {
        return MSGS.dialogDeviceTagAddHeader(selectedDevice.getClientId());
    }

    @Override
    public String getInfoMessage() {
        return MSGS.dialogDeviceTagAddInfo();
    }

    @Override
    public void createBody() {

        FormPanel formPanel = new FormPanel(FORM_LABEL_WIDTH);

        //
        // Tags
        tagsCombo = new ComboBox<GwtTag>();
        tagsCombo.setStore(new ListStore<GwtTag>());
        tagsCombo.setEditable(false);
        tagsCombo.setTypeAhead(false);
        tagsCombo.setAllowBlank(false);
        tagsCombo.disable();
        // tagsCombo.setEmptyText(MSGS.dialogDeviceTagAddFieldTagEmptyText());
        tagsCombo.setFieldLabel(MSGS.dialogDeviceTagAddFieldTag());
        tagsCombo.setTriggerAction(TriggerAction.ALL);
        tagsCombo.setDisplayField("tagName");

        GWT_TAG_SERVICE.findAll(selectedDevice.getScopeId(), new AsyncCallback<List<GwtTag>>() {

            @Override
            public void onFailure(Throwable caught) {
                exitMessage = MSGS.dialogDeviceTagAddFieldTagLoadingError(caught.getLocalizedMessage());
                exitStatus = false;
                hide();
            }

            @Override
            public void onSuccess(List<GwtTag> result) {
                tagsCombo.getStore().add(result);

                tagsCombo.setEmptyText(result.isEmpty() ? MSGS.dialogDeviceTagAddFieldTagEmptyTextNoTags() : MSGS.dialogDeviceTagAddFieldTagEmptyText());

                tagsCombo.enable();
            }
        });

        formPanel.add(tagsCombo);

        bodyPanel.add(formPanel);
    }

    @Override
    protected void onRender(Element parent, int pos) {
        super.onRender(parent, pos);
    }
}
