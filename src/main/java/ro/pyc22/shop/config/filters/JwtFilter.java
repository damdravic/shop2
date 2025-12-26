package ro.pyc22.shop.config.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ro.pyc22.shop.exceptions.ApiException;
import ro.pyc22.shop.exceptions.CustomAuthenticationEntryPoint;
import ro.pyc22.shop.util.JwtTokenService;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static java.util.Optional.ofNullable;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private static final String TOKEN_PREFIX = "Bearer ";
 //   private static final String[] PUBLIC_ROUTES = {"/images/**","/admin/login","/admin/register","/user/verify/code","/user/refresh/token", "/shop/public/**"};
    private static final String HTTP_OPTIONS_METHOD = "OPTIONS";
    protected  static final String TOKEN_KEY = "token";
    protected static  final String EMAIL_KEY = "email";
    private final JwtTokenService jwtTokenService;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;


    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        System.out.println("in shouldNotFilter");
        System.out.println("shouldNotFilter - path :  " + request.getServletPath());
        String path = request.getServletPath();

        if(request.getMethod().equalsIgnoreCase(HTTP_OPTIONS_METHOD)){
            return true;
        }

        //for admin and users
        if(        path.equals("/admin/login")
                || path.equals("/admin/register")
                || path.equals("/admin/verify/code")
                || path.equals("/admin/verify/token")
                || path.equals("/shop/customers/login")
                || path.equals("/shop/customers/register")
                || path.equals("/shop/customers/verify/token")
                || path.equals("/shop/customers/verify/code")
        ){
            return true;
        }

        //
        return  path.startsWith("/images");


    }




    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {


        try{

            String token = getToken(request);
            log.info("Token {}", token);
            boolean isPublic = isIsPublic(request);

            if(token == null || token.isBlank()){
                SecurityContextHolder.clearContext();
                filterChain.doFilter(request,response);
               return;
            }

            Map<String ,String> values = getRequestValues(request);


            if(jwtTokenService.isTokenValid(values.get(EMAIL_KEY),token)){
                List<GrantedAuthority> authorities = jwtTokenService.getAuthorities(values.get(TOKEN_KEY));
                Authentication authentication = jwtTokenService.generateAuthToken(values.get(EMAIL_KEY),authorities,request);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }else{
                SecurityContextHolder.clearContext();
            }
            filterChain.doFilter(request,response);
        }catch(Exception exception){
            System.out.println(exception.getMessage());
          exception.getStackTrace();
        }

    }

    private static boolean isIsPublic(HttpServletRequest request) {
        String path = request.getServletPath();
        return path.startsWith("/shop/public")
                || path.startsWith("/images")
                || path.equals("/shop/customers/login")
                || path.equals("/shop/customers/register")
                || path.equals("/shop/customers/verify/token")
                || path.equals("/shop/customers/verify/code")
                || path.equals("/admin/login")
                || path.equals("/admin/register")
                || path.equals("/admin/verify/code")
                || path.equals("/admin/verify/token");
    }


    private String getToken(HttpServletRequest request) {
      String token = request.getHeader(AUTHORIZATION);

      if(token != null && token.startsWith("Bearer ")){
          return token.substring(7);
      }

      if(request.getCookies() != null){
          log.info("Request {}",request.toString());

          for(Cookie c : request.getCookies()){
              log.info("Cookie -> {} " ,c.getName());
              if("accessToken".equals(c.getName())){
                  System.out.println("accessToken exist");
                  return c.getValue();
              }
          }
      }

      return null;
    }


    private Map<String, String> getRequestValues(HttpServletRequest request) {
        log.info("in getRequestValues");
        return Map.of(EMAIL_KEY,jwtTokenService.getSubject(getToken(request),request),TOKEN_KEY,getToken(request));
    }




}
