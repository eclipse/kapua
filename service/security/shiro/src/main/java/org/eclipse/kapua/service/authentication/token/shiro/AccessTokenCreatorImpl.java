package org.eclipse.kapua.service.authentication.token.shiro;

import java.util.Date;

import javax.xml.bind.annotation.XmlElement;

import org.eclipse.kapua.commons.model.AbstractKapuaEntityCreator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authentication.token.AccessToken;
import org.eclipse.kapua.service.authentication.token.AccessTokenCreator;

public class AccessTokenCreatorImpl extends AbstractKapuaEntityCreator<AccessToken> implements AccessTokenCreator {

    private static final long serialVersionUID = -27718046815190710L;

    @XmlElement(name = "tokenId")
    private String tokenId;

    @XmlElement(name = "userId")
    private KapuaId userId;

    @XmlElement(name = "expiresOn")
    private Date expiresOn;

    protected AccessTokenCreatorImpl(KapuaId scopeId) {
        super(scopeId);
    }

    @Override
    public String getTokenId() {
        return tokenId;
    }

    @Override
    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;

    }

    @Override
    public KapuaId getUserId() {
        return userId;
    }

    @Override
    public void setUserId(KapuaId userId) {
        this.userId = userId;
    }

    @Override
    public Date getExpiresOn() {
        return expiresOn;
    }

    @Override
    public void setExpiresOn(Date expiresOn) {
        this.expiresOn = expiresOn;
    }

}
