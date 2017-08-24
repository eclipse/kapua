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

import java.util.MissingResourceException;

import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.form.Validator;
import com.google.gwt.core.client.GWT;
import org.eclipse.kapua.app.console.module.api.client.messages.ValidationMessages;

public class TextFieldValidator implements Validator {

    private static final ValidationMessages MSGS = GWT.create(ValidationMessages.class);

    protected FieldType textFieldType;
    protected TextField<String> textField;

    public TextFieldValidator(TextField<String> textField, FieldType textFieldType) {

        this.textField = textField;
        this.textFieldType = textFieldType;

        // initialize the field for its validation
        if (this.textFieldType.getRegex() != null) {
            this.textField.setRegex(this.textFieldType.getRegex());
        }
        if (this.textFieldType.getToolTipMessage() != null) {
            this.textField.setToolTip(this.textFieldType.getToolTipMessage());
        }
        if (this.textFieldType.getRequiredMessage() != null) {
            this.textField.getMessages().setBlankText(this.textFieldType.getRequiredMessage());
        }
        if (this.textFieldType.getRegexMessage() != null) {
            this.textField.getMessages().setRegexText(this.textFieldType.getRegexMessage());
        }
    }

    public String validate(Field<?> field, String value) {

        String result = null;
        if (!value.matches(textFieldType.getRegex())) {
            result = textFieldType.getRegexMessage();
        }
        return result;
    }

    public enum FieldType {

        SIMPLE_NAME("simple_name", "^[a-zA-Z0-9\\-]{3,}$"), DEVICE_CLIENT_ID("device_client_id", "^[a-zA-Z0-9\\:\\_\\-]{3,}$"), NAME("name", "^[a-zA-Z0-9\\_\\-]{3,}$"), NAME_SPACE("name_space",
                "^[a-zA-Z0-9\\ \\_\\-]{3,}$"), PASSWORD("password", "^.*(?=.{12,})(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!\\~\\|]).*$"), EMAIL("email",
                        "^(\\w+)([-+.][\\w]+)*@(\\w[-\\w]*\\.){1,5}([A-Za-z]){2,4}$"), PHONE("phone",
                                "^(?:(?:\\+?1\\s*(?:[.-]\\s*)?)?(?:\\(\\s*([2-9]1[02-9]|[2-9][02-8]1|[2-9][02-8][02-9])\\s*\\)|([2-9]1[02-9]|[2-9][02-8]1|[2-9][02-8][02-9]))\\s*(?:[.-]\\s*)?)?([2-9]1[02-9]|[2-9][02-9]1|[2-9][02-9]{2})\\s*(?:[.-]\\s*)?([0-9]{4})(?:\\s*(?:#|x\\.?|ext\\.?|extension)\\s*(\\d+))?$"), ALPHABET(
                                        "alphabet", "^[a-zA-Z_]+$"), ALPHANUMERIC("alphanumeric", "^[a-zA-Z0-9_]+$"), NUMERIC("numeric", "^[+0-9.]+$");

        private String name;
        private String regex;
        private String regexMsg;
        private String toolTipMsg;
        private String requiredMsg;

        FieldType(String name, String regex) {

            this.name = name;
            this.regex = regex;
            regexMsg = name + "RegexMsg";
            toolTipMsg = name + "ToolTipMsg";
            requiredMsg = name + "RequiredMsg";
        }

        public String getName() {
            return name;
        }

        public String getRegex() {
            return regex;
        }

        public String getRegexMessage() {
            try {
                return MSGS.getString(regexMsg);
            } catch (MissingResourceException mre) {
                return null;
            }
        }

        public String getToolTipMessage() {
            try {
                return MSGS.getString(toolTipMsg);
            } catch (MissingResourceException mre) {
                return null;
            }
        }

        public String getRequiredMessage() {
            try {
                return MSGS.getString(requiredMsg);
            } catch (MissingResourceException mre) {
                return null;
            }
        }
    }
}
