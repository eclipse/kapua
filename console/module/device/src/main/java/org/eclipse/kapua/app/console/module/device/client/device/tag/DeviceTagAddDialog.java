/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.app.console.module.device.client.device.tag;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.eclipse.kapua.app.console.module.api.client.GwtKapuaErrorCode;
import org.eclipse.kapua.app.console.module.api.client.GwtKapuaException;
import org.eclipse.kapua.app.console.module.api.client.ui.dialog.entity.EntityAddEditDialog;
import org.eclipse.kapua.app.console.module.api.client.ui.panel.FormPanel;
import org.eclipse.kapua.app.console.module.api.client.util.DialogUtils;
import org.eclipse.kapua.app.console.module.api.client.util.FailureHandler;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSession;
import org.eclipse.kapua.app.console.module.device.client.messages.ConsoleDeviceMessages;
import org.eclipse.kapua.app.console.module.device.shared.model.GwtDevice;
import org.eclipse.kapua.app.console.module.device.shared.service.GwtDeviceService;
import org.eclipse.kapua.app.console.module.device.shared.service.GwtDeviceServiceAsync;
import org.eclipse.kapua.app.console.module.tag.shared.model.GwtTag;
import org.eclipse.kapua.app.console.module.tag.shared.service.GwtTagService;
import org.eclipse.kapua.app.console.module.tag.shared.service.GwtTagServiceAsync;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class DeviceTagAddDialog extends EntityAddEditDialog {

    private static final ConsoleDeviceMessages MSGS = GWT.create(ConsoleDeviceMessages.class);

    private static final GwtTagServiceAsync GWT_TAG_SERVICE = GWT.create(GwtTagService.class);
    private static final GwtDeviceServiceAsync GWT_DEVICE_SERVICE = GWT.create(GwtDeviceService.class);

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
                if (!isPermissionErrorMessage(cause)) {
                    if (cause instanceof GwtKapuaException) {
                        GwtKapuaException gwtCause = (GwtKapuaException) cause;
                        if (gwtCause.getCode().equals(GwtKapuaErrorCode.DUPLICATE_NAME)) {
                            tagsCombo.markInvalid(gwtCause.getMessage());
                        } else if (gwtCause.getCode().equals(GwtKapuaErrorCode.ENTITY_NOT_FOUND)) {
                            tagsCombo.markInvalid(gwtCause.getMessage());
                        }
                    }
                    FailureHandler.handleFormException(formPanel, cause);
                }
            }
        });
    }

    @Override
    public String getHeaderMessage() {
        return MSGS.dialogDeviceTagAddHeader();
    }

    @Override
    public String getInfoMessage() {
        return MSGS.dialogDeviceTagAddInfo();
    }

    @Override
    public void createBody() {
        submitButton.disable();

        FormPanel formPanel = new FormPanel(FORM_LABEL_WIDTH);
        Listener<BaseEvent> comboBoxListener = new Listener<BaseEvent>() {

            @Override
            public void handleEvent(BaseEvent be) {
                DeviceTagAddDialog.this.formPanel.fireEvent(Events.OnClick);
            }
        };

        //
        // Tags
        tagsCombo = new ComboBox<GwtTag>();
        tagsCombo.setStore(new ListStore<GwtTag>());
        tagsCombo.setEditable(false);
        tagsCombo.setTypeAhead(false);
        tagsCombo.setAllowBlank(false);
        tagsCombo.disable();
        tagsCombo.setEmptyText(MSGS.dialogDeviceTagAddFieldTagEmptyText());
        tagsCombo.setFieldLabel("* " + MSGS.dialogDeviceTagAddFieldTag());
        tagsCombo.setToolTip(MSGS.deviceFormTagTooltip());
        tagsCombo.setTriggerAction(TriggerAction.ALL);
        tagsCombo.setDisplayField("tagName");
        tagsCombo.setTemplate("<tpl for=\".\"><div role=\"listitem\" class=\"x-combo-list-item\" title={tagName}>{tagName}</div></tpl>");
        tagsCombo.addListener(Events.Select, comboBoxListener);

        GWT_TAG_SERVICE.findAll(selectedDevice.getScopeId(), new AsyncCallback<List<GwtTag>>() {

            @Override
            public void onFailure(Throwable caught) {
                exitStatus = false;
                if (!isPermissionErrorMessage(caught)) {
                    exitMessage = MSGS.dialogDeviceTagAddFieldTagLoadingError(caught.getLocalizedMessage());
                }
                hide();
            }

            @Override
            public void onSuccess(List<GwtTag> result) {
                Collections.sort(result, new Comparator<GwtTag>() {

                    @Override
                    public int compare(GwtTag tag1, GwtTag tag2) {
                        return tag1.getTagName().compareTo(tag2.getTagName());
                    }
                });
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
