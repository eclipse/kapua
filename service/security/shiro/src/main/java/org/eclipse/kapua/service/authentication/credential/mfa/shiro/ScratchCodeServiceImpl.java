/*******************************************************************************
 * Copyright (c) 2020, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.authentication.credential.mfa.shiro;

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.model.domains.Domains;
import org.eclipse.kapua.commons.util.ArgumentValidator;
import org.eclipse.kapua.model.KapuaEntityAttributes;
import org.eclipse.kapua.model.domain.Actions;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.authentication.credential.mfa.ScratchCode;
import org.eclipse.kapua.service.authentication.credential.mfa.ScratchCodeAttributes;
import org.eclipse.kapua.service.authentication.credential.mfa.ScratchCodeCreator;
import org.eclipse.kapua.service.authentication.credential.mfa.ScratchCodeFactory;
import org.eclipse.kapua.service.authentication.credential.mfa.ScratchCodeListResult;
import org.eclipse.kapua.service.authentication.credential.mfa.ScratchCodeQuery;
import org.eclipse.kapua.service.authentication.credential.mfa.ScratchCodeRepository;
import org.eclipse.kapua.service.authentication.credential.mfa.ScratchCodeService;
import org.eclipse.kapua.service.authentication.shiro.utils.AuthenticationUtils;
import org.eclipse.kapua.service.authentication.shiro.utils.CryptAlgorithm;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.storage.TxManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;

/**
 * {@link ScratchCodeService} implementation.
 */
