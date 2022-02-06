package com.fivepoints.spring.controllers;

import com.fivepoints.spring.entities.Category;
import com.fivepoints.spring.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("category")
@CrossOrigin(origins = "http://localhost:4200")
public class CategoryController {
    @Autowired
    CategoryService categorytagService;

    @GetMapping("/")
    public ResponseEntity<List<Category>> getAllCategories()
    {
        List<Category> categoryList = this.categorytagService.getAllCategories();
        return new ResponseEntity<>(categoryList, HttpStatus.OK);
    }
    @PostMapping("/")
    public Category AddCategory(@RequestBody Category category)
    {
        Category addCategory = this.categorytagService.addCategory(category);
        return addCategory;
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> findCategoryByID(@PathVariable("id") long id)
    {
        Category category = this.categorytagService.findCategoryById(id);
        return new ResponseEntity<>(category, HttpStatus.OK);
    }

}
