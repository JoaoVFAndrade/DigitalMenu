package com.br.digitalmenu.service;

import com.br.digitalmenu.model.Category;
import com.br.digitalmenu.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository){
        this.categoryRepository = categoryRepository;
    }

    public List<Category> findAll(){
        return categoryRepository.findAll();
    }

    public List<Category> findAllActive(){
        return categoryRepository.findByActiveTrue();
    }

    public Optional<Category> findById(Long id){
        return categoryRepository.findById(id);
    }

    public Category save(Category category){
        return categoryRepository.save(category);
    }

    public void delete(Long id){
        categoryRepository.findById(id).map(category -> {
            category.setActive(false);
            return categoryRepository.save(category);
        }).orElseThrow(() -> new RuntimeException("Categoria nao encontrada com id " + id));
    }
}
