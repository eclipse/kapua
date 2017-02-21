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
	`created_by_type`,
	`created_by_id`,
	`modified_on`,
	`modified_by_type`,
	`modified_by_id`,
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
		'USER',
		1,
		CURRENT_TIMESTAMP(),
		'USER',
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

		
INSERT INTO atht_credential (scope_id, id, created_on, created_by_type, created_by_id, modified_on, modified_by_type, modified_by_id, subject_type, subject_id, type, key, secret, optlock) 
		VALUES ('1', '1', CURRENT_TIMESTAMP(), 'USER', '1', CURRENT_TIMESTAMP(), 'USER', '1', 'USER',   			'1',  'PASSWORD', 'kapua-sys',    '$2a$12$BjLeC/gqcnEyk.XNo2qorul.a/v4HDuOUlfmojdSZXRSFTjymPdVm', '0'),
		       ('1', '2', CURRENT_TIMESTAMP(), 'USER', '1', CURRENT_TIMESTAMP(), 'USER', '1', 'USER',   			'1',  'API_KEY',  '12345678',     '$2a$12$BjLeC/gqcnEyk.XNo2qorul.a/v4HDuOUlfmojdSZXRSFTjymPdVm', '0'),
			   ('1', '3', CURRENT_TIMESTAMP(), 'USER', '1', CURRENT_TIMESTAMP(), 'USER', '1', 'BROKER_CONNECTION', 	NULL, 'PASSWORD', 'kapua-broker', '$2a$12$BjLeC/gqcnEyk.XNo2qorul.a/v4HDuOUlfmojdSZXRSFTjymPdVm', '0');


INSERT INTO athz_access_info
	VALUES
		(1, 1, NOW(), 'USER', 1, NOW(), 'USER', 1, 'USER', 				1, 		0, '', ''),
		(1, 2, NOW(), 'USER', 1, NOW(), 'USER', 1, 'BROKER_CONNECTION', NULL, 	0, '', '');
		
		
INSERT INTO athz_access_permission
	VALUES
		(1, 1, NOW(), 'USER', 1, 2, 'broker', 'connect', 1, null), -- kapua-broker assigned of permission: broker:connect:1:* 
		(1, 2, NOW(), 'USER', 1, 2, 'device', 'connect', 1, null); -- kapua-broker assigned of permission: device:connect:1:* 

		
INSERT INTO athz_access_role
	VALUES
		(1, 1, NOW(), 'USER', 1, 1, 1); -- kapua-sys assigned of role admin  
		

INSERT INTO athz_role
	VALUES 
		(1, 1, NOW(), 'USER', 1, NOW(), 'USER', 1, 'admin', 0, '','');

		
INSERT INTO athz_role_permission 
	VALUES
		(1,  1, NOW(), 'USER', 1, 1, null, null, null, null); -- kapua-sys assigned of permission: *:*:*:*	
		
INSERT INTO `usr_user` (`scope_id`, `id`, `name`, `created_on`, `created_by_type`, `created_by_id`, `modified_on`, `modified_by_type`, `modified_by_id`, `status`, `display_name`, `email`, `phone_number`, `user_type`, `external_id`, `optlock`, `attributes`, `properties`) 
		VALUES (1, 1, 'kapua-sys', CURRENT_TIMESTAMP(), 'USER', 1, CURRENT_TIMESTAMP(), 'USER', 1, 'ENABLED', 'Kapua Sysadmin', 'kapua-sys@eclipse.org', '+1 555 123 4567', 'INTERNAL', NULL, 0, NULL, NULL);

