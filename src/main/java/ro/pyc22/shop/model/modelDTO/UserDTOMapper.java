package ro.pyc22.shop.model.modelDTO;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.GrantedAuthority;
import ro.pyc22.shop.model.Role;
import ro.pyc22.shop.model.User;
import ro.pyc22.shop.services.RoleService;

import java.util.List;

@RequiredArgsConstructor
public class UserDTOMapper {

    private final RoleService<Role> roleService;

    public static  UserDTO formUser(User user){
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user,userDTO);
        return userDTO;

    }

    public static User toUser(UserDTO userDTO){
        User user = new User();
        BeanUtils.copyProperties(userDTO,user);
        return user;

    }

    public static UserDTO fromUserWithAuth(User user, List<GrantedAuthority> authorities){

        UserDTO userDTO = formUser(user);
        userDTO.setAuthorities(authorities);
        return userDTO;

    }


}
