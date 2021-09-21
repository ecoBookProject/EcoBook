package com.GroupFour.EcoBook.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.GroupFour.EcoBook.Model.UserModel;

@Repository
public interface UserRepository extends JpaRepository<UserModel, Long> {
// selecionar usuário por email
	@Query("SELECT obj FROM UserModel obj WHERE UPPER(obj.email) LIKE CONCAT('%',UPPER(:email),'%')")
	Optional<UserModel> findByEmail(@Param("email") String email);

}
