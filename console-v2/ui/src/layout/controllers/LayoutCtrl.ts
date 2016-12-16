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