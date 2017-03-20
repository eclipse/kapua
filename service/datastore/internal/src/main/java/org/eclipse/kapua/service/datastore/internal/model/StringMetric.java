package org.eclipse.kapua.service.datastore.internal.model;

import org.eclipse.kapua.service.datastore.model.Metric;

public class StringMetric extends MetricImpl<String> implements Metric<String>{

    public StringMetric(String name, Object value) {
        setName(name);
        setType(String.class);
        setValue(new String((String) value));
    }

    @Override
    public int compareTo(String o) {
        return getValue().compareTo(o);
    }
}
