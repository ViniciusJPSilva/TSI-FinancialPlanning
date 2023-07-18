package vjps.bloomcapital.finance.expense;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import vjps.bloomcapital.db.Persistent;
import vjps.bloomcapital.finance.Finance;
import vjps.bloomcapital.utility.Month;

/**
 * Classe responsável por representar uma Despesa, no contexto financeiro.
 * 
 * @author Vinicius J P Silva
 * 
 * @see {@link vjps.bloomcapital.finance.Finance}
 * @see {@link java.time.LocalDate}
 * @see {@link vjps.bloomcapital.finance.expense.FormPayment}
 */
public class Expense extends Finance implements Persistent {
	
	public static final DateTimeFormatter EXPENSE_DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy"),
									PAY_DAY_FORMAT = DateTimeFormatter.ofPattern("dd/MM");
	
	private Long code;
	private LocalDate expenseDate, payDay;
	private FormPayment formPayment;
	private String category;
	private boolean situation;
	
	public Expense() {
		category = "";
		code = null;
	}

	public Expense(String description, float value, LocalDate expenseDate, LocalDate payDay, FormPayment formPayment, String category, boolean situation) {
		super(description, value);
		this.expenseDate = expenseDate;
		this.payDay = payDay;
		this.formPayment = formPayment;
		this.category = category;
		this.situation = situation;
		code = null;
	}
	
	public Expense(Long code, String description, float value, LocalDate expenseDate, LocalDate payDay, FormPayment formPayment, String category, boolean situation) {
		super(description, value);
		this.code = code;
		this.expenseDate = expenseDate;
		this.payDay = payDay;
		this.formPayment = formPayment;
		this.category = category;
		this.situation = situation;
	}

	public LocalDate getExpenseDate() {
		return expenseDate;
	}

	public void setExpenseDate(LocalDate expenseDate) {
		this.expenseDate = expenseDate;
	}

	public LocalDate getPayDay() {
		return payDay;
	}

	public void setPayDay(LocalDate payDay) {
		this.payDay = payDay;
	}

	public FormPayment getFormPayment() {
		return formPayment;
	}

	public void setFormPayment(FormPayment formPayment) {
		this.formPayment = formPayment;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}
	
	/**
	 * Obtém a situação (situation) da despesa.
	 * 
	 * @return {@code true} caso esteja paga ou {@code false} caso não.
	 */
	public boolean isPaid() {
		return situation;
	}

	public void setSituation(boolean paid) {
		this.situation = paid;
	}

	@Override
	public String toString() {
		return String.format("Despesa%s, Descrição: %s, Valor: R$ %,.2f, Data da Despesa: %s, Dia do Pagamento: %s, Forma de Pagamento: %s, Categoria: %s, Situação: %s", 
				(hasCode()) ? String.format(" %d", code) : "", 
				getDescription(), 
				getValue(),
				expenseDate.format(EXPENSE_DATE_FORMAT),
				payDay.format(PAY_DAY_FORMAT),
				formPayment.getDescription(),
				category,
				getSituationDescription() 
			);
	}
	
	/**
	 * Gera um texto informando a situação da despesa.
	 * @return Situação da despesa.
	 */
	public String getSituationDescription() {
		return (situation) ? "Paga" : "A pagar";
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
	
	public Month getMonth() {
		return Month.name(payDay.getMonthValue());
	}
	
} // class Expense
