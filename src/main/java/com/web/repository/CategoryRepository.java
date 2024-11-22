package com.web.repository;

import com.web.dto.response.CategoryDto;
import com.web.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category,Long> {

    @Query("select c from Category c where c.name = ?1")
    public Optional<Category> findByName(String name);

    @Query("select c from Category c where c.name = ?1 and c.id <> ?2")
    public Optional<Category> findByNameAndId(String name, Long id);

    @Query("select c from Category c where c.deleted is null or c.deleted = false ")
    public List<Category> findAll();

    @Query(value = "select c.id, c.name, " +
            "(SELECT COUNT(re.real_estate_id) from real_estate_category re WHERE re.category_id = c.id) as quantity " +
            "from category c WHERE c.deleted = false or c.deleted is null", nativeQuery = true)
    public List<CategoryDto> findAllQuantity();

}
