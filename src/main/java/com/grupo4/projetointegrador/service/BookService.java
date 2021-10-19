package com.grupo4.projetointegrador.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.grupo4.projetointegrador.dto.BookDTO;
import com.grupo4.projetointegrador.model.BookModel;
import com.grupo4.projetointegrador.model.OrderModel;
import com.grupo4.projetointegrador.model.WishListModel;
import com.grupo4.projetointegrador.repository.BookRepository;
import com.grupo4.projetointegrador.repository.OrderRepository;
import com.grupo4.projetointegrador.repository.WishListRepository;
import com.grupo4.projetointegrador.service.exception.DataIntegratyViolationException;
import com.grupo4.projetointegrador.service.exception.ObjectNotFoundException;

@Service
public class BookService {
	
	@Autowired
	private BookRepository repository;

	@Autowired
	private OrderRepository orderRepo;

	@Autowired
	private WishListRepository wishListRepo;

	@Transactional(readOnly = true)
	public List<BookModel> findAll() {
		List<BookModel> list = repository.findAll();
		if (list.isEmpty()) {
			throw new DataIntegratyViolationException("Não existe nenhum livro");
		}
		return list;
	}

	@Transactional(readOnly = true)
	public Page<Object> findAllPages(Pageable pageable) {
		repository.findAll();
		Page<BookModel> result = repository.findAll(pageable);
		return result.map(x -> new BookDTO(x));
	}

	@Transactional(readOnly = true)
	public ResponseEntity<BookModel> findById(Long id) {
		return repository.findById((long) id).map(resp -> ResponseEntity.ok(resp))
				.orElseThrow(() -> new ObjectNotFoundException(
						"Objeto não encontrado! Id: " + id + " não existe, Tipo: " + BookModel.class.getName()));
	}

	@Transactional(readOnly = true)
	public ResponseEntity<List<BookModel>> findByDescriptionTitle(String title) {
		List<BookModel> user = repository.findByDescriptionTitle(title);
		if (user.isEmpty()) {
			throw new DataIntegratyViolationException("Não existe nenhum livro com esse titulo");
		}
		return ResponseEntity.ok(user);
	}

	/*BUSCA POR AUTOR*/
	@Transactional(readOnly = true)
	public ResponseEntity<List<BookModel>> findByAuthor(String author) {
		List<BookModel> user = repository.findByAuthor(author);
		if (user.isEmpty()) {
			throw new DataIntegratyViolationException("Não existe nenhum livro com esse autor.");
		}
		return ResponseEntity.ok(user);
	}
	public ResponseEntity<BookModel> created(BookModel book) {
		return ResponseEntity.ok(repository.save(book));
	}

	public ResponseEntity<BookModel> update(BookModel updateBook) {
		return ResponseEntity.ok(repository.save(updateBook));
	}

	public void delete(Long id) {
		findById(id);
		repository.deleteById(id);
	}

	double a = 0;
	int posicao = 0; // aramazena a posicao do item dentro do array de lista de desejos

