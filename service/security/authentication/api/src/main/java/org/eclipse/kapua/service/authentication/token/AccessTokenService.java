package org.eclipse.kapua.service.authentication.token;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.KapuaEntityService;
import org.eclipse.kapua.service.KapuaUpdatableEntityService;

public interface AccessTokenService extends KapuaEntityService<AccessToken, AccessTokenCreator>, KapuaUpdatableEntityService<AccessToken> {

    public AccessTokenListResult findByUserId(KapuaId scopeId, KapuaId userId)
            throws KapuaException;

    public AccessToken findByTokenId(KapuaId scopeId, KapuaId tokenId)
            throws KapuaException;
}
