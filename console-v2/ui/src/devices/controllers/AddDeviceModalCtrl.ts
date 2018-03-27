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
export default class AddDeviceModalCtrl {
    constructor(private $modalInstance: angular.ui.bootstrap.IModalServiceInstance,
        private $http: angular.IHttpService,
        private devicesService: IDevicesService) { 
            this.getGroups();
        }

    private regex: string = "^[a-zA-Z0-9-_]*$";
    private cliendIdMinLength: number = 3;
    private groups: Group[];

    getGroups(): any {
        this.devicesService.getGroups().then((responseData: ng.IHttpPromiseCallbackArg<ListResult<Group>>) => {
            this.groups = responseData.data.items.item;
        });
    }
    ok() {
        this.$modalInstance.close("ok");
    }
    cancel() {
        this.$modalInstance.dismiss("cancel");
    }
}
