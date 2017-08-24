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

import java.util.logging.Logger;

import org.eclipse.kapua.app.console.module.api.client.messages.ConsoleMessages;
import org.eclipse.kapua.app.console.module.api.client.util.ConsoleInfo;
import org.eclipse.kapua.app.console.core.client.util.Logout;
import org.eclipse.kapua.app.console.module.api.client.util.UserAgentUtils;
import org.eclipse.kapua.app.console.module.api.client.util.Years;
import org.eclipse.kapua.app.console.core.shared.model.GwtLoginInformation;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtSession;
import org.eclipse.kapua.app.console.core.shared.model.authentication.GwtJwtCredential;
import org.eclipse.kapua.app.console.core.shared.service.GwtAuthorizationService;
import org.eclipse.kapua.app.console.core.shared.service.GwtAuthorizationServiceAsync;
import org.eclipse.kapua.app.console.core.shared.service.GwtSettingsService;
import org.eclipse.kapua.app.console.core.shared.service.GwtSettingsServiceAsync;

import com.allen_sauer.gwt.log.client.Log;
import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.Html;
import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.Viewport;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FillLayout;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.TableData;
import com.extjs.gxt.ui.client.widget.layout.TableLayout;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;

/**
 * 
 * Entry point classes define <code>onModuleLoad()</code>.
 * 
 */
public class KapuaCloudConsole implements EntryPoint {

    private static final ConsoleMessages MSGS = GWT.create(ConsoleMessages.class);
    private static final Logger logger = Logger.getLogger(KapuaCloudConsole.class.getName());

    private GwtAuthorizationServiceAsync gwtAuthorizationService = GWT.create(GwtAuthorizationService.class);

    private GwtSettingsServiceAsync gwtSettingService = GWT.create(GwtSettingsService.class);

    private GwtSession currentSession;

    private Viewport viewport;

    private NorthView northView;
    private WestNavigationView westView;
    private LayoutContainer centerView;
    private HorizontalPanel southView;

    private Label creditLabel;

    /**
     * Note, we defer all application initialization code to {@link #onModuleLoad2()} so that the
     * UncaughtExceptionHandler can catch any unexpected exceptions.
     */
    public void onModuleLoad() {
        /*
         * Install an UncaughtExceptionHandler which will produce <code>FATAL</code> log messages
         */
        Log.setUncaughtExceptionHandler();

        // Use deferred command to catch initialization exceptions in onModuleLoad2
        Scheduler.get().scheduleDeferred(new ScheduledCommand() {

            public void execute() {
                onModuleLoad2();
            }
        });
    }

    /**
     * This is the 'real' entry point method.
     */
    public void onModuleLoad2() {
        //
        // Check if a session has already been established on the server-side
        gwtAuthorizationService.getCurrentSession(new AsyncCallback<GwtSession>() {

            public void onFailure(Throwable t) {
                // We do not have a valid session: display the login page
                renderLoginDialog();
            }

            public void onSuccess(GwtSession gwtSession) {
                if (gwtSession == null) {
                    // We do not have a valid session: display the login page
                    renderLoginDialog();
                } else {
                    //
                    // We have a valid session
                    currentSession = gwtSession;

                    render(currentSession);
                }
            }
        });
    }

