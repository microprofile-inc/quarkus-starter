package com.microprofile.resources;

import com.microprofile.entities.User;
import com.microprofile.payload.LoginBody;
import com.microprofile.payload.RegisterBody;
import com.microprofile.payload.ResultOfData;
import com.microprofile.repositorys.SessionRepository;
import com.microprofile.repositorys.UserRepository;
import com.microprofile.utils.GenerateViolationReport;
import io.quarkus.hibernate.validator.runtime.jaxrs.ViolationReport;
import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.media.SchemaProperty;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;

@OpenAPIDefinition(
    info = @Info(title="Session API", version = "1.0.1")
)
@Tag(name="Session", description="Register and login operations")
@Path("/session")
@Produces(MediaType.APPLICATION_JSON)
@Consumes({MediaType.APPLICATION_JSON})
public class SessionResource {
    @Inject
    protected UserRepository userRepository;

    @Inject
    protected SessionRepository sessionRepository;

    @POST
    @Path("login")
    @Operation(summary = "user login")
    @APIResponse(
        responseCode = "200",
        description = "User data",
        content = @Content(
            schema = @Schema(
                implementation = User.class,
                properties = {
                    @SchemaProperty(name = "data", type = SchemaType.OBJECT, implementation = User.class),
                }
            )
        )
    )
    @APIResponse(
        responseCode = "400",
        description = "User not found",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(
                implementation = ViolationReport.class
            )
        )
    )
    public Response login(@Valid LoginBody loginBody) {
        User user;
        if (loginBody.email != null && !loginBody.email.isEmpty()) {
            user = sessionRepository.loginForEmail(loginBody);
        } else {
            user = sessionRepository.loginForPhoneNumber(loginBody);
        }

        if(user != null) {
            return Response
                .status(Response.Status.OK)
                .entity(new ResultOfData<>(user))
                .build();
        } else {
            return Response
                .status(Response.Status.BAD_REQUEST)
                .entity(new GenerateViolationReport("用户或者密码不正确", Response.Status.BAD_REQUEST, List.of()).build())
                .build();
        }
    }

    @POST
    @Path("register")
    @Operation(summary = "User register")
    @APIResponse(
            responseCode = "200",
            description = "User data",
            content = @Content(
                    schema = @Schema(
                            implementation = User.class,
                            properties = {
                                    @SchemaProperty(name = "data", type = SchemaType.OBJECT, implementation = User.class),
                            }
                    )
            )
    )
    @Transactional
    public Response register(@Valid RegisterBody registerBody) {
        var error = sessionRepository.validOfRegister(registerBody);
        if(error == null) {
            User user = sessionRepository.create(registerBody);
            if(user.id != null) {
                LoginBody loginBody = new LoginBody();
                loginBody.password = registerBody.password;
                loginBody.code = registerBody.code;
                loginBody.phoneNumber = registerBody.phoneNumber;
                loginBody.countryCodeId = registerBody.countryCodeId;
                user.token = sessionRepository.loginForPhoneNumber(loginBody).token;
            }
            return Response
                .status(Response.Status.OK)
                .entity(new ResultOfData<>(user))
                .build();
        } else {
            return Response
                .status(Response.Status.BAD_REQUEST)
                .entity(error)
                .build();
        }
    }

    @GET
    @Path("user")
    @Authenticated
    @Transactional
    public Response user() {
        User user = userRepository.authUser();
        return Response.status(Response.Status.OK)
            .entity(new ResultOfData<>((user != null && user.token != null) ? user : null))
            .build();
    }
}
