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
import "angular-patternfly";
import "angular-ui-router";
import "angular-local-storage";
import "angular-patternfly/node_modules/patternfly/node_modules/datatables/media/js/jquery.dataTables.js";

import "angular-patternfly/node_modules/patternfly/dist/css/patternfly.css";
import "angular-patternfly/node_modules/patternfly/dist/css/patternfly-additions.css";
import "angular-patternfly/dist/styles/angular-patternfly.css";

import "./commons/module.ts";
import "./login/module.ts";
import "./layout/module.ts";
import "./welcome/module.ts";
import "./users/module.ts";
import "./devices/module.ts";

angular.module("app", [
    "ngAnimate",
    "ui.bootstrap",
    "ui.router",
    "patternfly",
    "patternfly.charts",
    "LocalStorageModule",
    "app.commons",
    "app.login",
    "app.layout",
    "app.welcome",
    "app.users",
    "app.devices"
])
    .config(["$locationProvider", "$urlRouterProvider", (
        $locationProvider: angular.ILocationProvider,
        $urlRouterProvider: angular.ui.IUrlRouterProvider
    ) => {
        $locationProvider.html5Mode({ enabled: true, requireBase: false });
        $urlRouterProvider.otherwise("/login");
    }])
    .run(["$state", "$rootScope", "$auth",
        (
            $state: angular.ui.IStateService,
            $rootScope: angular.IRootScopeService,
            $auth
        ) => {
            $rootScope.$on("$stateChangeStart", (
                event: angular.IAngularEvent,
                toState: angular.ui.IState,
                toParams, fromState: angular.ui.IState,
                fromParams
            ) => {
                if (toState.name.indexOf("kapua.") === 0 && !$auth.isAuthenticated()) {
                    event.preventDefault();
                    $state.go("login");
                }
            });

            $rootScope.$on("$stateChangeSuccess", (
                event: angular.IAngularEvent,
                toState: angular.ui.IState,
                toParams,
                fromState: angular.ui.IState,
                fromParams
            ) => {
                $rootScope["showHeader"] = toState.name.indexOf("kapua.") === 0;
            });
        }]);