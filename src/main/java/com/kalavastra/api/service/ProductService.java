package com.kalavastra.api.service;

import com.kalavastra.api.dto.ProductRequestDto;
import com.kalavastra.api.dto.ProductResponseDto;
import com.kalavastra.api.exception.ResourceNotFoundException;
import com.kalavastra.api.mapper.DomainMapper;
import com.kalavastra.api.model.Category;
import com.kalavastra.api.model.Product;
import com.kalavastra.api.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Handles product creation, updates, and paginated listings.
 */
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepo;
    private final CategoryService categoryService;
    private final DomainMapper mapper;
    
    public ProductResponseDto create(ProductRequestDto dto) {
        Category category = categoryService.getByCode(dto.getCategoryCode());
        if (category == null) {
        	throw new ResourceNotFoundException("Category", "categoryCode", dto.getCategoryCode());
        }

        Product product = mapper.dtoToProduct(dto);
        product.setCategory(category);

        // ✅ Auto-generate product code if not present
        if (product.getProductCode() == null || product.getProductCode().isBlank()) {
            String namePart = product.getName().toLowerCase().replaceAll("\\s+", "-");
            String randomPart = UUID.randomUUID().toString().substring(0, 6);
            product.setProductCode(namePart + "-" + randomPart);
        }

        return mapper.productToDto(productRepo.save(product));
    }


    public ProductResponseDto update(Long id, ProductRequestDto dto) {
        Product p = productRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id.toString()));
        mapper.updateProductFromDto(dto, p);
        p.setCategory(categoryService.getByCode(dto.getCategoryCode()));
        return mapper.productToDto(productRepo.save(p));
    }

    public void delete(Long id) {
        if (!productRepo.existsById(id)) {
            throw new ResourceNotFoundException("Product", "id", id.toString());
        }
        productRepo.deleteById(id);
    }

    public ProductResponseDto getById(Long id) {
        return productRepo.findById(id)
                .map(mapper::productToDto)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id.toString()));
    }

    public Page<ProductResponseDto> getAll(String categoryCode, Pageable pageable) {
        Page<Product> products;
        if (categoryCode != null) {
            Category category = categoryService.getByCode(categoryCode);
            products = productRepo.findAllByCategory(category, pageable);
        } else {
            products = productRepo.findAll(pageable);
        }

        return products.map(mapper::productToDto);
    }
    
    public ProductResponseDto updateByCode(String productCode, ProductRequestDto dto) {
        Product product = productRepo.findByProductCode(productCode)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productCode", productCode));

        mapper.updateProductFromDto(dto, product);

        // Also update category if categoryCode is provided
        if (dto.getCategoryCode() != null) {
            Category category = categoryService.getByCode(dto.getCategoryCode());
            product.setCategory(category);
        }

        // Update productCode again only if missing (or you can skip this to retain existing)
        if (product.getProductCode() == null || product.getProductCode().isBlank()) {
            String namePart = product.getName().toLowerCase().replaceAll("\\s+", "-");
            String randomPart = UUID.randomUUID().toString().substring(0, 6);
            product.setProductCode(namePart + "-" + randomPart);
        }

        return mapper.productToDto(productRepo.save(product));
    }
    
    /**
     * Fetch product by its business‐slug code.
     */
    public ProductResponseDto getByCode(String productCode) {
        Product product = productRepo.findByProductCode(productCode)
            .orElseThrow(() ->
                new ResourceNotFoundException("Product", "productCode", productCode)
            );
        return mapper.productToDto(product);
    }
    
    public Product getEntityByCode(String code) {
    	  return productRepo.findByProductCode(code)
    	    .orElseThrow(() -> new ResourceNotFoundException("Product", "productCode", code));
    	}


}
