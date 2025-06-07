package com.kalavastra.api.service;

import com.kalavastra.api.model.ProductImage;
import com.kalavastra.api.repository.ProductImageRepository;
import com.kalavastra.api.repository.ProductRepository;
import com.kalavastra.api.model.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductImageService {

	private final ProductImageRepository imageRepo;
	private final ProductRepository productRepo;

	public ProductImageService(ProductImageRepository imageRepo, ProductRepository productRepo) {
		this.imageRepo = imageRepo;
		this.productRepo = productRepo;
	}

	@Transactional
	public ProductImage addImage(Long productId, ProductImage pImg) {
		Product product = productRepo.findById(productId)
				.orElseThrow(() -> new IllegalArgumentException("Product not found"));
		ProductImage img = ProductImage.builder().product(product).imageUrl(pImg.getImageUrl())
				.isPrimary(pImg.getIsPrimary()).build();
		return imageRepo.save(img);
	}

	public List<ProductImage> listImages(Long productId) {
		return imageRepo.findAllByProductIdAndIsActiveTrue(productId);
	}

	@Transactional
	public void softDelete(Long imageId) {
		ProductImage img = imageRepo.findById(imageId)
				.orElseThrow(() -> new IllegalArgumentException("Image not found"));
		img.setIsActive(false);
		imageRepo.save(img);
	}

	@Transactional
	public ProductImage markPrimary(Long imageId) {
		ProductImage img = imageRepo.findById(imageId)
				.orElseThrow(() -> new IllegalArgumentException("Image not found"));
		// unset other primaries
		listImages(img.getProduct().getId()).stream().filter(ProductImage::getIsPrimary).forEach(pi -> {
			pi.setIsPrimary(false);
			imageRepo.save(pi);
		});
		img.setIsPrimary(true);
		return imageRepo.save(img);
	}
}