@Singleton
public class ScratchCodeServiceImpl implements ScratchCodeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScratchCodeServiceImpl.class);
    private final TxManager txManager;
    private final ScratchCodeRepository scratchCodeRepository;
    private final ScratchCodeFactory scratchCodeFactory;
    private final AuthorizationService authorizationService;
    private final PermissionFactory permissionFactory;

    public ScratchCodeServiceImpl(
            AuthorizationService authorizationService,
            PermissionFactory permissionFactory,
            TxManager txManager,
            ScratchCodeRepository scratchCodeRepository,
            ScratchCodeFactory scratchCodeFactory) {
        this.txManager = txManager;
        this.scratchCodeRepository = scratchCodeRepository;
        this.scratchCodeFactory = scratchCodeFactory;
        this.authorizationService = authorizationService;
        this.permissionFactory = permissionFactory;
    }

    @Override
    public ScratchCode create(ScratchCodeCreator scratchCodeCreator) throws KapuaException {
        // Argument Validation
        ArgumentValidator.notNull(scratchCodeCreator, "scratchCodeCreator");
        ArgumentValidator.notNull(scratchCodeCreator.getScopeId(), "scratchCodeCreator.scopeId");
        ArgumentValidator.notNull(scratchCodeCreator.getMfaOptionId(), "scratchCodeCreator.mfaOptionId");
        ArgumentValidator.notEmptyOrNull(scratchCodeCreator.getCode(), "scratchCodeCreator.code");
        // Check access
        authorizationService.checkPermission(permissionFactory.newPermission(Domains.CREDENTIAL, Actions.write,
                scratchCodeCreator.getScopeId()));
        // Do pre persist magic on key values
        String fullKey = scratchCodeCreator.getCode();
        // Do create
        final ScratchCode res = txManager.execute(tx -> {
            // Crypto code (it's ok to do than if BCrypt is used when checking a provided scratch code against the stored one)
            String encryptedCode = AuthenticationUtils.cryptCredential(CryptAlgorithm.BCRYPT, scratchCodeCreator.getCode());
            // Create code
            ScratchCodeImpl codeImpl = new ScratchCodeImpl(scratchCodeCreator.getScopeId(), scratchCodeCreator.getMfaOptionId(), encryptedCode);
            return scratchCodeRepository.create(tx, codeImpl);
        });
        res.setCode(fullKey);
        return res;
    }

    @Override
    public ScratchCode update(ScratchCode scratchCode) throws KapuaException {
        // Argument Validation
        ArgumentValidator.notNull(scratchCode, "scratchCode");
        ArgumentValidator.notNull(scratchCode.getId(), "scratchCode.id");
        ArgumentValidator.notNull(scratchCode.getScopeId(), "scratchCode.scopeId");
        ArgumentValidator.notNull(scratchCode.getMfaOptionId(), "scratchCode.mfaOptionId");
        ArgumentValidator.notEmptyOrNull(scratchCode.getCode(), "scratchCode.code");
        // Check access
        authorizationService.checkPermission(permissionFactory.newPermission(Domains.CREDENTIAL, Actions.write, scratchCode.getScopeId()));

        return txManager.execute(tx -> {
            ScratchCode currentscratchCode = scratchCodeRepository.find(tx, scratchCode.getScopeId(), scratchCode.getId())
                    .orElseThrow(() -> new KapuaEntityNotFoundException(ScratchCode.TYPE, scratchCode.getId()));

            // Passing attributes??
            return scratchCodeRepository.update(tx, currentscratchCode, scratchCode);
        });
    }

    @Override
    public ScratchCode find(KapuaId scopeId, KapuaId scratchCodeId) throws KapuaException {
        // Validation of the fields
        ArgumentValidator.notNull(scopeId, KapuaEntityAttributes.SCOPE_ID);
        ArgumentValidator.notNull(scratchCodeId, "scratchCodeId");
        // Check Access
        authorizationService.checkPermission(permissionFactory.newPermission(Domains.CREDENTIAL, Actions.read, scopeId));

        return txManager.execute(tx -> scratchCodeRepository.find(tx, scopeId, scratchCodeId))
                .orElse(null);
    }

    @Override
    public ScratchCodeListResult query(KapuaQuery query) throws KapuaException {
        // Argument Validation
        ArgumentValidator.notNull(query, "query");
        // Check Access
        authorizationService.checkPermission(permissionFactory.newPermission(Domains.CREDENTIAL, Actions.read, query.getScopeId()));

        return txManager.execute(tx -> scratchCodeRepository.query(tx, query));
    }

    @Override
    public long count(KapuaQuery query) throws KapuaException {
        // Argument Validation
        ArgumentValidator.notNull(query, "query");
        // Check Access
        authorizationService.checkPermission(permissionFactory.newPermission(Domains.CREDENTIAL, Actions.read, query.getScopeId()));

        return txManager.execute(tx -> scratchCodeRepository.count(tx, query));
    }

    @Override
    public void delete(KapuaId scopeId, KapuaId scratchCodeId) throws KapuaException {
        // Argument Validation
        ArgumentValidator.notNull(scratchCodeId, "scratchCode.id");
        ArgumentValidator.notNull(scopeId, "scratchCode.scopeId");
        // Check Access
        authorizationService.checkPermission(permissionFactory.newPermission(Domains.CREDENTIAL, Actions.delete, scopeId));

        txManager.execute(tx -> scratchCodeRepository.delete(tx, scopeId, scratchCodeId));
    }


    @Override
    public ScratchCodeListResult findByMfaOptionId(KapuaId scopeId, KapuaId mfaOptionId) throws KapuaException {
        // Argument Validation
        ArgumentValidator.notNull(scopeId, KapuaEntityAttributes.SCOPE_ID);
        ArgumentValidator.notNull(mfaOptionId, ScratchCodeAttributes.MFA_OPTION_ID);
        // Check Access
        authorizationService.checkPermission(permissionFactory.newPermission(Domains.CREDENTIAL, Actions.read, scopeId));

        return txManager.execute(tx -> scratchCodeRepository.findByMfaOptionId(tx, scopeId, mfaOptionId));
    }

    private void deleteScratchCodeByMfaOptionId(KapuaId scopeId, KapuaId mfaOptionId) throws KapuaException {
        ScratchCodeQuery query = scratchCodeFactory.newQuery(scopeId);
        query.setPredicate(query.attributePredicate(ScratchCodeAttributes.MFA_OPTION_ID, mfaOptionId));

        txManager.execute(tx -> {
            ScratchCodeListResult scratchCodesToDelete = scratchCodeRepository.query(tx, query);

            for (ScratchCode c : scratchCodesToDelete.getItems()) {
                scratchCodeRepository.delete(tx, c.getScopeId(), c.getId());
            }
            return null;
        });
    }

    private void deleteScratchCodeByAccountId(KapuaId scopeId, KapuaId accountId) throws KapuaException {
        ScratchCodeQuery query = scratchCodeFactory.newQuery(accountId);

        txManager.<Void>execute(tx -> {
            ScratchCodeListResult scratchCodesToDelete = scratchCodeRepository.query(tx, query);
            for (ScratchCode c : scratchCodesToDelete.getItems()) {
                scratchCodeRepository.delete(tx, c.getScopeId(), c.getId());
            }
            return null;
        });
    }

}
