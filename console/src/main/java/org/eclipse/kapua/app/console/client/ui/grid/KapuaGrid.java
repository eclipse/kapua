package org.eclipse.kapua.app.console.client.ui.grid;

import java.util.ArrayList;

import org.eclipse.kapua.app.console.shared.model.KapuaBaseModel;

import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridView;

public class KapuaGrid<M extends KapuaBaseModel> extends Grid<M> {

    public KapuaGrid() {
        // Dummy init required by GWT/GXT in order to not explode on initialization.
        super(null, new ColumnModel(new ArrayList<ColumnConfig>()));

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