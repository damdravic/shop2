package ro.pyc22.shop.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.security.Permission;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticatedUser {

    private User user;
    private List<Role> roles;
    private List<String> permissions;

}
