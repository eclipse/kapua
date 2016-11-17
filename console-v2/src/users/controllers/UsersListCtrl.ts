interface IUserResponse {
    type: string;
    limitExceeded: boolean;
    size: number;
    items: {
        item: IUser[];
    };
}

interface IUser {
    type: string;
    id: string;
    scopeId: string;
    createdOn: string;
    createdBy: string;
    modifiedOn: string;
    modifiedBy: string;
    optlock: number;
    name: string;
    status: string;
    displayName: string;
    email: string;
    phoneNumber: string;
}

export default class UsersListCtrl {
    private users: IUser[];
    constructor(private $http: angular.IHttpService,
                localStorageService: angular.local.storage.ILocalStorageService) {
        this.$http.get("http://localhost:8080/api/v1/users").then((responseData: angular.IHttpPromiseCallbackArg<IUserResponse>) => {
            this.users = responseData.data.items.item;
        });
    }
}