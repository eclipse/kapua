package org.eclipse.kapua.service.authorization.subject.shiro;

import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authorization.subject.Subject;
import org.eclipse.kapua.service.authorization.subject.SubjectFactory;
import org.eclipse.kapua.service.authorization.subject.SubjectType;

/**
 * {@link SubjectFactory} implementation.
 * 
 * @since 1.0.0
 *
 */
@KapuaProvider
public class SubjectFactoryImpl implements SubjectFactory {

    @Override
    public Subject newSubject(SubjectType type, KapuaId id) {
        return new SubjectImpl(type, id);
    }

}
