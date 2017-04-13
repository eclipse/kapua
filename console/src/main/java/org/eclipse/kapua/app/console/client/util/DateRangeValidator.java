/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.console.client.util;

import java.util.Date;

import org.eclipse.kapua.app.console.client.messages.ValidationMessages;

import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.Validator;
import com.google.gwt.core.client.GWT;

public class DateRangeValidator implements Validator
{

    private static final ValidationMessages MSGS = GWT.create(ValidationMessages.class);

    private DateField                       m_startDateField;
    private DateField                       m_endDateField;

    public DateRangeValidator(DateField startDateField, DateField endDateField)
    {
        m_startDateField = startDateField;
        m_endDateField = endDateField;
    }

    public String validate(Field<?> field, String value)
    {
        String result = null;

        Date currentDate = m_startDateField.getValue();
        Date endDate = m_endDateField.getValue();

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
