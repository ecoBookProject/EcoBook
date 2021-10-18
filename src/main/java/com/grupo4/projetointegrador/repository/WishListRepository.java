package com.grupo4.projetointegrador.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.grupo4.projetointegrador.model.WishListModel;

@Repository
public interface WishListRepository extends JpaRepository<WishListModel, Long> {

}
