/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.model.xml;

import javax.xml.bind.DatatypeConverter;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DateXmlAdapter extends XmlAdapter<String, Date> {

    public static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    public static final String TIME_ZONE_UTC = "UTC";

    @Override
    public String marshal(Date v) throws Exception {
        if (v == null) {
            return null;
        }

        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
        format.setTimeZone(TimeZone.getTimeZone(TIME_ZONE_UTC));
        return format.format(v);
    }

    @Override
    public Date unmarshal(String v) {
        if (v == null || v.isEmpty()) {
            return null;
        }

        return DatatypeConverter.parseDateTime(v).getTime();
    }
}
