package org.eclipse.kapua.app.console.client.ui.grid;

import java.util.List;

import org.eclipse.kapua.app.console.client.messages.ConsoleMessages;
import org.eclipse.kapua.app.console.client.ui.view.EntityView;
import org.eclipse.kapua.app.console.shared.model.GwtEntityModel;
import org.eclipse.kapua.app.console.shared.model.GwtSession;

import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.data.BasePagingLoader;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.GridSelectionModel;
import com.extjs.gxt.ui.client.widget.grid.GridView;
import com.extjs.gxt.ui.client.widget.toolbar.PagingToolBar;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;

public abstract class EntityGrid<M extends GwtEntityModel> extends KapuaGrid<M> {

    private static final ConsoleMessages MSGS = GWT.create(ConsoleMessages.class);

    protected EntityView<M> parentEntityView;
    protected GwtSession currentSession;

    protected BasePagingLoader<PagingLoadResult<M>> entityLoader;
    protected ListStore<M> entityStore;

    protected EntityGrid(EntityView<M> entityView, GwtSession currentSession) {
        super();

        //
        // Configure data loader

        // Proxy
        RpcProxy<PagingLoadResult<M>> dataProxy = getDataProxy();

        // Loader
        entityLoader = new BasePagingLoader<PagingLoadResult<M>>(dataProxy);

        // Store
        entityStore = new ListStore<M>(entityLoader);

        //
        // Grid load listener
        entityLoader.addLoadListener(new EntityLoadListener<M>(this, entityStore));

        //
        // Configure columns
        ColumnModel columnModel = new ColumnModel(getColumns());

        //
        // Set grid
        reconfigure(entityStore, columnModel);

        //
        // Set other properties
        this.parentEntityView = entityView;
        this.currentSession = currentSession;
    }

    @Override
    protected void onRender(Element target, int index) {
        super.onRender(target, index);

        //
        // Grid selection mode
        GridSelectionModel<M> selectionModel = getSelectionModel();
        selectionModel.setSelectionMode(SelectionMode.SINGLE);
        selectionModel.addSelectionChangedListener(new SelectionChangedListener<M>() {

            @Override
            public void selectionChanged(SelectionChangedEvent<M> se) {
                selectionChangedEvent(se.getSelectedItem());
            }
        });

        //
        // Grid view options
        GridView gridView = getView();
        gridView.setEmptyText(MSGS.gridEmptyResult());
    }

    protected abstract RpcProxy<PagingLoadResult<M>> getDataProxy();

    protected abstract List<ColumnConfig> getColumns();

    public void refresh() {
        entityLoader.load();
    }

    protected void selectionChangedEvent(M selectedItem) {
        if (parentEntityView != null) {
            parentEntityView.setSelectedEntity(selectedItem);
        }
    }

    public void setPagingToolbar(PagingToolBar resultPagingToolbar) {
        resultPagingToolbar.bind(entityLoader);
    }

}