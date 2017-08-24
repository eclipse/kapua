/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.console.core.client;

import org.eclipse.kapua.app.console.module.api.client.messages.ConsoleMessages;
import org.eclipse.kapua.app.console.module.api.client.ui.view.descriptor.MainViewDescriptor;
import org.eclipse.kapua.app.console.module.api.shared.service.GwtConsoleService;
import org.eclipse.kapua.app.console.module.api.shared.service.GwtConsoleServiceAsync;
import org.eclipse.kapua.app.console.core.shared.service.GwtAuthorizationService;
import org.eclipse.kapua.app.console.module.api.client.resources.icons.IconSet;
import org.eclipse.kapua.app.console.module.api.client.ui.widget.KapuaMenuItem;
import org.eclipse.kapua.app.console.module.api.client.util.ConsoleInfo;
import org.eclipse.kapua.app.console.module.api.client.util.FailureHandler;
import org.eclipse.kapua.app.console.core.client.util.Logout;
import org.eclipse.kapua.app.console.module.api.client.util.UserAgentUtils;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtSession;
import org.eclipse.kapua.app.console.module.account.shared.model.GwtAccount;
import org.eclipse.kapua.app.console.module.account.shared.model.GwtAccountStringListItem;
import org.eclipse.kapua.app.console.module.account.shared.service.GwtAccountService;
import org.eclipse.kapua.app.console.module.account.shared.service.GwtAccountServiceAsync;
import org.eclipse.kapua.app.console.core.shared.service.GwtAuthorizationServiceAsync;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.data.ListLoadResult;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MenuEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.layout.TableData;
import com.extjs.gxt.ui.client.widget.layout.TableLayout;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.extjs.gxt.ui.client.widget.menu.SeparatorMenuItem;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

import java.util.List;

public class NorthView extends LayoutContainer {

    private static final ConsoleMessages MSGS = GWT.create(ConsoleMessages.class);
    private final GwtAuthorizationServiceAsync gwtAuthorizationService = GWT.create(GwtAuthorizationService.class);
    private final GwtAccountServiceAsync gwtAccountService = GWT.create(GwtAccountService.class);
    private final GwtConsoleServiceAsync gwtConsoleService = GWT.create(GwtConsoleService.class);

    // UI stuff
    private KapuaCloudConsole parent;
    private Menu subAccountMenu;
    private Button userActionButton;

    // Data
    private GwtSession currentSession;
    private String rootAccountId;
    private String userId;

    private String username;
    private String userDisplayName;
    private String rootAccountName;
    private String selectedAccountName;
    // Listener
    private final SelectionListener<MenuEvent> switchToAccountListener = new SelectionListener<MenuEvent>() {

        @Override
        public void componentSelected(MenuEvent ce) {
            switchToAccount(ce);
        }
    };

    public NorthView(GwtSession currentSession,
            KapuaCloudConsole parent) {
        this.parent = parent;

        this.currentSession = currentSession;
        this.rootAccountId = currentSession.getRootAccountId();
        this.userId = currentSession.getUserId();

        this.username = currentSession.getUserName();
        this.userDisplayName = currentSession.getUserDisplayName();
        this.rootAccountName = currentSession.getRootAccountName();
        this.selectedAccountName = currentSession.getSelectedAccountName();
    }

    protected void onRender(Element parent, int index) {
        super.onRender(parent, index);

        TableLayout layout = new TableLayout(2);
        layout.setWidth("100%");
        layout.setTableStyle("background-color: transparent");

        ContentPanel panel = new ContentPanel();
        panel.setHeaderVisible(false);
        panel.setBorders(false);
        panel.setBodyBorder(false);
        panel.setId("header-panel");
        panel.setBodyStyle("background-color: transparent");
        panel.setLayout(layout);

        // Logo
        panel.add(getKapuaHeader(),
                new TableData(Style.HorizontalAlignment.LEFT,
                        Style.VerticalAlignment.MIDDLE));

        // User Action Menu
        panel.add(getUserActionMenu(),
                new TableData(Style.HorizontalAlignment.RIGHT,
                        Style.VerticalAlignment.MIDDLE));

        add(panel);
    }

