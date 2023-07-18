package vjps.bloomcapital.finance;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import vjps.bloomcapital.finance.expense.Expense;
import vjps.bloomcapital.finance.investment.Investment;
import vjps.bloomcapital.finance.revenue.Revenue;
import vjps.bloomcapital.utility.Month;

/**
 * A classe FinancesList representa uma lista de finanças que contém receitas, despesas e investimentos.
 * Permite adicionar, recuperar e calcular diferentes informações financeiras, como valores totais,
 * quantidade de itens e listas filtradas por mês e categoria. Além disso, fornece funcionalidades para
 * obter valores totais por categoria, valores acumulados de investimentos e valores pagos das despesas.
 * A classe também inclui uma lista de categorias básicas de despesas e um valor especial para representar
 * todas as categorias de despesas. Os dados financeiros são armazenados em listas separadas para cada tipo
 * de item financeiro. 
 *
 * @author Vinícius J P Silva
 * 
 * @see {@link Revenue}
 * @see {@link Expense}
 * @see {@link Investment}
 *
 */
public class FinancesList {
	
	public static final String ALL_CATEGORY_EXPENSES = "Todas",
			BASICS_CATEGORIES[] = {"Alimentação", "Educação", "Energia Elétrica", "Esporte", "Lazer", "Habitação", "Plano de Saúde", "Outros"};

	private List<Revenue> revenueList;
	private List<Expense> expenseList;
	private List<Investment> investmentList;
	
	public FinancesList() {
		revenueList = new ArrayList<>();
		expenseList = new ArrayList<>();
		investmentList = new ArrayList<>();
	}
	
	/**
	 * Adiciona receitas à lista de receitas.
	 *
	 * @param deletePrevious Indica se as receitas anteriores devem ser excluídas
	 * @param revenues As receitas a serem adicionadas
	 */
	public void addRevenue(boolean deletePrevious, Revenue... revenues) {
		if(deletePrevious) revenueList.clear();
		
		for(Revenue revenue : revenues)
			revenueList.add(revenue);
	}
	
	/**
	 * Adiciona despesas à lista de despesas.
	 *
	 * @param deletePrevious Indica se as despesas anteriores devem ser excluídas
	 * @param expenses As despesas a serem adicionadas
	 */
	public void addExpense(boolean deletePrevious, Expense... expenses) {
		if(deletePrevious) expenseList.clear();
		
		for(Expense expense : expenses)
			expenseList.add(expense);
	}
	
	
	/**
	 * Adiciona investimentos à lista de investimentos.
	 *
	 * @param deletePrevius Indica se os investimentos anteriores devem ser excluídos
	 * @param investments Os investimentos a serem adicionados
	 */
	public void addInvestment(boolean deletePrevius, Investment... investments) {
		if(investments == null) return;
		if(deletePrevius) investmentList.clear();
		
		for(Investment investment : investments)
			investmentList.add(investment);
	}
	
	
	/**
	 * Adiciona receitas à lista de receitas.
	 *
	 * @param deletePrevious Indica se as receitas anteriores devem ser excluídas
	 * @param revenues As receitas a serem adicionadas
	 */
	public void addRevenue(boolean deletePrevious, List<Revenue> revenues) {
		if(revenues == null) return;
		if(deletePrevious) revenueList.clear();
		
		for(Revenue revenue : revenues)
			revenueList.add(revenue);
	}
	
	/**
	 * Adiciona despesas à lista de despesas.
	 *
	 * @param deletePrevious Indica se as despesas anteriores devem ser excluídas
	 * @param expenses As despesas a serem adicionadas
	 */
	public void addExpense(boolean deletePrevious, List<Expense> expenses) {
		if(expenses == null) return;
		if(deletePrevious) expenseList.clear();
		
		for(Expense expense : expenses)
			expenseList.add(expense);
	}
	
	/**
	 * Adiciona investimentos à lista de investimentos.
	 *
	 * @param deletePrevius Indica se os investimentos anteriores devem ser excluídos
	 * @param investments Os investimentos a serem adicionados
	 */
	public void addInvestment(boolean deletePrevius, List<Investment> investments) {
		if(deletePrevius) investmentList.clear();
		
		for(Investment investment : investments)
			investmentList.add(investment);
	}
	
	/**
	 * Retorna a quantidade de receitas na lista.
	 *
	 * @return A quantidade de receitas
	 */
	public int amountOfRevenues() {
		return revenueList.size();
	}
	
	/**
	 * Retorna a quantidade de despesas na lista.
	 *
	 * @return A quantidade de despesas
	 */
	public int amountOfExpenses() {
		return expenseList.size();
	}
	
	/**
	 * Retorna a quantidade de investimentos na lista.
	 *
	 * @return A quantidade de investimentos
	 */
	public int amountOfInvestments() {
		return investmentList.size();
	}
	
	/**
	 * Retorna o valor total das receitas em um determinado mês.
	 *
	 * @param month O mês para o qual o valor total das receitas é calculado
	 * @return O valor total das receitas no mês especificado
	 */
	public float totalValuesRevenues(Month month) {
		return (float) revenueList.stream()
				.filter(revenue -> revenue.getData().getMonth().getValue() == month.getNumber())
				.mapToDouble((revenue) -> revenue.getValue())
				.sum();
	}
	
	/**
	 * Retorna o valor total das despesas em um determinado mês.
	 *
	 * @param month O mês para o qual o valor total das despesas é calculado
	 * @return O valor total das despesas no mês especificado
	 */
	public float totalValuesExpenses(Month month) {
		return (float) expenseList.stream()
				.filter(expense -> expense.getPayDay().getMonth().getValue() == month.getNumber())
				.mapToDouble((expense) -> expense.getValue())
				.sum();
	}
	
