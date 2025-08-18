package ro.pyc22.shop.repositories;

import jakarta.validation.constraints.NotNull;
import org.springframework.security.core.GrantedAuthority;
import ro.pyc22.shop.model.Role;
import ro.pyc22.shop.model.User;

import java.util.List;

public interface RoleRepository<T extends Role> {

    T createRole(T t);

  T getRoleByName(String name);

  void addRoleToUser(User user, String roleName);

  T getRoleByUserId(Long userId);

    List<T> findAllByUserId(@NotNull @NotNull Long id);
}
