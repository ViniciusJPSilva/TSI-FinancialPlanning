package vjps.bloomcapital.finance.investment;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import vjps.bloomcapital.db.Persistent;

/**
 * Classe responsável por representar um Investimento financeiro.
 * 
 * @author Vinicius J P Silva
 * 
 * @see {@link java.time.LocalDate}
 * @see {@link vjps.bloomcapital.db.Persistent}
 */
public class Investment implements Persistent {
	public static final DateTimeFormatter MATURITY_DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	
	private Long code;
	private String goal, strategy, name;
	private float amountInvested, position, grossIncome, profitability;
	private LocalDate maturityDate;
	
	public Investment() {
		goal = strategy = name = "";
		code = null;
	}

	public Investment(String goal, String strategy, String name, float amountInvested, float position,
			float grossIncome, float profitability, LocalDate maturityDate) {
		this.goal = goal;
		this.strategy = strategy;
		this.name = name;
		this.amountInvested = amountInvested;
		this.position = position;
		this.grossIncome = grossIncome;
		this.profitability = profitability;
		this.maturityDate = maturityDate;
		code = null;
	}
	
	public Investment(Long code, String goal, String strategy, String name, float amountInvested, float position,
			float grossIncome, float profitability, LocalDate maturityDate) {
		this.code = code;
		this.goal = goal;
		this.strategy = strategy;
		this.name = name;
		this.amountInvested = amountInvested;
		this.position = position;
		this.grossIncome = grossIncome;
		this.profitability = profitability;
		this.maturityDate = maturityDate;
	}

	public String getGoal() {
		return goal;
	}

	public void setGoal(String goal) {
		this.goal = goal;
	}

	public String getStrategy() {
		return strategy;
	}

	public void setStrategy(String strategy) {
		this.strategy = strategy;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public float getAmountInvested() {
		return amountInvested;
	}

	public void setAmountInvested(float amountInvested) {
		this.amountInvested = amountInvested;
	}

	public float getPosition() {
		return position;
	}

	public void setPosition(float position) {
		this.position = position;
	}

	public float getGrossIncome() {
		return grossIncome;
	}

	public void setGrossIncome(float grossIncome) {
		this.grossIncome = grossIncome;
	}

	public float getProfitability() {
		return profitability;
	}

	public void setProfitability(float profitability) {
		this.profitability = profitability;
	}

	public LocalDate getMaturityDate() {
		return maturityDate;
	}

	public void setMaturityDate(LocalDate maturityDate) {
		this.maturityDate = maturityDate;
	}
	
	@Override
	public String toString() {
		return String.format("Investimento%s, Objetivo: %s, Estratégia: %s, Nome: %s, Valor Investido: R$ %,.2f, Posição: R$ %,.2f, Rendimento Bruto: R$ %,.2f, Retabilidade: %,.2f%%, Vencimento: %s", 
				(hasCode()) ? String.format(" %d", code) : "", 
				goal, strategy, name, amountInvested, position,
				grossIncome, profitability, maturityDate.format(MATURITY_DATE_FORMAT)
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
	
} // class Investment
