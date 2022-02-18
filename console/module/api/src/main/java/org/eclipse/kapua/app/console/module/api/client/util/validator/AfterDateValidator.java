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

import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.Validator;
import com.google.gwt.core.client.GWT;
import org.eclipse.kapua.app.console.module.api.client.messages.ValidationMessages;

public class AfterDateValidator implements Validator {

    private final DateField otherDateField;
    private static final ValidationMessages VAL_MSGS = GWT.create(ValidationMessages.class);

    public AfterDateValidator(DateField otherDateField) {
        this.otherDateField = otherDateField;
    }

    @Override
    public String validate(Field<?> field, String s) {
        if (otherDateField.getValue() != null) {
            DateField thisDateField = (DateField) field;
            if (thisDateField.getValue().before(otherDateField.getValue()) || thisDateField.getValue().equals(otherDateField.getValue())) {
                otherDateField.clearInvalid();
            } else {
                return VAL_MSGS.startsOnDateLaterThanEndsOn();
            }
        }
        return null;
    }
}

