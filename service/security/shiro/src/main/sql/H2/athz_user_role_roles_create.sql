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

CREATE TABLE athz_user_role_roles (
  user_id					BIGINT(21) 	  UNSIGNED NOT NULL,
  role_id					BIGINT(21) 	  UNSIGNED NOT NULL,
  
  PRIMARY KEY (user_id, role_id)
  
) DEFAULT CHARSET=utf8;
