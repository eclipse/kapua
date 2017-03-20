package org.eclipse.kapua.service.datastore.internal.model;

import org.eclipse.kapua.service.datastore.model.Metric;

public class LongMetric extends MetricImpl<Long> implements Metric<Long>{

    public LongMetric(String name, Object value) {
        setName(name);
        setType(Long.class);
        setValue(new Long((Long) value).longValue());
    }

    @Override
    public int compareTo(Long o) {
        return getValue().compareTo(o);
    }
}
