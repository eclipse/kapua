package org.eclipse.kapua.app.console.client.ui.widget;

import org.eclipse.kapua.app.console.client.ui.button.AddButton;
import org.eclipse.kapua.app.console.client.ui.button.DeleteButton;
import org.eclipse.kapua.app.console.client.ui.button.EditButton;
import org.eclipse.kapua.app.console.client.ui.button.RefreshButton;
import org.eclipse.kapua.app.console.client.ui.dialog.KapuaDialog;
import org.eclipse.kapua.app.console.client.ui.grid.EntityGrid;
import org.eclipse.kapua.app.console.shared.model.GwtEntityModel;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.grid.GridSelectionModel;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.user.client.Element;

public class KapuaEntityCRUDToolbar<M extends GwtEntityModel> extends ToolBar {

    protected EntityGrid<M> entityGrid;
    protected GridSelectionModel<M> gridSelectionModel;

    protected ToolBar thisToolbar = this;

    protected AddButton addEntityButton;
    private boolean addEntityButtonShow = true;

    protected EditButton editEntityButton;
    private boolean editEntityButtonShow = true;

    protected DeleteButton deleteEntityButton;
    private boolean deleteEntityButtonShow = true;

    protected RefreshButton refreshEntityButton;
    private boolean refreshEntityButtonShow = true;

    @Override
    protected void onRender(Element target, int index) {
        super.onRender(target, index);

        if (addEntityButtonShow) {
            addEntityButton = new AddButton(getAddButtonSelectionListener());
            add(addEntityButton);
            add(new SeparatorToolItem());
        }

        if (editEntityButtonShow) {
            editEntityButton = new EditButton(getEditButtonSelectionListener());
            add(editEntityButton);
            add(new SeparatorToolItem());
        }

        if (deleteEntityButtonShow) {
            deleteEntityButton = new DeleteButton(getDeleteButtonSelectionListener());
            add(deleteEntityButton);
            add(new SeparatorToolItem());
        }

        if (refreshEntityButtonShow) {
            refreshEntityButton = new RefreshButton(getRefreshButtonSelectionListener());
            add(refreshEntityButton);
        }
    }

    //
    // Add button methods
    //
    protected SelectionListener<ButtonEvent> getAddButtonSelectionListener() {
        return new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                KapuaDialog dialog = getAddDialog();
                if (dialog != null) {
                    thisToolbar.disable();
                    dialog.addListener(Events.Hide, getHideDialogListener());
                    dialog.show();
                }
            }
        };
    }

    protected KapuaDialog getAddDialog() {
        return null;
    }

    public void setAddButtonVisible(boolean show) {
        this.addEntityButtonShow = show;
    }

    //
    // Edit button methods
    //
    protected SelectionListener<ButtonEvent> getEditButtonSelectionListener() {
        return new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                KapuaDialog dialog = getEditDialog();
                if (dialog != null) {
                    thisToolbar.disable();
                    dialog.addListener(Events.Hide, getHideDialogListener());
                    dialog.show();
                }
            }
        };
    }

    protected KapuaDialog getEditDialog() {
        return null;
    }

    public void setEditButtonVisible(boolean show) {
        this.editEntityButtonShow = show;
    }

    //
    // Delete button methods
    //
    protected SelectionListener<ButtonEvent> getDeleteButtonSelectionListener() {
        return new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                KapuaDialog dialog = getDeleteDialog();
                if (dialog != null) {
                    thisToolbar.disable();
                    dialog.addListener(Events.Hide, getHideDialogListener());
                    dialog.show();
                }
            }
        };
    }

    protected KapuaDialog getDeleteDialog() {
        return null;
    }

    public void setDeleteButtonVisible(boolean show) {
        this.deleteEntityButtonShow = show;
    }

    //
    // Refresh button methods
    //
    protected SelectionListener<ButtonEvent> getRefreshButtonSelectionListener() {
        return new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                thisToolbar.disable();
                entityGrid.refresh();
                thisToolbar.enable();
            }
        };
    }

    public void setRefreshButtonVisible(boolean show) {
        this.refreshEntityButtonShow = show;
    }

    //
    // Other methods
    private Listener<? extends BaseEvent> getHideDialogListener() {
        return new Listener<ComponentEvent>() {

            public void handleEvent(ComponentEvent be) {
                entityGrid.refresh();
                thisToolbar.enable();
            }
        };
    }

    public void setEntityGrid(EntityGrid<M> entityGrid) {
        this.entityGrid = entityGrid;
        this.gridSelectionModel = entityGrid.getSelectionModel();
    }
}
