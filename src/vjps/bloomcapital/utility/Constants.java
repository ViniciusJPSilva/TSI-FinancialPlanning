package vjps.bloomcapital.utility;

/**
 * Classe responsável por agrupar as constantes utilizadas no software
 * de planejamento financeiro Bloom Capital.
 * <p>
 * Esta classe possui unicamente a função de agrupar tais constantes, e as mesmas
 * devem ser acessadas de maneira estática. A classe não pode ser herdade e nem instanciada
 * seguindo os padrões de desenvolvimento de interface constante de Josh Bloch. 
 * 
 * @author Vinícius J P Silva
 *
 */
public final class Constants {
	// Impede que a classe seja instanciada.
	private Constants() {} 
	
	public static final String
		FINANCIAL_PLANNING = "Planejamento Financeiro",
		
		TITLE_IMPORT_LOGS = "Registros da Importação",
		TITLE_FILE_CHOOSER = "Importar receitas, despesas e investimentos",
		TITLE_SEARCH_EXPENSE = "Pesquisar Despesa",
		
		BORDER_TITLE_BUDGET = "Orçamento",
		
		BTN_INVESTMENT = "Investimentos...",
		BTN_IMPORT = "Importar...",
		BTN_SEARCH_EXPENSE = "Pesquisar Despesa...",
		BTN_BAR_GRAPH = "Gráfico em Barras",
		BTN_PIE_CHART = "Gráfico em Pizza",
		BTN_CLOSE = "Fechar",
		BTN_NEXT_EXPENSE = "Próxima despesa",
		
		RADIO_BTN_DATE = "Data",
		RADIO_BTN_DESCRIPTION = "Descrição",
		RADIO_BTN_VALUE = "Valor",
		
		
		LB_REVENUE = "Receitas",
		LB_EXPENSE = "Despesas",
		LB_BALANCE = "Saldo",
		LB_TOTAL_PAID = "Total Pago",
		LB_TOTAL_PAYABLE = "Total A Pagar",
		LB_INVESTMENT = "Investimentos",
		LB_MONTH = "Mês:",
		LB_CATEGORY = "Categoria:",
		LB_SEARCH_ITEM = "Item de despesa:",
		LB_SEARCH_FOR = "Procurar por:",
		LB_TOTAL_INVESTED = "Total Investido",
		LB_ACCUMULATED_TOTAL = "Total Acumulado",
		LB_GROSS_INCOME = "Rendimento Bruto",
		
		
		TIP_LB_MONTH = "Seleciona o mês do orçamento.",
		TIP_BTN_IMPORT = "Importação de receitas, despesas e investimentos.",
		TIP_BTN_INVESTMENT = "Detalhes de cada investimento.",
		TIP_BTN_SEARCH_EXPENSE = "Pesquisar despesa por data, descrição ou valor.",
		TIP_BTN_BAR_GRAPH = "Exibe o gráfico de barras.",
		TIP_BTN_PIE_CHART = "Exibe o gráfico em pizza.",
		TIP_BTN_OPEN_FILE = "Abre um arquivo.",
		
		
		MSG_OPEN_FILE = "Abrir",
		MSG_CSV_FILES = "Arquivos separados por vírgula (*.csv)",
		MSG_DATA_NOT_FOUND = "Nenhum dado encontrado.",
		
		ERR_BD_ACCESS = "Não foi possível conectar ao banco de dados.\nO programa será finalizado.",
		ERR_INSTANTIATE_REVENUE = "Erro ao instanciar a receita!",
		ERR_INVALID_DATA = "Dados inválidos!\nTente novamente",
		ERR_UPDATE_EXPENSE = "Não foi possível atualizar! Confirme se o tipo de pagamento e a descrição existem no banco de dados!",
		ERR_PAYDAY_MUST_BE_AFTER = "A data de pagamento deve ser posterior ou igual à data da despesa!",
		
		LOG_INVALID_DATA = "\tLinha %d: dados inválidos.\n",
		LOG_DATA_ALREADY_EXISTS = "\tLinha %d: dado já existente no Banco de Dados.\n",
		LOG_EMPTY_FILE = "Arquivo \"%s\":\nSTATUS: arquivo vazio.\n\n",
		LOG_INVALID_CSV_HEADER = "ATENÇÃO: cabeçalho CSV inválido.\n",
		LOG_INVALID_CSV_LINE = "\tLinha %d: não representa um dado financeiro identificável.\n",
		LOG_MAIN_STATUS = "Arquivo \"%s\":\n",
		LOG_STATUS = "STATUS: Total de dados importados com sucesso: %d.\n",
		
		MONTHS[] = {"Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"},
		TABLE_COLUMNS[] = {"  Data", "  Dia", "  Tipo", "  Descrição", "  Valor", "  Paga"},
		TABLE_INVESTMENTS_COLUMNS[] = {"  Objetivo", "  Estratégia", "  Nome", "  Valor Investido", "  Posição", "  Rendimento Bruto", "  Rentabilidade", "  Vencimento"},
	
		REGEX_ANY_WORDS = "(.*)",
		REGEX_DATE = "[0-9]{2}/[0-9]{2}/[0-9]{4}",
		REGEX_MONETARY_VALUE = "(\\d+(\\.)?)+(\\,\\d{1,2})?",
		REGEX_PAY_DAY = "[0-9]{2}/[0-9]{2}",
		REGEX_FORM_PAYMENT = "(CC|CD|Dinheiro|Pix)",
		REGEX_SITUATION = "(;Paga)?"
	;
	
	public static final char
		COMMA = ',',
		SEMI_COLON = ';'
		;
} // class Constants
