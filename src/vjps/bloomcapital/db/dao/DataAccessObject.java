package vjps.bloomcapital.db.dao;

import java.util.List;

/**
 * Data Access Object (DAO) fornece uma interface e encapsula os 
 * métodos de acesso ao Banco de Dados (BD), por meio de operações 
 * CRUD (Create, Read, Update, Delete).
 * 
 * @author Vinicius J P Silva
 * 
 * @see {@link java.util.List}
 */
public interface DataAccessObject<T> {
	
	/*
	 * Insere os dados do objeto T no BD.
	 */
	public boolean insert(T data);
	
	/*
	 * Remove os dados do objeto T do BD.
	 */
	public boolean delete(T data);
	
	/*
	 * Atualiza os dados do objeto T no BD.
	 */
	public boolean update(T data);
	
	/*
	 * Obtém os dados de determinado objeto no BD, baseado em seu código.
	 */
	public T select(Long code);
	
	/*
	 * Obtém uma lista com todos os objetos tipo T de sua respectiva tabela no BD.
	 */
	public List<T> selectAll();
	
} // interface DataAccessObject<T>
