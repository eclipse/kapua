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
import "satellizer";

import LoginCtrl from "./controllers/LoginCtrl";

import "./assets/styles/login.scss";

angular.module("app.login", ["satellizer", "app.commons"])
    .config(["$stateProvider", "$authProvider", "kapuaConfig",
        (
            $stateProvider: angular.ui.IStateProvider,
            $authProvider,
            kapuaConfig
        ) => {
            $stateProvider.state("login", {
                url: "/login",
                views: {
                    mainView: {
                        template: require("./views/login.html"),
                        controller: "LoginCtrl as vm"
                    }
                }
            });

            let currentIdentityProvider = kapuaConfig.oauth.currentIdentityProvider;
            let currentIdentityProviderConfig = kapuaConfig.oauth.customIdentityProviders[currentIdentityProvider];
            $authProvider.oauth2(currentIdentityProviderConfig);

            $authProvider.loginUrl = "/api/authentication/user";
        }])
    .run(["$templateCache", (
        $templateCache: angular.ITemplateCacheService
    ) => {
        $templateCache.put("login/views/login-user-pass.html", require("./views/login-user-pass.html"));
        $templateCache.put("login/views/login-sso.html", require("./views/login-sso.html"));
        $templateCache.put("login/views/login-both.html", require("./views/login-both.html"));
    }])
    .controller("LoginCtrl", ["$rootScope", "$http", "$state", "localStorageService", "$auth", "kapuaConfig", LoginCtrl]);