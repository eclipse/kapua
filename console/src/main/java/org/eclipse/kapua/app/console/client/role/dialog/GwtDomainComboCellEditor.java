package org.eclipse.kapua.app.console.client.role.dialog;

import org.eclipse.kapua.app.console.client.ui.widget.ComboEnumCellEditor;
import org.eclipse.kapua.app.console.shared.model.GwtPermission.GwtDomain;

import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;

public class GwtDomainComboCellEditor extends ComboEnumCellEditor<GwtDomain> {

    public GwtDomainComboCellEditor(SimpleComboBox<GwtDomain> field) {
        super(field);
    }

    @Override
    protected GwtDomain convertStringValue(String value) {
        return GwtDomain.valueOf(value);
    }

}
