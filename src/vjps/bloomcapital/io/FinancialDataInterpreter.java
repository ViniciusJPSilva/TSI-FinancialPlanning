package vjps.bloomcapital.io;

import java.time.LocalDate;
import java.util.regex.Pattern;

import mos.reader.Line;
import vjps.bloomcapital.finance.expense.Expense;
import vjps.bloomcapital.finance.expense.FormPayment;
import vjps.bloomcapital.finance.investment.Investment;
import vjps.bloomcapital.finance.revenue.Revenue;

import static vjps.bloomcapital.utility.Constants.*;

/**
 * Classe responsável por prover métodos estáticos que instanciam objetos 
 * para o software Bloom Capital a partir de uma {@code Line}.
 * 
 * @author Vinícius J P Silva
 * 
 * @see {@link mos.reader.Line}
 *
 */
public final class FinancialDataInterpreter {

	// Impede a instanciação de objetos desta classe.
	private FinancialDataInterpreter() {}
	
	/**
	 * Enumeração responsável por representar os tipos de arquivos CSV aceitos pelo software.
	 * Valores:<p>
	 * REVENUE, EXPENSE, INVESTMENT
	 */
	public static enum FILE {REVENUE, EXPENSE, INVESTMENT};
	
	public static final String HEADER_REVENUE_FILE = "Tipo;Data;Valor",
			HEADER_EXPENSE_FILE = "DataDespesa;DiaPagamento;FormaPagamento;Descrição;Categoria;Valor;Situação",
			HEADER_INVESTMENT_FILE = "Objetivo;Estratégia;Nome;Valor Investido;Posição; Rendimento Bruto;Rentabilidade;Vencimento",
			
			REVENUE_LINE_REGEX = String.format("^%s;%s;%s$", REGEX_ANY_WORDS, REGEX_DATE, REGEX_MONETARY_VALUE),
			EXPENSE_LINE_REGEX = String.format("^%s;%s;%s;%s;%s;%s%s$", REGEX_DATE, REGEX_PAY_DAY, REGEX_FORM_PAYMENT,
												REGEX_ANY_WORDS, REGEX_ANY_WORDS, REGEX_MONETARY_VALUE, REGEX_SITUATION),
			INVESTMENT_LINE_REGEX = String.format("^%s;%s;%s;%s;%s;%s;%s%%;%s$", REGEX_ANY_WORDS, REGEX_ANY_WORDS, REGEX_ANY_WORDS, REGEX_MONETARY_VALUE,
												REGEX_MONETARY_VALUE, REGEX_MONETARY_VALUE, REGEX_MONETARY_VALUE, REGEX_DATE);
	
	/**
	 * Identifica o tipo de arquivo com base na linha de cabeçalho do arquivo CSV.
	 * 
	 * @param line a linha do arquivo a ser analisada
	 * 
	 * @return o tipo de arquivo identificado, ou null se não for identificado
	 * 
	 * @see {@link FILE}
	 * @see {@link Line}
	 */
	public static FILE identifyFileDataTypeByHeader(Line line) {
		switch (lineToCSV(line)) {
		case HEADER_REVENUE_FILE: return FILE.REVENUE;
		case HEADER_EXPENSE_FILE: return FILE.EXPENSE;
		case HEADER_INVESTMENT_FILE: return FILE.INVESTMENT;
		default:
			return null;
		}
	}
	
	/**
	 * Identifica o tipo de arquivo com base em uma linha de dados.
	 *
	 * @param line A linha de dados a ser identificada
	 * 
	 * @return O tipo de arquivo identificado (REVENUE, EXPENSE, INVESTMENT) ou null se não for identificado
	 */
	public static FILE identifyDataByLine(Line line) {
		String csvLine = lineToCSV(line);
		
		if(Pattern.matches(REVENUE_LINE_REGEX, csvLine))
			return FILE.REVENUE;
		if(Pattern.matches(EXPENSE_LINE_REGEX, csvLine))
			return FILE.EXPENSE;
		if(Pattern.matches(INVESTMENT_LINE_REGEX, csvLine))
			return FILE.INVESTMENT;
		return null;
	}
	
	
	/**
	 * Converte um objeto {@code Line} para uma string formatada em CSV.
	 *
	 * @param line o objeto Line a ser convertido
	 * 
	 * @return uma string formatada em CSV que representa o objeto Line
	 * 
	 * @see {@link Line}
	 */
	private static String lineToCSV(Line line) {
		StringBuilder builder = new StringBuilder();
		
		String[] strLine = line.getLine();
		
		for(int index = 0; index < strLine.length - 1; index++)
			builder.append(String.format("%s;", strLine[index]));
		
		return builder.append(strLine[strLine.length - 1]).toString();
	}
	
	/**
	 * Cria um objeto {@code Revenue} com base nos dados fornecidos em uma {@code Line}.
	 * 
	 * @param line linha com os dados da receita.
	 * 
	 * @return objeto {@code Revenue} criado ou {@code null} caso ocorra algum erro durante a conversão dos dados.
	 */
	public static Revenue getRevenueFromline(Line line) {
		try {
			return new Revenue(line.getData(0),
					Float.parseFloat(standardizeFloatString(line.getData(2))),
					LocalDate.parse(line.getData(1), Revenue.DATE_FORMAT));
		} catch (Exception e) {
			return null;
		}
	} // getRevenueFromline()
	
	/**
	 * Cria um objeto {@code Expense} com base nos dados fornecidos em uma {@code Line}.
	 * 
	 * @param line linha com os dados da despesa.
	 * 
	 * @return objeto {@code Expense} criado ou {@code null} caso ocorra algum erro durante a conversão dos dados.
	 */
	public static Expense getExpenseFromline(Line line) {
		try {		
			return new Expense(line.getData(3),
					Float.parseFloat(standardizeFloatString(line.getData(5))), 
					LocalDate.parse(line.getData(0), Expense.EXPENSE_DATE_FORMAT), 
					LocalDate.parse(
							String.format("%s%s", 
								line.getData(1), 
								line.getData(0).substring(line.getData(0).lastIndexOf('/'))), 
							Expense.EXPENSE_DATE_FORMAT), 
					FormPayment.getByName(line.getData(2)), 
					line.getData(4), 
					(line.quantityOfData() >= 7) ? true : false
				);
		} catch (Exception e) {
			return null;
		}
	} // getExpenseFromline()
	
	
	/**
	 * Cria um objeto {@code Investment} com base nos dados fornecidos em uma {@code Line}.
	 * 
	 * @param line linha com os dados do investimento.
	 * 
	 * @return objeto {@code Investment} criado ou {@code null} caso ocorra algum erro durante a conversão dos dados.
	 */
	public static Investment getInvestmentFromline(Line line) {
		try {			
			return new Investment(line.getData(0), line.getData(1), line.getData(2), 
					Float.parseFloat(standardizeFloatString(line.getData(3))), 
					Float.parseFloat(standardizeFloatString(line.getData(4))), 
					Float.parseFloat(standardizeFloatString(line.getData(5))), 
					Float.parseFloat(standardizeFloatString(line.getData(6))), 
					LocalDate.parse(line.getData(7), Investment.MATURITY_DATE_FORMAT));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	} // getInvestmentFromline()
	

	/**
	 * Padroniza e retorna uma string que representa um valor {@code float}
	 * 
	 * @param value string com o valor float a ser padronizado
	 * 
	 * @return o valor float a ser padronizado, em formato string
	 */
	public static String standardizeFloatString(String value) {
		return value.replaceAll("\\.", "").replaceAll("\\%", "").replaceAll(",", ".");
	}

	
} // class FinancialDataInterpreter
