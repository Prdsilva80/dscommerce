package com.devsuperior.dscommerce.services;

import java.time.Instant;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dscommerce.dto.OrderDTO;
import com.devsuperior.dscommerce.dto.OrderItemDTO;
import com.devsuperior.dscommerce.entities.Order;
import com.devsuperior.dscommerce.entities.OrderItem;
import com.devsuperior.dscommerce.entities.OrderStatus;
import com.devsuperior.dscommerce.entities.Product;
import com.devsuperior.dscommerce.entities.User;
import com.devsuperior.dscommerce.repositories.OrderItemRepository;
import com.devsuperior.dscommerce.repositories.OrderRepository;
import com.devsuperior.dscommerce.repositories.ProductRepository;
import com.devsuperior.dscommerce.services.exceptions.ForbiddenException;
import com.devsuperior.dscommerce.services.exceptions.ResourceNotFoundException;

@Service
public class OrderService {

	private final OrderRepository orderRepository;
	private final ProductRepository productRepository;
	private final OrderItemRepository orderItemRepository;
	private final AuthService authService;

	public OrderService(OrderRepository orderRepository, ProductRepository productRepository,
			OrderItemRepository orderItemRepository, AuthService authService) {
		this.orderRepository = orderRepository;
		this.productRepository = productRepository;
		this.orderItemRepository = orderItemRepository;
		this.authService = authService;
	}

	@Transactional(readOnly = true)
	public OrderDTO findById(Long id) {

		Order order = orderRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Order not found"));

		User user = authService.authenticated();

		boolean isAdmin = user.getRoles().stream().anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN"));
		boolean isOwner = order.getClient() != null && order.getClient().getId().equals(user.getId());

		if (!isAdmin && !isOwner) {
			throw new ForbiddenException("Access denied");
		}

		return new OrderDTO(order);
	}

	@Transactional
	public OrderDTO insert(OrderDTO dto) {

		User client = authService.authenticated();

		Order order = new Order();
		order.setMoment(Instant.now());
		order.setStatus(OrderStatus.WAITING_PAYMENT);
		order.setClient(client);

		order = orderRepository.save(order);

		for (OrderItemDTO itemDto : dto.getItems()) {
			Product product = productRepository.getReferenceById(itemDto.getProductId());
			OrderItem item = new OrderItem(order, product, itemDto.getQuantity(), product.getPrice());
			order.getItems().add(item);
		}

		orderItemRepository.saveAll(order.getItems());

		return new OrderDTO(order);
	}
}
