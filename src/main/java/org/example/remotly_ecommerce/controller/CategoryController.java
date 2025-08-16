package org.example.remotly_ecommerce.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.remotly_ecommerce.exception.CategoryException;
import org.example.remotly_ecommerce.model.Category;
import org.example.remotly_ecommerce.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/categories")
@Slf4j
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable Long id) throws CategoryException {
        Optional<Category> category = categoryService.findById(id);

        if (category.isPresent()) {
            log.info("Category found with id {}", id);
        } else {
            log.info("Category not found with id {}", id);
        }

        return category
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @GetMapping("")
    public ResponseEntity<List<Category>> getAllCategories() throws CategoryException {
        List<Category> categories = categoryService.findAll();
        return ResponseEntity.ok(categories);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/insert")
    public ResponseEntity<Category> createCategory(@RequestBody Category category) throws CategoryException {
        log.debug("POST /api/categories - Creating category");
        Category created = categoryService.createCategory(category);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

}
