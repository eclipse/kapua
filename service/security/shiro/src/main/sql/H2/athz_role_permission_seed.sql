/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
 *  
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *  
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/

INSERT INTO athz_role_permission 
	VALUES
		(1,  1, NOW(), 1, 1, 'account', 			null, null, null),
		
		(1,  2, NOW(), 1, 1, 'broker', 				null, null, null),

		(1,  3, NOW(), 1, 1, 'device',				null, null, null),
		(1,  4, NOW(), 1, 1, 'device_connection', 	null, null, null),
		(1,  5, NOW(), 1, 1, 'device_event', 		null, null, null),
		(1,  6, NOW(), 1, 1, 'device_lifecycle', 	null, null, null),

		(1,  7, NOW(), 1, 1, 'device_management',	null, null, null),

		(1,  8, NOW(), 1, 1, 'data', 				null, null, null),
		
		(1,  9, NOW(), 1, 1, 'credential',			null, null, null),
		(1,	10, NOW(), 1, 1, 'access_token',		null, null, null),

		(1, 11, NOW(), 1, 1, 'access_info',			null, null, null),
		(1, 12, NOW(), 1, 1, 'role',				null, null, null),
		
		(1, 13, NOW(), 1, 1, 'user',				null, null, null),

		(1, 14, NOW(), 1, 1, 'domain',				null, null, null),

		(1, 15, NOW(), 1, 1, 'group',       null, null, null),
		