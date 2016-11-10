package org.eclipse.kapua.service.authentication.token;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.KapuaEntityService;
import org.eclipse.kapua.service.KapuaUpdatableEntityService;

/**
 * Access token service API
 * 
 * @since 1.0
 *
 */
public interface AccessTokenService extends KapuaEntityService<AccessToken, AccessTokenCreator>, KapuaUpdatableEntityService<AccessToken> {

    /**
     * Find all access token associated with the given userId.
     * 
     * @param scopeId
     * @param userId
     * @return
     * @throws KapuaException
     * 
     * @since 1.0
     */
    public AccessTokenListResult findByUserId(KapuaId scopeId, KapuaId userId)
            throws KapuaException;

    /**
     * Find the access token by the given tokenId.
     * 
     * @param scopeId
     * @param tokenId
     * @return
     * @throws KapuaException
     * 
     * @since 1.0
     */
    public AccessToken findByTokenId(KapuaId scopeId, KapuaId tokenId)
            throws KapuaException;
}