	/**
	 * Retorna o valor total das despesas de uma determinada categoria.
	 *
	 * @param category A categoria das despesas
	 * @return O valor total das despesas na categoria especificada
	 */
	public float totalValuesExpensesByCategory(String category) {
		return (float) expenseList.stream()
				.filter(expense -> expense.getCategory().equalsIgnoreCase(category))
				.mapToDouble((expense) -> expense.getValue())
				.sum();
	}
	
	/**
	 * Retorna o valor total das despesas de uma determinada categoria em um determinado mês.
	 *
	 * @param category A categoria das despesas
	 * @param month O mês para o qual o valor total das despesas é calculado
	 * 
	 * @return O valor total das despesas na categoria e mês especificados
	 */
	public float totalValuesExpensesByCategoryAndMonth(String category, Month month) {
		return (float) expenseList.stream()
				.filter(expense -> expense.getCategory().equalsIgnoreCase(category))
				.filter(expense -> expense.getPayDay().getMonth().getValue() == month.getNumber())
				.mapToDouble((expense) -> expense.getValue())
				.sum();
	}
	
	/**
	 * Retorna o valor total dos investimentos.
	 *
	 * @return O valor total dos investimentos
	 */
	public float totalValuesInvestments() {
		return (float) investmentList.stream().mapToDouble((investment) -> investment.getAmountInvested()).sum();
	}
	
	/**
	 * Retorna o valor total acumulado dos investimentos.
	 *
	 * @return O valor total acumulado dos investimentos
	 */
	public float totalAccumulatedInvestments() {
		return (float) investmentList.stream().mapToDouble((investment) -> investment.getPosition()).sum();
	}
	
	/**
	 * Retorna o valor total de renda bruta dos investimentos.
	 *
	 * @return O valor total de renda bruta dos investimentos
	 */
	public float totalGrossIncomeInvestments() {
		return (float) investmentList.stream().mapToDouble((investment) -> investment.getGrossIncome()).sum();
	}
	
	/**
	 * Retorna o valor total pago das despesas em um determinado mês.
	 *
	 * @param month O mês para o qual o valor total pago das despesas é calculado
	 * 
	 * @return O valor total pago das despesas no mês especificado
	 */
	public float totalPaid(Month month) {
		return (float) expenseList.stream()
				.filter(expense -> expense.getPayDay().getMonth().getValue() == month.getNumber())
				.filter(expense -> expense.isPaid())
				.mapToDouble(expense -> expense.getValue())
				.sum();
	}
	
	/**
	 * Retorna a lista de despesas de um determinado mês.
	 *
	 * @param month O mês para o qual as despesas são filtradas
	 * 
	 * @return A lista de despesas no mês especificado
	 */
	public List<Expense> getExpensesByMonth(Month month) {
		return expenseList.stream()
		        .filter(expense -> expense.getPayDay().getMonth().getValue() == month.getNumber())
		        .collect(Collectors.toList());
	}
	
	/**
	 * Retorna a lista de despesas de um determinado mês e categoria.
	 *
	 * @param month O mês para o qual as despesas são filtradas
	 * @param category A categoria das despesas
	 * 
	 * @return A lista de despesas no mês e categoria especificados
	 */
	public List<Expense> getExpensesByMonthAndCategory(Month month, String category) {
		List<Expense> expensesByMonth = getExpensesByMonth(month);
		if(category == null) return expensesByMonth;
		
		List<Expense> selectedExpenses = expensesByMonth.stream()
		        .filter(expense -> expense.getCategory().equalsIgnoreCase(category))
		        .collect(Collectors.toList());
		return selectedExpenses;
	}
	
	/**
	 * Retorna a lista de meses que possuem despesas.
	 *
	 * @return A lista de meses com despesas
	 */
	public List<Month> getAllExpensesMonths() {
		List<Month> months = expenseList.stream()
		        .map(Expense::getMonth)
		        .distinct()
		        .collect(Collectors.toList());
		
		Collections.sort(months, Comparator.comparingInt(Month::getNumber));
		
		return months;
	}
	
	/**
	 * Retorna a lista de categorias de despesas, incluindo a opção "Todas as Categorias".
	 *
	 * @param includeTotal Indica se a opção "Todas as Categorias" deve ser incluída na lista
	 * @return A lista de categorias de despesas
	 */
	public List<String> getAllExpensesCategorys(boolean includeTotal) {
	    Set<String> uniqueCategories = expenseList.stream()
	            .map(Expense::getCategory)
	            .collect(Collectors.toSet());
	    
	    List<String> categorys = new ArrayList<>(uniqueCategories);
	    
	    for (String c : BASICS_CATEGORIES) 
	        if (!categorys.contains(c)) 
	            categorys.add(c);
	       
	    if(includeTotal)
	    	categorys.add(0, ALL_CATEGORY_EXPENSES);
	    
	    return categorys;
	}
	
	/**
	 * Retorna um mapa contendo os valores totais das despesas por categoria em um determinado mês.
	 *
	 * @param month O mês para o qual os valores totais das despesas são calculados
	 * @return Um mapa contendo os valores totais das despesas por categoria
	 */
	public Map<String, Float> getTotalValuesByCategories(Month month) {
		Map<String, Float> result = new HashMap<>();

		for(String category : getAllExpensesCategorys(false))
			result.put(category, totalValuesExpensesByCategoryAndMonth(category, month));
		
		return result;
	}
	
	/**
	 * Retorna uma lista contendo todos os investimentos.
	 *
	 * @return Uma lista de todos os investimentos
	 */
	public List<Investment> getAllInvestments() {
		return List.copyOf(investmentList);
	}
	
} // class FinancesList
