/*******************************************************************************
 * Copyright (c) 2017, 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.console.module.api.client.ui.view;

import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Node;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.eclipse.kapua.app.console.module.api.client.ui.grid.EntityGrid;
import org.eclipse.kapua.app.console.module.api.client.ui.panel.EntityFilterPanel;
import org.eclipse.kapua.app.console.module.api.client.ui.panel.KapuaBorderLayoutData;
import org.eclipse.kapua.app.console.module.api.client.ui.panel.KapuaTabPanel;
import org.eclipse.kapua.app.console.module.api.client.ui.view.descriptor.TabDescriptor;
import org.eclipse.kapua.app.console.module.api.client.util.FailureHandler;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtEntityModel;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSession;
import org.eclipse.kapua.app.console.module.api.shared.service.GwtConsoleService;
import org.eclipse.kapua.app.console.module.api.shared.service.GwtConsoleServiceAsync;

import java.util.List;

public abstract class AbstractEntityView<M extends GwtEntityModel> extends AbstractView implements EntityView<M> {

    @Override
    public void onUserChange() {
        if (entityGrid != null) {
            entityGrid.getFilterQuery().setScopeId(currentSession.getSelectedAccountId());
            entityGrid.refresh();
        }
    }


    private EntityGrid<M> entityGrid;
    private KapuaTabPanel<M> tabsPanel;

    private static final GwtConsoleServiceAsync CONSOLE_SERVICE = GWT.create(GwtConsoleService.class);

    public AbstractEntityView() {
        super();
    }

    public AbstractEntityView(GwtSession currentSession) {
        this();
        this.currentSession = currentSession;
    }

    public void setSelectedEntity(M entity) {
        tabsPanel.setEntity(entity);
    }

    @Override
    protected void onRender(Element parent, int index) {

        super.onRender(parent, index);

        setLayout(new FitLayout());
        setBorders(false);

        LayoutContainer mf = new LayoutContainer();
        mf.setBorders(false);
        mf.setLayout(new BorderLayout());

        // Center Main panel:
        BorderLayoutData centerMainPanel = new BorderLayoutData(LayoutRegion.CENTER);
        centerMainPanel.setMargins(new Margins(0, 0, 0, 0));
        centerMainPanel.setSplit(false);

        final LayoutContainer resultContainer = new LayoutContainer(new BorderLayout());
        resultContainer.setBorders(false);

        mf.add(resultContainer, centerMainPanel);

        //
        // North sub panel: Entity grid
        entityGrid = getEntityGrid(this, currentSession);

        BorderLayoutData northData = new KapuaBorderLayoutData(LayoutRegion.NORTH, .45F);
        resultContainer.add(entityGrid, northData);
        northData.setMinSize(200);

        CONSOLE_SERVICE.getCustomTabsForView(getClass().getName(), new AsyncCallback<List<TabDescriptor>>() {

            @Override
            public void onFailure(Throwable t) {
                FailureHandler.handle(t);
            }

            @Override
            public void onSuccess(List<TabDescriptor> result) {
                tabsPanel = new KapuaTabPanel<M>();

                for (TabDescriptor tabDescriptor : result) {
                    if (tabDescriptor.isEnabled(currentSession)) {
                        tabsPanel.add(tabDescriptor.getTabViewInstance(AbstractEntityView.this, currentSession));
                    }
                }

                KapuaBorderLayoutData centerData = new KapuaBorderLayoutData(LayoutRegion.CENTER, .55F);
                centerData.setMarginTop(10);

                resultContainer.add(tabsPanel, centerData);
                resultContainer.layout(true);
                Node node0 = tabsPanel.getElement().getChild(0);
                Node node1 = node0.getChild(1);
                if (node1.getNodeType() == Node.ELEMENT_NODE) {
                    ((Element) node1).setAttribute("style", "border-top: 0px; border-bottom: 0px;");
                }
            }
        });
        add(mf);
    }

    public abstract EntityGrid<M> getEntityGrid(AbstractEntityView<M> entityView, GwtSession currentSession);

    public abstract EntityFilterPanel<M> getEntityFilterPanel(AbstractEntityView<M> entityView, GwtSession currentSession2);

    @Override
    public void onUnload() {
        super.onUnload();
    }

}
