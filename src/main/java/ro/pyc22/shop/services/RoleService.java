package ro.pyc22.shop.services;

import org.springframework.security.core.GrantedAuthority;
import ro.pyc22.shop.model.Role;

import java.util.List;

public interface RoleService<T extends Role> {

    T createRole(T t);

    T getRoleByName(String name);
    List<Role> getAllRolesByUserId( Long id);

    List<GrantedAuthority> getAllAuthorities(Long id);
}
