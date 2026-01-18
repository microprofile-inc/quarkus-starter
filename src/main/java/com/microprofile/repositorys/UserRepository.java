package com.microprofile.repositorys;

import com.microprofile.entities.User;
import com.microprofile.payload.UpdateUserPassword;
import com.microprofile.utils.GenerateViolationReport;
import com.microprofile.utils.SecurityTools;
import io.quarkus.hibernate.validator.runtime.jaxrs.ViolationReport;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

import java.util.List;

@ApplicationScoped
public class UserRepository extends RepositoryBase<User> {
    @Inject
    SecurityContext ctx;

    @Inject
    SecurityTools securityTools;

    @Inject
    SessionRepository sessionRepository;

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

    public ViolationReport updatePassword(UpdateUserPassword data) {
        User user = this.authUser(true);
        user.password = securityTools.generatePassword(data.password);
        if(!sessionRepository.verifySmsCode(user.countryCode, user.phoneNumber, data.code)) {
            List<ViolationReport.Violation> violations = List.of(
                    new ViolationReport.Violation("smsCode", "验证码不正确或已经过期")
            );
            return new GenerateViolationReport("Error", Response.Status.BAD_REQUEST, violations).build();
        }

        user.persistAndFlush();
        return null;
    }
}
