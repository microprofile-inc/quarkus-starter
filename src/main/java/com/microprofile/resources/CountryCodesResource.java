package com.microprofile.resources;

import com.microprofile.entities.CountryCode;
import com.microprofile.payload.ResultOfArray;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.info.License;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.media.SchemaProperty;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@OpenAPIDefinition(
    tags = {
        @Tag(name="widget", description="get all support CountryCodes."),
    },
    info = @Info(
        title="CountryCodes",
        version = "1.0.0",
        contact = @Contact(
            name = "CountryCodes API Support",
            url = "http://exampleurl.com/contact",
            email = "techsupport@example.com"),
        license = @License(
            name = "Apache 2.0",
            url = "https://www.apache.org/licenses/LICENSE-2.0.html"))
)
@Path("/country-codes")
public class CountryCodesResource {
    @Tag(name = "CountryCodes", description = "get all support CountryCodes.")
    @Operation(summary = "Update an existing pet")
    @APIResponse(
        responseCode = "200",
        description = "CountryCodes List",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(
                implementation = ResultOfArray.class,
                properties = {
                    @SchemaProperty(name = "records", type = SchemaType.ARRAY, implementation = CountryCode.class),
                }
            )
        )
    )
    @GET
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    public Response all() {
        return Response
            .status(Response.Status.OK)
            .entity(
                new ResultOfArray<>(CountryCode.findAll().list())
            )
            .build();
    }
}
