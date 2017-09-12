/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.api.resources.v1.resources.model;

import java.util.Date;

import org.eclipse.kapua.KapuaIllegalArgumentException;
import org.eclipse.kapua.model.xml.DateXmlAdapter;

import com.google.common.base.Strings;

/**
 * Adapted for query parameters of type {@link Date}.
 * 
 * @since 1.0.0
 */
public class DateParam extends DateXmlAdapter {

    private Date date;

    public DateParam(String stringDate) throws KapuaIllegalArgumentException {
        if (!Strings.isNullOrEmpty(stringDate)) {
            try {
                setDate(super.unmarshal(stringDate));
            } catch (IllegalArgumentException e) {
                throw new KapuaIllegalArgumentException("date", stringDate);
            }
        }
    }

    public Date getDate() {
        return new Date(date.getTime());
    }

    private void setDate(Date date) {
        this.date = new Date(date.getTime());
    }

}
