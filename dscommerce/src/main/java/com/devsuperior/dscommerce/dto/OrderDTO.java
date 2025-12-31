package com.devsuperior.dscommerce.dto;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import com.devsuperior.dscommerce.entities.Order;

public class OrderDTO {

	private Long id;
	private Instant moment;
	private String status;

	private ClientDTO client;
	private PaymentDTO payment;

	private List<OrderItemDTO> items = new ArrayList<>();

	public OrderDTO() {
	}

	public OrderDTO(Long id, Instant moment, String status, ClientDTO client, PaymentDTO payment) {
		this.id = id;
		this.moment = moment;
		this.status = status;
		this.client = client;
		this.payment = payment;
	}

	public OrderDTO(Order entity) {
		id = entity.getId();
		moment = entity.getMoment();
		status = entity.getStatus().name();
		client = new ClientDTO(entity.getClient());
		payment = (entity.getPayment() == null) ? null : new PaymentDTO(entity.getPayment());
		entity.getItems().forEach(item -> items.add(new OrderItemDTO(item)));
	}

	public Long getId() {
		return id;
	}

	public Instant getMoment() {
		return moment;
	}

	public String getStatus() {
		return status;
	}

	public ClientDTO getClient() {
		return client;
	}

	public PaymentDTO getPayment() {
		return payment;
	}

	public List<OrderItemDTO> getItems() {
		return items;
	}

	public Double getTotal() {
		return items.stream().mapToDouble(OrderItemDTO::getSubTotal).sum();
	}
}
