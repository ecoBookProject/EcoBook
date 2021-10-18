package com.grupo4.projetointegrador.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "tb_order")
public class OrderModel {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long idOrder;

	private double totalPurchase;

	private Long productsQuantity;

	@OneToOne
	@MapsId
	@JoinColumn(name = "client_id")
	@JsonIgnoreProperties({ "orders" })
	private UserModel user;

	@ManyToMany(mappedBy = "orders", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JsonIgnoreProperties({ "title", "description", "price", "author", "year", "inventory", "language", "isbn", "ean",
			"country", "publisher", "format", "pages", "photo", "quantityOrderP", "category", "users", "wishList", "orders" })
	private List<BookModel> products = new ArrayList<>();
	
	

	public OrderModel() {}

	public long getIdOrder() {
		return idOrder;
	}


	public void setIdOrder(long idOrder) {
		this.idOrder = idOrder;
	}


	public double getTotalPurchase() {
		return totalPurchase;
	}

	public void setTotalPurchase(double totalPurchase) {
		this.totalPurchase = totalPurchase;
	}

	public Long getProductsQuantity() {
		return productsQuantity;
	}

	public void setProductsQuantity(Long productsQuantity) {
		this.productsQuantity = productsQuantity;
	}

	public UserModel getUser() {
		return user;
	}

	public void setUser(UserModel user) {
		this.user = user;
	}

	public List<BookModel> getProducts() {
		return products;
	}

	public void setProducts(List<BookModel> products) {
		this.products = products;
	}

}
