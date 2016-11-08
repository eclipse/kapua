package org.eclipse.kapua.app.console.client.ui.widget;

import org.eclipse.kapua.app.console.shared.model.GwtEntityModel;
import org.eclipse.kapua.app.console.shared.model.GwtSession;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;

public abstract class EntityGridFieldToolbar<M extends GwtEntityModel> extends EntityCRUDToolbar<M> {

    protected EntityGridField<M> entityGridField;

    public EntityGridFieldToolbar(GwtSession currentSession) {
        super(currentSession);

        setRefreshButtonVisible(false);
        setEditButtonVisible(false);
    }

    @Override
    protected SelectionListener<ButtonEvent> getAddButtonSelectionListener() {
        return new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                ListStore<M> gridStore = entityGridField.getStore();
                gridStore.add(getNewModel());
            }
        };
    }

    @Override
    protected SelectionListener<ButtonEvent> getDeleteButtonSelectionListener() {
        return new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                M selectedModel = entityGridField.getSelectionModel().getSelectedItem();
                if (selectedModel != null) {
                    ListStore<M> gridStore = entityGridField.getStore();
                    gridStore.remove(selectedModel);
                }
            }
        };
    }

    public void setGrid(EntityGridField<M> entityGridField) {
        this.entityGridField = entityGridField;
    }

    public abstract M getNewModel();
}
