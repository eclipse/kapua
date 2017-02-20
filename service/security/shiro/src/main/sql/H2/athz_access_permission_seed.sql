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

INSERT INTO athz_access_permission
	VALUES
		(1, 1, NOW(), 'USER', 1, 2, 'broker', 'connect', 1, null), -- kapua-broker assigned of permission: broker:connect:1:* 
		(1, 2, NOW(), 'USER', 1, 2, 'device', 'connect', 1, null); -- kapua-broker assigned of permission: device:connect:1:* 