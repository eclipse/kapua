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
package org.eclipse.kapua.app.console.module.authorization.client.tabs.permission;

import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.CheckBoxGroup;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.eclipse.kapua.app.console.module.api.client.ui.dialog.entity.EntityAddEditDialog;
import org.eclipse.kapua.app.console.module.api.client.ui.panel.FormPanel;
import org.eclipse.kapua.app.console.module.api.client.util.DialogUtils;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtSession;
import org.eclipse.kapua.app.console.module.authorization.client.messages.ConsolePermissionMessages;
import org.eclipse.kapua.app.console.module.authorization.shared.model.GwtAccessInfo;
import org.eclipse.kapua.app.console.module.authorization.shared.model.GwtAccessPermission;
import org.eclipse.kapua.app.console.module.authorization.shared.model.GwtAccessPermissionCreator;
import org.eclipse.kapua.app.console.module.authorization.shared.model.GwtDomain;
import org.eclipse.kapua.app.console.module.authorization.shared.model.GwtGroup;
import org.eclipse.kapua.app.console.module.authorization.shared.model.GwtPermission;
import org.eclipse.kapua.app.console.module.authorization.shared.model.GwtPermission.GwtAction;
import org.eclipse.kapua.app.console.module.authorization.shared.service.GwtAccessInfoService;
import org.eclipse.kapua.app.console.module.authorization.shared.service.GwtAccessInfoServiceAsync;
import org.eclipse.kapua.app.console.module.authorization.shared.service.GwtAccessPermissionService;
import org.eclipse.kapua.app.console.module.authorization.shared.service.GwtAccessPermissionServiceAsync;
import org.eclipse.kapua.app.console.module.authorization.shared.service.GwtDomainService;
import org.eclipse.kapua.app.console.module.authorization.shared.service.GwtDomainServiceAsync;
import org.eclipse.kapua.app.console.module.authorization.shared.service.GwtGroupService;
import org.eclipse.kapua.app.console.module.authorization.shared.service.GwtGroupServiceAsync;

import java.util.List;

public class PermissionAddDialog extends EntityAddEditDialog {

    private final static ConsolePermissionMessages MSGS = GWT.create(ConsolePermissionMessages.class);

    private final static GwtDomainServiceAsync GWT_DOMAIN_SERVICE = GWT.create(GwtDomainService.class);
    private final static GwtAccessPermissionServiceAsync GWT_ACCESS_PERMISSION_SERVICE = GWT.create(GwtAccessPermissionService.class);
    private final static GwtAccessInfoServiceAsync GWT_ACCESS_INFO_SERVICE = GWT.create(GwtAccessInfoService.class);
    private final static GwtGroupServiceAsync GWT_GROUP_SERVICE = GWT.create(GwtGroupService.class);

    private ComboBox<GwtDomain> domainsCombo;
    private SimpleComboBox<GwtAction> actionsCombo;
    private ComboBox<GwtGroup> groupsCombo;
    private CheckBoxGroup forwardableChecboxGroup;
    private CheckBox forwardableChecbox;

    private final GwtGroup allGroup;
    private final GwtDomain allDomain = new GwtDomain("ALL");
    private final GwtAction allAction = GwtAction.ALL;

    private String accessInfoId;

    public PermissionAddDialog(GwtSession currentSession, String userId) {
        super(currentSession);

        allGroup = new GwtGroup();
        allGroup.setId(null);
        allGroup.setGroupName("ALL");

        GWT_ACCESS_INFO_SERVICE.findByUserIdOrCreate(currentSession.getSelectedAccountId(), userId, new AsyncCallback<GwtAccessInfo>() {

            @Override
            public void onSuccess(GwtAccessInfo result) {
                accessInfoId = result.getId();
                submitButton.enable();
            }

            @Override
            public void onFailure(Throwable caught) {
                exitMessage = MSGS.dialogAddPermissionErrorAccessInfo(caught.getLocalizedMessage());
                exitStatus = false;
                hide();
            }
        });

        DialogUtils.resizeDialog(this, 500, 220);
    }

    @Override
    public void submit() {

        GwtPermission newPermission = new GwtPermission(//
                domainsCombo.getValue().getDomainName(), //
                actionsCombo.getValue().getValue(), //
                currentSession.getSelectedAccountId(), //
                groupsCombo.getValue().getId(), //
                forwardableChecboxGroup.getValue() != null);

        GwtAccessPermissionCreator gwtAccessPermissionCreator = new GwtAccessPermissionCreator();
        gwtAccessPermissionCreator.setScopeId(currentSession.getSelectedAccountId());
        gwtAccessPermissionCreator.setAccessInfoId(accessInfoId);

        gwtAccessPermissionCreator.setPermission(newPermission);

        GWT_ACCESS_PERMISSION_SERVICE.create(xsrfToken, gwtAccessPermissionCreator, new AsyncCallback<GwtAccessPermission>() {

            @Override
            public void onSuccess(GwtAccessPermission gwtAccessPermission) {
                exitStatus = true;
                exitMessage = MSGS.dialogAddPermissionConfirmation();   // TODO Localize
                hide();
            }

            @Override
            public void onFailure(Throwable cause) {
                unmask();

                submitButton.enable();
                cancelButton.enable();
                status.hide();

                exitStatus = false;
                exitMessage = MSGS.dialogAddError(MSGS.dialogAddPermissionError(cause.getLocalizedMessage()));

                hide();
            }
        });
    }