    private void render(GwtSession gwtSession) {
        final BorderLayout borderLayout = new BorderLayout();

        viewport = new Viewport();
        viewport.setLayout(borderLayout);

        // Set class name based on account. This allows for styling based on account
        RootPanel.getBodyElement().addClassName(gwtSession.getSelectedAccountName());

        //
        // North View
        BorderLayoutData northData = new BorderLayoutData(LayoutRegion.NORTH, 36);
        northData.setCollapsible(false);
        northData.setFloatable(false);
        northData.setHideCollapseTool(false);
        northData.setSplit(false);
        northData.setMargins(new Margins(0, 0, 5, 0));

        northView = new NorthView(currentSession, this);

        viewport.add(northView, northData);

        //
        // Center View
        BorderLayoutData centerData = new BorderLayoutData(LayoutRegion.CENTER);
        centerData.setMargins(new Margins(0, 5, 0, 0));

        centerView = new LayoutContainer();
        centerView.setLayout(new FitLayout());
        centerView.setBorders(false);

        viewport.add(centerView, centerData);

        //
        // West View
        BorderLayoutData westData = new BorderLayoutData(LayoutRegion.WEST, 200);
        westData.setSplit(false);
        westData.setMargins(new Margins(0, 5, 0, 5));

        westView = new WestNavigationView(currentSession, centerView);

        viewport.add(westView, westData);

        //
        // South view
        BorderLayoutData southData = new BorderLayoutData(LayoutRegion.SOUTH, 18);
        southData.setCollapsible(false);
        southData.setFloatable(false);
        southData.setHideCollapseTool(false);
        southData.setSplit(false);
        southData.setMargins(new Margins(5, 5, 5, 5));

        southView = new HorizontalPanel();
        southView.setTableWidth("100%");

        TableData td = new TableData();
        td.setHorizontalAlign(HorizontalAlignment.LEFT);
        TableData tdVersion = new TableData();
        tdVersion.setHorizontalAlign(HorizontalAlignment.RIGHT);

        Label copyright = new Label(MSGS.copyright(Integer.toString(Years.getCurrentYear())));
        copyright.setStyleName("x-form-label");

        Label version = new Label(currentSession.getVersion() + "-" + currentSession.getBuildNumber());
        version.setStyleName("x-form-label");
        version.setToolTip(currentSession.getBuildVersion());

        southView.add(copyright, td);
        southView.add(version, tdVersion);

        viewport.add(southView, southData);

        //
        // RootPanel
        RootPanel.get().add(viewport);
    }

    private void renderLoginDialog() {
        final Viewport viewport = new Viewport();

        final BorderLayout borderLayout = new BorderLayout();
        viewport.setLayout(borderLayout);
        if (!UserAgentUtils.isIE() || UserAgentUtils.getIEDocumentMode() > 8) {
            viewport.setStyleName("login");
        } else {
            viewport.setStyleName("login-ie8");
        }

        //
        // center
        BorderLayoutData centerData = new BorderLayoutData(LayoutRegion.CENTER);
        centerData.setMargins(new Margins(0));
        centerData.setCollapsible(false);
        centerData.setFloatable(false);
        centerData.setHideCollapseTool(false);
        centerData.setSplit(false);
        LayoutContainer splash = new LayoutContainer(new FillLayout());
        viewport.add(splash, centerData);

        //
        // Header login page
        SimplePanel kapuaLogo = new SimplePanel();
        if (!UserAgentUtils.isIE() || UserAgentUtils.getIEDocumentMode() > 8) {
            kapuaLogo.setStyleName("kapuaLogo");
        } else {
            kapuaLogo.setStyleName("kapuaLogo-ie8");
        }

        TableLayout layout = new TableLayout(1);
        layout.setWidth("100%");

        LayoutContainer lcHeader = new LayoutContainer(layout);
        lcHeader.add(kapuaLogo, new TableData(Style.HorizontalAlignment.LEFT, Style.VerticalAlignment.BOTTOM));
        if (!UserAgentUtils.isIE() || UserAgentUtils.getIEDocumentMode() > 8) {
            lcHeader.setStyleName("loginHeader");
        } else {
            lcHeader.setStyleName("loginHeader-ie8");
        }

        BorderLayoutData northData = new BorderLayoutData(LayoutRegion.NORTH, 72);
        northData.setCollapsible(false);
        northData.setFloatable(false);
        northData.setHideCollapseTool(false);
        northData.setSplit(false);
        northData.setMargins(new Margins(0));
        viewport.add(lcHeader, northData);

        //
        // Footer login page
        creditLabel = new Label();
        creditLabel.setStyleName("margin-right:10px");

        layout = new TableLayout(2);
        layout.setWidth("100%");
        LayoutContainer lcFooter = new LayoutContainer(layout);
        if (!UserAgentUtils.isIE() || UserAgentUtils.getIEDocumentMode() > 8) {
            lcFooter.setStyleName("loginFooter");
        } else {
            lcFooter.setStyleName("loginFooter-ie8");
        }

        final Html genericNote = new Html();
        genericNote.setId("login-generic-note");

        lcFooter.add(genericNote, new TableData(Style.HorizontalAlignment.LEFT, Style.VerticalAlignment.BOTTOM));
        lcFooter.add(creditLabel, new TableData(Style.HorizontalAlignment.RIGHT, Style.VerticalAlignment.BOTTOM));

        BorderLayoutData southData = new BorderLayoutData(LayoutRegion.SOUTH, 18);
        southData.setCollapsible(false);
        southData.setFloatable(false);
        southData.setHideCollapseTool(false);
        southData.setSplit(false);
        southData.setMargins(new Margins(0));
        viewport.add(lcFooter, southData);

        RootPanel.get().add(viewport);

        loadFooterData(lcFooter, genericNote, creditLabel);

        // Check if coming from SSO login
        final String accessToken = Window.Location.getParameter(Logout.PARAMETER_ACCESS_TOKEN);

        if (accessToken != null && !accessToken.isEmpty()) {
            logger.info("Performing SSO login");
            performSsoLogin(viewport, accessToken);
        } else {
            showLoginDialog(viewport);
        }
    }

