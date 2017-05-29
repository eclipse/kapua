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
package org.eclipse.kapua.app.console.client.user;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.kapua.app.console.client.user.dialog.UserForm;
import org.eclipse.kapua.app.console.client.util.ConsoleInfo;
import org.eclipse.kapua.app.console.client.util.Constants;
import org.eclipse.kapua.app.console.client.util.FailureHandler;
import org.eclipse.kapua.app.console.shared.model.GwtSession;
import org.eclipse.kapua.app.console.shared.model.GwtXSRFToken;
import org.eclipse.kapua.app.console.shared.model.user.GwtUser;
import org.eclipse.kapua.app.console.shared.model.user.GwtUserCreator;
import org.eclipse.kapua.app.console.shared.model.user.GwtUserPermission;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.data.BaseListLoader;
import com.extjs.gxt.ui.client.data.ListLoadResult;
import com.extjs.gxt.ui.client.data.ListLoader;
import com.extjs.gxt.ui.client.data.MemoryProxy;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.form.LabelField;
import com.extjs.gxt.ui.client.widget.grid.CheckBoxSelectionModel;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.EditorGrid;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class UserManageForm extends UserForm {

    private EditorGrid<GwtUserPermission> permisssionGrid;
    private ColumnModel columnModel;

    private TabItem tabUserPermission;
    private FieldSet permissionsFieldSet;

    public UserManageForm(String accountId) {
        super(accountId);
        setHeading(MSGS.userFormNew());
    }

    public UserManageForm(String accountId, GwtUser existingUser, GwtSession session) {
        this(accountId);

        this.existingUser = existingUser;
        if (this.existingUser != null) {
            setHeading(MSGS.userFormUpdate(this.existingUser.getUsername()));
        }
        this.currentSession = session;
    }

    protected void onRender(Element parent, int index) {
        super.onRender(parent, index);
        //
        // Permission tab
        //
        FormLayout permissionsLayout = new FormLayout();
        permissionsLayout.setLabelWidth(Constants.LABEL_WIDTH_FORM);

        permissionsFieldSet = new FieldSet();
        permissionsFieldSet.setBorders(false);
        permissionsFieldSet.setLayout(permissionsLayout);

        // permissions table columns
        List<ColumnConfig> configs = new ArrayList<ColumnConfig>();

        final CheckBoxSelectionModel<GwtUserPermission> sm = new CheckBoxSelectionModel<GwtUserPermission>();
        configs.add(sm.getColumn());

        ColumnConfig column = new ColumnConfig("accountPermission", MSGS.userPermissionsPermissionName(), 270);
        column.setAlignment(HorizontalAlignment.LEFT);
        configs.add(column);

        // Permissions table
        columnModel = new ColumnModel(configs);

        //
        // Initial empty values
        List<GwtUserPermission> gwtUserPermissions = GwtUserPermission.getAllPermissions(accountId);
        MemoryProxy<List<GwtUserPermission>> proxy = new MemoryProxy<List<GwtUserPermission>>(gwtUserPermissions);
        ListLoader<ListLoadResult<ModelData>> loader = new BaseListLoader<ListLoadResult<ModelData>>(proxy);
        ListStore<GwtUserPermission> permissions = new ListStore<GwtUserPermission>(loader);
        loader.load();

        // Grid
        permisssionGrid = new EditorGrid<GwtUserPermission>(permissions, columnModel);
        permisssionGrid.setBorders(true);
        permisssionGrid.setStripeRows(true);
        permisssionGrid.getView().setAutoFill(true);
        permisssionGrid.setWidth(440);
        permisssionGrid.setHeight(374);
        permisssionGrid.setAutoExpandColumn("accountPermission");
        permisssionGrid.setSelectionModel(sm);
        permisssionGrid.addPlugin(sm);

        LabelField permissionsField = new LabelField();
        permissionsField.setFieldLabel(MSGS.userFormPermissions());
        permissionsFieldSet.add(permissionsField, formData);
        permissionsFieldSet.add(permisssionGrid, formData);

        tabUserPermission = new TabItem(MSGS.userFormAccess());
        tabUserPermission.setBorders(false);
        tabUserPermission.setStyleAttribute("background-color", "#E8E8E8");
        tabUserPermission.setScrollMode(Scroll.AUTOY);
        tabUserPermission.add(permissionsFieldSet);
        tabUserPermission.setLayout(new FitLayout());

        tabsPanel.add(tabUserPermission);

        add(formPanel);
    }

    protected void setEditability() {
        // nothing to do
    }

    protected void loadUser() {
        // populate if necessary
        if (existingUser != null) {
            gwtUserService.find(accountId, existingUser.getId(), new AsyncCallback<GwtUser>() {

                public void onFailure(Throwable caught) {
                    FailureHandler.handle(caught);
                }

                public void onSuccess(GwtUser gwtUser) {
                    populateTabUserInfo(gwtUser);
                }
            });
        } else {
            // New user. No need to show the status field set
            statusFieldSet.setVisible(false);
        }
    }

    private String getEnabledUserPermissions() {
        StringBuilder sb = new StringBuilder();

        CheckBoxSelectionModel<GwtUserPermission> sm = null;
        sm = (CheckBoxSelectionModel<GwtUserPermission>) permisssionGrid.getSelectionModel();
        List<GwtUserPermission> selectedPermissions = sm.getSelectedItems();
        for (GwtUserPermission p : selectedPermissions) {
            if (sb.length() != 0) {
                sb.append(",");
            }
            sb.append(p.getPermission());
        }

        if (sb.length() == 0) {
            return null;
        }
        return sb.toString();
    }

    protected void submitAccount() {
        // New account
        if (existingUser == null) {
            final GwtUserCreator gwtUserCreator = new GwtUserCreator();
            gwtUserCreator.setScopeId(accountId);
            gwtUserCreator.setUsername(username.getValue());
            gwtUserCreator.setPassword(password.getValue());
            gwtUserCreator.setDisplayName(displayName.getValue());
            gwtUserCreator.setEmail(email.getValue());
            gwtUserCreator.setPhoneNumber(phoneNumber.getValue());

            gwtUserCreator.setPermissions(getEnabledUserPermissions());

            gwtXSRFService.generateSecurityToken(new AsyncCallback<GwtXSRFToken>() {

                @Override
                public void onFailure(Throwable ex) {
                    FailureHandler.handle(ex);
                }

                @Override
                public void onSuccess(GwtXSRFToken token) {
                    gwtUserService.create(token, gwtUserCreator, new AsyncCallback<GwtUser>() {

                        public void onFailure(Throwable caught) {
                            FailureHandler.handleFormException(formPanel, caught);
                            status.hide();
                            formPanel.getButtonBar().enable();
                        }

                        public void onSuccess(GwtUser user) {
                            ConsoleInfo.display(MSGS.info(), MSGS.userCreatedConfirmation(user.getUnescapedUsername()));
                            hide();
                        }
                    });
                }
            });
        } else {
            super.submitAccount();
        }
    }

    protected void updateCall(GwtXSRFToken token) {
        gwtUserService.update(token, existingUser, new AsyncCallback<GwtUser>() {

            public void onFailure(Throwable caught) {
                FailureHandler.handleFormException(formPanel, caught);
                status.hide();
                formPanel.getButtonBar().enable();
            }

            public void onSuccess(GwtUser user) {
                ConsoleInfo.display(MSGS.info(), MSGS.userUpdatedConfirmation(user.getUnescapedUsername()));
                hide();
            }
        });
    }

    @Override
    protected void populateTabUserInfo(GwtUser gwtUser) {
        super.populateTabUserInfo(gwtUser);
    }
}