    @Override
    public String getHeaderMessage() {
        return MSGS.dialogAddPermissionHeader();
    }

    @Override
    public String getInfoMessage() {
        return MSGS.dialogAddPermissionInfo();
    }

    @Override
    public void createBody() {
        FormPanel permissionFormPanel = new FormPanel(FORM_LABEL_WIDTH);

        //
        // Domain
        domainsCombo = new ComboBox<GwtDomain>();
        domainsCombo.setStore(new ListStore<GwtDomain>());
        domainsCombo.setEditable(false);
        domainsCombo.setTypeAhead(false);
        domainsCombo.setAllowBlank(false);
        domainsCombo.disable();
        domainsCombo.setFieldLabel(MSGS.dialogAddPermissionDomain());
        domainsCombo.setTriggerAction(TriggerAction.ALL);
        domainsCombo.setEmptyText(MSGS.dialogAddPermissionLoading());
        domainsCombo.setDisplayField("domainName");
        GWT_DOMAIN_SERVICE.findAll(new AsyncCallback<List<GwtDomain>>() {

            @Override
            public void onFailure(Throwable caught) {
                exitMessage = MSGS.dialogAddPermissionErrorDomains(caught.getLocalizedMessage());
                exitStatus = false;
                hide();
            }

            @Override
            public void onSuccess(List<GwtDomain> result) {
                domainsCombo.getStore().add(allDomain);
                domainsCombo.getStore().add(result);
                domainsCombo.setValue(allDomain);
                domainsCombo.enable();
            }
        });

        domainsCombo.addSelectionChangedListener(new SelectionChangedListener<GwtDomain>() {

            @Override
            public void selectionChanged(SelectionChangedEvent<GwtDomain> se) {
                final GwtDomain selectedDomain = se.getSelectedItem();

                GWT_DOMAIN_SERVICE.findActionsByDomainName(selectedDomain.getDomainName(), new AsyncCallback<List<GwtAction>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        exitMessage = MSGS.dialogAddPermissionErrorActions(caught.getLocalizedMessage());
                        exitStatus = false;
                        hide();
                    }

                    @Override
                    public void onSuccess(List<GwtAction> result) {
                        actionsCombo.removeAll();
                        actionsCombo.add(allAction);
                        actionsCombo.add(result);
                        actionsCombo.setSimpleValue(allAction);
                        actionsCombo.enable();

                        if (selectedDomain.getGroupable()) {
                            groupsCombo.setEnabled(selectedDomain.getGroupable());
                            groupsCombo.setValue(allGroup);

                        } else {
                            groupsCombo.setEnabled(selectedDomain.getGroupable());
                            groupsCombo.setRawValue(MSGS.dialogAddPermissionGroupIdNotGroupable());
                        }
                    }
                });

            }
        });
        permissionFormPanel.add(domainsCombo);

        //
        // Action
        actionsCombo = new SimpleComboBox<GwtAction>();
        actionsCombo.disable();
        actionsCombo.setTypeAhead(false);
        actionsCombo.setAllowBlank(false);
        actionsCombo.setFieldLabel(MSGS.dialogAddPermissionAction());
        actionsCombo.setTriggerAction(TriggerAction.ALL);
        actionsCombo.setEmptyText(MSGS.dialogAddPermissionLoading());

        permissionFormPanel.add(actionsCombo);

        // Groups
        groupsCombo = new ComboBox<GwtGroup>();
        groupsCombo.setStore(new ListStore<GwtGroup>());
        groupsCombo.setEditable(false);
        groupsCombo.setTypeAhead(false);
        groupsCombo.setAllowBlank(false);
        groupsCombo.setDisplayField("groupName");
        groupsCombo.setValueField("id");
        groupsCombo.setFieldLabel(MSGS.dialogAddPermissionGroupId());
        groupsCombo.setTriggerAction(TriggerAction.ALL);
        groupsCombo.setEmptyText(MSGS.dialogAddPermissionLoading());
        groupsCombo.disable();
        GWT_GROUP_SERVICE.findAll(currentSession.getSelectedAccountId(), new AsyncCallback<List<GwtGroup>>() {

            @Override
            public void onFailure(Throwable caught) {
                exitMessage = MSGS.dialogAddPermissionErrorGroups(caught.getLocalizedMessage());
                exitStatus = false;
                hide();
            }

            @Override
            public void onSuccess(List<GwtGroup> result) {
                groupsCombo.getStore().removeAll();
                groupsCombo.getStore().add(allGroup);
                groupsCombo.getStore().add(result);
                groupsCombo.setValue(allGroup);
                groupsCombo.enable();
            }
        });
        permissionFormPanel.add(groupsCombo);

        //
        // Forwardable
        forwardableChecbox = new CheckBox();
        forwardableChecbox.setBoxLabel("");

        forwardableChecboxGroup = new CheckBoxGroup();
        forwardableChecboxGroup.setFieldLabel(MSGS.dialogAddPermissionForwardable());
        forwardableChecboxGroup.add(forwardableChecbox);
        permissionFormPanel.add(forwardableChecboxGroup);

        //
        // Add form panel to body
        bodyPanel.add(permissionFormPanel);
    }

    @Override
    protected void onRender(Element parent, int pos) {
        super.onRender(parent, pos);
        submitButton.disable();
    }
}
