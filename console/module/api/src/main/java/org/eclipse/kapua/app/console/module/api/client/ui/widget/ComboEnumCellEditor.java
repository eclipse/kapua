/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.console.module.api.client.ui.widget;

import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
import com.extjs.gxt.ui.client.widget.form.SimpleComboValue;
import com.extjs.gxt.ui.client.widget.grid.CellEditor;
import org.eclipse.kapua.app.console.module.api.shared.model.Enum;

public abstract class ComboEnumCellEditor<E extends Enum> extends CellEditor {

    public ComboEnumCellEditor(SimpleComboBox<E> field) {
        super(field);
    }

    @Override
    public Object preProcessValue(Object value) {

        SimpleComboValue<E> comboValue = null;
        if (value != null) {
            SimpleComboBox<E> dummyCombo = new SimpleComboBox<E>();
            dummyCombo.add(convertStringValue((String) value));
            comboValue = dummyCombo.getStore().getAt(0);
        }

        return comboValue;
    }

    protected abstract E convertStringValue(String value);

    @Override
    @SuppressWarnings("unchecked")
    public Object postProcessValue(Object value) {
        String stringValue = null;

        if (value != null) {
            SimpleComboValue<E> simpleComboValue = (SimpleComboValue<E>) value;

            E enumValue = simpleComboValue.getValue();
            if (enumValue != null) {
                stringValue = enumValue.name();
            }
        }

        return stringValue;
    }
}