	public BookModel buyProduct(long idProduct, long idOrder) {

		Optional<BookModel> produtoExistente = repository.findById(idProduct);
		Optional<OrderModel> pedidoExistente = orderRepo.findById(idOrder);

		if (produtoExistente.isPresent() && pedidoExistente.isPresent() && produtoExistente.get().getInventory() == 0) {
			/* ACRESCENTA MAIS 50 PRODUTOS AO ESTOQUE */
			produtoExistente.get().setInventory(20);

		}

		/*
		 * SE O 'PRODUTO' E 'PEDIDO' EXISTIREM, E SE O 'ESTOQUE' CONTEM PRODUTOS
		 * DISPONIVEIS ENTRA NA CONDICAO
		 */
		if (produtoExistente.isPresent() && pedidoExistente.isPresent() && produtoExistente.get().getInventory() >= 0
				&& !(pedidoExistente.get().getProducts().isEmpty())) {

			/* ADICIONA O PRODUTO AO CARRINHO DO USUARIO */
			produtoExistente.get().getOrders().add(pedidoExistente.get());

			System.out.println("Retorno: " + pedidoExistente.get().getProducts().contains(produtoExistente.get()));

			System.out.println("QTD produtos " + pedidoExistente.get().getProducts().size());

			/* ARMAZENA A QTD DE PRODUTOS */
			int contador = 0;

			/* ARMAZENA OS IDs DOS PRODUTOS LISTADOS DENTRO DO CARRINHO DO USUARIO */
			long[] vetor = new long[pedidoExistente.get().getProducts().size()];

			for (int i = 0; i < pedidoExistente.get().getProducts().size(); i++) {

				vetor[i] = pedidoExistente.get().getProducts().get(i).getIdProduct();

				System.out.println("Posicao do vetor [" + i + "] = " + vetor[i]);
				System.out.println("Produto ID: " + produtoExistente.get().getIdProduct());

				if (vetor[i] == produtoExistente.get().getIdProduct()) {
					contador++;

				}

			}

			System.out.println("Valor Total a Pagar ATUAL " + pedidoExistente.get().getTotalPurchase());

			/* RETIRA O VALOR EXISTENTE DO CARRINHO PARA PODER SER RECALCULADO */
			pedidoExistente.get().setTotalPurchase(
					pedidoExistente.get().getTotalPurchase() - (produtoExistente.get().getPrice() * contador));

			/* COMPENSA ACRESCENTANDO O NOVO PRODUTO AO CARRINHO ==> O ID INFORMADO */
			contador++;
			System.out.println("QTD de produto: " + contador);

			/* INSERE O VALOR DO CONTADOR == QTD DE PRODUTOS POR ID */
			produtoExistente.get().setQuantityOrderP(contador);
			/* DEBITA O ESTOQUE SEMPRE QUE E INSERIDO UM PRODUTO A UM CARRINHO */
			produtoExistente.get().setInventory(produtoExistente.get().getInventory() - 1);

			System.out.println("Valor Total a Pagar ZERADO " + pedidoExistente.get().getTotalPurchase());

			/* AJUSTA O VALOR DO CARRINHO DE UM USUARIO ESPECIFICO */
			pedidoExistente.get().setTotalPurchase(pedidoExistente.get().getTotalPurchase()
					+ (produtoExistente.get().getPrice() * produtoExistente.get().getQuantityOrderP()));

			System.out.println("Valor Total a Pagar ATUALIZADO " + pedidoExistente.get().getTotalPurchase());

			System.out.println("Contador: " + contador);
			System.out.println("QTD Produtos Comprados: " + produtoExistente.get().getQuantityOrderP());
			System.out.println("Valor Produto: " + produtoExistente.get().getPrice());
			System.out.println("Valor Carrinho: " + pedidoExistente.get().getTotalPurchase());

			System.out.println("IF");
			System.out.println("ID: " + pedidoExistente.get().getIdOrder() + " | Valor a pagar: "
					+ pedidoExistente.get().getTotalPurchase());

			repository.save(produtoExistente.get());
			orderRepo.save(pedidoExistente.get());
			orderRepo.save(pedidoExistente.get()).getTotalPurchase();

			return repository.save(produtoExistente.get());

		} else if (produtoExistente.isPresent() && pedidoExistente.isPresent()) {
			/* ADICIONA O PRODUTO AO CARRINHO DO USUARIO */
			produtoExistente.get().getOrders().add(pedidoExistente.get());

			/* ADICIONA UM PRODUTO AO QTD PRODUTOS DENTRO DE PRODUTO */
			produtoExistente.get().setQuantityOrderP(1);
			/* GERENCIA O ESTOQUE DEBITNADO UM PRODUTO DO ESTOQUE */
			produtoExistente.get().setInventory(produtoExistente.get().getInventory() - 1);

			/* ATUALIZA O VALOR DO CARRINHO DO USUARIO */
			pedidoExistente.get().setTotalPurchase(pedidoExistente.get().getTotalPurchase()
					+ (produtoExistente.get().getPrice() * produtoExistente.get().getQuantityOrderP()));

			System.out.println("ELSE");
			System.out.println("ID: " + pedidoExistente.get().getIdOrder() + " | Valor a pagar: "
					+ pedidoExistente.get().getTotalPurchase());

			repository.save(produtoExistente.get());
			orderRepo.save(pedidoExistente.get());
			orderRepo.save(pedidoExistente.get()).getTotalPurchase();

			return repository.save(produtoExistente.get());

		}

		return null;
	}

