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

INSERT INTO `usr_user` (`scope_id`, `id`, `name`, `created_on`, `created_by`, `modified_on`, `modified_by`, `status`, `display_name`, `email`, `phone_number`, `user_type`, `external_id`, `optlock`, `attributes`, `properties`) 
		VALUES (1, 1, 'kapua-sys',    CURRENT_TIMESTAMP(), 1, CURRENT_TIMESTAMP(), 1, 'ENABLED', 'Kapua Sysadmin', 'kapua-sys@eclipse.org',    '+1 555 123 4567', 'INTERNAL', NULL, 0, NULL, NULL),
		       (1, 2, 'kapua-broker', CURRENT_TIMESTAMP(), 1, CURRENT_TIMESTAMP(), 1, 'ENABLED', 'Kapua Broker',   'kapua-broker@eclipse.org', '+1 555 123 4567', 'INTERNAL', NULL, 0, NULL, NULL);
