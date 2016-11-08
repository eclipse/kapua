package org.eclipse.kapua.app.console.client.ui.widget;

import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
import com.extjs.gxt.ui.client.widget.form.SimpleComboValue;
import com.extjs.gxt.ui.client.widget.grid.CellEditor;

public class ComboEnumCellEditor<E extends org.eclipse.kapua.app.console.shared.model.Enum> extends CellEditor {

    public ComboEnumCellEditor(SimpleComboBox<E> field) {
        super(field);
    }

    @Override
    public Object postProcessValue(Object value) {
        String stringValue = null;

        if (value != null) {
            @SuppressWarnings("unchecked")
            SimpleComboValue<E> simpleComboValue = (SimpleComboValue<E>) value;

            E enumValue = simpleComboValue.getValue();
            if (enumValue != null) {
                stringValue = enumValue.name();
            }
        }

        return stringValue;
    }
}
