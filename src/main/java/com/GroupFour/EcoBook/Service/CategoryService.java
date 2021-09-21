package com.GroupFour.EcoBook.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.GroupFour.EcoBook.Model.CategoryModel;
import com.GroupFour.EcoBook.Repository.CategoryRepository;


@Service
public class CategoryService {

	@Autowired
	private CategoryRepository repository;

	public ResponseEntity<List<CategoryModel>> findByDescriptionGenre(String genre) {
		List<CategoryModel> listObj = repository.findByDescriptionGenre(genre);
		if (listObj.isEmpty()) {
			throw new DataIntegratyViolationException("Não existe nenhum gênero com esse nome");
		}
		return ResponseEntity.status(200).body(listObj);

	}
}
