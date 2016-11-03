package org.eclipse.kapua.service.device.management.command;

import javax.xml.bind.annotation.XmlRegistry;

import org.eclipse.kapua.locator.KapuaLocator;

/**
 * Device bundle xml factory class
 * 
 * @since 1.0
 *
 */
@XmlRegistry
public class DeviceCommandXmlRegistry {
    private final KapuaLocator locator = KapuaLocator.getInstance();
    private final DeviceCommandFactory factory = locator.getFactory(DeviceCommandFactory.class);
    
    /**
     * Creates a new device command input
     * 
     * @return
     */
    public DeviceCommandInput newCommandInput()
    {
        return factory.newCommandInput();
    }
    
    /**
     * Creates a new device command output
     * 
     * @return
     */
    public DeviceCommandOutput newCommandOutput()
    {
        return factory.newCommandOutput();
    }

}
