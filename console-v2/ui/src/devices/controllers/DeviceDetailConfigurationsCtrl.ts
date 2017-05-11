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
export default class DeviceDetailConfigurationsCtrl {
    private configs: DeviceConfiguration[];
    private configsResetCopy: DeviceConfiguration[];
    private snapshots: DeviceSnapshot[];

    constructor(private $scope: any,
        private $stateParams: angular.ui.IStateParamsService,
        private $http: angular.IHttpService,
        private $templateCache: any,
        private devicesService: IDevicesService) {
        $scope.deviceId = $stateParams["id"];
        this.getConfigsByDeviceId($scope.deviceId);
        this.getSnapshots($scope.deviceId);

        let rollbackSnapshot = function (action, item) {
            alert("Rollback snapshot with ID: " + item.id);
            // devicesService.rollbackSnapshot($scope.deviceId, item).then((responseData: ng.IHttpPromiseCallbackArg<DeviceSnapshots>) => {
            //     $scope.snapshots = responseData.data.snapshotId;
            // });
        }

        let downloadSnapshot = function (action, item): void {
            alert("Download snapshot with ID: " + item.id);
        }

        $scope.actionButtons = [
            {
                name: 'Rollback',
                class: 'btn-default',
                title: 'Rollback',
                include: 'rollback-button',
                actionFn: rollbackSnapshot
            },
            {
                name: 'Download',
                class: 'btn-primary',
                title: 'Download',
                include: 'download-button',
                actionFn: downloadSnapshot
            }
        ];

        var rollbackButtonInclude = '<span class="fa fa-undo"></span> {{actionButton.name}}';
        this.$templateCache.put('rollback-button', rollbackButtonInclude);

        var downloadButtonInclude = '<span class="fa fa-download"></span> {{actionButton.name}}';
        this.$templateCache.put('download-button', downloadButtonInclude);

    }

    private config = {
        selectedItems: false,
        useExpandingRows: false,
        showSelectBox: false
    };

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

    getDateFromJSON(timestamp: number): Date {
        return new Date(timestamp);
    }

    getConfigsByDeviceId(deviceID: string) {
        this.devicesService.getConfigsByDeviceId(deviceID).then((responseData: ng.IHttpPromiseCallbackArg<DeviceConfigurations>) => {
            this.prepareConfigsForView(responseData.data.configuration);
        });
    }

    getSnapshots(deviceID: string) {
        this.devicesService.getSnapshotsByDeviceId(deviceID).then((responseData: ng.IHttpPromiseCallbackArg<DeviceSnapshots>) => {
            this.$scope.snapshots = responseData.data.snapshotId;
        });
    }

    applyConfig(config: DeviceConfiguration, deviceID: string) {
        alert("Applaying changes on: " + config.definition.name);
        // this.devicesService.applyConfig(this.prepareConfigForPost(config), deviceID).then((responseData: ng.IHttpPromiseCallbackArg<DeviceConfigurations>) => {
        //     this.prepareConfigsForView(responseData.data.configuration);
        // });
    }

    uploadAndApplySnapshot() {
        alert("Snapshot uploading ... ");
    }

}