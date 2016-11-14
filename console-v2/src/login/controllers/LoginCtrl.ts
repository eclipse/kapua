export default class LayoutCtrl {
    constructor($rootScope: angular.IRootScopeService) {
        $rootScope.$on("$stateChangeSuccess", (event, toState: angular.ui.IState, toParams, fromState: angular.ui.IState, fromParams) => {
            if (toState.name.indexOf("kapua.") === 0) {
                angular.element("html").addClass("layout-pf layout-pf-fixed");
            } else if (toState.name === "login") {
                angular.element("html").removeClass("layout-pf layout-pf-fixed");
            }
        });
    }
}