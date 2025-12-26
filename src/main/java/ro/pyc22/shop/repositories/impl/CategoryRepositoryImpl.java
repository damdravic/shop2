package ro.pyc22.shop.repositories.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.tags.form.SelectTag;
import ro.pyc22.shop.model.Category;
import ro.pyc22.shop.repositories.CategoryRepository;
import ro.pyc22.shop.repositories.rowMappers.CategoryRowMapper;

import java.util.List;
import java.util.Objects;

import static ro.pyc22.shop.repositories.queries.CategoryQueries.INSERT_CATEGORY_QUERY;
import static ro.pyc22.shop.repositories.queries.CategoryQueries.SELECT_ALL_CATEGORIES_QUERY;

@RequiredArgsConstructor
@Slf4j
@Repository
public class CategoryRepositoryImpl implements CategoryRepository<Category> {

    private final NamedParameterJdbcTemplate jdbc;


    @Override
    public Category createCategory(Category category) {

        KeyHolder kh = new GeneratedKeyHolder();
        SqlParameterSource params = getParams(category);


        try{
            jdbc.update(INSERT_CATEGORY_QUERY,params,kh);
        }catch(DataAccessException dae){

            log.error(dae.getMessage());
        }

        category.setId(Objects.requireNonNull(kh.getKey()).longValue());
        return category;
    }

    private SqlParameterSource getParams(Category category) {
        return new MapSqlParameterSource()
                .addValue("parentId",category.getParentId())
                .addValue("categoryName",category.getCategoryName())
                .addValue("categoryDescription" , category.getCategoryDescription())
                .addValue("slug",category.getSlug())
                .addValue("isActive",category.getIsActive())
                .addValue("sortOrder",category.getSortOrder());

    }

    @Override
    public List<Category> getAllCategories() {

        try{
           return jdbc.query(SELECT_ALL_CATEGORIES_QUERY,new CategoryRowMapper());
        }catch (DataAccessException dao){
            log.error(dao.getMessage());
        }
        return List.of();
    }
}
