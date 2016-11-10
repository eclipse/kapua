package org.eclipse.kapua.app.console.client.role.dialog;

import org.eclipse.kapua.app.console.client.ui.widget.ComboEnumCellEditor;
import org.eclipse.kapua.app.console.shared.model.GwtPermission.GwtAction;

import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;

public class GwtActionComboCellEditor extends ComboEnumCellEditor<GwtAction> {

    public GwtActionComboCellEditor(SimpleComboBox<GwtAction> field) {
        super(field);
    }

    @Override
    protected GwtAction convertStringValue(String value) {
        return GwtAction.valueOf(value);
    }

}
