/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *  
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *  
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/

INSERT INTO athz_domain
	VALUES
		(NULL,  1, NOW(), 'USER', 1, 'account',				'accountService'),
		
		(NULL,  2, NOW(), 'USER', 1, 'broker',				'brokerService'),
		
		(NULL,  3, NOW(), 'USER', 1, 'credential',			'credentialService'),
		(NULL,  4, NOW(), 'USER', 1, 'access_token',		'accessTokenService'),
		
		(NULL,  5, NOW(), 'USER', 1, 'access_info',			'accessInfoService'),
		(NULL,  6, NOW(), 'USER', 1, 'domain',				'domainService'),
		(NULL,  7, NOW(), 'USER', 1, 'group',				'groupService'),
		(NULL,  8, NOW(), 'USER', 1, 'role',				'roleService'),

		(NULL,  9, NOW(), 'USER', 1, 'data',				'datastoreService'),

		(NULL, 10, NOW(), 'USER', 1, 'device',				'deviceService'),
		(NULL, 11, NOW(), 'USER', 1, 'device_connection',	'deviceConnectionService'),
		(NULL, 12, NOW(), 'USER', 1, 'device_management',	'deviceManagementService'),
		(NULL, 13, NOW(), 'USER', 1, 'device_event',		'deviceEventService'),
		(NULL, 14, NOW(), 'USER', 1, 'device_lifecycle',	'deviceLifecycleService'),
		
		(NULL, 15, NOW(), 'USER', 1, 'user',				'userService');
