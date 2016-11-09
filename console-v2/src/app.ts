import "angular-patternfly";
import "angular-ui-router";

import "angular-patternfly/node_modules/patternfly/dist/css/patternfly.css";
import "angular-patternfly/dist/styles/angular-patternfly.css";

import "./login/module.ts";

angular.module("app", [
    "ui.router",
    "app.login"
]);

angular.bootstrap(document, ["app"], {
    strictDi: true
});