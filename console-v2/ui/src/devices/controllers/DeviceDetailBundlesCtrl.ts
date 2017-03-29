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
export default class DeviceDetailBundlesCtrl {
    private deviceId: string;
    private bundles: DeviceBundle[];

    constructor(private $stateParams: angular.ui.IStateParamsService,
        private $http: angular.IHttpService,
        private $scope: any,
        private devicesService: IDevicesService) {
        $scope.deviceId = $stateParams["id"];

        this.getBundles($scope.deviceId);

        let startBundle = function (action, item) {
            alert("Starting bundle with ID: " + item.id);
            // devicesService.startDeviceBundle($scope.deviceId, item.id).then((responseData: ng.IHttpPromiseCallbackArg<DeviceBundles>) => {
            //     $scope.bundles = responseData.data.bundle;
            // });
        }

        let stopBundle = function (action, item): void {
            alert("Stopping bundle with ID: " + item.id);
            // devicesService.stopDeviceBundle($scope.deviceId, item.id).then((responseData: ng.IHttpPromiseCallbackArg<DeviceBundles>) => {
            //     $scope.bundles = responseData.data.bundle;
            // });
        }

        $scope.actionButtons = [
            {
                name: 'Start',
                class: 'btn-primary',
                title: 'Start bundle',
                actionFn: startBundle
            },
            {
                name: 'Stop',
                class: 'btn-danger',
                title: 'Stop bundle',
                actionFn: stopBundle
            }
        ];
    }

    private config = {
        selectedItems: false,
        useExpandingRows: false,
        showSelectBox: false
    };

    getBundles(deviceID: string) {
        this.devicesService.getBundlesByDeviceId(this.$scope.deviceId).then((responseData: ng.IHttpPromiseCallbackArg<DeviceBundles>) => {
            this.$scope.bundles = responseData.data.bundle;
        });
    }
    enableButtonForItemFn(action, item): boolean {
        return action.name === 'Start' && item.state !== 'ACTIVE' || action.name === 'Stop' && item.state !== 'RESOLVED';
    }









}