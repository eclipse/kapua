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
import com.extjs.gxt.ui.client.widget.form.TextField;

public class PasswordUpdateFieldValidator extends PasswordFieldValidator {

    /**
     * @param passwordField
     */
    public PasswordUpdateFieldValidator(TextField<String> passwordField, int minLength) {
        super(passwordField, minLength);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.kapua.app.console.client.util.PasswordFieldValidator#validate(com.extjs.gxt.ui.client.widget.form.Field, java.lang.String)
     */
    @Override
    public String validate(Field<?> field, String value) {
        if (value == null || value.equals("")) {
            textField.setRegex(null);
            return null;
        }
        return super.validate(field, value);
    }

}
