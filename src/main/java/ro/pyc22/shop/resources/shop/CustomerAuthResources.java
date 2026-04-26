package ro.pyc22.shop.resources.shop;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ro.pyc22.shop.model.*;
import ro.pyc22.shop.model.enumerations.RoleEnum;
import ro.pyc22.shop.model.modelDTO.UserDTO;
import ro.pyc22.shop.model.modelDTO.UserDTOMapper;
import ro.pyc22.shop.services.RoleService;
import ro.pyc22.shop.services.UserService;
import ro.pyc22.shop.util.JwtTokenService;

import java.time.Duration;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/shop/customers")
@Slf4j
public class CustomerAuthResources {

    private final UserService<User> userService;
    private final JwtTokenService tokenService;
    private final RoleService<Role> roleService;


    @PostMapping("/login")
    public ResponseEntity<HttpResponse> login (@RequestBody Credentials credentials){
        System.out.println("in customer login");
        System.out.println(credentials.getEmail() + " --- " + credentials.getPassword());
        UserDTO user = userService.getUser(credentials.getEmail());
        //TODO if is null nothing happening
        System.out.println("User is using Mfa " + user.getFirstName());
        return  user.isUsingMfa() ? sendCode(user) :
                sendResponse(user);
    }
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response){
        log.info("in logout resource");
        Cookie cookie = new Cookie("accessToken",null);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        return ResponseEntity.ok().build();
    }



    private ResponseEntity<HttpResponse> sendResponse(UserDTO userDTO) {
        System.out.println("in sendResponse");
        String token = tokenService.generateAccessToken(getPrincipal(userDTO));

        ResponseCookie accessCookie = ResponseCookie.from("accessToken",token)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .sameSite("Lax")
                .maxAge(Duration.ofMinutes(150))
                .build();

        System.out.println("Cookie" + accessCookie);


        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE,accessCookie.toString())
                .body(
                HttpResponse.builder()
                        .httpStatus(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .message("User loaded")
                        .data(Map.of("customer" ,userDTO))

                .build()
        );
    }

    private UserPrincipal getPrincipal(UserDTO userDTO) {
        return new UserPrincipal(UserDTOMapper.toUser(userDTO),roleService.getAllAuthorities(userDTO.getId()));
    }

    private ResponseEntity<HttpResponse> sendCode(UserDTO user) {
        return null ;
    }


@GetMapping("/loadMe")
    public ResponseEntity<HttpResponse> loadMe(Authentication authentication){

   String email = authentication.getName();
   UserDTO user = userService.getUser(email);
    //TODO if is null nothing happening
   log.info(user.getFirstName());


    return  ResponseEntity.ok().body(
            HttpResponse.builder()
                    .data(Map.of("customer",user ))
                    .build());
    }


    @PostMapping("/register")
    public ResponseEntity<HttpResponse> register(@RequestBody User regCustomer){
        log.info("in resource register");
        log.info(regCustomer.toString());
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .message("Welcome")
                        .httpStatus(HttpStatus.OK)
                        .data(Map.of("username",userService.create(regCustomer, RoleEnum.ROLE_CUSTOMER.name())))
                        .statusCode(HttpStatus.OK.value())
                        .developerMessage("new customer registered")

                        .build());
    }



}
