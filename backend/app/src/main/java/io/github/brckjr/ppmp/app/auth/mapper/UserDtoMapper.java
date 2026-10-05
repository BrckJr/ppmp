package io.github.brckjr.ppmp.app.auth.mapper;

import io.github.brckjr.ppmp.api.auth.dto.UserDto;
import io.github.brckjr.ppmp.domain.model.shared.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "cdi")
public interface UserDtoMapper {

  default UserDto toDto(User user) {
    return new UserDto(user.getId(), user.getUsername(), user.getEmail());
  }
}
