package com.grupo4.projetointegrador.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import com.grupo4.projetointegrador.model.OrderModel;
import com.grupo4.projetointegrador.model.WishListModel;

public class UserDTO {

	private long IdClient;
	private String name;
	
	@NotBlank
	@Email
	private String email;
	
	@NotBlank
	private String password;
	
	private String type_user;

	private String token;
	
	private OrderModel orders;
	
	private WishListModel wishList;

	public long getIdClient() {
		return IdClient;
	}

	public void setIdClient(long idClient) {
		IdClient = idClient;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getType_user() {
		return type_user;
	}

	public void setType_user(String type_user) {
		this.type_user = type_user;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public OrderModel getOrders() {
		return orders;
	}

	public void setOrders(OrderModel orders) {
		this.orders = orders;
	}

	public WishListModel getWishList() {
		return wishList;
	}

	public void setWishList(WishListModel wishList) {
		this.wishList = wishList;
	}
	
}
