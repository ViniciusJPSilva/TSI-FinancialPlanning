package vjps.bloomcapital.io;

/**
 * Exceção lançada quando ocorre um erro ao criar uma instância de um objeto.
 * 
 * @author Vinícius J P Silva
 * 
 * @see {@link RuntimeException} 
 *
 */
public class ObjectCreationException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private static final String STD_MESSAGE = "Ocorreu um erro ao instanciar o objeto.";
	
	public ObjectCreationException() {
		super(STD_MESSAGE);
	}

	public ObjectCreationException(String message) {
		super(message);
	}

	public ObjectCreationException(String message, Throwable cause) {
		super(message, cause);
	}
	
} // class DataInterpreterException
