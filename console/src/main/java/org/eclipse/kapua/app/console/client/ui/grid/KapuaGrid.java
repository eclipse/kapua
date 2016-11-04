package org.eclipse.kapua.app.console.client.ui.grid;

import org.eclipse.kapua.app.console.shared.model.KapuaBaseModel;

import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridView;

public class KapuaGrid<M extends KapuaBaseModel> extends Grid<M> {

    public KapuaGrid(ListStore<M> store, ColumnModel cm) {
        super(store, cm);

        //
        // Grid properties
        setBorders(false);
        setStateful(false);
        setLoadMask(true);
        setStripeRows(true);
        setTrackMouseOver(false);
        disableTextSelection(false);

        //
        // Grid view options
        GridView gridView = getView();
        gridView.setAutoFill(true);
        gridView.setForceFit(true);
        gridView.setSortingEnabled(false);
    }
}