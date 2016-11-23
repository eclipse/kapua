import "angular-patternfly";
import "angular-ui-router";
import "angular-local-storage";

import "angular-patternfly/node_modules/patternfly/dist/css/patternfly.css";
import "angular-patternfly/node_modules/patternfly/dist/css/patternfly-additions.css";
import "angular-patternfly/dist/styles/angular-patternfly.css";

import "./constants/module.ts";
import "./login/module.ts";
import "./layout/module.ts";
import "./welcome/module.ts";
import "./users/module.ts";

angular.module("app", [
    "ui.router",
    "patternfly",
    "patternfly.charts",
    "LocalStorageModule",
    "app.constants",
    "app.login",
    "app.layout",
    "app.welcome",
    "app.users"
])
    .config(["$locationProvider", "$urlRouterProvider",
        ($locationProvider: angular.ILocationProvider,
         $urlRouterProvider: angular.ui.IUrlRouterProvider) => {
            $locationProvider.html5Mode({ enabled: true, requireBase: false });
        }])
    .run(["$state", "$rootScope", "$auth",
        ($state: angular.ui.IStateService,
         $rootScope: angular.IRootScopeService,
         $auth) => {
            $rootScope.$on("$stateChangeStart", (event: angular.IAngularEvent, toState: angular.ui.IState, toParams, fromState: angular.ui.IState, fromParams) => {
                if (toState.name.indexOf("kapua.") === 0 && !$auth.isAuthenticated()) {
                    event.preventDefault();
                    $state.go("login");
                }
            });
            $rootScope.$on("$locationChangeSuccess", function() {
                if (!$state.transition) {
                    $state.go("login");
                }
            });
        }]);

angular.bootstrap(document, ["app"], {
    strictDi: true
});