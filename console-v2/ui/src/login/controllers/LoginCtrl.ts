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
interface ILoginData {
    username: string;
    password: string;
}

export default class LoginCtrl {
    private loginData: ILoginData;

    constructor(
        private $rootScope: angular.IRootScopeService,
        private $http: angular.IHttpService,
        private $state: angular.ui.IStateService,
        private localStorageService: angular.local.storage.ILocalStorageService,
        private $auth,
        private kapuaConfig
    ) {
        if ($auth.isAuthenticated()) {
            $state.go("kapua.welcome");
        }
    }

    private doLogin(loginData: ILoginData) {
        this.$auth.login(loginData).then(
            (response: angular.IHttpPromiseCallbackArg<any>) => {
                this.setAuthToken(response.data.tokenId);
            },
            (responseData: angular.IHttpPromiseCallbackArg<any>) => {
                console.log("fail!");
            });
    }

    private doSSOLogin() {
        this.$auth.authenticate("oauth2").then((response: angular.IHttpPromiseCallbackArg<any>) => {
            this.setAuthToken(response.data.tokenId);
        });
    }

    private setAuthToken(token: string) {
        this.$auth.setToken(token);
        this.$state.go("kapua.welcome");
    }
}