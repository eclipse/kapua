import "angular-patternfly";
import "angular-ui-router";
import "angular-local-storage";

import "angular-patternfly/node_modules/patternfly/dist/css/patternfly.css";
import "angular-patternfly/node_modules/patternfly/dist/css/patternfly-additions.css";
import "angular-patternfly/dist/styles/angular-patternfly.css";

import "./login/module.ts";
import "./layout/module.ts";
import "./welcome/module.ts";

angular.module("app", [
    "ui.router",
    "patternfly",
    "patternfly.charts",
    "LocalStorageModule",
    "app.login",
    "app.layout",
    "app.welcome"
]);

angular.bootstrap(document, ["app"], {
    strictDi: true
});