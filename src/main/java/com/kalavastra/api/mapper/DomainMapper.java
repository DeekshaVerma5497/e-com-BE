package com.kalavastra.api.mapper;

import com.kalavastra.api.dto.AddressRequestDto;
import com.kalavastra.api.dto.AddressResponseDto;
import com.kalavastra.api.dto.CartItemResponseDto;
import com.kalavastra.api.dto.CartResponseDto;
import com.kalavastra.api.dto.CartSummaryDto;
import com.kalavastra.api.dto.CategoryDto;
import com.kalavastra.api.dto.ProductRequestDto;
import com.kalavastra.api.dto.ProductResponseDto;
import com.kalavastra.api.dto.UserResponseDto;
import com.kalavastra.api.model.Category;
import com.kalavastra.api.model.Product;
import com.kalavastra.api.model.User;
import com.kalavastra.api.model.Address;
import com.kalavastra.api.model.Cart;
import com.kalavastra.api.model.CartItem;

import java.util.List;

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
  
  /** Cart → full response. */
  @Mapping(target = "userId", source = "user.userId")
  CartResponseDto cartToDto(Cart cart);

  /** CartItem → DTO. */
  @Mapping(target = "cartItemId", source = "cartItemId")
  @Mapping(target = "productCode", source = "product.productCode")
  @Mapping(target = "name",     source = "product.name")
  @Mapping(target = "unitPrice", source = "product.price")
  @Mapping(target = "lineTotal", expression = "java(cartItem.getProduct().getPrice().multiply(java.math.BigDecimal.valueOf(cartItem.getQuantity())))")
  CartItemResponseDto cartItemToDto(CartItem cartItem);

  /** Bulk list mapping. */
  List<CartItemResponseDto> cartItemsToItemDtos(List<CartItem> items);

  /** Cart → summary view. */
  @Mapping(target = "userId", source = "user.userId")
  @Mapping(target = "distinctItemCount", expression = "java(cart.getItems().size())")
  @Mapping(target = "totalQuantity",      expression = "java(cart.getItems().stream().mapToInt(CartItem::getQuantity).sum())")
  @Mapping(target = "totalPrice",         expression = "java(cart.getItems().stream().map(i -> i.getProduct().getPrice().multiply(java.math.BigDecimal.valueOf(i.getQuantity()))).reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add))")
  CartSummaryDto cartToSummaryDto(Cart cart);



}
