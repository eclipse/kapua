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

INSERT INTO `act_account` (
	`scope_id`,
	`id`,
	`name`,
	`created_on`,
	`created_by`,
	`modified_on`,
	`modified_by`,
	`org_name`,
	`org_person_name`,
	`org_email`,
	`org_phone_number`,
	`org_address_line_1`,
	`org_address_line_2`,
	`org_address_line_3`,
	`org_zip_postcode`,
	`org_city`,
	`org_state_province_county`,
	`org_country`,
	`parent_account_path`,
	`optlock`,
	`attributes`,
	`properties`)
VALUES (NULL,
		1,
		'kapua-sys',
		CURRENT_TIMESTAMP(),
		1,
		CURRENT_TIMESTAMP(),
		1,
		'kapua-org',
		'Kapua Sysadmin',
		'kapua-sys@eclipse.org',
		'+1 555 123 4567',
		NULL,
		NULL,
		NULL,
		NULL,
		NULL,
		NULL,
        NULL,
		'\1',
		0,
		NULL,
		NULL);

INSERT INTO atht_credential (scope_id, id, created_on, created_by, modified_on, modified_by, subject_type, subject_id, type, key, secret, optlock) 
		VALUES ('1', '1', CURRENT_TIMESTAMP(), '1', CURRENT_TIMESTAMP(), '1', 'USER',   '1',  'PASSWORD', 'kapua-sys',    '$2a$12$BjLeC/gqcnEyk.XNo2qorul.a/v4HDuOUlfmojdSZXRSFTjymPdVm', '0'),
		       ('1', '2', CURRENT_TIMESTAMP(), '1', CURRENT_TIMESTAMP(), '1', 'USER',   '1',  'API_KEY',  '12345678',     '$2a$12$BjLeC/gqcnEyk.XNo2qorul.a/v4HDuOUlfmojdSZXRSFTjymPdVm', '0'),
			   ('1', '3', CURRENT_TIMESTAMP(), '1', CURRENT_TIMESTAMP(), '1', 'BROKER_CONNECTION', NULL, 'PASSWORD', 'kapua-broker', '$2a$12$BjLeC/gqcnEyk.XNo2qorul.a/v4HDuOUlfmojdSZXRSFTjymPdVm', '0');

INSERT INTO athz_access_info
	VALUES
		(1, 1, NOW(), 1, NOW(), 1, 'USER', 1, 0, '', ''),
		(1, 2, NOW(), 1, NOW(), 1, 'BROKER_CONNECTION', NULL, 0, '', '');

INSERT INTO athz_access_permission
	VALUES
		(1, 1, NOW(), 1, 2, 'broker', 'connect', 1); -- kapua-broker assigned of permission: broker:connect:1
		
INSERT INTO athz_access_role
	VALUES
		(1, 1, NOW(), 1, 1, 1); -- kapua-sys assigned of role admin

INSERT INTO athz_role
	VALUES
		(1, 1, NOW(), 1, NOW(), 1, 'admin', 0, '','');

INSERT INTO athz_role_permission 
	VALUES
		(1,  1, NOW(), 1, 1, 'account', 			null, null),
		(1,  2, NOW(), 1, 1, 'broker', 				null, null),
		(1,  3, NOW(), 1, 1, 'device',				null, null),
		(1,  4, NOW(), 1, 1, 'device_connection', 	null, null),
		(1,  5, NOW(), 1, 1, 'device_event', 		null, null),
		(1,  6, NOW(), 1, 1, 'device_lifecycle', 	null, null),
		(1,  7, NOW(), 1, 1, 'device_management',	null, null),
		(1,  8, NOW(), 1, 1, 'data', 				null, null),
		(1,  9, NOW(), 1, 1, 'credential',			null, null),
		(1,	10, NOW(), 1, 1, 'access_token',		null, null),
		(1, 11, NOW(), 1, 1, 'access_info',			null, null),
		(1, 12, NOW(), 1, 1, 'domain',				null, null),
		(1, 13, NOW(), 1, 1, 'group',				null, null),
		(1, 14, NOW(), 1, 1, 'role',				null, null),
		(1, 15, NOW(), 1, 1, 'user',				null, null),
		;

INSERT INTO athz_domain
	VALUES
		(NULL,  1, NOW(), 1, 'account',				'accountService'),
		(NULL,  2, NOW(), 1, 'broker',				'brokerService'),
		(NULL,  3, NOW(), 1, 'credential',			'credentialService'),
		(NULL,  4, NOW(), 1, 'access_token',		'accessTokenService'),
		(NULL,  5, NOW(), 1, 'access_info',			'accessInfoService'),
		(NULL,  6, NOW(), 1, 'domain',				'domainService'),
		(NULL,  7, NOW(), 1, 'group',				'groupService'),
		(NULL,  8, NOW(), 1, 'role',				'roleService'),
		(NULL,  9, NOW(), 1, 'data',				'datastoreService'),
		(NULL, 10, NOW(), 1, 'device',				'deviceService'),
		(NULL, 11, NOW(), 1, 'device_connection',	'deviceConnectionService'),
		(NULL, 12, NOW(), 1, 'device_management',	'deviceManagementService'),
		(NULL, 13, NOW(), 1, 'device_event',		'deviceEventService'),
		(NULL, 14, NOW(), 1, 'device_lifecycle',	'deviceLifecycleService'),
		(NULL, 15, NOW(), 1, 'user',				'userService');

		
INSERT INTO athz_domain_actions 
	VALUES 
		(1, 'read'),
		(1, 'write'),
		(1, 'delete'),

		(2, 'connect'),		

		(3, 'read'),
		(3, 'write'),
		(3, 'delete'),

		(4, 'read'),
		(4, 'write'),
		(4, 'delete'),

		(5, 'read'),
		(5, 'write'),
		(5, 'delete'),
				
		(6, 'read'),
		(6, 'write'),
		(6, 'delete'),
		
		(7, 'read'),
		(7, 'write'),
		(7, 'delete'),
		
		(8, 'read'),
		(8, 'write'),
		(8, 'delete'),
		
		(9, 'read'),
		(9, 'write'),
		(9, 'delete'),

		(10, 'read'),
		(10, 'write'),
		(10, 'delete'),
		
		(11, 'read'),
		(11, 'write'),
		(11, 'delete'),
		
		(12, 'read'),
		(12, 'write'),
		(12, 'delete'),
		(12, 'execute'),
		(12, 'connect'),
		
		(13, 'read'),
		(13, 'write'),
		(13, 'delete'),
		
		(14, 'read'),
		(14, 'write'),
		(14, 'delete'),
		
		(15, 'read'),
		(15, 'write'),
		(15, 'delete'),
		
		(16, 'read'),
		(16, 'write'),
		(16, 'delete');
		
INSERT INTO `usr_user` (`scope_id`, `id`, `name`, `created_on`, `created_by`, `modified_on`, `modified_by`, `status`, `display_name`, `email`, `phone_number`, `user_type`, `external_id`, `optlock`, `attributes`, `properties`)
		VALUES (1, 1, 'kapua-sys', CURRENT_TIMESTAMP(), 1, CURRENT_TIMESTAMP(), 1, 'ENABLED', 'Kapua Sysadmin', 'kapua-sys@eclipse.org',    '+1 555 123 4567', 'INTERNAL', NULL, 0, NULL, NULL);

