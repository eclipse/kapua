package org.eclipse.kapua.service.device.call.kura.model.deploy;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Kura deployment package.
 * 
 * @since 1.0
 *
 */
@XmlRootElement(name = "package")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "name", "version", "bundleInfos" })
public class KuraDeploymentPackage
{
    @XmlElement(name = "name")
    public String name;

    @XmlElement(name = "version")
    public String version;

    @XmlElementWrapper(name = "bundles")
    @XmlElement(name = "bundle")
    public KuraBundleInfo[] bundleInfos;

    /**
     * Get the deployment package name
     * 
     * @return
     */
    public String getName()
    {
        return name;
    }

    /**
     * Set the deployment package name
     * 
     * @param name
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Get the deployment package version
     * 
     * @return
     */
    public String getVersion()
    {
        return version;
    }

    /**
     * Set the deployment package version
     * 
     * @param version
     */
    public void setVersion(String version)
    {
        this.version = version;
    }

    /**
     * Get the bundle information array
     * 
     * @return
     */
    public KuraBundleInfo[] getBundleInfos()
    {
        return bundleInfos;
    }

    /**
     * Set the bundle information array
     * 
     * @param bundleInfos
     */
    public void setBundleInfos(KuraBundleInfo[] bundleInfos)
    {
        this.bundleInfos = bundleInfos;
    }
}
