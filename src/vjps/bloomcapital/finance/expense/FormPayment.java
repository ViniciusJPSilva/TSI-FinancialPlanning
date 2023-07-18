package vjps.bloomcapital.finance.expense;

/**
 * Enumeração responsável por representar as diversas formas de pagamento da aplicação.
 * 
 * @author Vinicius J P Silva
 */
public enum FormPayment {
	CC("CC", "Cartão de Crédito"),
	CD("CD", "Cartão de Débito"),
	MONEY("Dinheiro", "Dinheiro"),
	PIX("Pix", "PIX");
	
	private String name, description;

	private FormPayment(String name, String description) {
		this.name = name;
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	@Override
	public String toString() {
		return String.format("%s - %s", name, description);
	}
	
	public static FormPayment getByDescription(String description) {
		for(FormPayment formPayment : FormPayment.values())
			if(formPayment.description.equalsIgnoreCase(description))
				return formPayment;
		return null;
	}
	
	public static FormPayment getByName(String name) {
		for(FormPayment formPayment : FormPayment.values())
			if(formPayment.name.equalsIgnoreCase(name))
				return formPayment;
		return null;
	}
	
} // enum FormPayment
