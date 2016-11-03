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
package org.eclipse.kapua.app.console.client;

import org.eclipse.kapua.app.console.client.messages.ConsoleMessages;
import org.eclipse.kapua.app.console.client.resources.Resources;
import org.eclipse.kapua.app.console.client.util.ConsoleInfo;
import org.eclipse.kapua.app.console.client.util.FailureHandler;
import org.eclipse.kapua.app.console.client.util.UserAgentUtils;
import org.eclipse.kapua.app.console.shared.model.GwtSession;
import org.eclipse.kapua.app.console.shared.model.GwtUser;
import org.eclipse.kapua.app.console.shared.model.account.GwtAccount;
import org.eclipse.kapua.app.console.shared.model.account.GwtAccountStringListItem;
import org.eclipse.kapua.app.console.shared.service.GwtAccountService;
import org.eclipse.kapua.app.console.shared.service.GwtAccountServiceAsync;
import org.eclipse.kapua.app.console.shared.service.GwtAuthorizationService;
import org.eclipse.kapua.app.console.shared.service.GwtAuthorizationServiceAsync;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.data.ListLoadResult;
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
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class NorthView extends LayoutContainer {

    private static final ConsoleMessages MSGS = GWT.create(ConsoleMessages.class);
    private final GwtAuthorizationServiceAsync gwtAuthorizationService = GWT.create(GwtAuthorizationService.class);
    private final GwtAccountServiceAsync gwtAccountService = GWT.create(GwtAccountService.class);

    // UI stuff
    private KapuaCloudConsole parent;
    private Menu subAccountMenu;
    private Button userActionButton;

    // Data
    private GwtSession currentSession;
    private GwtAccount rootAccount;
    private GwtUser user;

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
        this.rootAccount = currentSession.getRootAccount();
        this.user = currentSession.getGwtUser();
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
        //
        // Switch to sub-account menu item (added only if this user has 'account:view' permission and if this account has children
        MenuItem switchToAccountMenuItem = null;
        if (currentSession.hasAccountReadPermission()) {
            switchToAccountMenuItem = createAccountNavigationMenuItem();
        }

        //
        // Logout menu item
        MenuItem userLogoutMenuItem = new MenuItem();
        userLogoutMenuItem.setText(MSGS.consoleHeaderUserActionLogout());
        userLogoutMenuItem.setIcon(AbstractImagePrototype.create(Resources.INSTANCE.userLogout16()));
        userLogoutMenuItem.addSelectionListener(new SelectionListener<MenuEvent>() {

            @Override
            public void componentSelected(MenuEvent ce) {
                gwtAuthorizationService.logout(new AsyncCallback<Void>() {

                    public void onFailure(Throwable caught) {
                        FailureHandler.handle(caught);
                    }

                    public void onSuccess(Void arg0) {
                        ConsoleInfo.display("Info", "Logged out!");

                        Window.Location.reload();
                    }

                });
            }

        });

        //
        // Adding User Actions to a common menu
        Menu userActionMenu = new Menu();
        userActionMenu.setAutoWidth(true);

        if (switchToAccountMenuItem != null) {
            userActionMenu.add(switchToAccountMenuItem);
            userActionMenu.add(new SeparatorMenuItem());
        }

        userActionMenu.add(new SeparatorMenuItem());
        userActionMenu.add(userLogoutMenuItem);

        //
        // User Action Button
        userActionButton = new Button();
        userActionButton.setMenu(userActionMenu);
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
        MenuItem rootAccountMenuItem = new MenuItem();
        rootAccountMenuItem.setIcon(AbstractImagePrototype.create(Resources.INSTANCE.administrator()));
        rootAccountMenuItem.setText(MSGS.accountSelectorItemYourAccount(rootAccount.getName()));
        rootAccountMenuItem.setToolTip(MSGS.accountSelectorTooltipYourAccount());
        rootAccountMenuItem.setId(rootAccount.getId().toString());
        rootAccountMenuItem.addSelectionListener(switchToAccountListener);

        subAccountMenu = new Menu();
        subAccountMenu.setAutoWidth(true);
        subAccountMenu.add(rootAccountMenuItem);
        subAccountMenu.add(new SeparatorMenuItem());

        populateNavigatorMenu(subAccountMenu, rootAccount.getId());

        MenuItem switchToAccountMenuItem = new MenuItem();
        switchToAccountMenuItem.setText(MSGS.consoleHeaderUserActionSwitchToAccount());
        switchToAccountMenuItem.setIcon(AbstractImagePrototype.create(Resources.INSTANCE.switchToAccount16()));
        switchToAccountMenuItem.setSubMenu(subAccountMenu);

        return switchToAccountMenuItem;
    }

    /**
     * Populates a Menu objects with a entry for each child account for the given account.
     * It does this recursively for each child.
     * 
     * @param menu
     *            the menu to fill
     * @param account
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
                                MenuItem childAccountMenuItem = new MenuItem();
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
            public void onSuccess(GwtAccount result) {
                if (result != null) {
                    currentSession.setSelectedAccount(result);
                }

                // Update userActionButtonLabel with the current data
                updateUserActionButtonLabel();

                // Force the west view (which contains the navigation menu) to reload available components
                parent.getWestView().addMenuItems();
                parent.getWestView().setDashboardSelected(false);

                // Unmask the whole page
                parent.getViewport().unmask();
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
        String accountName = currentSession.getSelectedAccount().getName();
        String username = user.getUsername();
        String displayName = user.getDisplayName();

        // Set label {displayName || username} @ {selectedAccountName}
        if (displayName == null ||
                displayName.isEmpty()) {
            displayName = username;
        }
        userActionButton.setText(MSGS.consoleHeaderUserActionButtonLabel(displayName, accountName));
    }
}
