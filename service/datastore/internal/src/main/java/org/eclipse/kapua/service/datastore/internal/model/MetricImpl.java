package org.eclipse.kapua.service.datastore.internal.model;

import org.eclipse.kapua.service.datastore.model.Metric;

public abstract class MetricImpl<T> implements Metric<T> {

    private String name;
    private Class<T> type;
    private T value;
    
    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
      this.name = name;
    }

    @Override
    public Class<T> getType() {
        return type;
    }

    @Override
    public void setType(Class<T> type) {
        this.type = type;
    }

    @Override
    public T getValue() {
        return value;
    }

    @Override
    public void setValue(T value) {
        this.value = value;
    }
}
