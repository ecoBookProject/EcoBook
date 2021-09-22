package com.GroupFour.EcoBook.Service;

import java.nio.charset.Charset;
import java.util.Optional;

import javax.validation.Valid;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.GroupFour.EcoBook.Dto.UserDTO;
import com.GroupFour.EcoBook.Model.UserModel;
import com.GroupFour.EcoBook.Repository.UserRepository;
import com.GroupFour.EcoBook.Service.Exception.ObjectNotFoundException;

@Service
public class UserService {

	@Autowired
	private UserRepository repository;
	
	public Optional<Object> createUser(UserModel usuario) {
		return Optional.ofNullable(repository.findByEmail(usuario.getEmail()).map(usuarioExistente -> {
			return Optional.empty().orElseThrow(() -> new ObjectNotFoundException("Email ja cadastrado"));
		}).orElseGet(() -> {
			BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
			String senhaEncoder = encoder.encode(usuario.getPassword());
			usuario.setPassword(senhaEncoder);
			return Optional.ofNullable(repository.save(usuario));
		}));
	}

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
					user.get().setIdClient(usuarioExistente.getIdClient());
					user.get().setName(usuarioExistente.getName());
					user.get().setType_user(usuarioExistente.getType_user());
					user.get().setPassword(usuarioExistente.getPassword());
					
					return Optional.ofNullable(user);
			}else {
				return Optional.empty().orElseThrow(() -> new ObjectNotFoundException("Senha Incorreta")); //Senha esteja incorreta
			}
			
		}).orElseGet(() -> {
			return Optional.empty().orElseThrow(() -> new ObjectNotFoundException("Usuario não registrado na base de dados.")); //Email não existente
		}));
	}
	
	

}
