package com.web.validate;

import com.web.entity.Category;
import com.web.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class CategoryValidator implements Validator {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return Category.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Category category = (Category) target;
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "category.name.empty", "Tên danh mục không được để trống");
        if(category.getId() == null){
            if (categoryRepository.findByName(category.getName()).isPresent()) {
                errors.rejectValue("name", "category.name", "Tên danh mục đã tồn tại");
            }
        }
        else{
            if (categoryRepository.findByNameAndId(category.getName(), category.getId()).isPresent()) {
                errors.rejectValue("name", "category.name", "Tên danh mục đã tồn tại");
            }
        }
    }


}
