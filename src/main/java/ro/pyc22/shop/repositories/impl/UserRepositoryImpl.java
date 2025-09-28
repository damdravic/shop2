package ro.pyc22.shop.repositories.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import ro.pyc22.shop.exceptions.ApiException;
import ro.pyc22.shop.model.AuthenticatedUser;
import ro.pyc22.shop.model.Role;
import ro.pyc22.shop.model.User;
import ro.pyc22.shop.repositories.RoleRepository;
import ro.pyc22.shop.repositories.UserRepository;
import ro.pyc22.shop.repositories.rowMappers.UserRowMapper;

import java.util.*;

import static ro.pyc22.shop.model.enumerations.RoleEnum.ROLE_USER;
import static ro.pyc22.shop.repositories.queries.UserQueries.INSERT_USER_QUERY;
import static ro.pyc22.shop.repositories.queries.UserQueries.SELECT_USER_BY_EMAIL;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository<User> {

    private final NamedParameterJdbcTemplate jdbc;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository<Role> roleRepository;

    @Override
    public User create(User user)  {
        KeyHolder kh = new GeneratedKeyHolder();
        SqlParameterSource  params = getParams(user);
       try {

           jdbc.update(INSERT_USER_QUERY, params, kh);
           user.setId(Objects.requireNonNull(kh.getKey()).longValue());
           roleRepository.addRoleToUser(user,ROLE_USER.name());
       }catch(DataAccessException dae){
           //TODO to implement ExceptionHandler
           System.out.println("error access" + dae.getMessage());
        }





         return user;
    }

    @Override
    public User getUserByEmail(String email) {
       try{
             return jdbc.queryForObject(SELECT_USER_BY_EMAIL, Map.of("email",email), new UserRowMapper());
       }catch(EmptyResultDataAccessException ex){
           throw new UsernameNotFoundException("This user not exist in owr database ! Please try again ...");

       }catch (DataAccessException dae){
           //TODO to implement ExceptionHandler

          throw new ApiException(dae.getMessage());
       }

    }

    @Override
    public AuthenticatedUser getAuthenticatesUserByUserId(String email) {

        User user = getUserByEmail(email);
        List<Role> roles = roleRepository.findAllByUserId(user.getId());
        //fake permissions.All authenticatedUser will have this permissions TODO --  fix it
        List<String> permissions = new ArrayList<>(List.of("PRODUCT:READ"));



        return new AuthenticatedUser(user,roles,permissions);
    }

    private SqlParameterSource getParams(User user) {
        System.out.println("in getParams");
        return new MapSqlParameterSource()
                .addValue("firstName",user.getFirstName())
                .addValue("lastName",user.getLastName())
                .addValue("email",user.getEmail())
                .addValue("password",passwordEncoder.encode(user.getPassword()));

    }
}
