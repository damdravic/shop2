package ro.pyc22.shop.config.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ro.pyc22.shop.exceptions.ApiException;
import ro.pyc22.shop.util.JwtTokenService;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static java.util.Optional.ofNullable;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private static final String TOKEN_PREFIX = "Bearer ";
    private static final String[] PUBLIC_ROUTES = {"/shop/**","/admin/login","/user/verify/code","/user/refresh/token"};
    private static final String HTTP_OPTIONS_METHOD = "OPTIONS";
    protected  static final String TOKEN_KEY = "token";
    protected static  final String EMAIL_KEY = "email";
    private final JwtTokenService jwtTokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {


        try{
            Map<String ,String> values = getRequestValues(request);
            String token = getToken(request);


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

    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return  request.getMethod().equalsIgnoreCase(HTTP_OPTIONS_METHOD) || asList(PUBLIC_ROUTES).contains(request.getRequestURI());


    }
    private String getToken(HttpServletRequest request) {
      String token = request.getHeader(AUTHORIZATION);
      if(token != null && token.startsWith("Bearer ")){
          return token.substring(7);
      }
      if(request.getCookies() != null){
          for(Cookie c : request.getCookies()){
              if("accessToken".equals(c.getName())){
                  return c.getValue();
              }
          }
      }
      return null;
    }


    private Map<String, String> getRequestValues(HttpServletRequest request) {
        return Map.of(EMAIL_KEY,jwtTokenService.getSubject(getToken(request),request),TOKEN_KEY,getToken(request));
    }




}
