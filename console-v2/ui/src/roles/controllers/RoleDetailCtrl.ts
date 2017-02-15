/*******************************************************************************
* Copyright (c) 2016, 2017 Eurotech and/or its affiliates                       
*                                                                               
* All rights reserved. This program and the accompanying materials              
* are made available under the terms of the Eclipse Public License v1.0         
* which accompanies this distribution, and is available at                      
* http://www.eclipse.org/legal/epl-v10.html                                     
*                                                                               
* Contributors:                                                                 
*     Eurotech - initial API and implementation                                 
*                                                                               
*******************************************************************************/
export default class RoleDetailCtrl {
    private role: Role;
    private roleDescription: RoleDescriptions;
    private RolePermissions: RolePermissions;
    private roleSubjects: RoleSubjects;
    private descriptionStatus = {
        "title": "Description",
        "count": 0,
        "href": null,
        "iconClass": "fa fa-file-text",
        "notifications": []
    };
    private permissionsStatus = {
        "title": "Permissions",
        "count": 1,
        "href": null,
        "iconClass": "fa fa-key",
        "notifications": []
    };
    private subjectsStatus = {
        "title": "Subjects",
        "count": 2,
        "href": null,
        "iconClass": "fa fa-pencil-square-o",
        "notifications": []
    };

    constructor(private $stateParams: angular.ui.IStateParamsService,
        private $http: angular.IHttpService) {
        this.descriptionStatus.href = `roles/${$stateParams["idRole"]}/description`;
        this.permissionsStatus.href = `roles/${$stateParams["idRole"]}/permissions`;
        this.subjectsStatus.href = `roles/${$stateParams["idRole"]}/subjects`;
    }
}