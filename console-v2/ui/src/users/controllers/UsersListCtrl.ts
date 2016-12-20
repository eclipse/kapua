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
interface IUserResponse {
    type: string;
    limitExceeded: boolean;
    size: number;
    items: {
        item: IUser[];
    };
}

interface IUser {
    type: string;
    id: string;
    scopeId: string;
    createdOn: string;
    createdBy: string;
    modifiedOn: string;
    modifiedBy: string;
    optlock: number;
    name: string;
    status: string;
    displayName: string;
    email: string;
    phoneNumber: string;
}

export default class UsersListCtrl {
    private users: IUser[];
    constructor(private $http: angular.IHttpService,
                localStorageService: angular.local.storage.ILocalStorageService) {
        this.$http.get("/api/users").then((responseData: angular.IHttpPromiseCallbackArg<IUserResponse>) => {
            this.users = responseData.data.items.item;
        });
    }
}