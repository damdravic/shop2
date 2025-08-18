package ro.pyc22.shop.repositories.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ro.pyc22.shop.model.Role;
import ro.pyc22.shop.model.User;
import ro.pyc22.shop.model.UserPrincipal;
import ro.pyc22.shop.repositories.RoleRepository;
import ro.pyc22.shop.repositories.UserRepository;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;


@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository<User> userRepository;
    private final RoleRepository<Role> roleRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.getUserByEmail(username);
        List<GrantedAuthority> authorities = new ArrayList<>();

        if(user == null){
            throw new UsernameNotFoundException("User not found");
        }else{
            List<Role> roles = roleRepository.findAllByUserId(user.getId());

                for(Role r : roles){
                if (r.getName() != null && !r.getName().isBlank()) {
                    authorities.add(new SimpleGrantedAuthority("ROLE_" + r.getName().trim()));
                }

                if(r.getPermission() != null && !r.getPermission().isBlank()){
                    authorities.add(new SimpleGrantedAuthority(r.getPermission().trim()));
                    }
                }

            authorities = new ArrayList<>(new LinkedHashSet<>(authorities));

            }

            return new UserPrincipal(user,authorities);
        }

    }

