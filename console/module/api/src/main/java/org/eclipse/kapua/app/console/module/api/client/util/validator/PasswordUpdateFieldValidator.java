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

public class PasswordUpdateFieldValidator extends PasswordFieldValidator {

    /**
     * @param passwordField
     */
    public PasswordUpdateFieldValidator(TextField<String> passwordField) {
        super(passwordField);

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
