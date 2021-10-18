package com.grupo4.projetointegrador.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.grupo4.projetointegrador.model.BookModel;
import com.grupo4.projetointegrador.model.OrderModel;
import com.grupo4.projetointegrador.repository.OrderRepository;
import com.grupo4.projetointegrador.service.BookService;

import io.swagger.annotations.Api;

@RestController
@Transactional
@RequestMapping("/order")
@CrossOrigin("*")
@Api(tags = "Controlador de ORDER", description = "Utilitario de Pedidos")
public class OrderController {

	@Autowired
	private OrderRepository repository;

	@Autowired
	private BookService service;

	@GetMapping
	public ResponseEntity<List<OrderModel>> findAllByOrders() {
		return ResponseEntity.ok(repository.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<OrderModel> findByIdOrder(@PathVariable long id) {
		return repository.findById(id).map(resp -> ResponseEntity.ok(resp)).orElse(ResponseEntity.notFound().build());
	}

	@GetMapping("/my-orders/{idOrder}")
	public ResponseEntity<List<BookModel>> findAllByProductsInWishList(@PathVariable long idOrder) {
		return ResponseEntity.ok(service.searchByCartProducts(idOrder));
	}

	@PostMapping
	public ResponseEntity<OrderModel> postOrder(@RequestBody OrderModel order) {
		return ResponseEntity.status(HttpStatus.CREATED).body(repository.save(order));
	}

	@PutMapping
	public ResponseEntity<OrderModel> putOrder(@RequestBody OrderModel order) {
		return ResponseEntity.ok(repository.save(order));
	}

	@DeleteMapping("/{id}")
	public void deleteOrder(@PathVariable long id) {
		repository.deleteById(id);
	}

	@DeleteMapping("/product_order/books/{idProduct}/orders/{idOrder}")
	public void putProduto(@PathVariable long idProduct, @PathVariable long idOrder) {
		service.deleteProduct(idProduct, idOrder);
	}

}
