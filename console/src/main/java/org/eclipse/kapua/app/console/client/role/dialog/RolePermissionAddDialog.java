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
 *
 *******************************************************************************/
package org.eclipse.kapua.app.console.client.role.dialog;

import java.util.List;

import org.eclipse.kapua.app.console.client.messages.ConsoleRoleMessages;
import org.eclipse.kapua.app.console.client.ui.dialog.entity.EntityAddEditDialog;
import org.eclipse.kapua.app.console.client.ui.widget.EnumComboBox;
import org.eclipse.kapua.app.console.client.util.DialogUtils;
import org.eclipse.kapua.app.console.client.util.FailureHandler;
import org.eclipse.kapua.app.console.shared.model.GwtGroup;
import org.eclipse.kapua.app.console.shared.model.GwtPermission;
import org.eclipse.kapua.app.console.shared.model.GwtPermission.GwtAction;
import org.eclipse.kapua.app.console.shared.model.GwtPermission.GwtDomain;
import org.eclipse.kapua.app.console.shared.model.GwtSession;
import org.eclipse.kapua.app.console.shared.model.authorization.GwtRole;
import org.eclipse.kapua.app.console.shared.model.authorization.GwtRolePermission;
import org.eclipse.kapua.app.console.shared.model.authorization.GwtRolePermissionCreator;
import org.eclipse.kapua.app.console.shared.service.GwtDomainService;
import org.eclipse.kapua.app.console.shared.service.GwtDomainServiceAsync;
import org.eclipse.kapua.app.console.shared.service.GwtGroupService;
import org.eclipse.kapua.app.console.shared.service.GwtGroupServiceAsync;
import org.eclipse.kapua.app.console.shared.service.GwtRoleService;
import org.eclipse.kapua.app.console.shared.service.GwtRoleServiceAsync;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class RolePermissionAddDialog extends EntityAddEditDialog {

    private final static ConsoleRoleMessages MSGS = GWT.create(ConsoleRoleMessages.class);
    private final static GwtDomainServiceAsync domainService = GWT.create(GwtDomainService.class);
    private final static GwtGroupServiceAsync groupService = GWT.create(GwtGroupService.class);

    protected EnumComboBox<GwtDomain> domainCombo;
    protected EnumComboBox<GwtAction> actionCombo;
    protected ComboBox<GwtGroup> groupCombo;
    protected GwtRole selectedRole;
    
    private final GwtGroup allGroup;

    public RolePermissionAddDialog(GwtSession currentSession) {
        super(currentSession);
        DialogUtils.resizeDialog(this, 400, 200);
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

        domainService.findAll(new AsyncCallback<List<GwtDomain>>() {

            @Override
            public void onSuccess(List<GwtDomain> domainslist) {
                domainCombo.add(domainslist);
                if (!domainslist.isEmpty()) {
                    domainCombo.setSimpleValue(domainslist.get(0));
                }
                refreshActions();
            }

            @Override
            public void onFailure(Throwable t) {
                FailureHandler.handle(t);
            }
        });
        domainCombo = new EnumComboBox<GwtDomain>();
        domainCombo.setFieldLabel(MSGS.permissionAddDialogDomain());
        domainCombo.setForceSelection(true);
        domainCombo.setTypeAhead(false);
        domainCombo.setEditable(false);
        domainCombo.setAllowBlank(false);
        formPanel.add(domainCombo);

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

        groupCombo = new ComboBox<GwtGroup>();
        groupCombo.setStore(new ListStore<GwtGroup>());
        groupCombo.setFieldLabel(MSGS.permissionAddDialogGroup());
        groupCombo.setForceSelection(true);
        groupCombo.setTypeAhead(true);
        groupCombo.setAllowBlank(false);
        groupCombo.setDisplayField("groupName");
        groupCombo.setValueField("id");
        groupService.findAll(currentSession.getSelectedAccount().getId(), new AsyncCallback<List<GwtGroup>>() {

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

        m_bodyPanel.add(formPanel);
    }

    private void refreshActions() {
        domainService.findActionsByDomainName(domainCombo.getValue().getValue().toString(), new AsyncCallback<List<GwtAction>>() {

            @Override
            public void onSuccess(List<GwtAction> actionslist) {
                actionCombo.getStore().removeAll();
                actionCombo.add(actionslist);
                if (!actionslist.isEmpty()) {
                    actionCombo.setSimpleValue(actionslist.get(0));
                }
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

        permission.setDomain(domainCombo.getValue().getValue().toString());
        permission.setAction(actionCombo.getValue().getValue().toString());
        permission.setGroupId(groupCombo.getValue().getId());
        permission.setTargetScopeId(currentSession.getSelectedAccount().getId());
        GwtRolePermissionCreator rolePermission = new GwtRolePermissionCreator();
        rolePermission.setScopeId(currentSession.getSelectedAccount().getId());
        rolePermission.setRoleId(selectedRole.getId());

        GwtRoleServiceAsync roleService = GWT.create(GwtRoleService.class);
        roleService.addRolePermission(xsrfToken, rolePermission, permission, new AsyncCallback<GwtRolePermission>() {

            @Override
            public void onSuccess(GwtRolePermission rolePermission) {
                m_exitStatus = true;
                m_exitMessage = MSGS.dialogAddConfirmation();
                hide();
            }

            @Override
            public void onFailure(Throwable cause) {
                m_exitStatus = false;
                m_exitMessage = MSGS.dialogAddError(cause.getLocalizedMessage());
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
