/*******************************************************************************
 * Copyright (c) 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.datastore;

import org.eclipse.kapua.service.datastore.model.ChannelInfo;
import org.eclipse.kapua.service.datastore.model.ChannelInfoListResult;
import org.eclipse.kapua.service.datastore.model.query.ChannelInfoQuery;
import org.eclipse.kapua.service.storable.StorableFactory;

/**
 * {@link ChannelInfoFactory} definition.
 * <p>
 * {@link StorableFactory} for {@link ChannelInfo}.
 *
 * @since 1.3.0
 */
public interface ChannelInfoFactory extends StorableFactory<ChannelInfo, ChannelInfoListResult, ChannelInfoQuery> {
}
