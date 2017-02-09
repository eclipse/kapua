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
    private roleDescriptions: RoleDescriptions;
    private descriptionStatus = {
        "title": "Description",
        "count": 0,
        "href": null,
        "iconClass": "fa fa-file-text",
        "notifications": [
            // {
            //     "iconClass": "pficon pficon-error-circle-o",
            //     "count": 4,
            //     "href": "#"
            // },
            // {
            //     "iconClass": "pficon pficon-warning-triangle-o",
            //     "count": 1
            // }
        ]
    };

    constructor(private $stateParams: angular.ui.IStateParamsService,
        private $http: angular.IHttpService) {
        this.descriptionStatus.href = `roles/${$stateParams["idRole"]}/description`;
    }
}