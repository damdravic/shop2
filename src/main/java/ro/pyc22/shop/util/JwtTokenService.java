package ro.pyc22.shop.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;

import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import ro.pyc22.shop.exceptions.ApiException;
import ro.pyc22.shop.model.User;
import ro.pyc22.shop.model.UserPrincipal;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.auth0.jwt.algorithms.Algorithm.*;
import static java.lang.System.currentTimeMillis;
import static java.util.Arrays.stream;

@Service
public class JwtTokenService {
    public static final String NAMES = "names";
    private static final long ACCESS_TOKEN_EXPIRATION_TIME = 300_000_000;
    private static final long REFRESH_TOKEN_EXPIRATION_TIME = 432_000_000;
    @Value(value = "${jwt.secret}")
    private String secret;

    private static final String AUTHORITIES = "authorities";


    public String generateAccessToken(UserPrincipal userPrincipal){
    String[] claims = getClaimsFroUser(userPrincipal);
    String[] names = getNamesFromUser(userPrincipal);
    return JWT.create().withIssuer("PYC")
            .withAudience("")
            .withIssuedAt(new Date())
            .withSubject(userPrincipal.getUsername())
            .withArrayClaim(AUTHORITIES,claims)
            .withArrayClaim(NAMES,names)
            .withExpiresAt(new Date(currentTimeMillis() +  ACCESS_TOKEN_EXPIRATION_TIME))
            .sign(HMAC512(secret.getBytes()));
}

    private String[] getNamesFromUser(UserPrincipal userPrincipal) {
        return new String[]{
                userPrincipal.getUser().getFirstName()
        };
    }

    private String[] getClaimsFroUser(UserPrincipal userPrincipal) {
        return userPrincipal.getAuthorities().stream().map(GrantedAuthority::getAuthority).toArray(String[]::new);
    }

        public UsernamePasswordAuthenticationToken  generateAuthToken(String email, List<GrantedAuthority> authorities, HttpServletRequest request){
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(email,null,authorities);
        usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        return usernamePasswordAuthenticationToken;

    }


    public String getSubject(String token,HttpServletRequest request){
    try{
        return  getVerifier().verify(token).getSubject();
    }catch(InvalidClaimException ex ){
            request.setAttribute("invalidClaims", ex.getMessage());
            throw ex;
    }catch(TokenExpiredException ex ){
        request.setAttribute("expiredToken" , ex.getMessage());
        throw ex;
    }catch(Exception ex){
        throw new ApiException("Token verification failed !!! ");
    }
    }



public boolean isTokenValid(String email, String token){
        JWTVerifier verifier = getVerifier();
    return StringUtils.isNotEmpty(email) && !isTokenExpired(verifier,token);
}

    private boolean isTokenExpired(JWTVerifier verifier, String token) {
        Date expiration  = verifier.verify(token).getExpiresAt();
        return expiration.before(new Date());

    }


    private JWTVerifier getVerifier(){
        JWTVerifier jwtVerifier;
        try{
            Algorithm algorithm = HMAC512(secret);
            jwtVerifier = JWT.require(algorithm).withIssuer("PYC").build();
        }catch (JWTVerificationException ex){
            throw new JWTVerificationException("Verification toked invalid");
        }
 return jwtVerifier;
}


    private String[] getClaimsFromToken(String token) {
        JWTVerifier jwtVerifier = getVerifier();
        return jwtVerifier.verify(token).getClaim(AUTHORITIES).asArray(String.class);
    }

    public List<GrantedAuthority> getAuthorities(String token){
        String[] claims = getClaimsFromToken(token);
        return stream(claims).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }


}
