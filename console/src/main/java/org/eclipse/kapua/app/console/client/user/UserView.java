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
 *
 *******************************************************************************/
package org.eclipse.kapua.app.console.client.user;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.kapua.app.console.client.messages.ConsoleMessages;
import org.eclipse.kapua.app.console.client.resources.icons.IconSet;
import org.eclipse.kapua.app.console.client.resources.icons.KapuaIcon;
import org.eclipse.kapua.app.console.client.ui.button.AddButton;
import org.eclipse.kapua.app.console.client.ui.button.Button;
import org.eclipse.kapua.app.console.client.ui.button.DeleteButton;
import org.eclipse.kapua.app.console.client.ui.button.EditButton;
import org.eclipse.kapua.app.console.client.ui.button.RefreshButton;
import org.eclipse.kapua.app.console.client.util.ConsoleInfo;
import org.eclipse.kapua.app.console.client.util.FailureHandler;
import org.eclipse.kapua.app.console.client.util.KapuaLoadListener;
import org.eclipse.kapua.app.console.client.util.SwappableListStore;
import org.eclipse.kapua.app.console.client.widget.color.Color;
import org.eclipse.kapua.app.console.shared.model.GwtSession;
import org.eclipse.kapua.app.console.shared.model.GwtUser;
import org.eclipse.kapua.app.console.shared.model.GwtXSRFToken;
import org.eclipse.kapua.app.console.shared.model.account.GwtAccount;
import org.eclipse.kapua.app.console.shared.service.GwtSecurityTokenService;
import org.eclipse.kapua.app.console.shared.service.GwtSecurityTokenServiceAsync;
import org.eclipse.kapua.app.console.shared.service.GwtUserService;
import org.eclipse.kapua.app.console.shared.service.GwtUserServiceAsync;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.data.BaseListLoader;
import com.extjs.gxt.ui.client.data.ListLoadResult;
import com.extjs.gxt.ui.client.data.LoadEvent;
import com.extjs.gxt.ui.client.data.ModelKeyProvider;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.grid.CellEditor;
import com.extjs.gxt.ui.client.widget.grid.CheckColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.grid.GridSelectionModel;
import com.extjs.gxt.ui.client.widget.grid.GridView;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class UserView extends LayoutContainer {

    private static final ConsoleMessages MSGS = GWT.create(ConsoleMessages.class);

    private final GwtUserServiceAsync gwtUserService = GWT.create(GwtUserService.class);

    private GwtSession m_currentSession;
    private GwtAccount m_selectedAccount;

    private ColumnModel m_columnModel;
    private Grid<GwtUser> m_grid;
    BaseListLoader<ListLoadResult<GwtUser>> m_usersLoader;
    private Button m_newButton;
    private Button m_editButton;
    private Button m_refreshButton;
    private Button m_deleteButton;

    private boolean m_dirty;
    private boolean m_initialized;

    private final GwtSecurityTokenServiceAsync gwtXSRFService = GWT.create(GwtSecurityTokenService.class);

    public UserView(GwtSession currentSession) {
        m_currentSession = currentSession;

        m_dirty = true;
        m_initialized = false;
    }

    public void setAccount(GwtAccount selectedAccount) {
        m_dirty = true;
        m_selectedAccount = selectedAccount;
    }

    protected void onRender(Element parent, int index) {
        super.onRender(parent, index);

        //
        // Borderlayout that expands to the whole screen
        setLayout(new FitLayout());

        LayoutContainer mf = new LayoutContainer();
        mf.setLayout(new BorderLayout());

        //
        // Users Table
        BorderLayoutData centerData = new BorderLayoutData(LayoutRegion.CENTER);
        centerData.setMargins(new Margins(0, 0, 0, 0));
        centerData.setSplit(false);

        ContentPanel usersTablePanel = new ContentPanel();
        usersTablePanel.setBorders(true);
        usersTablePanel.setBodyBorder(false);
        usersTablePanel.setHeaderVisible(false);
        usersTablePanel.setScrollMode(Scroll.AUTO);
        usersTablePanel.setLayout(new FitLayout());
        usersTablePanel.setTopComponent(getUsersToolBar());

        usersTablePanel.add(getUserGrid());
        mf.add(usersTablePanel, centerData);

        add(mf);
        m_initialized = true;
    }

    private Grid<GwtUser> getUserGrid() {

        //
        // Columns
        ColumnConfig column = null;
        List<ColumnConfig> configs = new ArrayList<ColumnConfig>();

        column = new ColumnConfig("status", 50);
        column.setAlignment(HorizontalAlignment.CENTER);
        GridCellRenderer<GwtUser> setStatusIcon = new GridCellRenderer<GwtUser>() {

            public KapuaIcon render(GwtUser model, String property, ColumnData config, int rowIndex, int colIndex, ListStore<GwtUser> employeeList, Grid<GwtUser> grid) {
                KapuaIcon enabledIcon = new KapuaIcon(IconSet.USER);
                switch (GwtUser.GwtUserStatus.valueOf(model.getStatus())) {
                case ENABLED:
                    enabledIcon.setColor(Color.GREEN);
                    enabledIcon.setTitle(MSGS.enabled());
                    break;
                case DISABLED:
                    enabledIcon.setColor(Color.YELLOW);
                    enabledIcon.setTitle(MSGS.disabled());
                    break;
                }
                return enabledIcon;
            }
        };
        column.setRenderer(setStatusIcon);
        configs.add(column);

        column = new ColumnConfig("username", MSGS.userName(), 100);
        column.setAlignment(HorizontalAlignment.LEFT);
        configs.add(column);

        column = new ColumnConfig("email", MSGS.userEmail(), 100);
        column.setAlignment(HorizontalAlignment.LEFT);
        configs.add(column);

        column = new ColumnConfig("loginAttempts", MSGS.userLoginAttempts(), 100);
        column.setAlignment(HorizontalAlignment.CENTER);
        configs.add(column);

        column = new ColumnConfig("loginAttemptsResetOnFormatted", MSGS.userLoginAttemptsResetOn(), 130);
        column.setAlignment(HorizontalAlignment.CENTER);
        configs.add(column);

        column = new ColumnConfig("lockedOnFormatted", MSGS.userLockedOn(), 130);
        column.setAlignment(HorizontalAlignment.CENTER);
        configs.add(column);

        column = new ColumnConfig("unlockOnFormatted", MSGS.userUnlockOn(), 130);
        column.setAlignment(HorizontalAlignment.CENTER);
        configs.add(column);

        CheckColumnConfig checkColumn = new CheckColumnConfig("administrator", MSGS.userAdministrator(), 45);
        checkColumn.setAlignment(HorizontalAlignment.CENTER);
        CellEditor checkBoxEditor = new CellEditor(new CheckBox());
        checkBoxEditor.setEnabled(false);
        checkColumn.setEditor(checkBoxEditor);
        configs.add(checkColumn);

        column = new ColumnConfig("sortedAccountPermissions", MSGS.userPermissions(), 300);
        column.setAlignment(HorizontalAlignment.LEFT);
        configs.add(column);

        m_columnModel = new ColumnModel(configs);

        // rpc data proxy
        RpcProxy<ListLoadResult<GwtUser>> proxy = new RpcProxy<ListLoadResult<GwtUser>>() {

            @Override
            protected void load(Object loadConfig, AsyncCallback<ListLoadResult<GwtUser>> callback) {
                gwtUserService.findAll(m_selectedAccount.getId(), callback);
            }
        };

        // grid loader
        m_usersLoader = new BaseListLoader<ListLoadResult<GwtUser>>(proxy);
        SwappableListStore<GwtUser> store = new SwappableListStore<GwtUser>(m_usersLoader);
        store.setKeyProvider(new ModelKeyProvider<GwtUser>() {

            public String getKey(GwtUser gwtUser) {
                return String.valueOf(gwtUser.getId());
            }
        });

        //
        // Grid
        m_grid = new Grid<GwtUser>(store, m_columnModel);
        m_grid.setBorders(false);
        m_grid.setStateful(false);
        m_grid.setLoadMask(true);
        m_grid.setStripeRows(true);
        m_grid.getView().setAutoFill(true);
        m_grid.setAutoExpandColumn("permissions");
        GridView gridView = m_grid.getView();
        gridView.setEmptyText(MSGS.userTableNoUsers());

        m_usersLoader.addLoadListener(new DataLoadListener(m_grid));

        GridSelectionModel<GwtUser> selectionModel = new GridSelectionModel<GwtUser>();
        selectionModel.setSelectionMode(SelectionMode.SINGLE);
        m_grid.setSelectionModel(selectionModel);

        m_grid.getSelectionModel().addSelectionChangedListener(new SelectionChangedListener<GwtUser>() {

            @Override
            public void selectionChanged(SelectionChangedEvent<GwtUser> se) {
                if (se.getSelectedItem() != null) {
                    if (m_currentSession.hasUserUpdatePermission()) {
                        m_editButton.setEnabled(true);
                    }
                    if (m_currentSession.hasUserDeletePermission()) {
                        m_deleteButton.setEnabled(true);
                    }
                } else {
                    m_editButton.setEnabled(false);
                    m_deleteButton.setEnabled(false);
                }
            }
        });

        //
        // populate
        refresh();

        return m_grid;
    }

    private ToolBar getUsersToolBar() {
        ToolBar menuToolBar = new ToolBar();

        //
        // New User Button
        m_newButton = new AddButton(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                UserManageForm userManageForm = new UserManageForm(m_selectedAccount.getId());
                userManageForm.addListener(Events.Hide, new Listener<ComponentEvent>() {

                    public void handleEvent(ComponentEvent be) {
                        m_dirty = true;
                        refresh();
                    }
                });
                userManageForm.show();
            }

        });
        m_newButton.setEnabled(m_currentSession.hasUserCreatePermission());
        menuToolBar.add(m_newButton);
        menuToolBar.add(new SeparatorToolItem());

        //
        // Edit User Button
        m_editButton = new EditButton(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                if (m_grid != null) {
                    GwtUser gwtUser = m_grid.getSelectionModel().getSelectedItem();
                    if (gwtUser != null) {
                        UserManageForm userManageForm = new UserManageForm(m_selectedAccount.getId(),
                                m_grid.getSelectionModel().getSelectedItem(),
                                m_currentSession);
                        userManageForm.addListener(Events.Hide, new Listener<ComponentEvent>() {

                            public void handleEvent(ComponentEvent be) {
                                m_dirty = true;
                                refresh();
                            }
                        });
                        userManageForm.show();
                    }
                }
            }

        });
        m_editButton.setEnabled(false);
        menuToolBar.add(m_editButton);
        menuToolBar.add(new SeparatorToolItem());

        //
        // Refresh Button
        m_refreshButton = new RefreshButton(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                Button btn = (Button) ce.getButton();
                btn.setEnabled(false);

                updateUserGrid();

                btn.setEnabled(true);
            }
        });
        m_refreshButton.setEnabled(true);
        menuToolBar.add(m_refreshButton);
        menuToolBar.add(new SeparatorToolItem());

        //
        // Delete User Button
        m_deleteButton = new DeleteButton(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                final GwtUser gwtUser = m_grid.getSelectionModel().getSelectedItem();
                if (gwtUser != null) {

                    // ask for confirmation
                    MessageBox.confirm(MSGS.confirm(), MSGS.userDeleteConfirmation(gwtUser.getUsername()),
                            new Listener<MessageBoxEvent>() {

                                public void handleEvent(MessageBoxEvent ce) {

                                    // if confirmed, delete
                                    Dialog dialog = ce.getDialog();
                                    if (dialog.yesText.equals(ce.getButtonClicked().getText())) {

                                        // A user cannot delete itself
                                        if (m_currentSession.getGwtUser().getId() == m_grid.getSelectionModel().getSelectedItem().getId()) {
                                            ConsoleInfo.display(MSGS.error(), MSGS.userCannotDeleteItself());
                                        } else {
                                            gwtXSRFService.generateSecurityToken(new AsyncCallback<GwtXSRFToken>() {

                                                @Override
                                                public void onFailure(Throwable ex) {
                                                    FailureHandler.handle(ex);
                                                }

                                                @Override
                                                public void onSuccess(GwtXSRFToken token) {
                                                    gwtUserService.delete(token,
                                                            m_grid.getSelectionModel().getSelectedItem().getScopeId(),
                                                            gwtUser,
                                                            new AsyncCallback<Void>() {

                                                                public void onFailure(Throwable caught) {
                                                                    FailureHandler.handle(caught);
                                                                }

                                                                public void onSuccess(Void arg) {
                                                                    ConsoleInfo.display(MSGS.info(),
                                                                            MSGS.userDeletedConfirmation(gwtUser.getUnescapedUsername()));
                                                                    m_dirty = true;
                                                                    refresh();
                                                                }
                                                            });
                                                }
                                            });

                                        }
                                    }
                                }
                            });
                }
            }
        });
        m_deleteButton.setEnabled(false);
        menuToolBar.add(m_deleteButton);

        return menuToolBar;
    }

    public void refresh() {
        if (m_initialized && m_dirty && m_selectedAccount != null) {
            updateUserGrid();
            m_dirty = false;
        }
    }

    private void updateUserGrid() {
        m_usersLoader.load();
    }

    // --------------------------------------------------------------------------------------
    //
    // Data Load Listener
    //
    // --------------------------------------------------------------------------------------

    private class DataLoadListener extends KapuaLoadListener {

        private Grid<GwtUser> m_usersGrid;
        private GwtUser m_selectedUser;

        public DataLoadListener(Grid<GwtUser> rulesGrid) {
            m_usersGrid = rulesGrid;
            m_selectedUser = null;
        }

        public void loaderBeforeLoad(LoadEvent le) {
            m_selectedUser = m_usersGrid.getSelectionModel().getSelectedItem();
        }

        public void loaderLoad(LoadEvent le) {
            if (le.exception != null) {
                FailureHandler.handle(le.exception);
            }

            ListStore<GwtUser> store = m_usersGrid.getStore();
            if (m_selectedUser != null) {
                GwtUser modelUser = store.findModel(String.valueOf(m_selectedUser.getId()));
                if (modelUser != null) {
                    m_usersGrid.getSelectionModel().select(modelUser, false);
                    m_usersGrid.getView().focusRow(store.indexOf(modelUser));
                    return;
                }
            }

            if (m_usersGrid.getSelectionModel().getSelectedItem() == null) {
                if (store.getCount() > 0) {
                    m_usersGrid.getSelectionModel().select(0, false);
                    m_usersGrid.getView().focusRow(0);
                }
            }
        }
    }
}
