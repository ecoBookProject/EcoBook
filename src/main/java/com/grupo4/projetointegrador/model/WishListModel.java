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
@Table(name = "tb_wishlist")
public class WishListModel {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long IdWishList;

	@OneToOne
	@MapsId
	@JoinColumn(name = "client_id")
	@JsonIgnoreProperties({ "wishList" })
	private UserModel user;

	@ManyToMany(mappedBy = "wishList", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JsonIgnoreProperties({ "title", "description", "price", "author", "year", "inventory", "language", "isbn", "ean",
		"country", "publisher", "format", "pages", "photo", "quantityOrderP", "category", "users", "wishList", "orders" })
	private List<BookModel> products = new ArrayList<>();

	public Long getIdWishList() {
		return IdWishList;
	}

	public void setIdWishList(Long idWishList) {
		IdWishList = idWishList;
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
