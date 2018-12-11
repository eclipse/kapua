/*******************************************************************************
 * Copyright (c) 2017, 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/

DELETE FROM act_account WHERE id NOT IN (1);

DELETE FROM atht_credential WHERE NOT (scope_id = 1 AND id IN (1,2,3));

DELETE FROM atht_access_token;

DELETE FROM athz_role WHERE NOT (scope_id = 1 AND id = 1);

DELETE FROM athz_role_permission WHERE NOT (scope_id = 1 AND role_id = 1 AND id IN (1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20));

DELETE FROM dvc_device_connection;

DELETE FROM dvc_device;

DELETE FROM dvc_device_event;

DELETE FROM sys_configuration WHERE NOT (scope_id = 1 AND id IN (1,2,3,4,5,6,7,8,9));

DELETE FROM usr_user WHERE NOT (scope_id = 1 AND id IN (1,2));

DELETE FROM athz_access_info WHERE NOT (scope_id = 1 AND id IN (1,2));

DELETE FROM athz_access_permission WHERE NOT (scope_id = 1 AND id = 1);

DELETE FROM athz_access_role WHERE NOT (scope_id = 1 AND id = 1);

DELETE FROM athz_domain_actions WHERE domain_id NOT IN (1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20);

DELETE FROM athz_domain WHERE NOT (scope_id IS null AND id IN (1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20));

DELETE FROM athz_group;

DELETE FROM tag_tag;
