package com.kalavastra.api.model;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "order_returns")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderReturn {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "return_id")
	private Long returnId;

	@CreationTimestamp
	@Column(name = "date_requested", updatable = false)
	private OffsetDateTime dateRequested;

	@UpdateTimestamp
	@Column(name = "date_updated")
	private OffsetDateTime dateUpdated;

	@Column(name = "reason", columnDefinition = "text")
	private String reason;

	@Column(name = "refund_amount", precision = 10, scale = 2)
	private BigDecimal refundAmount;

	@Column(name = "refund_method", length = 50)
	private String refundMethod;

	@Column(name = "status", length = 50)
	private String status;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "order_item_id", nullable = false)
	private OrderItem orderItem;
}
