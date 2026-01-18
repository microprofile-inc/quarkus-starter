package com.microprofile.repositorys;

import com.microprofile.entities.CountryCode;
import com.microprofile.entities.SmsCode;
import com.microprofile.entities.User;
import com.microprofile.enums.E_ROLE;
import com.microprofile.payload.LoginBody;
import com.microprofile.payload.RegisterBody;
import com.microprofile.utils.GenerateViolationReport;
import com.microprofile.utils.SecurityTools;
import com.microprofile.utils.TokenTools;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.hibernate.validator.runtime.jaxrs.ViolationReport;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.microprofile.utils.Utils.generateUserName;

@ApplicationScoped
public class SessionRepository implements PanacheRepositoryBase<User, UUID> {
    @Inject
    UserRepository userRepository;

    @Inject
    SecurityTools securityTools;

    @Inject
    TokenTools tokenTools;

    @Transactional
    public User create(RegisterBody registerBody) {
        User user = new User();
        user.username = generateUserName(registerBody.phoneNumber);
        user.phoneNumber = registerBody.phoneNumber;
        user.countryCode = CountryCode.findById(registerBody.countryCodeId);
        user.email = registerBody.email;
        user.password = securityTools.generatePassword(registerBody.password);

        user.persistAndFlush();
        return user;
    }

    public ViolationReport validOfRegister(RegisterBody registerBody) {
        long phoneCount = User.find("phoneNumber", registerBody.phoneNumber).count();
        if(phoneCount > 0) {
            List<ViolationReport.Violation> violation = List.of(
                new ViolationReport.Violation("phoneNumber", "手机号已经注册，请登录")
            );
            return new GenerateViolationReport("手机号已经注册，请登录", Response.Status.BAD_REQUEST, violation)
                    .build();
        }

        if(!this.verifySmsCode(CountryCode.findById(registerBody.countryCodeId), registerBody.phoneNumber, registerBody.code)) {
            List<ViolationReport.Violation> violation = List.of(
                    new ViolationReport.Violation("smsCode", "验证码不正确或已经过期")
            );
            return new GenerateViolationReport("验证码不正确或已经过期", Response.Status.BAD_REQUEST, violation)
                    .build();
        }

        return null;
    }

    @Transactional
    public User loginForPhoneNumber(LoginBody loginBody) {
        Optional<User> user
            = User.find("phoneNumber = ?1 and countryCode = ?2", loginBody.phoneNumber, CountryCode.findById(loginBody.countryCodeId))
            .firstResultOptional();

        if(user.isPresent()) {
            if(securityTools.matches(loginBody.password, user.get().password)) {
                User authUser = user.get();
                tokenTools.generate(authUser, List.of(authUser.role));
                return authUser;
            }
        }
        return null;
    }

    @Transactional
    public User loginForEmail(LoginBody loginBody) {
        Optional<User> user = User.find("email", loginBody.email).firstResultOptional();
        if(user.isPresent()) {
            if(securityTools.matches(loginBody.password, user.get().password)) {
                User authUser = user.get();
                tokenTools.generate(authUser, List.of("User"));
                return authUser;
            }
        }
        return null;
    }

    public Boolean verifySmsCode(CountryCode countryCode, String phoneNumber, String code) {
        CountryCode country = CountryCode.findById(countryCode.id);
        Optional<SmsCode> smsCode = SmsCode
            .find("countryCode = ?1 and phoneNumber = ?2 and code = ?3 and effective = true", country,  phoneNumber, code)
            .firstResultOptional();
        Instant now = Instant.now();
        SmsCode t;
        if(smsCode.isPresent()) {
            t = smsCode.get();
            t.effective = false;
            t.persistAndFlush();
            return now.getEpochSecond() - t.whenCreated.getEpochSecond() < 120;
        }

        return false;
    }

    @Transactional
    public User login(LoginBody loginBody) {
        Optional<User> user = User.find("username", loginBody.phoneNumber).firstResultOptional();
        Optional<SmsCode> code = SmsCode
            .find("phoneNumber = ?1 and code = ?2 and effective = true", loginBody.phoneNumber, loginBody.code)
            .firstResultOptional();

        Instant now = Instant.now();
        if(user.isPresent()) {
            if(code.isPresent() && code.get().whenCreated.getEpochSecond() - now.getEpochSecond() < 120) {
                User authUser = user.get();
                tokenTools.generate(authUser, List.of("User"));

                SmsCode smsCode = code.get();
                smsCode.effective = false;
                smsCode.persistAndFlush();
                return authUser;
            } else {
                return null;
            }
        } else if(code.isPresent() && code.get().whenCreated.getEpochSecond() - now.getEpochSecond() < 120) {
            // create new user
            User newUser = new User();
            newUser.phoneNumber = loginBody.phoneNumber;
            newUser.username = loginBody.phoneNumber;
            newUser.persistAndFlush();
            tokenTools.generate(newUser, List.of("User"));

            SmsCode smsCode = code.get();
            smsCode.effective = false;
            smsCode.persistAndFlush();
            return newUser;
        } else {
            return null;
        }
    }

}
