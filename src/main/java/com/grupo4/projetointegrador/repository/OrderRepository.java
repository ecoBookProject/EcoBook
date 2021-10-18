package com.grupo4.projetointegrador.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.grupo4.projetointegrador.model.OrderModel;

@Repository
public interface OrderRepository extends JpaRepository<OrderModel, Long> {
	
	void save(double totalPurchase);
}
