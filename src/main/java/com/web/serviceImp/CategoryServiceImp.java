package com.web.serviceImp;

import com.web.dto.response.CategoryDto;
import com.web.entity.Category;
import com.web.repository.CategoryRepository;
import com.web.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CategoryServiceImp implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Category save(Category category) {
        category.setDeleted(false);
        categoryRepository.save(category);
        return category;
    }

    @Override
    public Category update(Category category) {
        category.setDeleted(false);
        categoryRepository.save(category);
        return category;
    }

    @Override
    public void delete(Long categoryId) {
        try {
            categoryRepository.deleteById(categoryId);
        }
        catch (Exception e){
            Category category = categoryRepository.findById(categoryId).get();
            category.setDeleted(true);
            categoryRepository.save(category);
        }
    }

    @Override
    public Category findById(Long id) {
        return null;
    }

    @Override
    public List<Category> findAll() {
        List<Category> categories = categoryRepository.findAll();
        return categories;
    }

    @Override
    public List<CategoryDto> findAllQuantity() {
        return categoryRepository.findAllQuantity();
    }
}
