package org.eclipse.kapua.service.datastore.internal.model;

import java.util.Date;

import org.eclipse.kapua.service.datastore.model.Metric;

public class DateMetric extends MetricImpl<Date> implements Metric<Date>{

    public DateMetric(String name, Object value) {
        setName(name);
        setType(Date.class);
        setValue((Date) value);
    }
    
    @Override
    public int compareTo(Date o) {
        return getValue().compareTo(o);
    }
}
