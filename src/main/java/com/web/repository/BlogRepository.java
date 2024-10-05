package com.web.repository;

import com.web.entity.Blog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BlogRepository extends JpaRepository<Blog,Long> {

    @Query(value = "select b.* from blog b order by b.num_view desc limit 7", nativeQuery = true)
    public List<Blog> bestView();

}
