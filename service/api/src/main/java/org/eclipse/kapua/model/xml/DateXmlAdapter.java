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
package org.eclipse.kapua.model.xml;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import javax.xml.bind.DatatypeConverter;
import javax.xml.bind.annotation.adapters.XmlAdapter;

public class DateXmlAdapter extends XmlAdapter<String, Date> {

    private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.sss'Z'";

    private static final String TIME_ZONE_UTC = "UTC";

    @Override
    public Date unmarshal(String v) throws Exception {
        return DatatypeConverter.parseDateTime(v).getTime();
    }

    @Override
    public String marshal(Date v) throws Exception {
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
        format.setTimeZone(TimeZone.getTimeZone(TIME_ZONE_UTC));
        return format.format(v);
    }
}
