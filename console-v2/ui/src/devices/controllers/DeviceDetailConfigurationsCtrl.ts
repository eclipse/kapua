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
    private deviceId: string;
    private configs: DeviceConfiguration[];

    constructor(private $stateParams: angular.ui.IStateParamsService,
        private $http: angular.IHttpService,
        private devicesService: IDevicesService) {
        this.deviceId = $stateParams["id"];

        this.services.href = `devices/${$stateParams["id"]}/configurations/services`;
        this.snapshots.href = `devices/${$stateParams["id"]}/configurations/snapshots`;
    }

    private services = {
        "title": "Services",
        "count": null,
        "href": null,
        "iconClass": "fa fa-puzzle-piece"
    };
    private snapshots = {
        "title": "Snapshots",
        "count": null,
        "href": null,
        "iconClass": "fa fa-file-image-o"
    };


}