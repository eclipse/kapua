package org.eclipse.kapua.service.device.management.configuration;

import javax.xml.bind.annotation.XmlRegistry;

import org.eclipse.kapua.locator.KapuaLocator;

@XmlRegistry
public class DeviceConfigurationXmlRegistry {
    private final KapuaLocator locator = KapuaLocator.getInstance();
    private final DeviceConfigurationFactory factory = locator.getFactory(DeviceConfigurationFactory.class);

    public DeviceConfiguration newConfiguration()
    {
        return factory.newConfigurationInstance();
    }
    
    public DeviceComponentConfiguration newComponentConfiguration()
    {
        return factory.newComponentConfigurationInstance(null);
    }
}
