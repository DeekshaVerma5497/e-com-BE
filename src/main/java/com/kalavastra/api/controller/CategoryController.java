package com.kalavastra.api.controller;

import com.kalavastra.api.model.Category;
import com.kalavastra.api.service.CategoryService;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService svc;

    @PostMapping
    public ResponseEntity<Category> create(@RequestBody Category c) {
        return ResponseEntity.ok(svc.create(c));
    }

    @GetMapping("/{code}")
    public ResponseEntity<Category> get(@PathVariable String code) {
        return ResponseEntity.ok(svc.getByCode(code));
    }

    @PutMapping("/{code}")
    public ResponseEntity<Category> update(
        @PathVariable String code,
        @RequestBody Category c
    ) {
        return ResponseEntity.ok(svc.update(code, c));
    }

    @GetMapping
    public ResponseEntity<List<Category>> listAll() {
        return ResponseEntity.ok(svc.getAllCategories());
    }
}
