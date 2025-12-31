package com.devsuperior.dscommerce.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devsuperior.dscommerce.dto.ProductDTO;
import com.devsuperior.dscommerce.dto.ProductMinDTO;
import com.devsuperior.dscommerce.services.ProductService;

@RestController
@RequestMapping(value = "/products")
public class ProductController {

	private final ProductService service;

	public ProductController(ProductService service) {
		this.service = service;
	}

	@GetMapping
	public Page<ProductMinDTO> findAllPaged(@PageableDefault(size = 12, sort = "name") Pageable pageable) {
		return service.findAllPaged(pageable);
	}

	@GetMapping(value = "/{id}")
	public ProductDTO findById(@PathVariable Long id) {
		return service.findById(id);
	}
}
