/*******************************************************************************
 * Copyright (c) 2018, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.app.console.module.authorization.shared.model;

import org.eclipse.kapua.app.console.module.api.shared.model.query.GwtQuery;

public class GwtAccessTagQuery extends GwtQuery {

    private static final long serialVersionUID = 2816436160710170749L;
    private String accessInfoId;
    private String tagId;

    public String getAccessInfoId() {
        return accessInfoId;
    }

    public void setAccessInfoId(String accessInfoId) {
        this.accessInfoId = accessInfoId;
    }

    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

}
