package org.eclipse.kapua.service.datastore.internal.model;

import org.eclipse.kapua.service.datastore.model.Metric;

public class IntMetric extends MetricImpl<Integer> implements Metric<Integer> {

    public IntMetric(String name, Object value) {
        setName(name);
        setType(Integer.class);
        setValue(new Integer((Integer) value).intValue());
    }
    
    @Override
    public int compareTo(Integer o) {
        return super.getValue().compareTo(o);
    }
}
