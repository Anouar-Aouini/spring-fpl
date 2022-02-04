package com.fivepoints.spring.services;

import com.fivepoints.spring.entities.Category;
import com.fivepoints.spring.exceptions.ResourceNotFoundException;
import com.fivepoints.spring.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class CategoryService {
    @Autowired
    CategoryRepository categoryRepository;

    public List<Category> getAllCategories()
    {
        return this.categoryRepository.findAll();
    }
    public Category findCategoryById(long id){
        Optional<Category> tag = this.categoryRepository.findById(id);
        return tag.orElseThrow(() -> new ResourceNotFoundException("Category not found"));
    }
    public Category addCategory(Category category){
        return this.categoryRepository.save(category);
    }
    public List<Category> deleteTagById(long id){
        this.categoryRepository.deleteById(id);
        return this.categoryRepository.findAll();
    }
}
