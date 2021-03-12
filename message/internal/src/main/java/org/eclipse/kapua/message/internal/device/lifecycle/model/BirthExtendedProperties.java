/*******************************************************************************
 * Copyright (c) 2021 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.message.internal.device.lifecycle.model;

import com.fasterxml.jackson.annotation.JsonRootName;

import java.util.List;

/**
 * Birth extended property definition.
 *
 * @since 1.5.0
 */
@JsonRootName("deviceExtendedProperties")
public class BirthExtendedProperties {

    private String version;

    private List<BirthExtendedPropertyGroups> propertyGroups;
}
