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

import java.util.Date;

import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.Validator;
import com.google.gwt.core.client.GWT;
import org.eclipse.kapua.app.console.module.api.client.messages.ValidationMessages;

public class DateRangeValidator implements Validator {

    private static final ValidationMessages MSGS = GWT.create(ValidationMessages.class);

    private DateField startDateField;
    private DateField endDateField;

    public DateRangeValidator(DateField startDateField, DateField endDateField) {
        this.startDateField = startDateField;
        this.endDateField = endDateField;
    }

    public String validate(Field<?> field, String value) {
        String result = null;

        Date currentDate = startDateField.getValue();
        Date endDate = endDateField.getValue();

        if (currentDate == null) {
            currentDate = new Date();
        }

        if (endDate != null) {
            if (endDate.before(currentDate)) {
                result = MSGS.dateRangeValidatorEndDateBeforeStartDate();
            }
        }
        return result;
    }

}
