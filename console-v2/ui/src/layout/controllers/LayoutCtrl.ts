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
export default class LayoutCtrl {
    public navigationItems;
    constructor(
        private $http: angular.IHttpService,
        private $state: angular.ui.IStateService,
        private localStorageService: angular.local.storage.ILocalStorageService,
        private $auth,
        private kapuaConfig,
        private $rootScope: angular.IRootScopeService,
        private externalModulesList,
    ) { 
        this.navigationItems = externalModulesList.getModules();
    }

    private getLogoImage() {
        return require("../assets/img/logo-white.svg");
    }

    private doLogout() {
        this.$http.post("/api/authentication/logout", {}).then((response: angular.IHttpPromiseCallbackArg<any>) => {
            this.$auth.logout();
            this.$state.go("login");
        });
    }
}