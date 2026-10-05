package io.github.brckjr.ppmp.app.auth;

import io.github.brckjr.ppmp.api.auth.AuthApi;
import io.github.brckjr.ppmp.api.auth.dto.LoginRequestDto;
import io.github.brckjr.ppmp.api.auth.dto.RegisterRequestDto;
import io.github.brckjr.ppmp.api.auth.dto.UserDto;
import io.github.brckjr.ppmp.app.auth.mapper.UserDtoMapper;
import io.github.brckjr.ppmp.domain.model.shared.User;
import io.github.brckjr.ppmp.domain.service.auth.AuthService;
import io.github.brckjr.ppmp.domain.service.auth.AuthenticationFailedException;
import io.github.brckjr.ppmp.domain.service.auth.TooManyLoginAttemptsException;
import io.github.brckjr.ppmp.domain.service.auth.UserAlreadyExistsException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.NotAuthorizedException;
import jakarta.ws.rs.core.Response;

@ApplicationScoped
public class AuthApp implements AuthApi {

  private final AuthService authService;
  private final SessionService sessionService;
  private final CurrentUser currentUser;
  private final UserDtoMapper mapper;

  @Inject
  public AuthApp(AuthService authService, SessionService sessionService, CurrentUser currentUser, UserDtoMapper mapper) {
    this.authService = authService;
    this.sessionService = sessionService;
    this.currentUser = currentUser;
    this.mapper = mapper;
  }

  @Override
  public Response register(RegisterRequestDto request) {
    try {
      User user = authService.register(request.username(), request.email(), request.password());
      return Response.status(Response.Status.CREATED)
        .cookie(sessionService.startSession(user))
        .entity(mapper.toDto(user))
        .build();
    } catch (UserAlreadyExistsException ex) {
      throw new ClientErrorException(ex.getMessage(), Response.Status.CONFLICT);
    } catch (IllegalArgumentException ex) {
      throw new BadRequestException(ex.getMessage(), ex);
    }
  }

  @Override
  public Response login(LoginRequestDto request) {
    try {
      User user = authService.authenticate(request.identifier(), request.password());
      return Response.ok(mapper.toDto(user)).cookie(sessionService.startSession(user)).build();
    } catch (AuthenticationFailedException ex) {
      throw new NotAuthorizedException(ex.getMessage(), Response.status(Response.Status.UNAUTHORIZED).build());
    } catch (TooManyLoginAttemptsException ex) {
      throw new ClientErrorException(ex.getMessage(), Response.Status.TOO_MANY_REQUESTS);
    }
  }

  @Override
  public Response logout() {
    return Response.noContent().cookie(sessionService.endSession()).build();
  }

  @Override
  public UserDto me() {
    return authService.findActiveUser(currentUser.id())
      .map(mapper::toDto)
      .orElseThrow(() -> new NotAuthorizedException("Invalid session", Response.status(Response.Status.UNAUTHORIZED).build()));
  }
}
