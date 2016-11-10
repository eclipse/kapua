package org.eclipse.kapua.app.console.client.ui.widget;

import java.util.Arrays;

import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;

public class EnumComboBox<E extends org.eclipse.kapua.app.console.shared.model.Enum> extends SimpleComboBox<E> {

    public EnumComboBox() {
        super();

        setEditable(false);
        setTriggerAction(TriggerAction.ALL);
        setAllowBlank(false);
    }

    public void add(E[] values) {
        super.add(Arrays.asList(values));
    }
}