	/* DELETAR OBJETOS DO PRODUTO */
	public void deleteProduct(long idProduct, long idOrder) {

		Optional<BookModel> produtoExistente = repository.findById(idProduct);
		Optional<OrderModel> pedidoExistente = orderRepo.findById(idOrder);

		if (pedidoExistente.get().getProducts().contains(produtoExistente.get())) {
			/* REMOVE O CARRINHO DO PRODUTO */
			produtoExistente.get().getOrders().remove(pedidoExistente.get());

			/* GERENCIA O ESTOQUE DEBITNADO UM PRODUTO DO ESTOQUE */
			produtoExistente.get().setInventory(produtoExistente.get().getInventory() + 1);

			int contador = 0;

			/* ARMAZENA OS IDs DOS PRODUTOS LISTADOS DENTRO DO CARRINHO DO USUARIO */
			long[] vetor = new long[pedidoExistente.get().getProducts().size()];

			for (int i = 0; i < pedidoExistente.get().getProducts().size(); i++) {

				vetor[i] = pedidoExistente.get().getProducts().get(i).getIdProduct();

				System.out.println("Posicao do vetor [" + i + "] = " + vetor[i]);
				System.out.println("Produto ID: " + produtoExistente.get().getIdProduct());

				if (vetor[i] == produtoExistente.get().getIdProduct()) {
					contador++;
				}
			}
			produtoExistente.get().setQuantityOrderP(contador - 1);

			/* AJUSTA O VALOR DO CARRINHO DE UM USUARIO ESPECIFICO */
			pedidoExistente.get()
					.setTotalPurchase(pedidoExistente.get().getTotalPurchase() - produtoExistente.get().getPrice());

			a = pedidoExistente.get().getTotalPurchase();
			a = Math.floor(a * 100) / 100;
			pedidoExistente.get().setTotalPurchase(a);

			if (pedidoExistente.get().getTotalPurchase() < 0) {
				pedidoExistente.get().setTotalPurchase(0);
			}
			repository.save(produtoExistente.get());
			orderRepo.save(pedidoExistente.get());
			orderRepo.save(pedidoExistente.get()).getTotalPurchase();
		}
	}

	/* ADICIONA UM PRODUTO ESPECIFICO A LISTA DE DESEJOS DO USUARIO */
	public BookModel addProductWishList(Long idProduct, Long idWishList) {
		Optional<BookModel> existingProduct = repository.findById(idProduct);
		Optional<WishListModel> existingWishList = wishListRepo.findById(idWishList);

		/*
		 * CASO OS ITENS EXISTAM NA BASE DE DADOS E O PRODUTO AINDA NAO ESTEJA INCLUSO
		 * DENTRO DA LSITA DE DESEJOS
		 */
		if (existingProduct.isPresent() && existingWishList.isPresent()
				&& !(existingProduct.get().getWishList().contains(existingWishList.get()))) {
			// ADICIONA O PRODUTO A LISTA DE DESEJOS DO USUARIO

			System.out.println("Acessou o produto e lista por id");

			existingProduct.get().getWishList().add(existingWishList.get());

			System.out.println("Adicionou o produto a lista");

			repository.save(existingProduct.get());

			System.out.println("Salvou o produto com o novo dado");

			return repository.save(existingProduct.get());
		}
		return null;
	}

	/* REMOVE UM PRODUTO ESPECIFICO DA LISTA DE DESEJOS DO USUARIO */
	public BookModel removeProductWishList(Long idProduct, Long idWishList) {
		Optional<BookModel> existingProduct = repository.findById(idProduct);
		Optional<WishListModel> existingWishList = wishListRepo.findById(idWishList);

		/*
		 * CASO OS ITENS EXISTAM NA BASE DE DADOS E O PRODUTO AINDA NAO ESTEJA INCLUSO
		 * DENTRO DA LSITA DE DESEJOS
		 */
		if (existingProduct.get().getWishList().contains(existingWishList.get())) {
			/* REMOVE O CARRINHO DO PRODUTO */
			existingProduct.get().getWishList().remove(existingWishList.get());

			repository.save(existingProduct.get());

			return repository.save(existingProduct.get());
		}
		return null;
	}

	/* PESQUISANDO POR PRODUTO ESPECIFICO EM UMA LISTA DE DESEJOS DE PRODUTOS */
	public List<BookModel> searchByIdProducInWishList(Long idWishList, String title) {
		Optional<WishListModel> existingWishList = wishListRepo.findById(idWishList);

		// ARMAZENA OS IDs DOS PRODUTOS LISTADOS DENTRO DO CARRINHO DO USUARIO
		long[] vetor = new long[existingWishList.get().getProducts().size()];

		for (int i = 0; i < vetor.length; i++) {

			if (existingWishList.get().getProducts().get(i).getTitle().contains(title)) {

				return repository.findAllByTitleContainingIgnoreCase(title);
			}
		}
		return null;
	}

	/* PESQUISANDO POR PRODUTO ESPECIFICO EM UMA LISTA DE DESEJOS DE PRODUTOS */
	public List<BookModel> searchByProductInWishList(Long idWishList) {
		Optional<WishListModel> existingWishList = wishListRepo.findById(idWishList);

		if (existingWishList.isPresent()) {
			existingWishList.get().getProducts();

			return wishListRepo.save(existingWishList.get()).getProducts();
		}
		return null;
	}

	/* PESQUISANDO POR PRODUTO ESPECIFICO EM UMA LISTA DE DESEJOS DE PRODUTOS */
	public List<BookModel> searchByCartProducts(Long idOrder) {
		Optional<OrderModel> existingOrder = orderRepo.findById(idOrder);

		if (existingOrder.isPresent()) {
			existingOrder.get().getProducts();

			return orderRepo.save(existingOrder.get()).getProducts();
		}
		return null;
	}
 
}
