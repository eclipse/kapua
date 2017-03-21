package org.eclipse.kapua.service.datastore.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.eclipse.persistence.oxm.annotations.XmlPath;

@XmlRootElement(name = "metric")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(propOrder = { "name",
                        "type",
                         "value"})
public interface Metric<T> extends Comparable<T> {
    
    /**
     * Get the name
     * 
     * @return
     * 
     * @since 1.0.0
     */
    @XmlElement(name = "name")
    public String getName();

    /**
     * Set the name
     * 
     * @param name
     * 
     * @since 1.0.0
     */
    public void setName(String name);
    
    
    /**
     * Get the type
     * 
     * @return
     * 
     * @since 1.0.0
     */
    @XmlElement(name = "type")
    @XmlJavaTypeAdapter(MetricInfoTypeAdapter.class)
    public Class<T> getType();

    /**
     * Set the  type
     * 
     * @param type
     * 
     * @since 1.0.0
     */
    public void setType(Class<T> type);
    
    /**
     * Get the metric value
     * 
     * @return
     * 
     * @since 1.0.0
     */
    @XmlElement(name = "value")
    @XmlPath(".")
    public T getValue();
    
    /**
     * Set the metric value
     * 
     * @param value
     * 
     * @since 1.0.0
     */
    public void setValue(T value);
}
