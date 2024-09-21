package com.web.service;

import com.web.dto.response.CategoryDto;
import com.web.entity.Category;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CategoryService {

    public Category save(Category category);

    public Category update(Category category);

    public void delete(Long categoryId);

    public Category findById(Long id);

    public List<Category> findAll();

    public List<CategoryDto> findAllQuantity();

}
