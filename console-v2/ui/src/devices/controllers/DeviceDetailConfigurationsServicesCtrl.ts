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
export default class DeviceDetailConfigurationsServicesCtrl {
    private deviceId: string;
    private configs: DeviceConfiguration[];
    private configsResetCopy: DeviceConfiguration[];

    constructor(private $stateParams: angular.ui.IStateParamsService,
        private $http: angular.IHttpService,
        private devicesService: IDevicesService) {
        this.deviceId = $stateParams["id"];
        this.getConfigsByDeviceId(this.deviceId);
    }

    removeCharacters(str: string): string {
        return str.replace(/[^a-zA-Z ]/g, "")
    }

    findConfigForResetCopy(configId): DeviceConfiguration {
        let resetConfigs: DeviceConfiguration[] = angular.copy(this.configsResetCopy);
        let resetConfig: DeviceConfiguration;
        resetConfigs.forEach((config: DeviceConfiguration) => {
            if (config.id === configId)
                resetConfig = config;
        });
        return resetConfig;
    }

    resetForm(config: DeviceConfiguration): void {
        let form = this['form' + config.definition.name];
        form.$setPristine();
        form.$setUntouched();
        config.properties.property = this.findConfigForResetCopy(config.id).properties.property;
    }

    findPropertyIndex(config: DeviceConfiguration, AD: AD): number {
        let index: number;
        config.properties.property.forEach((property: Property) => {
            if (property.name === AD.name)
                index = config.properties.property.indexOf(property);
        });
        return index;
    }

    prepareConfigForPost(config: DeviceConfiguration): DeviceConfiguration {
        let dataForPost: DeviceConfiguration = angular.copy(config);
        dataForPost.properties.property.forEach((property: Property) => {
            property.value[0] = property.value[0].toString();
        });
        return dataForPost;
    }

    prepareConfigsForView(configs: DeviceConfiguration[]): void {
        this.configs = configs;
        this.configs.forEach((config: DeviceConfiguration) => {
            config.definition.AD.forEach((AD: AD) => {
                if (AD.type === "Integer" && !AD.Option.length)
                    config.properties.property[this.findPropertyIndex(config, AD)].value[0] = parseInt(config.properties.property[this.findPropertyIndex(config, AD)].value[0]);
                if ((AD.type === "Double" || AD.type === "Float") && !AD.Option.length)
                    config.properties.property[this.findPropertyIndex(config, AD)].value[0] = parseFloat(config.properties.property[this.findPropertyIndex(config, AD)].value[0]);
            });
        });
        this.configsResetCopy = angular.copy(this.configs);
    }

    getConfigsByDeviceId(deviceID: string) {
        this.devicesService.getConfigsByDeviceId(deviceID).then((responseData: ng.IHttpPromiseCallbackArg<DeviceConfigurations>) => {
            this.prepareConfigsForView(responseData.data.configuration);
        });
    }

    applyConfig(config: DeviceConfiguration, deviceID: string) {
        alert("Applaying changes on: " + config.definition.name);
        // this.devicesService.applyConfig(this.prepareConfigForPost(config), deviceID).then((responseData: ng.IHttpPromiseCallbackArg<DeviceConfigurations>) => {
        //     this.prepareConfigsForView(responseData.data.configuration);
        // });
    }

}