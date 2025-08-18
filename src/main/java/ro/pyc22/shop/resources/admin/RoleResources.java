package ro.pyc22.shop.resources.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ro.pyc22.shop.model.Role;
import ro.pyc22.shop.services.RoleService;

@RestController
@RequiredArgsConstructor
public class RoleResources {

    private final RoleService<Role> roleService;

    @PostMapping("/public/newRole")
    public Role newRole(@RequestBody Role role){
        return roleService.createRole(role);
    }
}
