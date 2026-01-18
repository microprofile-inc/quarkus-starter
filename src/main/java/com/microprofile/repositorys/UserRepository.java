package com.microprofile.repositorys;

import com.microprofile.entities.User;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.SecurityContext;

import java.util.List;

@ApplicationScoped
public class UserRepository extends RepositoryBase<User> {
    @Inject
    SecurityContext ctx;

    public List<User> list(Page page, Sort sort) {
        return this.findAll(sort)
            .page(page)
            .list();
    }

    public User findByName(String name) {
        return this.find("name", name).firstResult();
    }

    public User authUser() {
        User user = null;
        if(ctx.getUserPrincipal() != null) {
            user = User.find("username", ctx.getUserPrincipal().getName()).firstResult();
        }
        return user;
    }

    public User authUser(boolean fullData) {
        User user = null;
        if(ctx.getUserPrincipal() != null) {
            user = User.find("username", ctx.getUserPrincipal().getName()).firstResult();
            if(fullData) {
                user.setGetFullData(true);
            }
        }
        return user;
    }
}
