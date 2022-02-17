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
import com.extjs.gxt.ui.client.widget.form.Validator;
import com.google.gwt.core.client.GWT;
import org.eclipse.kapua.app.console.module.api.client.messages.ValidationMessages;

public class DeviceDisplayNameValidator implements Validator {

    private static final ValidationMessages MSGS = GWT.create(ValidationMessages.class);

    private TextField<String> deviceDisplayNameField;
    private String regex = ".{3,}";

    public DeviceDisplayNameValidator(TextField<String> deviceDisplayNameField) {
        this.deviceDisplayNameField = deviceDisplayNameField;
        this.deviceDisplayNameField.setRegex(regex);
    }

    @Override
    public String validate(Field<?> field, String value) {
        String result = null;

        if (!value.equals(value.trim())) {
            result = MSGS.deviceFormDisplayNameValidationMessage();
        }

        if (!value.trim().matches(deviceDisplayNameField.getRegex())) {
            result = MSGS.deviceFormDisplayNameValidationMessage();
        }
        return result;
    }

}
