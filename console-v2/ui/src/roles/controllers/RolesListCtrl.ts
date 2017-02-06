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
interface IRoleResponse {
    items: {
        item: IRole[];
    };
}

interface IRole {

}

export default class RolesListCtrl {
    private roles: IRole[];
    constructor(private $http: angular.IHttpService,
        localStorageService: angular.local.storage.ILocalStorageService) {
        // will be done after implement service

        // this.$http.get("/api/roles").then((responseData: angular.IHttpPromiseCallbackArg<IRoleResponse>) => {
        //     this.roles = responseData.data.items.item;
        // });
    }
}