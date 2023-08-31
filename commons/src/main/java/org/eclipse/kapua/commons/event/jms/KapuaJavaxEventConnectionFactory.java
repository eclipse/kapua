/*******************************************************************************
 * Copyright (c) 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.commons.event.jms;

import javax.jms.ConnectionFactory;

import org.eclipse.kapua.service.KapuaService;

/**
 * WARNING!!!!
 * this class is needed only because we don't have the full injection refactoring done yet.
 * Once will be available this class will be correctly eliminated
 * TODO delete me when direct injection will be possible
 */
public interface KapuaJavaxEventConnectionFactory extends KapuaService, ConnectionFactory {

}