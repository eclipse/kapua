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

INSERT INTO atht_credential (scope_id, id, created_on, created_by_type, created_by_id, modified_on, modified_by_type, modified_by_id, subject_type, subject_id, type, key, secret, optlock) 
		VALUES ('1', '1', CURRENT_TIMESTAMP(), 'USER', '1', CURRENT_TIMESTAMP(), 'USER', '1', 'USER',   			'1',  'PASSWORD', 'kapua-sys',    '$2a$12$BjLeC/gqcnEyk.XNo2qorul.a/v4HDuOUlfmojdSZXRSFTjymPdVm', '0'),
		       ('1', '2', CURRENT_TIMESTAMP(), 'USER', '1', CURRENT_TIMESTAMP(), 'USER', '1', 'USER',   			'1',  'API_KEY',  '12345678',     '$2a$12$BjLeC/gqcnEyk.XNo2qorul.a/v4HDuOUlfmojdSZXRSFTjymPdVm', '0'),
			   ('1', '3', CURRENT_TIMESTAMP(), 'USER', '1', CURRENT_TIMESTAMP(), 'USER', '1', 'BROKER_CONNECTION', 	NULL, 'PASSWORD', 'kapua-broker', '$2a$12$BjLeC/gqcnEyk.XNo2qorul.a/v4HDuOUlfmojdSZXRSFTjymPdVm', '0');