    private void showLoginDialog(final Viewport viewport) {
        // Dialog window
        final LoginDialog loginDialog = new LoginDialog();

        loginDialog.addListener(Events.Hide, new Listener<ComponentEvent>() {

            public void handleEvent(ComponentEvent be) {
                if (loginDialog.isAllowMainScreen()) {
                    renderMainScreen(viewport, loginDialog.getCurrentSession());
                }
            }
        });

        if (!UserAgentUtils.isIE()) {
            Window.addResizeHandler(new ResizeHandler() {

                public void onResize(ResizeEvent arg0) {
                    loginDialog.center();
                }
            });
        }

        loginDialog.show();
    }

    private void performSsoLogin(final Viewport viewport, String authToken) {

        // show wait dialog

        final Dialog dlg = new Dialog();
        dlg.setHeading(MSGS.ssoWaitDialog_title());
        dlg.setButtons("");
        dlg.setClosable(false);
        dlg.setResizable(false);
        dlg.setModal(true);
        dlg.setOnEsc(false);

        final Label label = new Label(MSGS.ssoWaitDialog_text());
        dlg.add(label);

        dlg.show();
        dlg.center();

        // start login process

        final GwtJwtCredential credentials = new GwtJwtCredential(authToken);
        gwtAuthorizationService.login(credentials, new AsyncCallback<GwtSession>() {

            @Override
            public void onFailure(Throwable caught) {
                dlg.hide();
                ConsoleInfo.display(MSGS.loginError(), caught.getLocalizedMessage());

                Logout.logout();
            }

            @Override
            public void onSuccess(final GwtSession gwtSession) {
                logger.fine("User: " + gwtSession.getUserId());
                dlg.hide();
                renderMainScreen(viewport, gwtSession);
            }
        });
    }

    private void loadFooterData(final LayoutContainer container, final Html genericNote, final Label creditLabel) {
        this.gwtSettingService.getLoginInformation(new AsyncCallback<GwtLoginInformation>() {

            @Override
            public void onFailure(Throwable caught) {
                ConsoleInfo.display(MSGS.error(), caught.getLocalizedMessage());
            }

            @Override
            public void onSuccess(GwtLoginInformation result) {
                if (result.getBackgroundCredits() != null) {
                    creditLabel.setText(result.getBackgroundCredits());
                    creditLabel.repaint();
                }

                if (result.getInformationSnippet() != null) {
                    genericNote.setHtml(result.getInformationSnippet());
                    genericNote.repaint();
                }

                // re-layout
                container.layout(true);
            }
        });
    }

    public Viewport getViewport() {
        return viewport;
    }

    public NorthView getNorthView() {
        return northView;
    }

    public WestNavigationView getWestView() {
        return westView;
    }

    public LayoutContainer getCenterView() {
        return centerView;
    }

    public HorizontalPanel getSouthView() {
        return southView;
    }

    private void renderMainScreen(final Viewport viewport, GwtSession session) {
        currentSession = session;

        if (currentSession != null) {
            String username = currentSession.getUserName();
            if (username != null) {

                //
                // Enter into the normal viewport
                RootPanel.get().remove(viewport);
                render(currentSession);

                return;
            }
        }

        // or else

        ConsoleInfo.display(MSGS.error(), MSGS.loginError());
        showLoginDialog(viewport);
    }
}
