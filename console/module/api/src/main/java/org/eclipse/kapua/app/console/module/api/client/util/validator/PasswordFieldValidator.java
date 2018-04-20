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
package org.eclipse.kapua.app.console.module.api.client.util.validator;

import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.TextField;

public class PasswordFieldValidator extends TextFieldValidator {

    public PasswordFieldValidator(TextField<String> passwordField) {
        super(passwordField, FieldType.PASSWORD);
        textField.setRegex(null);
    }

    public String validate(Field<?> field, String value) {

        // if the field is not dirty, ignore the validation
        // this is needed for the update flow, in which we do not show the whole password
        boolean isDirty = textField.isDirty();
        if (!isDirty) {
            textField.setRegex(null);
            return null;
        }

        if (textFieldType.getRegex() != null) {
            textField.setRegex(textFieldType.getRegex());
        }

        return super.validate(field, value);
    }
}
