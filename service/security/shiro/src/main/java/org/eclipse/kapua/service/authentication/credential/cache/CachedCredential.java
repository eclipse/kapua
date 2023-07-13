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
package org.eclipse.kapua.service.authentication.credential.cache;

import java.util.Date;

public class CachedCredential {

    private Date modifiedOn;
    private String tmpHash;
    private String hash;

    CachedCredential(Date modifiedOn, String tmpHash, String hash) {
        this.modifiedOn = modifiedOn;
        this.tmpHash = tmpHash;
        this.hash = hash;
    }

    public boolean isTokenMatches(String tmpHash, String hash) {
        return this.tmpHash.equals(tmpHash) && this.hash.equals(hash);
    }

    public boolean isStillValid(Date lastModifiedOn) {
        return (modifiedOn!=null && !modifiedOn.before(lastModifiedOn));
    }

}
