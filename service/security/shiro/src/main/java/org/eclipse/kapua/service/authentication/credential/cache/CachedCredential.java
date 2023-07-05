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

import java.util.Base64;
import java.util.Date;

import org.apache.commons.codec.digest.DigestUtils;

public class CachedCredential {

    private Date modifiedOn;
    private String digest;
    private String hashed;

    CachedCredential(Date modifiedOn, String digest, String hashed) {
        this.modifiedOn = modifiedOn;
        this.digest = digest;
        this.hashed = hashed;
    }

    public boolean isTokenMatches(String tokenPassword, String hashed) {
        String digest = Base64.getEncoder().encodeToString(DigestUtils.sha3_512(tokenPassword));
        return this.digest.equals(digest) && this.hashed.equals(hashed);
    }

    public boolean isStillValid(Date lastModifiedOn) {
        return (modifiedOn!=null && !modifiedOn.before(lastModifiedOn));
    }

}
