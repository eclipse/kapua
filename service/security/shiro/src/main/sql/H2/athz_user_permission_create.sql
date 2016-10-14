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

CREATE TABLE athz_user_permission (
  scope_id             	    BIGINT(21) 	  UNSIGNED NOT NULL,
  id                     	BIGINT(21) 	  UNSIGNED NOT NULL,
  created_on             	TIMESTAMP(3)  DEFAULT 0,
  created_by             	BIGINT(21)    UNSIGNED NOT NULL,
  
  user_id					BIGINT(21) 	  UNSIGNED NOT NULL,
  domain					VARCHAR(64)   NOT NULL,
  action					VARCHAR(64),
  target_scope_id		    BIGINT(21),
  
  PRIMARY KEY (id)

) DEFAULT CHARSET=utf8;

CREATE UNIQUE INDEX idx_user_permission_scope_id ON athz_user_permission (scope_id, user_id, domain, action, target_scope_id);
