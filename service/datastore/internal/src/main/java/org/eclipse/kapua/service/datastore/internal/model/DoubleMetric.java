package org.eclipse.kapua.service.datastore.internal.model;

import org.eclipse.kapua.service.datastore.model.Metric;

public class DoubleMetric extends MetricImpl<Double> implements Metric<Double>{

    public DoubleMetric(String name, Object value) {
        setName(name);
        setType(Double.class);
        setValue(new Double((Double) value).doubleValue());
    }

    @Override
    public int compareTo(Double o) {
        return getValue().compareTo(o);
    }
}
