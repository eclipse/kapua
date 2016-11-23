export default class LayoutCtrl {
    navigationItems = [
        {
            title: "Welcome",
            iconClass: "fa fa-info",
            href: "#/welcome"
        },
        {
            title: "Devices",
            iconClass: "fa fa-hdd-o",
            href: "#/devices"
        },
        {
            title: "Users",
            iconClass: "fa fa-users",
            href: "#/users"
        },
        {
            title: "Settings",
            iconClass: "fa fa-cog",
            href: "#/settings"
        },
        {
            title: "Child Accounts",
            iconClass: "fa fa-sitemap",
            href: "#/child-accounts"
        }
    ];

    constructor(private $http: angular.IHttpService,
        private $state: angular.ui.IStateService,
        private localStorageService: angular.local.storage.ILocalStorageService,
        private $auth,
        private kapuaConfig) {
            
         }

    private getLogoImage() {
        return require("../assets/img/logo-white.svg");
    }

    private doLogout() {
        this.$http.post(this.kapuaConfig.restApi.baseUrl + "/authentication/logout", {}).then((response: angular.IHttpPromiseCallbackArg<any>) => {
            this.$state.go("login");
        });
    }
}