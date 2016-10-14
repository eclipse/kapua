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

INSERT INTO athz_user_permission (`scope_id`, `id`, `created_on`, `created_by`, `user_id`, `domain`) 
		VALUES 	('1', '1', CURRENT_TIMESTAMP(), '1', '1', 'account'),
			   	('1', '2', CURRENT_TIMESTAMP(), '1', '1', 'user'),
				('1', '3', CURRENT_TIMESTAMP(), '1', '1', 'device_event'),
				('1', '4', CURRENT_TIMESTAMP(), '1', '1', 'device_connection'),
				('1', '5', CURRENT_TIMESTAMP(), '1', '1', 'device'),
				('1', '6', CURRENT_TIMESTAMP(), '1', '1', 'data'),
				('1', '7', CURRENT_TIMESTAMP(), '1', '1', 'broker'),
				('1', '8', CURRENT_TIMESTAMP(), '1', '1', 'credential'),
				('1', '9', CURRENT_TIMESTAMP(), '1', '1', 'role'),
				('1', '10', CURRENT_TIMESTAMP(), '1', '1', 'user_permission'),				
				('1', '11', CURRENT_TIMESTAMP(), '1', '1', 'device_lifecycle'),
				('1', '12', CURRENT_TIMESTAMP(), '1', '1', 'device_management'),
				('1', '101', CURRENT_TIMESTAMP(), '1', '2', 'account'),
				('1', '102', CURRENT_TIMESTAMP(), '1', '2', 'user'),
				('1', '103', CURRENT_TIMESTAMP(), '1', '2', 'device_event'),
				('1', '104', CURRENT_TIMESTAMP(), '1', '2', 'device_connection'),
				('1', '105', CURRENT_TIMESTAMP(), '1', '2', 'device'),
				('1', '106', CURRENT_TIMESTAMP(), '1', '2', 'data'),
				('1', '107', CURRENT_TIMESTAMP(), '1', '2', 'broker'),
				('1', '108', CURRENT_TIMESTAMP(), '1', '2', 'credential'),
				('1', '109', CURRENT_TIMESTAMP(), '1', '2', 'role'),
				('1', '110', CURRENT_TIMESTAMP(), '1', '2', 'user_permission'),
				('1', '111', CURRENT_TIMESTAMP(), '1', '2', 'device_lifecycle'),
				('1', '112', CURRENT_TIMESTAMP(), '1', '2', 'device_management');
