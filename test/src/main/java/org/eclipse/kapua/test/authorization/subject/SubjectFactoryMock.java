package org.eclipse.kapua.test.authorization.subject;

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
public class SubjectFactoryMock implements SubjectFactory {

    @Override
    public Subject newSubject(SubjectType type, KapuaId id) {
        return new SubjectMock(type, id);
    }

}