    private Widget getKapuaHeader() {
        SimplePanel logo = new SimplePanel();

        if (!UserAgentUtils.isIE() ||
                UserAgentUtils.getIEDocumentMode() > 8) {
            logo.setStyleName("headerLogo");
        } else {
            logo.setStyleName("headerLogo-ie8");
        }

        return logo;
    }

    private Widget getUserActionMenu() {

        userActionButton = new Button();

        userActionButton.addListener(Events.OnMouseOver, new Listener<BaseEvent>() {

            @Override
            public void handleEvent(BaseEvent be) {
                final Menu userActionMenu = new Menu();
                MenuItem switchToAccountMenuItem = null;
                if (currentSession.hasAccountReadPermission()) {
                    switchToAccountMenuItem = createAccountNavigationMenuItem();
                }

                //
                // Logout menu item
                KapuaMenuItem userLogoutMenuItem = new KapuaMenuItem();
                userLogoutMenuItem.setText(MSGS.consoleHeaderUserActionLogout());
                userLogoutMenuItem.setIcon(IconSet.SIGN_OUT);
                userLogoutMenuItem.addSelectionListener(new SelectionListener<MenuEvent>() {

                    @Override
                    public void componentSelected(MenuEvent ce) {
                        gwtAuthorizationService.logout(new AsyncCallback<Void>() {

                            public void onFailure(Throwable caught) {
                                FailureHandler.handle(caught);
                            }

                            public void onSuccess(Void arg0) {
                                ConsoleInfo.display("Info", "Logged out!");
                                Logout.logout();
                            }

                        });
                    }

                });
                if (switchToAccountMenuItem != null) {
                    userActionMenu.add(switchToAccountMenuItem);
                    userActionMenu.add(new SeparatorMenuItem());
                }

                KapuaMenuItem changePassword = new KapuaMenuItem();
                changePassword.setText(MSGS.changePassword());
                changePassword.setIcon(IconSet.KEY);
                changePassword.addSelectionListener(new SelectionListener<MenuEvent>() {

                    @Override
                    public void componentSelected(MenuEvent ce) {
                        ChangePasswordDialog changePasswordDialog = new ChangePasswordDialog(currentSession);
                        changePasswordDialog.show();
                    }

                });
                userActionMenu.add(changePassword);
                userActionMenu.add(new SeparatorMenuItem());
                userActionMenu.add(userLogoutMenuItem);
                userActionButton.setMenu(userActionMenu);

            }
        });
        userActionButton.setId("header-button");
        userActionButton.addStyleName("x-btn-arrow-custom");

        updateUserActionButtonLabel();

        return userActionButton;
    }

    /**
     * Creates an MenuItem with the list of all child accounts of the account of the current logged user,
     * and also add as first sub-MenuItem the name of the root account.
     * 
     * @return the MenuItem
     */
    public MenuItem createAccountNavigationMenuItem() {
        KapuaMenuItem rootAccountMenuItem = new KapuaMenuItem();
        rootAccountMenuItem.setText(MSGS.accountSelectorItemYourAccount(rootAccountName));
        rootAccountMenuItem.setToolTip(MSGS.accountSelectorTooltipYourAccount());
        rootAccountMenuItem.setIcon(IconSet.USER_MD);
        rootAccountMenuItem.setId(rootAccountId);
        rootAccountMenuItem.addSelectionListener(switchToAccountListener);

        subAccountMenu = new Menu();
        subAccountMenu.setAutoWidth(true);
        subAccountMenu.add(rootAccountMenuItem);
        subAccountMenu.add(new SeparatorMenuItem());

        populateNavigatorMenu(subAccountMenu, rootAccountId);

        KapuaMenuItem switchToAccountMenuItem = new KapuaMenuItem();
        switchToAccountMenuItem.setText(MSGS.consoleHeaderUserActionSwitchToAccount());
        switchToAccountMenuItem.setIcon(IconSet.USERS);
        switchToAccountMenuItem.setSubMenu(subAccountMenu);

        return switchToAccountMenuItem;
    }

