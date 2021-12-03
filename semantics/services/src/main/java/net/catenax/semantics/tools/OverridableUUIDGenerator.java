package net.catenax.semantics.tools;

import org.hibernate.id.UUIDGenerator;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.HibernateException;

import java.io.Serializable;

public class OverridableUUIDGenerator extends UUIDGenerator {


    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
        Serializable id = session.getEntityPersister(null, object).getClassMetadata().getIdentifier(object, session);
        return id != null ? id : super.generate(session, object);
    }
}