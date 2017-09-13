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
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.form.Validator;
import com.google.gwt.core.client.GWT;
import org.eclipse.kapua.app.console.module.api.client.messages.ValidationMessages;

public class NumberFieldValidator implements Validator {

    private static final ValidationMessages MSGS = GWT.create(ValidationMessages.class);

    protected NumberField numberField;

    protected Long minValue;
    protected Long maxValue;
    protected String fieldName;

    public NumberFieldValidator(Long minValue,
            Long maxValue,
            String fieldName) {
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.fieldName = fieldName;
    }

    @Override
    public String validate(Field<?> field, String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }

        Long valueL = Long.parseLong(value);

        if (minValue != null &&
                minValue > valueL) {
            return MSGS.getString(fieldName + "Min");
        }

        if (maxValue != null &&
                maxValue < valueL) {
            return MSGS.getString(fieldName + "Max");
        }

        // TODO Auto-generated method stub
        return null;
    }

}
