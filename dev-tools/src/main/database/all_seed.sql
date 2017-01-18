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
		(1, 1, NOW(), 1, 2, 'broker', 'connect', 1, null); -- kapua-broker assigned of permission: broker:connect:1

INSERT INTO athz_access_role
	VALUES
		(1, 1, NOW(), 1, 1, 1); -- kapua-sys assigned of role admin

INSERT INTO athz_role
	VALUES
		(1, 1, NOW(), 1, NOW(), 1, 'admin', 0, '','');

INSERT INTO athz_role_permission
	VALUES
		(1, 1, NOW(), 1, 1, 'account', null, null, null),
		(1, 2, NOW(), 1, 1, 'user', null, null, null),
		(1, 3, NOW(), 1, 1, 'device_event', null, null, null),
		(1, 4, NOW(), 1, 1, 'device_connection', null, null, null),
		(1, 5, NOW(), 1, 1, 'device', null, null, null),
		(1, 6, NOW(), 1, 1, 'data', null, null, null),
		(1, 7, NOW(), 1, 1, 'broker', null, null, null),
		(1, 8, NOW(), 1, 1, 'credential', null, null, null),
		(1, 9, NOW(), 1, 1, 'role', null, null, null),
		(1, 10, NOW(), 1, 1, 'user_permission', null, null, null),
		(1, 11, NOW(), 1, 1, 'device_lifecycle', null, null, null),
		(1, 12, NOW(), 1, 1, 'device_management', null, null, null),
		(1, 13, NOW(), 1, 1, 'account', null, null, null),
		(1, 14, NOW(), 1, 1, 'account', null, null, null);
