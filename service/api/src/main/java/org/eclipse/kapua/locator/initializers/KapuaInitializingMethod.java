/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.locator.initializers;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Parameterless methods annotated with this will be invoked immediately after the KapuaLocator
 * has been fully initialized, as long as the class is initialized within the Locator itself (e.g.: in a guice module)
 * <p>
 * Methods with the lowest priority will be called first, then the others in ascending order.
 * <p>
 * Please notice this is different from use of {@link javax.annotation.PostConstruct}. Methods annotated with PostConstruct are executed immediately after instantiation  of the object,
 * but potentially before the entire Locator context is fully initialized - so indirect dependencies are not guaranteed (e.g.: JAXB/XMLUtils being fully initialized)
 * <p>
 * priority 10 is used for database initialization (e.g.: DB driver init and liquibase run)
 * priority 20 is used for data populators/initializators (e.g.: wired-to-db entity aligners)
 *
 * @since 2.0.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface KapuaInitializingMethod {
    short priority() default 100;
}