    /**
     * Populates a Menu objects with a entry for each child account for the given account.
     * It does this recursively for each child.
     * 
     * @param menu
     *            the menu to fill
     * @param accountId
     *            the account of the current menu item.
     */
    private void populateNavigatorMenu(final Menu menu, final String accountId) {
        gwtAccountService.findChildrenAsStrings(accountId,
                false,
                new AsyncCallback<ListLoadResult<GwtAccountStringListItem>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        FailureHandler.handle(caught);
                    }

                    @Override
                    public void onSuccess(ListLoadResult<GwtAccountStringListItem> result) {
                        // If no children are found, add "No Child" label
                        if (result.getData() != null &&
                                result.getData().size() == 0) {
                            MenuItem noChildMenuItem = new MenuItem(MSGS.accountSelectorItemNoChild());
                            noChildMenuItem.disable();
                            menu.add(noChildMenuItem);
                        } else {
                            // For each child found create a item menu and search for its children
                            for (GwtAccountStringListItem item : result.getData()) {
                                // Add item menu entry
                                KapuaMenuItem childAccountMenuItem = new KapuaMenuItem();
                                childAccountMenuItem.setIcon(IconSet.USER);
                                childAccountMenuItem.setText(item.getValue());
                                childAccountMenuItem.setTitle(item.getValue());
                                childAccountMenuItem.setId(String.valueOf(item.getId()));
                                menu.add(childAccountMenuItem);

                                // Add selection listener to make the switch happen when selected
                                childAccountMenuItem.addSelectionListener(switchToAccountListener);

                                // Search for its children
                                if (item.hasChildAccount()) {
                                    Menu childMenu = new Menu();
                                    childMenu.setAutoWidth(true);
                                    childAccountMenuItem.setSubMenu(childMenu);

                                    populateNavigatorMenu(childMenu, item.getId());
                                }
                            }
                        }
                    }
                });
    }

    /**
     * Make the switch between accounts when an account is selected from
     * the account navigator.
     * 
     * @param ce
     *            The MenuEvent that has been fired with the selection of a menu entry.
     */
    private void switchToAccount(MenuEvent ce) {
        String accountName = ce.getItem().getTitle();
        String accountIdString = ce.getItem().getId();

        // Mask the whole page
        parent.getViewport().mask(MSGS.accountSelectorMenuMaskLoading(accountName));

        gwtAccountService.find(accountIdString, new AsyncCallback<GwtAccount>() {

            @Override
            public void onFailure(Throwable caught) {
                parent.getViewport().unmask();
                FailureHandler.handle(caught);
            }

            @Override
            public void onSuccess(final GwtAccount result) {
                gwtConsoleService.getCustomEntityViews(new AsyncCallback<List<MainViewDescriptor>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        parent.getViewport().unmask();
                        FailureHandler.handle(caught);
                    }

                    @Override
                    public void onSuccess(List<MainViewDescriptor> viewDescriptors) {
                        if (result != null) {
                            currentSession.setSelectedAccountId(result.getId());
                            currentSession.setSelectedAccountName(result.getName());
                        }

                        // Update userActionButtonLabel with the current data
                        updateUserActionButtonLabel();

                        // Force the west view (which contains the navigation menu) to reload available components
                        parent.getWestView().addMenuItems(viewDescriptors);
                        parent.getWestView().setDashboardSelected(false);
                        parent.getWestView().layout(true);

                        // Unmask the whole page
                        parent.getViewport().unmask();
                    }
                });
            }
        });
    }

    /**
     * Updates the UserActionButton label with the current selected informations for username and target account.<br/>
     * The label format is:<br/>
     * 
     * If the display name of the user is defined:<br/>
     * {displayName} @ {selectedAccountName}<br/>
     * else:<br/>
     * {username} @ {selectedAccountName}<br/>
     */
    private void updateUserActionButtonLabel() {
        // Current selected scope
        String accountName = currentSession.getSelectedAccountName();

        // Set label {displayName || username} @ {selectedAccountName}
        if (userDisplayName == null ||
                userDisplayName.isEmpty()) {
            userDisplayName = username;
        }
        userActionButton.setText(MSGS.consoleHeaderUserActionButtonLabel(userDisplayName, accountName));
    }
}
