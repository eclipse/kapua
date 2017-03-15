/*******************************************************************************
* Copyright (c) 2016, 2017 Eurotech and/or its affiliates                       
*                                                                               
* All rights reserved. This program and the accompanying materials              
* are made available under the terms of the Eclipse Public License v1.0         
* which accompanies this distribution, and is available at                      
* http://www.eclipse.org/legal/epl-v10.html                                     
*                                                                               
* Contributors:                                                                 
*     Eurotech - initial API and implementation                                 
*                                                                               
*******************************************************************************/
export default class DeviceMapperService implements IDeviceMapperService {
    
    constructor() {
    }

    prepareViewItems(device: Device): DeviceViewModel[] {
        let items: DeviceViewModel[] = [
            {
                name: "Cloud Connection Information",
                data: [
                    {
                        displayName: "Status",
                        value: device.status ? device.status : "UNDEFINED"
                    },
                    {
                        displayName: "Connection Status",
                        value: device.connection.status ? device.connection.status : "UNDEFINED"
                    },
                    {
                        displayName: "Connection ID",
                        value: device.connectionId ? device.connectionId : "UNDEFINED"
                    },
                    {
                        displayName: "Client ID",
                        value: device.clientId ? device.clientId : "UNDEFINED"
                    },
                    {
                        displayName: "Display Name:",
                        value: device.displayName ? device.displayName : "Untitled"
                    },
                    {
                        displayName: "ID",
                        value: device.id ? device.id : "UNDEFINED"
                    },
                    {
                        displayName: "Modified By",
                        value: device.modifiedBy ? device.modifiedBy : "UNDEFINED"
                    },
                    {
                        displayName: "Modified On",
                        value: device.modifiedOn ? device.modifiedOn : "UNDEFINED"
                    },
                    {
                        displayName: "Created By",
                        value: device.createdBy ? device.createdBy : "UNDEFINED"
                    },
                    {
                        displayName: "Created On",
                        value: device.createdOn ? device.createdOn : "UNDEFINED"
                    },
                    {
                        displayName: "Scope ID",
                        value: device.scopeId ? device.scopeId : "UNDEFINED"
                    },
                    {
                        displayName: "Accepted Payload Encoding",
                        value: device.acceptEncoding ? device.acceptEncoding : "UNDEFINED"
                    },
                    {
                        displayName: "Type",
                        value: device.type ? device.type : "UNDEFINED"
                    },
                    {
                        displayName: "OptLock",
                        value: device.optlock ? device.optlock : "UNDEFINED"
                    }
                ]
            },
            {
                name: "Device Credentials",
                data: [
                    {
                        displayName: "Device Credentials Mode",
                        value: device.deviceCredentialsMode ? device.deviceCredentialsMode : "UNDEFINED"
                    }
                ]
            },

            {
                name: "Hardware Information",
                data: [
                    {
                        displayName: "Model ID",
                        value: device.modelId ? device.modelId : "UNDEFINED"
                    },
                    {
                        displayName: "Serial Number",
                        value: device.serialNumber ? device.serialNumber : "UNDEFINED"
                    }
                ]
            },
            {
                name: "Java Information",
                data: [
                    {
                        displayName: "Java Virtual Machine Version",
                        value: device.jvmVersion ? device.jvmVersion : "UNDEFINED"
                    },
                    {
                        displayName: "OSGI Framework Version",
                        value: device.osgiFrameworkVersion ? device.osgiFrameworkVersion : "UNDEFINED"
                    }
                ]
            },
            {
                name: "Software Information",
                data: [
                    {
                        displayName: "Firmware Version",
                        value: device.firmwareVersion ? device.firmwareVersion : "UNDEFINED"
                    },
                    {
                        displayName: "BIOS Version",
                        value: device.biosVersion ? device.biosVersion : "UNDEFINED"
                    },
                    {
                        displayName: "Operating System Version",
                        value: device.osVersion ? device.osVersion : "UNDEFINED"
                    },
                    {
                        displayName: "Application Identifiers",
                        value: device.applicationIdentifiers ? device.applicationIdentifiers : "UNDEFINED"
                    },
                ]
            },

        ];
        return items;
    }

}