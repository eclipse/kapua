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
package org.eclipse.kapua.app.console.module.authorization.client.role.dialog;

import java.util.List;

import org.eclipse.kapua.app.console.module.api.client.ui.dialog.entity.EntityAddEditDialog;
import org.eclipse.kapua.app.console.module.api.client.ui.widget.EnumComboBox;
import org.eclipse.kapua.app.console.module.api.client.util.DialogUtils;
import org.eclipse.kapua.app.console.module.api.client.util.FailureHandler;
import org.eclipse.kapua.app.console.module.authorization.client.messages.ConsoleRoleMessages;
import org.eclipse.kapua.app.console.module.authorization.shared.model.GwtDomain;
import org.eclipse.kapua.app.console.module.authorization.shared.model.GwtGroup;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtSession;
import org.eclipse.kapua.app.console.module.authorization.shared.model.GwtPermission;
import org.eclipse.kapua.app.console.module.authorization.shared.model.GwtPermission.GwtAction;
import org.eclipse.kapua.app.console.module.authorization.shared.model.GwtRole;
import org.eclipse.kapua.app.console.module.authorization.shared.model.GwtRolePermission;
import org.eclipse.kapua.app.console.module.authorization.shared.model.GwtRolePermissionCreator;
import org.eclipse.kapua.app.console.module.authorization.shared.service.GwtDomainService;
import org.eclipse.kapua.app.console.module.authorization.shared.service.GwtDomainServiceAsync;
import org.eclipse.kapua.app.console.module.authorization.shared.service.GwtGroupService;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.CheckBoxGroup;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.eclipse.kapua.app.console.module.authorization.shared.service.GwtGroupServiceAsync;
import org.eclipse.kapua.app.console.module.authorization.shared.service.GwtRoleService;
import org.eclipse.kapua.app.console.module.authorization.shared.service.GwtRoleServiceAsync;

public class RolePermissionAddDialog extends EntityAddEditDialog {

    private final static ConsoleRoleMessages MSGS = GWT.create(ConsoleRoleMessages.class);
    private final static GwtDomainServiceAsync DOMAIN_SERVICE = GWT.create(GwtDomainService.class);
    private final static GwtGroupServiceAsync GROUP_SERVICE = GWT.create(GwtGroupService.class);

    private ComboBox<GwtDomain> domainCombo;
    private EnumComboBox<GwtAction> actionCombo;
    private ComboBox<GwtGroup> groupCombo;
    private CheckBoxGroup forwardableChecboxGroup;
    private CheckBox forwardableChecbox;

    private GwtRole selectedRole;

    private final GwtGroup allGroup;
    private final GwtDomain allDomain = new GwtDomain("ALL");
    private final GwtAction allAction = GwtAction.ALL;

    public RolePermissionAddDialog(GwtSession currentSession) {
        super(currentSession);
        DialogUtils.resizeDialog(this, 400, 250);
        allGroup = new GwtGroup();
        allGroup.setId(null);
        allGroup.setGroupName("ALL");
    }

    public void setSelectedRole(GwtRole selectedRole) {
        this.selectedRole = selectedRole;
    }

