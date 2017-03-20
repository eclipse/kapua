package org.eclipse.kapua.service.datastore.internal.model;

import org.eclipse.kapua.service.datastore.model.Metric;

public class BooleanMetric extends MetricImpl<Boolean> implements Metric<Boolean> {

    public BooleanMetric(String name, Object value) {
        setName(name);
        setType(Boolean.class);
        setValue(new Boolean((Boolean) value).booleanValue());
    }

    @Override
    public int compareTo(Boolean o) {
        return getValue().compareTo(o);
    }
}
