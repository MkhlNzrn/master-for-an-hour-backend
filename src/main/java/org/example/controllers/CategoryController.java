package org.example.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.entities.Category;
import org.example.exceptions.CategoryAlreadyExistsException;
import org.example.exceptions.CategoryNotFoundException;
import org.example.exceptions.NoCategoriesFoundException;
import org.example.repositories.CategoryRepository;
import org.example.services.impl.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@Tag(name = "Operations with Categories")
public class CategoryController {

    private final CategoryRepository categoryRepository;
    private final CategoryService categoryService;

    @Operation(description = "Get all Categories")
    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        if (categories.isEmpty()) throw new NoCategoriesFoundException();
        return ResponseEntity.ok(categories);
    }

    @Operation(description = "Get all main Categories (Пока только main категории)")
    @GetMapping("/main")
    public ResponseEntity<List<Category>> getAllMainCategories() {
        List<Category> categories = categoryRepository.findAll();
        if (categories.isEmpty()) throw new NoCategoriesFoundException();
        return ResponseEntity.ok(categories);
    }

    @PostMapping("/{id}/photo")
    public ResponseEntity<String> uploadPhoto(@RequestParam("file") MultipartFile multipartFile, @PathVariable Long id) throws IOException {
        return ResponseEntity.ok(categoryService.uploadPhoto(multipartFile, id));
    }

    @Operation(description = "Get Category by Id")
    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable Long id) {
        return ResponseEntity.ok(categoryRepository.findById(id).orElseThrow(() -> new CategoryNotFoundException(id)));
    }

    @Operation(description = "Delete Category by Id")
    @DeleteMapping("/{id}")
    public ResponseEntity<Long> deleteCategoryById(@PathVariable Long id) {
        categoryRepository.deleteById(id);
        return ResponseEntity.ok(id);
    }

    @Operation(description = "Create Category")
    @PostMapping("/")
    public ResponseEntity<Long> createCategory(@RequestParam String name) {
        if (categoryRepository.findByName(name).isPresent()) throw new CategoryAlreadyExistsException(name);
        return ResponseEntity.ok(categoryRepository.save(new Category(name)).getId());
    }
}
