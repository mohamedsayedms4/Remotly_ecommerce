package org.example.remotly_ecommerce.service;

import org.example.remotly_ecommerce.exception.CategoryException;
import org.example.remotly_ecommerce.model.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService {

    Optional<Category> findById(Long id) throws CategoryException;
    List<Category> findAll() throws CategoryException;
    Category createCategory(Category category)throws CategoryException;
    Optional<Category> updateCategory(Category category);
    void deleteCategory(int id);
}
