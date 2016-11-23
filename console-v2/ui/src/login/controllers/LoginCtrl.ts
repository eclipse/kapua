interface ILoginData {
    username: string;
    password: string;
}

export default class LoginCtrl {
    private loginData: ILoginData;

    constructor(private $rootScope: angular.IRootScopeService,
        private $http: angular.IHttpService,
        private $state: angular.ui.IStateService,
        private localStorageService: angular.local.storage.ILocalStorageService,
        private $auth,
        private kapuaConfig) {
        if ($auth.isAuthenticated()) {
            $state.go("kapua.welcome");
        }
        $rootScope.$on("$stateChangeSuccess", (event, toState: angular.ui.IState, toParams, fromState: angular.ui.IState, fromParams) => {
            if (toState.name.indexOf("kapua.") === 0) {
                angular.element("html").addClass("layout-pf layout-pf-fixed");
            } else if (toState.name === "login") {
                angular.element("html").removeClass("layout-pf layout-pf-fixed");
            }
        });
    }

    private doLogin(loginData: ILoginData) {
        this.$auth.login(loginData).then((response: angular.IHttpPromiseCallbackArg<any>) => {
            this.setAuthToken(response.data.tokenId);
        },
            (responseData: angular.IHttpPromiseCallbackArg<any>) => {
                console.log("fail!");
            });
    }

    private doSSOLogin() {
        this.$auth.authenticate("oauth2").then((response: angular.IHttpPromiseCallbackArg<any>) => {
            this.setAuthToken(response.data.tokenId);
        });
    }

    private setAuthToken(token: string) {
        this.$auth.setToken(token);
        this.$state.go("kapua.welcome");
    }
}