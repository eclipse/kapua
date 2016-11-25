import "satellizer";

import LoginCtrl from "./controllers/LoginCtrl";

import "./assets/styles/login.scss";

angular.module("app.login", ["satellizer", "app.constants"])
    .config(["$stateProvider", "$authProvider", "kapuaConfig",
        ($stateProvider: angular.ui.IStateProvider,
            $authProvider,
            kapuaConfig) => {
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
            // TODO remove with Authorization: Bearer
            $authProvider.tokenHeader = "X-Access-Token";
            $authProvider.tokenType = "";
        }])
    .controller("LoginCtrl", ["$rootScope", "$http", "$state", "localStorageService", "$auth", "kapuaConfig", LoginCtrl]);