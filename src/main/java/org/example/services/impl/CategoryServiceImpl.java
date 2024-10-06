package org.example.services.impl;

import org.example.entities.Category;
import org.example.entities.Master;
import org.example.exceptions.CategoryNotFoundException;
import org.example.exceptions.MasterNotFoundException;
import org.example.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Value("${media.path}")
    private String PATH_TO_MEDIA;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public String uploadPhoto(MultipartFile multipartFile, Long id) throws IOException {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new CategoryNotFoundException(id));
        if (!category.getUrl().isEmpty()) {
            Path path = Path.of(category.getUrl());
            Files.delete(path);
        }
        Files.createDirectories(Paths.get(PATH_TO_MEDIA + category.getName() + "/photo/"));
        File file = new File(PATH_TO_MEDIA + category.getName() + "/photo/" + multipartFile.getOriginalFilename());
        multipartFile.transferTo(file);
        category.setUrl(file.getAbsolutePath());
        categoryRepository.save(category);
        return file.getAbsolutePath();
    }
}
