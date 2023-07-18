package vjps.bloomcapital.finance.revenue;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import vjps.bloomcapital.db.Persistent;
import vjps.bloomcapital.finance.Finance;

/**
 * Classe responsável por representar uma Receita, no contexto financeiro.
 * 
 * @author Vinicius J P Silva
 * 
 * @see {@link vjps.bloomcapital.finance.Finance}
 * @see {@link java.time.LocalDate}
 */
public class Revenue extends Finance implements Persistent {
	
	// Define o formato padrão da data das receitas: dia/mês/ano
	public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

	private Long code;
	private LocalDate date;

	public Revenue() {
		code = null;
	}

	public Revenue(String description, float value, LocalDate data) {
		super(description, value);
		this.date = data;
		code = null;
	}
	
	public Revenue(Long code, String description, float value, LocalDate data) {
		super(description, value);
		this.code = code;
		this.date = data;
	}

	public LocalDate getData() {
		return date;
	}

	public void setData(LocalDate data) {
		this.date = data;
	}

	@Override
	public String toString() {
		return String.format("Receita%s, Tipo: %s, Valor: R$ %,.2f, Data: %s", 
				(hasCode()) ? String.format(" %d", code) : "", 
				getDescription(), 
				getValue(),
				date.format(DATE_FORMAT)
			);
	}
	
	@Override
	public Long getCode() {
		return code;
	}

	@Override
	public void setCode(Long code) {
		this.code = code;		
	}

	@Override
	public boolean hasCode() {
		return (code != null) ? true : false;
	}
	
} // class Revenue
