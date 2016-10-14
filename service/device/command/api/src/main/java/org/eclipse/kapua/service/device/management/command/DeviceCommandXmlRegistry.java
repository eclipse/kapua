package org.eclipse.kapua.service.device.management.command;

import javax.xml.bind.annotation.XmlRegistry;

import org.eclipse.kapua.locator.KapuaLocator;

@XmlRegistry
public class DeviceCommandXmlRegistry {
    private final KapuaLocator locator = KapuaLocator.getInstance();
    private final DeviceCommandFactory factory = locator.getFactory(DeviceCommandFactory.class);
    
    public DeviceCommandInput newCommandInput()
    {
        return factory.newCommandInput();
    }
    
    public DeviceCommandOutput newCommandOutput()
    {
        return factory.newCommandOutput();
    }
}
