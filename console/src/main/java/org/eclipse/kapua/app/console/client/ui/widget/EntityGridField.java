package org.eclipse.kapua.app.console.client.ui.widget;

import java.util.List;

import org.eclipse.kapua.app.console.client.messages.ConsoleMessages;
import org.eclipse.kapua.app.console.client.ui.grid.KapuaEditableGrid;
import org.eclipse.kapua.app.console.client.ui.panel.ContentPanel;
import org.eclipse.kapua.app.console.shared.model.GwtEntityModel;
import org.eclipse.kapua.app.console.shared.model.GwtSession;

import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.form.LabelField;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.GridSelectionModel;
import com.extjs.gxt.ui.client.widget.grid.GridView;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;

public abstract class EntityGridField<M extends GwtEntityModel> extends ContentPanel {

    private static final ConsoleMessages MSGS = GWT.create(ConsoleMessages.class);
    protected GwtSession currentSession;

    protected EntityGridFieldToolbar<M> entityGridToolbar;

    LabelField label;

    protected KapuaEditableGrid<M> entityGrid;
    protected ListStore<M> entityStore;

    protected EntityGridField(GwtSession currentSession) {
        //
        // Set other properties
        this.currentSession = currentSession;

        //
        // Container borders
        setBorders(false);
        setBodyBorder(false);
        setHeaderVisible(false);
        setBodyStyle("background-color:transparent");

        //
        // CRUD toolbar
        entityGridToolbar = getToolbar();
    }

    protected abstract EntityGridFieldToolbar<M> getToolbar();

    @Override
    protected void onRender(Element target, int index) {
        super.onRender(target, index);

        //
        // Grid Label
        if (label == null) {
            label = new LabelField();
        }
        add(label);

        //
        // Grdi container
        ContentPanel gridContainer = new ContentPanel(new FitLayout());
        gridContainer.setHeaderVisible(false);
        add(gridContainer);

        //
        // Grid toolbar
        if (entityGridToolbar != null) {
            gridContainer.setTopComponent(entityGridToolbar);
        }

        //
        // Configure Entity Grid
        // Data Store
        entityStore = new ListStore<M>();

        //
        // Configure columns
        ColumnModel columnModel = new ColumnModel(getColumns());

        //
        // Set grid
        entityGrid = new KapuaEditableGrid<M>(entityStore, columnModel);
        entityGrid.setHeight(200);
        gridContainer.add(entityGrid);

        //
        // Bind the grid to CRUD toolbar
        entityGridToolbar.setGrid(this);

        //
        // Grid selection mode
        GridSelectionModel<M> selectionModel = entityGrid.getSelectionModel();
        selectionModel.setSelectionMode(SelectionMode.SINGLE);
        selectionModel.addSelectionChangedListener(new SelectionChangedListener<M>() {

            @Override
            public void selectionChanged(SelectionChangedEvent<M> se) {

            }
        });

        //
        // Grid view options
        GridView gridView = entityGrid.getView();
        gridView.setEmptyText(MSGS.gridEmptyResult());
    }

    public List<M> getModels() {
        return getStore().getModels();
    }

    public ListStore<M> getStore() {
        return entityGrid.getStore();
    }

    public GridSelectionModel<M> getSelectionModel() {
        return entityGrid.getSelectionModel();
    }

    public void setFieldLabel(String text) {
        if (label == null) {
            label = new LabelField();
        }
        label.setText(text);
    }

    public void setToolTip(String text) {
        label.setToolTip(text);
    }

    protected abstract List<ColumnConfig> getColumns();

}
