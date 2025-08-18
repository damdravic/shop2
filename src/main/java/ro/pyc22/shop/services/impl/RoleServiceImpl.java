package ro.pyc22.shop.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import ro.pyc22.shop.model.Role;
import ro.pyc22.shop.repositories.RoleRepository;
import ro.pyc22.shop.services.RoleService;

import java.util.*;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService<Role> {

    private final RoleRepository<Role>  roleRepository;

    @Override
    public Role createRole(Role role) {
        return roleRepository.createRole(role);
    }

    @Override
    public Role getRoleByName(String name) {
        return null;
    }

    @Override
    public List<Role> getAllRolesByUserId(Long id) {
        return roleRepository.findAllByUserId(id);
    }

    @Override
    public List<GrantedAuthority> getAllAuthorities(Long id) {
        List<Role> roles = getAllRolesByUserId(id);
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();


        for(Role r : roles){

            if(r.getName() != null ){
                authorities.add(new SimpleGrantedAuthority("ROLE_"+r.getName()));
            }
            if(r.getPermission() != null){
                authorities.add(new SimpleGrantedAuthority(r.getPermission()));
            }

            authorities = new ArrayList<>(new LinkedHashSet<>(authorities));

        }

       return authorities;

    }


}
