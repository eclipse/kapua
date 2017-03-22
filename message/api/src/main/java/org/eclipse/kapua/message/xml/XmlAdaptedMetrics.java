package org.eclipse.kapua.message.xml;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "metrics")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class XmlAdaptedMetrics{

    private List<XmlAdaptedMetric> adaptedMetrics;
    
    public XmlAdaptedMetrics() {
        super();
    }
    
    public XmlAdaptedMetrics(List<XmlAdaptedMetric> adaptedItems) {
        setAdaptedMetrics(adaptedItems);
    }

    @XmlElement(name = "metric")
    public List<XmlAdaptedMetric> getAdaptedMetrics() {
        return adaptedMetrics != null ? adaptedMetrics : new ArrayList<>();
    }

    public void setAdaptedMetrics(List<XmlAdaptedMetric> adaptedMetrics) {
        this.adaptedMetrics = adaptedMetrics;
    }
}
