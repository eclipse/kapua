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
package org.eclipse.kapua.service.datastore.internal.model;

import org.eclipse.kapua.service.datastore.model.StorableId;

/**
 * Storable identifier implementation
 *
 * @since 1.0
 *
 */
public class StorableIdImpl implements StorableId {

    private final String sid;

    /**
     * Construct a storable id with the provided id
     * 
     * @param id
     */
    public StorableIdImpl(String id) {
        sid = id;
    }

    @Override
    public String toString() {
        return sid;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        StorableIdImpl other = (StorableIdImpl) obj;
        if (sid == null) {
            if (other.sid != null) {
                return false;
            }
        } else if (!sid.equals(other.sid)) {
            return false;
        }
        return true;
    }

}
