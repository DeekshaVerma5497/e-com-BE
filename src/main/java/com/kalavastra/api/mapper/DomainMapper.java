package com.kalavastra.api.mapper;

import com.kalavastra.api.dto.AddressRequestDto;
import com.kalavastra.api.dto.AddressResponseDto;
import com.kalavastra.api.dto.CategoryDto;
import com.kalavastra.api.dto.ProductRequestDto;
import com.kalavastra.api.dto.ProductResponseDto;
import com.kalavastra.api.dto.UserResponseDto;
import com.kalavastra.api.model.Category;
import com.kalavastra.api.model.Product;
import com.kalavastra.api.model.User;
import com.kalavastra.api.model.Address;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(
  componentModel           = "spring",
  unmappedTargetPolicy     = ReportingPolicy.IGNORE
)
public interface DomainMapper {
	
	/** Convert our JPA User entity into the API response DTO. */
	UserResponseDto userToUserResponseDto(User user);


  // Category ↔ DTO
  CategoryDto categoryToDto(Category category);
  Category dtoToCategory(CategoryDto dto);

  /** 
   * Copy all non‑null fields from the DTO into the entity; 
   * we ignore categoryCode here so it doesn’t get nulled out.
   */
  @Mapping(target = "categoryCode", ignore = true)
  void updateCategoryFromDto(CategoryDto dto, @MappingTarget Category category);

  // Product ↔ DTO
  @Mapping(target = "categoryName", source = "category.name")
  ProductResponseDto productToDto(Product product);

  @Mapping(target = "productCode", ignore = true)
  @Mapping(target = "category", ignore = true)  // we wire the Category entity in the service
  Product dtoToProduct(ProductRequestDto dto);

  @Mapping(target = "productCode", ignore = true)
  @Mapping(target = "category", ignore = true)
  void updateProductFromDto(ProductRequestDto dto, @MappingTarget Product product);
  
  // ADDRESS
  AddressResponseDto addressToDto(Address address);

  @Mapping(target = "addressId", ignore = true)
  @Mapping(target = "dateCreated", ignore = true)
  @Mapping(target = "dateUpdated", ignore = true)
  @Mapping(target = "isActive", ignore = true)
  Address dtoToAddress(AddressRequestDto dto);

  @Mapping(target = "addressId", ignore = true)
  @Mapping(target = "user", ignore = true)            // set in service
  @Mapping(target = "dateCreated", ignore = true)
  @Mapping(target = "dateUpdated", ignore = true)
  @Mapping(target = "isActive", ignore = true)
  void updateAddressFromDto(AddressRequestDto dto, @MappingTarget Address address);



}
