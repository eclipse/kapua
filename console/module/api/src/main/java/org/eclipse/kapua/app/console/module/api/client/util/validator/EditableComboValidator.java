/*******************************************************************************
 * Copyright (c) 2017, 2022 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.console.module.api.client.util.validator;

import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
import com.extjs.gxt.ui.client.widget.form.Validator;
import com.google.gwt.core.client.GWT;
import org.eclipse.kapua.app.console.module.api.client.messages.ValidationMessages;

public class EditableComboValidator implements Validator {

    private static final ValidationMessages MSGS = GWT.create(ValidationMessages.class);

    private SimpleComboBox<String> comboField;
    private String regex = "(^[ ]*0[ ]*-[ ]*[a-zA-z0-9]*|^[ ]*[0-9]*[ ]*)";

    public EditableComboValidator(SimpleComboBox<String> comboField) {
        this.comboField = comboField;
        this.comboField.setRegex(regex);
    }

    public String validate(Field<?> field, String value) {
        String result = null;
        if (!value.matches(comboField.getRegex())) {
            result = MSGS.editableComboRegexMsg();
        }
        return result;
    }
}
