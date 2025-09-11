package ro.pyc22.shop.resources.admin;

import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ro.pyc22.shop.model.*;
import ro.pyc22.shop.model.modelDTO.UserDTO;
import ro.pyc22.shop.model.modelDTO.UserDTOMapper;
import ro.pyc22.shop.repositories.RoleRepository;
import ro.pyc22.shop.services.RoleService;
import ro.pyc22.shop.services.UserService;
import ro.pyc22.shop.util.JwtTokenService;

import java.time.Duration;
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


    //TODO for test  -- you need to change  and add methode in role service
    private final RoleRepository<Role> roleRepository;

    @PostMapping("/login")
    public ResponseEntity<HttpResponse> login(@RequestBody Credentials credentials){

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(credentials.getEmail(),credentials.getPassword()));

        UserDTO user =  userService.getUserByEmail(credentials.getEmail());
         return  user.isUsingMfa() ? sendPassword(user) :  sendResponse(user);



    }

 /*   private ResponseEntity<HttpResponse> sendResponse(UserDTO userDTO) {
        log.info("sendResponse");
         return ResponseEntity.ok().body(
                 HttpResponse.builder()
                         .httpStatus(HttpStatus.OK)
                         .statusCode(HttpStatus.OK.hashCode())
                         .message("Authentication success !!!")
                         .data(Map.of("user",userDTO,
                                 "accessToken",tokenService.generateAccessToken(getPrincipal(userDTO))))
                         .build()
         );

    }

  */



    private ResponseEntity<HttpResponse> sendResponse(UserDTO userDTO){

        String token = tokenService.generateAccessToken(getPrincipal(userDTO));

        ResponseCookie accessCookie = ResponseCookie.from("accessToken",token)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .sameSite("Lax")
                .maxAge(Duration.ofMinutes(15))
                .build();
        ResponseCookie authMarker = ResponseCookie.from("auth", "1")
                .httpOnly(false)
                .secure(false)              // true pe HTTPS
                .path("/")
                .sameSite("Lax")            // IMPORTANT pt localhost
                .maxAge(Duration.ofMinutes(15))
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE,accessCookie.toString())
                .header(HttpHeaders.SET_COOKIE, authMarker.toString())
                .body(
                        HttpResponse.builder()
                                .httpStatus(HttpStatus.OK)
                                .statusCode(HttpStatus.OK.hashCode())
                                .message("Authentication success !!!")
                                .data(Map.of("user",userDTO))
                                .build()
                );
    }
/*
    @GetMapping("/auth/me")
    public ResponseEntity<HttpResponse> me(@CookieValue(value="accessToken", required=false) String at) {
        if (at == null || !tokenService.isTokenValid(tokenService.getSubject(at,),at)) return tokenInvalidResponse();
        var user = userService.getCurrentUser(at); // din token/db
        return ResponseEntity.ok(Map.of(
                "id", user.getId(),
                "email", user.getEmail(),
                "roles", user.getRoles(),
                "permissions", user.getPermissions()
        ));
    }
    */
    @GetMapping("/auth/me")
    public ResponseEntity<HttpResponse> me(@AuthenticationPrincipal UserPrincipal userPrincipal){
        User user = userPrincipal.getUser();
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .httpStatus(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.hashCode())
                        .message("Authentication success !!!")
                        .data(Map.of("name",user.getFirstName(),
                                "email",user.getEmail(),
                                "role",roleRepository.getRoleByUserId(user.getId())))
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
