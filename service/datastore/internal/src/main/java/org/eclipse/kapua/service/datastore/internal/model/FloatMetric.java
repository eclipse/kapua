package org.eclipse.kapua.service.datastore.internal.model;

import org.eclipse.kapua.service.datastore.model.Metric;

public class FloatMetric extends MetricImpl<Float> implements Metric<Float> {

    public FloatMetric(String name, Object value) {
        setName(name);
        setType(Float.class);
        setValue(new Float((Float) value).floatValue());
    }

    
    @Override
    public int compareTo(Float o) {
        return getValue().compareTo(o);
    }
}
