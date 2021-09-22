package com.GroupFour.EcoBook.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.GroupFour.EcoBook.Model.BookModel;
import com.GroupFour.EcoBook.Repository.BookRepository;
import com.GroupFour.EcoBook.Service.Exception.DataIntegratyViolationException;



@Service
public class BookService {

	@Autowired
	private BookRepository repository;

	public ResponseEntity<List<BookModel>> findByDescriptionTitle(String title) {
		List<BookModel> listObj = repository.findByDescriptionTitle(title);
		if (listObj.isEmpty()) {
			throw new DataIntegratyViolationException("Não existe nenhum livro com esse título");
		}
		return ResponseEntity.status(200).body(listObj);
	}

}
