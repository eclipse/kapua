/*******************************************************************************
 * Copyright (c) 2017 Red Hat Inc and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat Inc - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.console.module.about.client.about;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.Style.VerticalAlignment;
import com.extjs.gxt.ui.client.core.XTemplate;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.data.BeanModelFactory;
import com.extjs.gxt.ui.client.data.BeanModelLookup;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.Text;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.RowExpander;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.TableData;
import com.extjs.gxt.ui.client.widget.layout.TableLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Image;
import org.eclipse.kapua.app.console.module.api.client.ui.view.AbstractView;
import org.eclipse.kapua.app.console.module.api.client.util.ConsoleInfo;
import org.eclipse.kapua.app.console.module.api.client.ui.view.View;
import org.eclipse.kapua.app.console.module.about.client.messages.ConsoleAboutMessages;
import org.eclipse.kapua.app.console.module.about.shared.model.GwtAboutDependency;
import org.eclipse.kapua.app.console.module.about.shared.model.GwtAboutInformation;
import org.eclipse.kapua.app.console.module.about.shared.service.GwtAboutService;
import org.eclipse.kapua.app.console.module.about.shared.service.GwtAboutServiceAsync;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import static org.eclipse.kapua.app.console.module.api.client.util.Years.getCurrentYear;

public class AboutView extends AbstractView implements View {

    private static final Logger logger = Logger.getLogger("AboutView");

    private static final ConsoleAboutMessages MSGS = GWT.create(ConsoleAboutMessages.class);

    private static final GwtAboutServiceAsync ABOUT_SERVICE = GWT.create(GwtAboutService.class);

    public AboutView() {
        setBorders(false);
//        setBodyBorder(false);
//        setHeaderVisible(false);
    }

    public static String getName() {
        return MSGS.title();
    }

    // generating strings in native methods easier than in Java, can be created in Java as well
    public native String getTemplate() /*-{
        return [ //
            '<div style="padding: 1em;">', //
            '<p><h2>Additional information</h2></p><hr/>', //
            '<tpl if="notice && noticeMimeType==\'text/plain\'">', //
            '<p><pre>{notice}</pre></p><hr/>', // show notice
            '</tpl>', //
            '<tpl if="notice && noticeMimeType==\'text/html\'">', //
            '<iframe width="100%" height="300" frameborder="0" srcdoc="{notice}" sandbox></iframe><hr/>', // show notice
            '</tpl>', //
            '<tpl if="licenseText">', //
            '<p><pre>{licenseText}</pre></p><hr/>', // show license text
            '</tpl>', //
            '</div>' //
        ].join("");
    }-*/;

    @Override
    protected void onRender(final Element parent, int index) {
        super.onRender(parent, index);

        setLayout(new FitLayout());

        // Kapua logo
        final Image kapuaIcon = new Image("img/icon-color.svg");
        kapuaIcon.setHeight("200px");

        // Kapua welcome
        final Text blurbText = new Text();
        blurbText.setId("about-blurb-text");
        blurbText.setText(MSGS.aboutBlurb(Integer.toString(getCurrentYear())));

        final TableLayout tableLayout = new TableLayout(2);
        tableLayout.setWidth("100%");
        tableLayout.setHeight("100%");
        tableLayout.setCellPadding(40);
        tableLayout.setCellHorizontalAlign(HorizontalAlignment.CENTER);
        tableLayout.setCellVerticalAlign(VerticalAlignment.MIDDLE);

        final LayoutContainer blurb = new LayoutContainer(tableLayout);
        blurb.add(kapuaIcon);
        blurb.add(blurbText, new TableData(HorizontalAlignment.LEFT, VerticalAlignment.MIDDLE));

        // create dependencies table

        // columns

        final RowExpander rowExpander = new RowExpander(XTemplate.create(getTemplate()));

        final List<ColumnConfig> columns = new LinkedList<ColumnConfig>();
        columns.add(rowExpander);
        columns.add(new ColumnConfig("id", MSGS.columnNameId(), 200));
        columns.add(new ColumnConfig("name", MSGS.columnNameName(), 300));
        columns.add(new ColumnConfig("version", MSGS.columnNameVersion(), 200));
        columns.add(new ColumnConfig("license", MSGS.columnNameLicense(), 300));
        final ColumnModel cm = new ColumnModel(columns);

        // content model

        final ListStore<BeanModel> store = new ListStore<BeanModel>();

        // the grid

        final Grid<BeanModel> grid = new Grid<BeanModel>(store, cm);
        grid.setId("about-grid");
        grid.setAutoExpandColumn("id");

        // init grid plugins
        rowExpander.init(grid);

        // create layout

        final BorderLayout borderLayout = new BorderLayout();
        borderLayout.setContainerStyle(null); // reset style and keep default background
        final LayoutContainer mainPanel = new LayoutContainer(borderLayout);
        mainPanel.setHeight("100%");
        mainPanel.add(blurb, new BorderLayoutData(LayoutRegion.NORTH, 280));
        mainPanel.add(grid, new BorderLayoutData(LayoutRegion.CENTER));

        // add main content

        add(mainPanel);

        // fill table widget

        fillAboutInformation(grid, store);
    }

    private void fillAboutInformation(final Grid<BeanModel> grid, final ListStore<BeanModel> store) {
        ABOUT_SERVICE.getInformation(new AsyncCallback<GwtAboutInformation>() {

            @Override
            public void onFailure(final Throwable caught) {
                ConsoleInfo.display(MSGS.failedToLoad(), caught.getLocalizedMessage());
            }

            @Override
            public void onSuccess(final GwtAboutInformation about) {
                applyAboutInformation(about, grid, store);
            }
        });
    }

    protected void applyAboutInformation(final GwtAboutInformation about, final Grid<BeanModel> grid, final ListStore<BeanModel> store) {
        logger.info("Entries: " + about.getDependencies().size());

        final BeanModelFactory factory = BeanModelLookup.get().getFactory(GwtAboutDependency.class);
        store.add(factory.createModel(about.getDependencies()));
        grid.getView().refresh(false);
    }
}
