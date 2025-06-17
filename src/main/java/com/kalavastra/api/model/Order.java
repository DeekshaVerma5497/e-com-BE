package com.kalavastra.api.model;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "order_id")
	private Long orderId;

	@CreationTimestamp
	@Column(name = "date_created", updatable = false)
	private OffsetDateTime dateCreated;

	@UpdateTimestamp
	@Column(name = "date_updated")
	private OffsetDateTime dateUpdated;

	@Column(name = "status", length = 20, nullable = false)
	private String status;

	@Column(name = "total_amount", precision = 10, scale = 2, nullable = false)
	private BigDecimal totalAmount;

	@Column(name = "address_id", nullable = false)
	private Long addressId;

	@Column(name = "user_id", nullable = false, length = 50)
	private String userId;

	@Column(name = "order_code", length = 50, nullable = false, unique = true)
	private String orderCode;

	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<OrderItem> items = new ArrayList<>();
}
