package ro.pyc22.shop.resources.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ro.pyc22.shop.model.User;
import ro.pyc22.shop.services.UserService;

@RestController
@RequiredArgsConstructor
public class UserResource {
    private final UserService<User> userService;


    @PostMapping("/admin/register")
    public User creteUser(@RequestBody User user){
       return userService.create(user);

    }





}
