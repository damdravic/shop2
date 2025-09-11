package ro.pyc22.shop.resources.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.pyc22.shop.model.HttpResponse;
import ro.pyc22.shop.model.User;
import ro.pyc22.shop.model.modelDTO.UserDTO;
import ro.pyc22.shop.model.modelDTO.UserDTOMapper;
import ro.pyc22.shop.services.UserService;

import java.util.Map;

import static ro.pyc22.shop.model.modelDTO.UserDTOMapper.formUser;

@RestController
@RequiredArgsConstructor
public class UserResource {
    private final UserService<User> userService;


    @PostMapping("/admin/register")
    public ResponseEntity<HttpResponse> creteUser(@RequestBody User user){
      UserDTO userDTO = formUser(userService.create(user));
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .httpStatus(HttpStatus.CREATED)
                        .statusCode(HttpStatus.CREATED.value())
                        .message("User Created")
                        .data(Map.of("user",userDTO))
                        .build()
        );


    }





}
