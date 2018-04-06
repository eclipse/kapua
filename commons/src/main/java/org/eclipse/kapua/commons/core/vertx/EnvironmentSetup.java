/*******************************************************************************
 * Copyright (c) 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.commons.core.vertx;

import org.eclipse.kapua.commons.core.ObjectContextConfig;
import org.eclipse.kapua.commons.core.spi.ObjectContextConfigFactory;

/**
 * EnvironmentSetup is used to provide initialization info.  
 *
 */
public interface EnvironmentSetup {

    public void configure(ObjectContextConfig context);

    public void configure(ObjectContextConfigFactory factory);
}
