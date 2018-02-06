/*******************************************************************************
 * Copyright (c) 2018 Eurotech and/or its affiliates and others
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

import org.eclipse.kapua.app.console.module.api.client.messages.ValidationMessages;

import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.TimeField;
import com.extjs.gxt.ui.client.widget.form.Validator;
import com.google.gwt.core.client.GWT;

public class TimeFieldValidator implements Validator{
    private static final ValidationMessages MSGS = GWT.create(ValidationMessages.class);

    protected TimeField timeField;
    protected FieldType timeFieldType;

    public TimeFieldValidator(TimeField timeField, FieldType timeFieldType) {
        this.timeField=timeField;
        this.timeFieldType=timeFieldType;
        // initialize the field for its validation
        if (this.timeFieldType.getRegex() != null) {
            this.timeField.setRegex(this.timeFieldType.getRegex());
        }
        if (this.timeFieldType.getToolTipMessage() != null) {
            this.timeField.setToolTip(this.timeFieldType.getToolTipMessage());
        }
        if (this.timeFieldType.getRequiredMessage() != null) {
            this.timeField.getMessages().setBlankText(this.timeFieldType.getRequiredMessage());
        }
        if (this.timeFieldType.getRegexMessage() != null) {
            this.timeField.getMessages().setRegexText(this.timeFieldType.getRegexMessage());
        }
}

    public String validate(Field<?> field, String value) {

        String result = null;
        if (!value.matches(timeFieldType.getRegex())) {
            result = timeFieldType.getRegexMessage();
        }
        return result;
    }

    public enum FieldType{
        TIME("time","^((2[0-3]|[01][0-9]|10):([0-5][0-9]))$");

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
                return "The time should be in format HH:mm.";
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