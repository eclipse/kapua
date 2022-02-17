/*******************************************************************************
 * Copyright (c) 2017, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.tag.internal;

import org.eclipse.kapua.commons.model.query.KapuaListResultImpl;
import org.eclipse.kapua.service.tag.Tag;
import org.eclipse.kapua.service.tag.TagListResult;

/**
 * {@link TagListResult} implementation.
 *
 * @since 1.0.0
 */
public class TagListResultImpl extends KapuaListResultImpl<Tag> implements TagListResult {

    private static final long serialVersionUID = 791258978210911362L;
}
