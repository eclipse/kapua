/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.device.registry.connection.option.internal;

import org.eclipse.kapua.commons.model.query.KapuaListResultImpl;
import org.eclipse.kapua.service.device.registry.connection.option.DeviceConnectionOption;
import org.eclipse.kapua.service.device.registry.connection.option.DeviceConnectionOptionListResult;

/**
 * Device connection options list.
 *
 * @since 1.0.0
 */
public class DeviceConnectionOptionListResultImpl extends KapuaListResultImpl<DeviceConnectionOption> implements DeviceConnectionOptionListResult {

    private static final long serialVersionUID = -4450707993798807403L;
}
