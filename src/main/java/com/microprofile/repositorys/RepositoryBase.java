package com.microprofile.repositorys;

import com.microprofile.entities.User;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.apache.commons.beanutils2.BeanUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

public abstract class RepositoryBase<T extends PanacheEntityBase> implements PanacheRepositoryBase<T, UUID> {

    @Inject
    protected UserRepository userRepository;

    @Transactional
    public T create(T entity) {
        entity.persistAndFlush();
        return entity;
    }

    @Transactional
    public T save(T entity) {
        EntityManager em = this.getEntityManager();

        if(entity.isPersistent()) {
            entity.persist();
        } else {
            em.merge(entity);
        }
        return entity;
    }

    public void updateProperties (T dest, Object orig) throws InvocationTargetException, IllegalAccessException {
        BeanUtils.copyProperties(dest, orig);
        this.save(dest);
    }

    public Boolean checkOwnership(User owner) {
        User user = userRepository.authUser();
        if(owner != null) {
            return user.id.equals(owner.id);
        }
        return false;
    }
}
