package vjps.bloomcapital.finance;

/**
 * Classe responsável por representar uma finança, ou seja, uma entrada ou saída de um recurso.
 * 
 * @author Vinicius J P Silva
 */
public abstract class Finance {
	
	private String description;
	private float value;
	
	public Finance() {
		description = "";
	}

	public Finance(String description, float value) {
		this.description = description;
		this.value = value;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public float getValue() {
		return value;
	}

	public void setValue(float value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return String.format("Finança: Descrição: %s, Valor: R$ %,.2f", 
				description, 
				value
			);
	}
	
} // class Finance
