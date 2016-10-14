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

INSERT INTO atht_credential (`scope_id`, `id`, `created_on`, `created_by`, `modified_on`, `modified_by`, `user_id`, `credential_type`, `credential_key`, `optlock`) 
		VALUES ('1', '1', CURRENT_TIMESTAMP(), '1', CURRENT_TIMESTAMP(), '1', '1', 'PASSWORD', '$2a$12$BjLeC/gqcnEyk.XNo2qorul.a/v4HDuOUlfmojdSZXRSFTjymPdVm', '0'),
			   ('1', '2', CURRENT_TIMESTAMP(), '1', CURRENT_TIMESTAMP(), '1', '2', 'PASSWORD', '$2a$12$BjLeC/gqcnEyk.XNo2qorul.a/v4HDuOUlfmojdSZXRSFTjymPdVm', '0');
