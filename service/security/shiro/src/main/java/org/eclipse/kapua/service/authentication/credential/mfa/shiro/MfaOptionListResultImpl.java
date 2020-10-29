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
package org.eclipse.kapua.service.authentication.credential.mfa.shiro;

import org.eclipse.kapua.commons.model.query.KapuaListResultImpl;
import org.eclipse.kapua.service.authentication.credential.mfa.MfaOption;
import org.eclipse.kapua.service.authentication.credential.mfa.MfaOptionListResult;

/**
 * {@link MfaOption} list result implementation.
 */
public class MfaOptionListResultImpl extends KapuaListResultImpl<MfaOption> implements MfaOptionListResult {

    private static final long serialVersionUID = -4204695192086365901L;
}
