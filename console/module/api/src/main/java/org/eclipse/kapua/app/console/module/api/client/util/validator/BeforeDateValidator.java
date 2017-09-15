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

import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.Validator;
import com.google.gwt.core.client.GWT;
import org.eclipse.kapua.app.console.module.api.client.messages.ValidationMessages;

public class BeforeDateValidator implements Validator {

    private final DateField otherDateField;
    private static final ValidationMessages VAL_MSGS = GWT.create(ValidationMessages.class);

    public BeforeDateValidator(DateField otherDateField) {
        this.otherDateField = otherDateField;
    }

    @Override
    public String validate(Field<?> field, String s) {
        if (otherDateField.getValue() != null) {
            DateField thisDateField = (DateField) field;
            return thisDateField.getValue().after(otherDateField.getValue()) ? null : VAL_MSGS.endsOnDateEarlierThanStartsOn();
        }
        return null;
    }
}
