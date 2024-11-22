package com.web.service;

import com.web.dto.response.CategoryDto;
import com.web.entity.Category;
import com.web.exception.MessageException;
import com.web.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public Category save(Category category) {
        category.setDeleted(false);
        categoryRepository.save(category);
        return category;
    }

    public Category update(Category category) {
        category.setDeleted(false);
        categoryRepository.save(category);
        return category;
    }

    public void delete(Long categoryId) {
        try {
            categoryRepository.deleteById(categoryId);
        }
        catch (Exception e){
            throw new MessageException("Danh mục đã có bất động sản, không thể xóa");
//            Category category = categoryRepository.findById(categoryId).get();
//            category.setDeleted(true);
//            categoryRepository.save(category);
        }
    }

    public Category findById(Long id) {
        return null;
    }

    public List<Category> findAll() {
        List<Category> categories = categoryRepository.findAll();
        return categories;
    }

    public List<CategoryDto> findAllQuantity() {
        return categoryRepository.findAllQuantity();
    }
}
