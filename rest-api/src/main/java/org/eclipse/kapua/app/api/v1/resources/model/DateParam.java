package org.eclipse.kapua.app.api.v1.resources.model;

import java.util.Date;

import org.eclipse.kapua.KapuaIllegalArgumentException;
import org.eclipse.kapua.model.xml.DateXmlAdapter;

import com.google.common.base.Strings;

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
        return date;
    }

    private void setDate(Date date) {
        this.date = date;
    }

}
