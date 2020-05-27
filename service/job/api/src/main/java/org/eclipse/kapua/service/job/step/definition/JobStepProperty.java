/*******************************************************************************
 * Copyright (c) 2017, 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.job.step.definition;

public interface JobStepProperty {

    String getName();

    void setName(String name);

    String getPropertyType();

    void setPropertyType(String propertyType);

    String getPropertyValue();

    void setPropertyValue(String propertyValue);

    String getExampleValue();

    void setExampleValue(String exampleValue);

}
