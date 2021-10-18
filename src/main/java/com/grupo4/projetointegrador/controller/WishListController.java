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
import com.grupo4.projetointegrador.model.WishListModel;
import com.grupo4.projetointegrador.repository.WishListRepository;
import com.grupo4.projetointegrador.service.BookService;

import io.swagger.annotations.Api;

@RestController
@Transactional
@RequestMapping("/wishlist")
@CrossOrigin("*")
@Api(tags = "Controlador de WISHLIST", description = "Utilitario de Lista de Desejos")
public class WishListController {

	@Autowired
	private WishListRepository repository;

	@Autowired
	private BookService service;

	@GetMapping
	public ResponseEntity<List<WishListModel>> findAllByWishList() {
		return ResponseEntity.ok(repository.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<WishListModel> findByIdWishList(@PathVariable long id) {
		return repository.findById(id).map(resp -> ResponseEntity.ok(resp)).orElse(ResponseEntity.notFound().build());
	}

	@GetMapping("/wishlist/{idWishList}/name/{name}")
	public ResponseEntity<List<BookModel>> findAllByNomeProdutoListaDeDesejos(@PathVariable long idWishList,
			@PathVariable String name) {
		return ResponseEntity.ok(service.searchByIdProducInWishList(idWishList, name));
	}

	@GetMapping("/wishlist/{idWishList}")
	public ResponseEntity<List<BookModel>> findAllByProductsWishList(@PathVariable long idWishList) {

		return ResponseEntity.ok(service.searchByProductInWishList(idWishList));
	}

	@PostMapping
	public ResponseEntity<WishListModel> postWishList(@RequestBody WishListModel wishList) {
		return ResponseEntity.status(HttpStatus.CREATED).body(repository.save(wishList));
	}

	@PutMapping
	public ResponseEntity<WishListModel> putListaDeDesejos(@RequestBody WishListModel wishList) {
		return ResponseEntity.ok(repository.save(wishList));
	}

	@DeleteMapping("/list_product/books/{idProduct}/listaDesejos/{idWishList}")
	public ResponseEntity<BookModel> removeProdutoListaDeDesejos(@PathVariable long idProduct,
			@PathVariable long idWishList) {
		return ResponseEntity.ok(service.removeProductWishList(idProduct, idWishList));
	}

	@DeleteMapping("/{id}")
	public void deleteListaDeDesejos(@PathVariable long id) {
		repository.deleteById(id);
	}

}
