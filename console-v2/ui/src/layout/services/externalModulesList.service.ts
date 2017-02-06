import ListItem from './listItem.model';
import {IExternalModulesList} from './iExternalModulesList.service';

 export default class ExternalModulesList implements IExternalModulesList {
    injection(): any[] {
    return [ExternalModulesList]
    }

    constructor() { }

    getModules(): ListItem[] {
        //TODO: This should be api call    
        var moduleListJson =  [
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
                title: "Roles",
                iconClass: "fa fa-user-plus",
                href: "#/roles"
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

        var moduleList = [];

        if(moduleListJson.length > 0) {
            for(var item in moduleListJson) {
             moduleList.push(new ListItem(moduleListJson[item]));
            }
        }

        return moduleList;
    }
  }