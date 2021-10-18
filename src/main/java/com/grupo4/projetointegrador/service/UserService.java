package com.grupo4.projetointegrador.service;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Optional;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.grupo4.projetointegrador.dto.UserDTO;
import com.grupo4.projetointegrador.model.OrderModel;
import com.grupo4.projetointegrador.model.UserModel;
import com.grupo4.projetointegrador.model.WishListModel;
import com.grupo4.projetointegrador.repository.OrderRepository;
import com.grupo4.projetointegrador.repository.UserRepository;
import com.grupo4.projetointegrador.repository.WishListRepository;
import com.grupo4.projetointegrador.service.exception.DataIntegratyViolationException;
import com.grupo4.projetointegrador.service.exception.ObjectNotFoundException;

@Service
public class UserService {
	
	@Autowired
	private UserRepository repository;
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private WishListRepository wishListRepository;
	
	/*Busca todos aos usuarios*/
	@Transactional(readOnly = true)
	public List<UserModel> findAll(){
		List<UserModel> list = repository.findAll();
		if(list.isEmpty()) {
			throw new DataIntegratyViolationException("Não existe nenhum usuario");
		}
		return list;
	}
	
	/*
	 * Busca pelo ID
	 */
	@Transactional(readOnly = true)
	public ResponseEntity<UserModel> findById(Long id) {
		return repository.findById((long) id).map(
				resp -> ResponseEntity.ok(resp)).orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado! Id: " + id + " não existe, Tipo: " + UserModel.class.getName()));
	}
	
	/*
	 * Busca pelo email do usuario
	 */
	@Transactional(readOnly = true)
	public ResponseEntity<Optional<UserModel>> findByEmail(String email) {
		Optional<UserModel> user = repository.findByEmail(email);
		if(user.isEmpty()) {
			throw new DataIntegratyViolationException("Não existe nenhuma usuario com esse email");
		}
		return ResponseEntity.ok(user);
	}
	
	/*
	 * Cria um novo usuario
	 */
	public Optional<Object> createUser(UserModel usuario) {
		return Optional.ofNullable(repository.findByEmail(usuario.getEmail()).map(usuarioExistente -> {
			return Optional.empty().orElseThrow(() -> new ObjectNotFoundException("Email ja cadastrado"));
		}).orElseGet(() -> {
			BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
			String senhaEncoder = encoder.encode(usuario.getPassword());
			usuario.setPassword(senhaEncoder);
			
			/*GERANDO CARRINHO USUARIO*/
			/* INSTANCIA UM NOVO CARRINHO 'Pedido' */
			OrderModel order = new OrderModel();
			
			/*GERANDO LISTA DE DESEJOS USUARIO*/
			/* INSTANCIA UMA NOVA LISTA DE DESEJOS 'listaDeDesejos' */
			WishListModel wishList = new WishListModel();
			
			/* ASSOCIA O USUARIO AO CARRINHO */
			order.setUser(usuario);
			
			/* ASSOCIA O USUARIO AO LISTA DE DESEJOS */
			wishList.setUser(usuario);
			
			/* REGISTRA O CARRINHO NA BASE DE DADOS */
			orderRepository.save(order);
			
			/* REGISTRA A LISTA DE DESEJOS NA BASE DE DADOS */
			wishListRepository.save(wishList);
			
			return Optional.ofNullable(repository.save(usuario));
		}));
	}
	
	/*
	 * Metodo de login 
	 */
	public Optional<?> logar(Optional<UserDTO> user){
		//Verifica o email ou no meu caso o user
		return Optional.ofNullable(repository.findByEmail(user.get().getEmail()).map(usuarioExistente -> {
			BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
	
			//verifica as senhas 
			if(encoder.matches(user.get().getPassword(), usuarioExistente.getPassword())) {
					
					String auth = user.get().getEmail() + ":" + user.get().getPassword();
					byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset.forName("US-ASCII")));
					String authHeader = "Basic " + new String(encodedAuth);
					
					user.get().setToken(authHeader);
					user.get().setIdClient (usuarioExistente.getIdClient());
					user.get().setName(usuarioExistente.getName());
					user.get().setType_user(usuarioExistente.getType_user());
					user.get().setPassword(usuarioExistente.getPassword());
					user.get().setOrders(usuarioExistente.getOrders());
					user.get().setWishList(usuarioExistente.getWishList());
					
					return Optional.ofNullable(user);
			}else {
				return Optional.empty().orElseThrow(() -> new ObjectNotFoundException("Senha Incorreta")); //Senha esteja incorreta
			}
			
		}).orElseGet(() -> {
			return Optional.empty().orElseThrow(() -> new ObjectNotFoundException("Usuario não registrado na base de dados.")); //Email não existente
		}));
	}
	
	/*
	 * Atualizando um usuario
	 */
	public Optional<Object> update(Optional<UserModel> usuario) {
		return Optional.ofNullable(repository.findById(usuario.get().getIdClient()).map(usuarioExistente -> {
			BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
			String senhaCriptografada = encoder.encode(usuario.get().getPassword());
			
			usuarioExistente.setName(usuario.get().getName());
			usuarioExistente.setEmail(usuario.get().getEmail());
			usuarioExistente.setCpf(usuario.get().getCpf());
			usuarioExistente.setPhone(usuario.get().getPhone());
			usuarioExistente.setPassword(senhaCriptografada);
			usuarioExistente.setCep(usuario.get().getCep());
			usuarioExistente.setAddress(usuario.get().getAddress());
			usuarioExistente.setNumber(usuario.get().getNumber());
			usuarioExistente.setComplement(usuario.get().getComplement());
			usuarioExistente.setDistrict(usuario.get().getDistrict());
			usuarioExistente.setCity(usuario.get().getCity());
			usuarioExistente.setState(usuario.get().getState());
			usuarioExistente.setType_user(usuario.get().getType_user());
			usuarioExistente.setOrders(usuario.get().getOrders());
			usuarioExistente.setWishList(usuario.get().getWishList());
			return Optional.ofNullable(repository.save(usuarioExistente));
			
		}).orElseThrow(() ->
				new ObjectNotFoundException("Erro ao atualizar usuario")
		));
	}
	
	public void delete(Long id) {
		findById(id);
		repository.deleteById(id);
	}

}
