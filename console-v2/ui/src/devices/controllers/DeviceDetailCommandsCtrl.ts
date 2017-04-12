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
export default class DeviceDetailCommandsCtrl {
    private deviceId: string;
    private execute: string;
    private servicePassword: string;

    constructor(private $scope: any,
        private $stateParams: angular.ui.IStateParamsService,
        private deviceService: IDevicesService) {
        this.deviceId = $stateParams["id"];

        $scope.fileUpload = function (element) {
            $scope.file = element.files[0];
            $scope.filePath = element.value;
            $scope.$apply();
        }
    }

    resetForm(): void {
        this.execute = "";
        this.servicePassword = "";
        this.$scope.file = null;
        this.$scope.filePath = "";
    }

    executeCommand(deviceID: string): void {
        alert("Command executed ... ");
        // this.deviceService.executeCommand(deviceID);
    }
}