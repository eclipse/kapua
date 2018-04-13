/*******************************************************************************
 * Copyright (c) 2017 Red Hat Inc and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat Inc - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.kura.simulator.app.annotated;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Map a method for command access
 * <p>
 * This marks a method to be called by a verb (GET, PUT, POST, EXECUTE, DELETE)
 * and a resource name.
 * </p>
 * <p>
 * The verb and the name of the resource are extracted from the method name. The
 * part before the first uppercase character is the verb, the rest is the name
 * of the resource. So {@code getData} would be mapped to the verb {@code GET}
 * and the resource {@code data}, where {@code deleteUserEntry} would be mapped
 * to {@code DELETE} and {@code userEntry}.
 * </p>
 * <p>
 * <strong>Note: </strong>Marking methods with multiple, conflicting annotations
 * may result in an undefined behavior.
 * </p>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
public @interface Resource {
}
