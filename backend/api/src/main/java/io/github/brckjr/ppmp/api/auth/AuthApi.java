package io.github.brckjr.ppmp.api.auth;

import io.github.brckjr.ppmp.api.auth.dto.LoginRequestDto;
import io.github.brckjr.ppmp.api.auth.dto.RegisterRequestDto;
import io.github.brckjr.ppmp.api.auth.dto.UserDto;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Authentication", description = "Registration, login and the current session. The session is kept in an HttpOnly cookie.")
public interface AuthApi {

  @POST
  @Path("/register")
  @Operation(summary = "Register", description = "Creates a new account and signs the user in")
  @APIResponse(responseCode = "201", description = "Registered", content = @Content(schema = @Schema(implementation = UserDto.class)))
  @APIResponse(responseCode = "400", description = "Invalid username, email or password")
  @APIResponse(responseCode = "409", description = "Username or email already in use")
  Response register(@Valid RegisterRequestDto request);

  @POST
  @Path("/login")
  @Operation(summary = "Login", description = "Signs in with username or email and password")
  @APIResponse(responseCode = "200", description = "Signed in", content = @Content(schema = @Schema(implementation = UserDto.class)))
  @APIResponse(responseCode = "401", description = "Invalid credentials")
  @APIResponse(responseCode = "429", description = "Too many failed attempts")
  Response login(@Valid LoginRequestDto request);

  @POST
  @Path("/logout")
  @Operation(summary = "Logout", description = "Ends the session by clearing the session cookie")
  @APIResponse(responseCode = "204", description = "Signed out")
  Response logout();

  @GET
  @Path("/me")
  @Operation(summary = "Current user", description = "Returns the signed-in user")
  @APIResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = UserDto.class)))
  @APIResponse(responseCode = "401", description = "Not signed in")
  UserDto me();
}
