package ro.pyc22.shop.resources.admin;

import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ro.pyc22.shop.model.*;
import ro.pyc22.shop.model.modelDTO.UserDTO;
import ro.pyc22.shop.model.modelDTO.UserDTOMapper;
import ro.pyc22.shop.services.RoleService;
import ro.pyc22.shop.services.UserService;
import ro.pyc22.shop.util.JwtTokenService;

import java.util.Map;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@Slf4j
public class AuthResources {


    private final AuthenticationManager authenticationManager;
    private final UserService<User> userService;
    private final RoleService<Role> roleService;
    private final JwtTokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<HttpResponse> login(@RequestBody Credentials credentials){
        log.info("credentials");

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(credentials.getEmail(),credentials.getPassword()));
        log.info("credentials 2");
        UserDTO user =  userService.getUserByEmail(credentials.getEmail());
         return  user.isUsingMfa() ? sendPassword(user) :  sendResponse(user);



    }

    private ResponseEntity<HttpResponse> sendResponse(UserDTO userDTO) {
        log.info("sendResponse");
         return ResponseEntity.ok().body(
                 HttpResponse.builder()
                         .httpStatus(HttpStatus.OK)
                         .statusCode(HttpStatus.OK.hashCode())
                         .message("Authentication success !!!")
                         .data(Map.of("user",userDTO,"accessToken",tokenService.generateAccessToken(getPrincipal(userDTO))))
                         .build()
         );

    }

    private UserPrincipal getPrincipal(UserDTO userDTO) {

        return new UserPrincipal(UserDTOMapper.toUser(userDTO),roleService.getAllAuthorities(userDTO.getId()));
    }

    private ResponseEntity<HttpResponse> sendPassword(UserDTO userDTO) {
        log.info("sendCode");
        return  null;
    }


}
