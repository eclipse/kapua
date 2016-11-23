interface ILoginData {
    username: string;
    password: string;
}

export default class LoginCtrl {
    private loginData: ILoginData;

    constructor(
        private $rootScope: angular.IRootScopeService,
        private $http: angular.IHttpService,
        private $state: angular.ui.IStateService,
        private localStorageService: angular.local.storage.ILocalStorageService,
        private $auth,
        private kapuaConfig
    ) {
        if ($auth.isAuthenticated()) {
            $state.go("kapua.welcome");
        }
    }

    private doLogin(loginData: ILoginData) {
        this.$auth.login(loginData).then(
            (response: angular.IHttpPromiseCallbackArg<any>) => {
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