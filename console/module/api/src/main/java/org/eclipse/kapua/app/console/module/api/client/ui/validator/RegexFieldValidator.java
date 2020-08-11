/*******************************************************************************
 * Copyright (c) 2018, 2020 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.app.console.module.api.client.ui.validator;

import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.Validator;
import com.google.gwt.core.client.GWT;
import org.eclipse.kapua.app.console.module.api.client.messages.ConsoleMessages;

public class RegexFieldValidator implements Validator {

    private static final ConsoleMessages MSGS = GWT.create(ConsoleMessages.class);

    private GwtValidationRegex regex;
    private String validationMessage;

    public RegexFieldValidator(GwtValidationRegex regex) {
        this(regex, null);
    }

    public RegexFieldValidator(GwtValidationRegex regex, String validationMessage) {
        this.regex = regex;
        this.validationMessage = validationMessage;
    }

    @Override
    public String validate(Field<?> field, String value) {
        if (!value.matches(regex.getRegex())) {

            String illegalChars = value;
            illegalChars.replaceAll(regex.getRegex(), "");

            return (validationMessage != null && !validationMessage.isEmpty()) ? validationMessage : MSGS.regexValidatorErrorMessage(illegalChars);
        }

        return null;
    }
}
