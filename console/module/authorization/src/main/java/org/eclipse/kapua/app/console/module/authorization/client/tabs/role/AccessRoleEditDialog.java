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
 *******************************************************************************/
package org.eclipse.kapua.app.console.module.authorization.client.tabs.role;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.kapua.app.console.module.api.client.ui.dialog.entity.EntityAddEditDialog;
import org.eclipse.kapua.app.console.module.authorization.client.messages.ConsolePermissionMessages;
import org.eclipse.kapua.app.console.module.authorization.shared.service.GwtAccessInfoServiceAsync;
import org.eclipse.kapua.app.console.module.api.client.ui.panel.FormPanel;
import org.eclipse.kapua.app.console.module.api.client.util.DialogUtils;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtSession;
import org.eclipse.kapua.app.console.module.authorization.shared.model.GwtAccessInfo;
import org.eclipse.kapua.app.console.module.authorization.shared.model.GwtAccessRole;
import org.eclipse.kapua.app.console.module.authorization.shared.model.GwtAccessRoleCreator;
import org.eclipse.kapua.app.console.module.authorization.shared.model.GwtRole;
import org.eclipse.kapua.app.console.module.authorization.shared.service.GwtAccessInfoService;

import org.eclipse.kapua.app.console.module.authorization.shared.service.GwtAccessRoleServiceAsync;
import org.eclipse.kapua.app.console.module.authorization.shared.service.GwtAccessRoleService;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class AccessRoleEditDialog extends EntityAddEditDialog {

    private final static ConsolePermissionMessages MSGS = GWT.create(ConsolePermissionMessages.class);
    private final static GwtAccessRoleServiceAsync GWT_ACCESS_ROLE_SERVICE = GWT.create(GwtAccessRoleService.class);
    private final static GwtAccessInfoServiceAsync GWT_ACCESS_INFO_SERVICE = GWT.create(GwtAccessInfoService.class);
    private List<SelectionChangedListener<GwtRole>> listeners = new ArrayList<SelectionChangedListener<GwtRole>>();
    private String accessInfoId;
    private String userId;
    private List<GwtAccessRole> checkedRolesList;
    private List<GwtRole> roleList;
    private CheckBox checkBox;
    private Map<GwtRole, CheckBox> mapping;

    public AccessRoleEditDialog(GwtSession currentSession, String userId) {
        super(currentSession);
        this.userId = userId;
        mapping = new HashMap<GwtRole, CheckBox>();
        checkedRolesList = new ArrayList<GwtAccessRole>();
        roleList = new ArrayList<GwtRole>();
        GWT_ACCESS_INFO_SERVICE.findByUserIdOrCreate(currentSession.getSelectedAccountId(), userId, new AsyncCallback<GwtAccessInfo>() {

            @Override
            public void onSuccess(GwtAccessInfo result) {
                accessInfoId = result.getId();
                submitButton.enable();
            }

            @Override
            public void onFailure(Throwable caught) {
                exitStatus = false;
                exitMessage = MSGS.dialogAddError(MSGS.dialogAddRoleErrorAccessInfo(caught.getLocalizedMessage()));
                hide();
            }
        });
        DialogUtils.resizeDialog(this, 260, 300);
    }

    @Override
    public void submit() {
        List<GwtRole> result = new ArrayList<GwtRole>();
        for (Map.Entry<GwtRole, CheckBox> e : mapping.entrySet()) {
            if (e.getValue().getValue()) {
                result.add(e.getKey());
            }
        }
        List<GwtAccessRoleCreator> listCreator = new ArrayList<GwtAccessRoleCreator>();
        for (GwtRole role : result) {
            GwtAccessRoleCreator gwtAccessRoleCreator = new GwtAccessRoleCreator();
            gwtAccessRoleCreator.setScopeId(currentSession.getSelectedAccountId());
            gwtAccessRoleCreator.setAccessInfoId(accessInfoId);
            gwtAccessRoleCreator.setRoleId(role.getId());
            listCreator.add(gwtAccessRoleCreator);
        }
        GWT_ACCESS_ROLE_SERVICE.createCheck(xsrfToken, currentSession.getSelectedAccountId(), userId, listCreator, new AsyncCallback<GwtAccessRole>() {

            @Override
            public void onSuccess(GwtAccessRole arg0) {
                exitStatus = true;
                exitMessage = MSGS.dialogEditRoleConfirmation();
                hide();
            }

            @Override
            public void onFailure(Throwable cause) {
                unmask();
                submitButton.enable();
                cancelButton.enable();
                status.hide();
                exitStatus = false;
                exitMessage = MSGS.dialogAddError(MSGS.dialogAddRoleError(cause.getLocalizedMessage()));
                hide();
            }
        });
    }

    @Override
    public String getHeaderMessage() {
        return MSGS.dialogEditRoleHeader();
    }

    @Override
    public String getInfoMessage() {
        return MSGS.dialogEditRoleInfo();
    }

    @Override
    public void createBody() {
        FormPanel roleFormPanel = new FormPanel(FORM_LABEL_WIDTH);
        for (GwtRole gwtRole : roleList) {
            checkBox = new CheckBox();
            checkBox.setFieldLabel(gwtRole.getName());
            checkBox.setAutoWidth(true);
            roleFormPanel.add(checkBox);
            for (GwtAccessRole gwtAccessRole : checkedRolesList) {
                if (gwtRole.getId().equals(gwtAccessRole.getRoleId())) {
                    checkBox.setValue(true);
                }
            }
            mapping.put(gwtRole, checkBox);
        }
        bodyPanel.add(roleFormPanel);
    }

    @Override
    protected void onRender(Element parent, int pos) {
        super.onRender(parent, pos);
        submitButton.disable();
    }

    public void addSelectionListener(SelectionChangedListener<GwtRole> listener) {
        listeners.add(listener);
    }

    public void setCheckedRolesList(List<GwtAccessRole> checkedRolesList) {
        this.checkedRolesList = checkedRolesList;
    }

    public void setAllRoles(List<GwtRole> allRolesList) {
        roleList = allRolesList;
    }
}