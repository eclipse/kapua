/*******************************************************************************
* Copyright (c) 2011, 2016 Eurotech and/or its affiliates                       
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
export default class DeviceDetailPackagesCtrl {
  private deviceId: string;
  private packages: DevicePackage[];

  constructor(private $stateParams: angular.ui.IStateParamsService,
    private $scope: any,
    private $http: angular.IHttpService,
    private $templateCache: any,
    private devicesService: IDevicesService) {
    $scope.deviceId = $stateParams["id"];

    this.getPackages($scope.deviceId);

    let uninstallPackage = function (action, item) {
      alert("Uninstalling: " + item.name);
      // devicesService.uninstallPackage($scope.deviceId, item);
    }

    var uninstallButtonInclude = '<span class="fa fa-minus-circle"></span> {{actionButton.name}}';
    this.$templateCache.put('uninstall-button', uninstallButtonInclude);

    $scope.actionButtons = [
      {
        name: 'Uninstall',
        class: 'btn-primary',
        title: 'Uninstall package',
        include: 'uninstall-button',
        actionFn: uninstallPackage
      }
    ];
  }

  private config = {
    selectedItems: false,
    useExpandingRows: true,
    showSelectBox: false
  };

  installPackage(packageItem): void {
    alert("Installing ...");
    this.devicesService.downloadPackage(this.$scope.deviceId, packageItem);
  }
  

  getPackages(deviceID: string): void {
    this.devicesService.getPackagesByDeviceId(deviceID).then((responseData: ng.IHttpPromiseCallbackArg<DevicePackages>) => {
      this.$scope.packages = responseData.data.devicePackage;
      this.$scope.packages.forEach(devPackage => {
        if (!devPackage.bundleInfos.bundleInfo.length)
          devPackage.disableRowExpansion = true;
      });
    });
  }
}