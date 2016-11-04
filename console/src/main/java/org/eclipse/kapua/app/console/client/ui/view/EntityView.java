package org.eclipse.kapua.app.console.client.ui.view;

import java.util.List;

import org.eclipse.kapua.app.console.client.messages.ConsoleMessages;
import org.eclipse.kapua.app.console.client.ui.grid.EntityGrid;
import org.eclipse.kapua.app.console.client.ui.panel.ContentPanel;
import org.eclipse.kapua.app.console.client.ui.panel.EntityFilterPanel;
import org.eclipse.kapua.app.console.client.ui.panel.KapuaBorderLayoutData;
import org.eclipse.kapua.app.console.client.ui.panel.KapuaTabPanel;
import org.eclipse.kapua.app.console.client.ui.tab.KapuaTabItem;
import org.eclipse.kapua.app.console.shared.model.GwtEntityModel;
import org.eclipse.kapua.app.console.shared.model.GwtSession;

import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;

public abstract class EntityView<M extends GwtEntityModel> extends LayoutContainer {

    private final ConsoleMessages MSGS = GWT.create(ConsoleMessages.class);

    private GwtSession currentSession;

    private EntityFilterPanel filterPanel;
    private EntityGrid<M> entityGrid;
    private KapuaTabPanel<M> tabsPanel;

    public EntityView() {
        super();

        setLayout(new BorderLayout());
        setBorders(false);
    }

    public EntityView(GwtSession currentSession) {
        this();
        this.currentSession = currentSession;
    }

    public void setSelectedEntity(M entity) {
        tabsPanel.setEntity(entity);
    }

    protected void onRender(final Element parent, int index) {

        super.onRender(parent, index);

        //
        // East Panel: Filtering menu
        filterPanel = getEntityFilterPanel(this, currentSession);
        if (filterPanel != null) {

            filterPanel = new EntityFilterPanel(currentSession);

            ContentPanel panel = new ContentPanel();
            panel.setLayout(new FitLayout());
            panel.setBorders(false);
            panel.setBodyBorder(false);
            panel.setHeading(MSGS.deviceFilteringPanelHeading());
            panel.add(filterPanel);

            KapuaBorderLayoutData eastData = new KapuaBorderLayoutData(LayoutRegion.EAST, 250);
            eastData.setMarginLeft(5);
            add(panel, eastData);
        }

        //
        // Center Main panel:
        LayoutContainer resultContainer = new LayoutContainer(new BorderLayout());
        resultContainer.setBorders(false);

        KapuaBorderLayoutData centerMainPanel = new KapuaBorderLayoutData(LayoutRegion.CENTER);
        add(resultContainer, centerMainPanel);

        //
        // North sub panel: Entity grid
        entityGrid = getEntityGrid(this, currentSession);

        if (filterPanel != null) {
            filterPanel.setEntityGrid(entityGrid);
        }

        BorderLayoutData northData = new KapuaBorderLayoutData(LayoutRegion.NORTH, .45F);
        resultContainer.add(entityGrid, northData);

        //
        // Center sub panel: Entity sub tabs
        tabsPanel = new KapuaTabPanel<M>();

        List<KapuaTabItem<M>> tabItems = getTabs(this, currentSession);

        for (KapuaTabItem<M> kti : tabItems) {
            tabsPanel.add(kti);
        }

        KapuaBorderLayoutData centerData = new KapuaBorderLayoutData(LayoutRegion.CENTER);
        centerData.setMarginTop(5);

        resultContainer.add(tabsPanel, centerData);
    }

    public abstract List<KapuaTabItem<M>> getTabs(EntityView<M> entityView, GwtSession currentSession);

    public abstract EntityGrid<M> getEntityGrid(EntityView<M> entityView, GwtSession currentSession);

    public abstract EntityFilterPanel getEntityFilterPanel(EntityView<M> entityView, GwtSession currentSession2);

    public void onUnload() {
        super.onUnload();
    }

}