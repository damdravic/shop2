package ro.pyc22.shop.repositories.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Repository;
import ro.pyc22.shop.exceptions.ApiException;
import ro.pyc22.shop.model.Role;
import ro.pyc22.shop.model.User;
import ro.pyc22.shop.repositories.RoleRepository;
import ro.pyc22.shop.repositories.rowMappers.RoleRowMapper;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static ro.pyc22.shop.repositories.queries.RoleQueries.*;

@Repository
@RequiredArgsConstructor
public class RoleRepositoryImpl implements RoleRepository<Role> {

    private final NamedParameterJdbcTemplate jdbc;
    @Override
    public Role createRole(Role role) {
        KeyHolder kh = new GeneratedKeyHolder();
        try{
           jdbc.update(INSERT_ROLE_QUERY, getParam(role),kh);
    }catch(DataAccessException dae){
            throw  new ApiException(dae.getMessage());
        }

        role.setId(Objects.requireNonNull(kh.getKey()).longValue());
        return role;
}

    @Override
    public Role getRoleByName(String name) {
        try{
          return   jdbc.queryForObject(SELECT_ROLE_BY_NAME_QUERY,Map.of("name",name),new RoleRowMapper());
        }catch(EmptyResultDataAccessException ex){
            throw new ApiException("Role not found");

        }catch(DataAccessException ex){
            throw new ApiException("Error - " + ex.getMessage());
        }
    }

    @Override
    public void addRoleToUser(User user, String roleName) {
        try{
            Role role = getRoleByName(roleName);
            jdbc.update(INSERT_ROLE_TO_USERS_ROLES,Map.of("userId",user.getId(),"roleId",role.getId()));
        }catch(DataAccessException ex){
            throw  new ApiException(ex.getMessage());
        }}

    @Override
    public Role getRoleByUserId(Long userId) {
        return jdbc.queryForObject(SELECT_ROLE_BY_USER_ID_QUERY, Map.of("userId",userId), new RoleRowMapper());
    }

    @Override
    public List<Role> findAllByUserId(Long id) {
        try{
            return jdbc.query(SELECT_ALL_ROLES_BY_USER_ID,Map.of("id",id),new RoleRowMapper());

        }catch (DataAccessException ex){

            throw new ApiException(ex.getMessage());
        }

    }


    private SqlParameterSource getParam(Role role) {
        return new MapSqlParameterSource()
                .addValue("name",role.getName())
                .addValue("permission",role.getPermission());
    }


}