    @Override
    public void createBody() {
        FormPanel formPanel = new FormPanel();
        formPanel.setHeaderVisible(false);
        formPanel.setBodyBorder(false);
        //
        // Domain
        DOMAIN_SERVICE.findAll(new AsyncCallback<List<GwtDomain>>() {

            @Override
            public void onSuccess(List<GwtDomain> domainslist) {
                domainCombo.getStore().removeAll();
                domainCombo.getStore().add(allDomain);
                domainCombo.getStore().add(domainslist);
                domainCombo.setValue(allDomain);
                refreshActions();
            }

            @Override
            public void onFailure(Throwable t) {
                FailureHandler.handle(t);
            }
        });
        domainCombo = new ComboBox<GwtDomain>();
        domainCombo.setStore(new ListStore<GwtDomain>());
        domainCombo.setFieldLabel(MSGS.permissionAddDialogDomain());
        domainCombo.setForceSelection(true);
        domainCombo.setTypeAhead(false);
        domainCombo.setEditable(false);
        domainCombo.setAllowBlank(false);
        formPanel.add(domainCombo);

        //
        // Action
        actionCombo = new EnumComboBox<GwtAction>();
        domainCombo.addListener(Events.Select, new Listener<BaseEvent>() {

            @Override
            public void handleEvent(BaseEvent be) {
                refreshActions();
            }
        });
        actionCombo.setFieldLabel(MSGS.permissionAddDialogAction());
        actionCombo.setForceSelection(true);
        actionCombo.setTypeAhead(false);
        actionCombo.setEditable(false);
        actionCombo.setAllowBlank(false);
        formPanel.add(actionCombo);

        //
        // Groups
        groupCombo = new ComboBox<GwtGroup>();
        groupCombo.setStore(new ListStore<GwtGroup>());
        groupCombo.setFieldLabel(MSGS.permissionAddDialogGroup());
        groupCombo.setForceSelection(true);
        groupCombo.setTypeAhead(true);
        groupCombo.setAllowBlank(false);
        groupCombo.setDisplayField("groupName");
        groupCombo.setValueField("id");
        GROUP_SERVICE.findAll(currentSession.getSelectedAccountId(), new AsyncCallback<List<GwtGroup>>() {

            @Override
            public void onSuccess(List<GwtGroup> groups) {
                groupCombo.getStore().removeAll();
                groupCombo.getStore().add(allGroup);
                groupCombo.getStore().add(groups);
                groupCombo.setValue(allGroup);
            }

            @Override
            public void onFailure(Throwable t) {
                FailureHandler.handle(t);
            }
        });
        formPanel.add(groupCombo);

        //
        // Forwardable
        forwardableChecbox = new CheckBox();
        forwardableChecbox.setBoxLabel("");

        forwardableChecboxGroup = new CheckBoxGroup();
        forwardableChecboxGroup.setFieldLabel(MSGS.permissionAddDialogForwardable());
        forwardableChecboxGroup.add(forwardableChecbox);
        formPanel.add(forwardableChecboxGroup);

        bodyPanel.add(formPanel);
    }

    private void refreshActions() {
        DOMAIN_SERVICE.findActionsByDomainName(domainCombo.getValue().getDomainName(), new AsyncCallback<List<GwtAction>>() {

            @Override
            public void onSuccess(List<GwtAction> actionslist) {
                actionCombo.getStore().removeAll();
                actionCombo.add(allAction);
                actionCombo.add(actionslist);
                actionCombo.setSimpleValue(allAction);
            }

            @Override
            public void onFailure(Throwable t) {
                FailureHandler.handle(t);
            }
        });
    }

    @Override
    public void submit() {
        GwtPermission permission = new GwtPermission();
        permission.setDomain(domainCombo.getValue().getDomainName());
        permission.setAction(actionCombo.getValue().getValue().toString());
        permission.setGroupId(groupCombo.getValue().getId());
        permission.setTargetScopeId(currentSession.getSelectedAccountId());
        permission.setForwardable(forwardableChecboxGroup.getValue() != null);

        GwtRolePermissionCreator rolePermission = new GwtRolePermissionCreator();
        rolePermission.setScopeId(currentSession.getSelectedAccountId());
        rolePermission.setRoleId(selectedRole.getId());

        GwtRoleServiceAsync roleService = GWT.create(GwtRoleService.class);
        roleService.addRolePermission(xsrfToken, rolePermission, permission, new AsyncCallback<GwtRolePermission>() {

            @Override
            public void onSuccess(GwtRolePermission rolePermission) {
                exitStatus = true;
                exitMessage = MSGS.dialogAddConfirmation();
                hide();
            }

            @Override
            public void onFailure(Throwable cause) {
                exitStatus = false;
                exitMessage = MSGS.dialogAddError(cause.getLocalizedMessage());
            }
        });

    }

    @Override
    public String getHeaderMessage() {
        return MSGS.permissionAddDialogHeader();
    }

    @Override
    public String getInfoMessage() {
        return MSGS.permissionAddDialogMessage();
    }

}
