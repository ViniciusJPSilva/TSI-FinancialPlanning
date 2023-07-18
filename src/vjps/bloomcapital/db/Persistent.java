package vjps.bloomcapital.db;

/**
 * Interface que deve ser implementada por classes cujo 
 * objetos podem ser persistidos no Banco de Dados.
 * <p>
 * Define um contrato que tais classes devem possuir um atributo '{@code code}' 
 * que será utilizado como identificador (ID) pelo Banco de Dados.
 * <p>
 * É necessário que a classe que implementa esta interface crie
 * um atributo '{@code Long code}' que representa o código (ID) do objeto.
 * <p>
 * <b>O código não é necessáriamente a chave primária do objeto!</b>
 * 
 * @author Vinicius J P Silva
 */
public interface Persistent {
	
	/**
	 * Obtém o código (ID) do objeto.
	 * 
	 * @return Código caso tenha ou {@code null} caso não.
	 */
	public Long getCode();
	
	/*
	 * Altera o código (ID) do objeto.
	 */
	public void setCode(Long code);
	
	/**
	 * Verifica se o objeto possui um código atribuído.
	 * 
	 * @return {@code true} caso possua ou {@code false} caso não.
	 */
	public boolean hasCode();
	
} // interface Persistent
