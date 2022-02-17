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
package org.eclipse.kapua.app.console.module.authorization.client.tabs.role;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.eclipse.kapua.app.console.module.api.client.GwtKapuaException;
import org.eclipse.kapua.app.console.module.api.client.messages.ConsoleMessages;
import org.eclipse.kapua.app.console.module.api.client.ui.dialog.entity.EntityAddEditDialog;
import org.eclipse.kapua.app.console.module.api.client.ui.panel.FormPanel;
import org.eclipse.kapua.app.console.module.api.client.util.ConsoleInfo;
import org.eclipse.kapua.app.console.module.api.client.util.DialogUtils;
import org.eclipse.kapua.app.console.module.api.client.util.FailureHandler;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSession;
import org.eclipse.kapua.app.console.module.authorization.client.messages.ConsolePermissionMessages;
import org.eclipse.kapua.app.console.module.authorization.shared.model.GwtAccessInfo;
import org.eclipse.kapua.app.console.module.authorization.shared.model.GwtAccessRole;
import org.eclipse.kapua.app.console.module.authorization.shared.model.GwtAccessRoleCreator;
import org.eclipse.kapua.app.console.module.authorization.shared.model.GwtRole;
import org.eclipse.kapua.app.console.module.authorization.shared.service.GwtAccessInfoService;
import org.eclipse.kapua.app.console.module.authorization.shared.service.GwtAccessInfoServiceAsync;
import org.eclipse.kapua.app.console.module.authorization.shared.service.GwtAccessRoleService;
import org.eclipse.kapua.app.console.module.authorization.shared.service.GwtAccessRoleServiceAsync;
import org.eclipse.kapua.app.console.module.authorization.shared.service.GwtRoleService;
import org.eclipse.kapua.app.console.module.authorization.shared.service.GwtRoleServiceAsync;

import java.util.List;

public class AccessRoleAddDialog extends EntityAddEditDialog {

    private static final ConsolePermissionMessages MSGS = GWT.create(ConsolePermissionMessages.class);
    private static final ConsoleMessages CMSGS = GWT.create(ConsoleMessages.class);

    private static final GwtRoleServiceAsync GWT_ROLE_SERVICE = GWT.create(GwtRoleService.class);
    private static final GwtAccessRoleServiceAsync GWT_ACCESS_ROLE_SERVICE = GWT.create(GwtAccessRoleService.class);
    private static final GwtAccessInfoServiceAsync GWT_ACCESS_INFO_SERVICE = GWT.create(GwtAccessInfoService.class);

    private ComboBox<GwtRole> rolesCombo;
    private String accessInfoId;

    public AccessRoleAddDialog(GwtSession currentSession, String userId) {
        super(currentSession);
        GWT_ACCESS_INFO_SERVICE.findByUserIdOrCreate(currentSession.getSelectedAccountId(), userId, new AsyncCallback<GwtAccessInfo>() {

            @Override
            public void onSuccess(GwtAccessInfo result) {
                accessInfoId = result.getId();
            }

            @Override
            public void onFailure(Throwable caught) {
                exitStatus = false;
                if (!isPermissionErrorMessage(caught)) {
                    exitMessage = MSGS.dialogAddError(MSGS.dialogAddRoleErrorAccessInfo(caught.getLocalizedMessage()));
                }

                hide();
            }
        });

        DialogUtils.resizeDialog(this, 400, 150);
    }

    @Override
    public void submit() {
        GwtAccessRoleCreator gwtAccessRoleCreator = new GwtAccessRoleCreator();

        gwtAccessRoleCreator.setScopeId(currentSession.getSelectedAccountId());

        gwtAccessRoleCreator.setAccessInfoId(accessInfoId);
        gwtAccessRoleCreator.setRoleId(rolesCombo.getValue().getId());

        GWT_ACCESS_ROLE_SERVICE.create(xsrfToken, gwtAccessRoleCreator, new AsyncCallback<GwtAccessRole>() {

            @Override
            public void onSuccess(GwtAccessRole arg0) {
                exitStatus = true;
                exitMessage = MSGS.dialogAddRoleConfirmation();
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
                    switch (((GwtKapuaException) cause).getCode()) {
                    case DUPLICATE_NAME:
                        exitMessage = MSGS.dialogAddRoleDuplicateError();
                        break;
                    default:
                        exitMessage = MSGS.dialogAddError(MSGS.dialogAddRoleError(cause.getLocalizedMessage()));
                    }
                }
                rolesCombo.markInvalid(exitMessage);
                ConsoleInfo.display(CMSGS.error(), exitMessage);
            }
        });
    }

    @Override
    public String getHeaderMessage() {
        return MSGS.dialogAddRoleHeader();
    }

    @Override
    public String getInfoMessage() {
        return MSGS.dialogAddRoleInfo();
    }

    @Override
    public void createBody() {
        FormPanel roleFormPanel = new FormPanel(FORM_LABEL_WIDTH);

        //
        // Role
        rolesCombo = new ComboBox<GwtRole>();
        rolesCombo.setEditable(false);
        rolesCombo.setTypeAhead(false);
        rolesCombo.setAllowBlank(false);
        rolesCombo.setFieldLabel("* " + MSGS.dialogAddRoleComboName());
        rolesCombo.setToolTip(MSGS.dialogAddRoleComboNameTooltip());
        rolesCombo.setTriggerAction(TriggerAction.ALL);
        rolesCombo.setStore(new ListStore<GwtRole>());
        rolesCombo.setDisplayField("name");
        rolesCombo.setTemplate("<tpl for=\".\"><div role=\"listitem\" class=\"x-combo-list-item\" title={name}>{name}</div></tpl>");
        rolesCombo.setValueField("id");
        rolesCombo.addListener(Events.Select, new Listener<BaseEvent>() {

            @Override
            public void handleEvent(BaseEvent be) {
                AccessRoleAddDialog.this.formPanel.fireEvent(Events.OnClick);
            }
        });

        GWT_ROLE_SERVICE.findAll(currentSession.getSelectedAccountId(), new AsyncCallback<List<GwtRole>>() {

            @Override
            public void onFailure(Throwable caught) {
                exitStatus = false;
                FailureHandler.handle(caught);
                hide();
            }

            @Override
            public void onSuccess(List<GwtRole> result) {
                rolesCombo.getStore().add(result);
                rolesCombo.setEmptyText(result.isEmpty() ? MSGS.dialogAddRoleComboEmptyTextNoRoles() : MSGS.dialogAddRoleComboEmptyText());
            }
        });
        roleFormPanel.add(rolesCombo);

        //
        // Add form panel to body
        bodyPanel.add(roleFormPanel);
    }

    @Override
    protected void onRender(Element parent, int pos) {
        super.onRender(parent, pos);
        submitButton.disable();
    }
}
