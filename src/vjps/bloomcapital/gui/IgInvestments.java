package vjps.bloomcapital.gui;

import static vjps.bloomcapital.utility.Constants.LB_ACCUMULATED_TOTAL;
import static vjps.bloomcapital.utility.Constants.LB_GROSS_INCOME;
import static vjps.bloomcapital.utility.Constants.LB_TOTAL_INVESTED;
import static vjps.bloomcapital.utility.Constants.TABLE_INVESTMENTS_COLUMNS;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import vjps.bloomcapital.finance.FinancesList;
import vjps.bloomcapital.finance.investment.Investment;

/**
 * Janela de investimentos.
 * Permite ao usuário visualizar e interagir com informações sobre investimentos financeiros.
 * 
 * @author Vinícius J P Silva
 */
public class IgInvestments extends JDialog {

	private static final long serialVersionUID = 1L;

	public final Font STD_FONT = new Font("Tahoma", Font.PLAIN, 12),
			HEADER_TEXT_FONT = new Font("Arial", Font.BOLD, 16),
			HEADER_VALUE_FONT = new Font("Arial", Font.BOLD, 20)
			;
	
	public final Color STD_BG_COLOR = new Color(255, 255, 255),
			HEADER_FOREGROUND_COLOR = new Color(0, 0, 160)
			;
	
	public final int HEADER_PANELS_HORIZONTAL_GAP = 20,
			HEADER_ELEMENTS_VERTICAL_GAP = 10,
			HEADER_GROUPS_HORIZONTAL_GAP = 80
			;
	
	private List<Investment> investmentList;
	
	/**
	 * Cria e exibe a caixa de diálogo.
	 */
	public IgInvestments(Component component, FinancesList financesList) {
		
		investmentList = financesList.getAllInvestments();
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				dispose();
			}
		});
		
		getContentPane().setLayout(new BorderLayout());

		JPanel mainPanel = new JPanel();
		mainPanel.setBackground(STD_BG_COLOR);
		getContentPane().add(mainPanel, BorderLayout.CENTER);
		mainPanel.setLayout(null);

		JPanel headerPanel = new JPanel();
		headerPanel.setBackground(STD_BG_COLOR);
		headerPanel.setBounds(0, 0, 984, 70);
		mainPanel.add(headerPanel);
		headerPanel.setLayout(null);
		
		JPanel investmentsPanel = new JPanel();
		investmentsPanel.setBounds(6, 6, 546, 58);
		headerPanel.add(investmentsPanel);
		investmentsPanel.setBackground(STD_BG_COLOR);
		investmentsPanel.setLayout(new GridLayout(2, 3, HEADER_PANELS_HORIZONTAL_GAP, HEADER_ELEMENTS_VERTICAL_GAP));
		investmentsPanel.setBorder(BorderFactory.createEmptyBorder(0, HEADER_PANELS_HORIZONTAL_GAP, 0, 0));
		
		JLabel totalInvestedTextLabel = new JLabel(LB_TOTAL_INVESTED);
		totalInvestedTextLabel.setForeground(HEADER_FOREGROUND_COLOR);
		totalInvestedTextLabel.setFont(HEADER_TEXT_FONT);
		totalInvestedTextLabel.setHorizontalAlignment(SwingConstants.CENTER);
		investmentsPanel.add(totalInvestedTextLabel);
		
		JLabel accumulatedTotalTextLabel = new JLabel(LB_ACCUMULATED_TOTAL);
		accumulatedTotalTextLabel.setForeground(HEADER_FOREGROUND_COLOR);
		accumulatedTotalTextLabel.setFont(HEADER_TEXT_FONT);
		accumulatedTotalTextLabel.setHorizontalAlignment(SwingConstants.CENTER);
		investmentsPanel.add(accumulatedTotalTextLabel);
		
		JLabel grossIncomeTextLabel = new JLabel(LB_GROSS_INCOME);
		grossIncomeTextLabel.setForeground(HEADER_FOREGROUND_COLOR);
		grossIncomeTextLabel.setFont(HEADER_TEXT_FONT);
		grossIncomeTextLabel.setHorizontalAlignment(SwingConstants.CENTER);
		investmentsPanel.add(grossIncomeTextLabel);

		
		JLabel totalInvestedValueLabel = new JLabel(String.format("R$ %,.2f", financesList.totalValuesInvestments()));
		totalInvestedValueLabel.setHorizontalAlignment(SwingConstants.CENTER);
		totalInvestedValueLabel.setFont(HEADER_VALUE_FONT);
		investmentsPanel.add(totalInvestedValueLabel);
		
		JLabel accumulatedTotalValueLabel = new JLabel(String.format("R$ %,.2f", financesList.totalAccumulatedInvestments()));
		accumulatedTotalValueLabel.setHorizontalAlignment(SwingConstants.CENTER);
		accumulatedTotalValueLabel.setFont(HEADER_VALUE_FONT);
		investmentsPanel.add(accumulatedTotalValueLabel);
		
		JLabel grossIncomeValueLabel = new JLabel(String.format("R$ %,.2f", financesList.totalGrossIncomeInvestments()));
		grossIncomeValueLabel.setHorizontalAlignment(SwingConstants.CENTER);
		grossIncomeValueLabel.setFont(HEADER_VALUE_FONT);
		investmentsPanel.add(grossIncomeValueLabel);
		
		JPanel buttonPane = new JPanel();
		buttonPane.setBackground(STD_BG_COLOR);
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
		
		JButton barChartButton = new JButton("Gráfico em Barras");
		barChartButton.setBackground(STD_BG_COLOR);
		barChartButton.setFont(STD_FONT);
		barChartButton.setMnemonic(KeyEvent.VK_B);
		barChartButton.setToolTipText("Exibir o gráfico em barras.");
		buttonPane.add(barChartButton);
			
		JButton columnChartButton = new JButton("Gráfico em Colunas");
		columnChartButton.setBackground(STD_BG_COLOR);
		columnChartButton.setFont(STD_FONT);
		columnChartButton.setToolTipText("Exibir o gráfico de colunas.");
		columnChartButton.setMnemonic(KeyEvent.VK_C);
		columnChartButton.setActionCommand("OK");
		buttonPane.add(columnChartButton);
		getRootPane().setDefaultButton(columnChartButton);
	
	
		JButton closeButton = new JButton("Fechar");
		closeButton.setBackground(STD_BG_COLOR);
		closeButton.setFont(STD_FONT);
		closeButton.setMnemonic(KeyEvent.VK_F);
		closeButton.setToolTipText("Fechar a caixa de diálogo.");
		buttonPane.add(closeButton);
			
		
		
		JPanel tablePanel = new JPanel();
		tablePanel.setBackground(STD_BG_COLOR);
		tablePanel.setBorder(new TitledBorder(new LineBorder(new Color(200, 200, 200)), "Investimentos", TitledBorder.LEADING, TitledBorder.TOP, STD_FONT, null));
		tablePanel.setBounds(0, 70, 984, 304);
		mainPanel.add(tablePanel);
		tablePanel.setLayout(null);
		
		DefaultTableModel defaultTableModel = new DefaultTableModel(TABLE_INVESTMENTS_COLUMNS, 1);
		
		JTable table = new JTable(defaultTableModel);
		table.setEnabled(false);
		table.setBounds(10, 25, 964, 266);
		table.setShowVerticalLines(true);
		table.setShowHorizontalLines(true);
		table.setBackground(STD_BG_COLOR);
		
		table.getTableHeader().setReorderingAllowed(false);
		table.getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer () {
			private static final long serialVersionUID = 1L;
			private int customHeight = 30;
			
			private Border cellBorder = BorderFactory.createLineBorder(new Color(50, 50, 50, 50), 1);
			
		    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		        Component headerComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		        
		        setBorder(cellBorder); // Define a borda para a célula
		        setPreferredSize(new Dimension(getPreferredSize().width, customHeight)); // Define uma nova altura (height) para a célula

		        headerComponent.setBackground(STD_BG_COLOR); // Cor do Background 
		        headerComponent.setForeground(Color.BLACK); // Cor da fonte

		        return headerComponent;
		    }
		});
		
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
		scrollPane.setBounds(10, 25, 964, 266);
		scrollPane.getViewport().setBackground(new Color(240, 240, 240));
		tablePanel.add(scrollPane);
		
		defaultTableModel.setRowCount(investmentList.size());
		int line = 0;
		
		for(Investment investment : investmentList) {
			defaultTableModel.setValueAt(investment.getGoal(), line, 0);
			defaultTableModel.setValueAt(investment.getStrategy(), line, 1);
			defaultTableModel.setValueAt(investment.getName(), line, 2);
			defaultTableModel.setValueAt(String.format("R$ %,.2f", investment.getAmountInvested()), line, 3);
			defaultTableModel.setValueAt(String.format("R$ %,.2f", investment.getPosition()), line, 4);
			defaultTableModel.setValueAt(String.format("R$ %,.2f", investment.getGrossIncome()), line, 5);
			defaultTableModel.setValueAt(String.format("%,.2f%%", investment.getProfitability()), line, 6);
			defaultTableModel.setValueAt(investment.getMaturityDate().format(Investment.MATURITY_DATE_FORMAT), line++, 7);
		}
		
		barChartButton.addActionListener((e) -> new IgInvestmentChart(this, "Planejamento Financeiro - Investimentos", PlotOrientation.HORIZONTAL, createDatasetAmountAndPosition(), createDatasetProfitability()));
		columnChartButton.addActionListener((e) -> new IgInvestmentChart(this, "Planejamento Financeiro - Investimentos", PlotOrientation.VERTICAL, createDatasetAmountAndPosition(), createDatasetProfitability()));
		closeButton.addActionListener((e) -> dispose());
		
		
		setTitle("Planejamento Financeiro - Investimentos");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		setSize(1000, 450);
		setLocationRelativeTo(component);

		setResizable(false);
		setModal(true);
		setVisible(true);
	} //
	
	/**
	 * Cria o conjunto de dados para o gráfico de valor investido e posição dos investimentos.
	 * 
	 * @return O conjunto de dados para o gráfico de valor investido e posição
	 */
	private CategoryDataset createDatasetAmountAndPosition() {
		final String AMOUNT_INVESTED = "Valor investido";        
		final String POSITION = "Posição";  
		
		final DefaultCategoryDataset dataset = new DefaultCategoryDataset( );      
		
		for(Investment investment : investmentList) {
			String category = String.format("%s - %d", investment.getGoal(), investment.getCode());
			dataset.addValue(investment.getAmountInvested(), AMOUNT_INVESTED, category);
			dataset.addValue(investment.getPosition(), POSITION, category);
		}
		
		return dataset; 
	}
	
	/**
	 * Cria o conjunto de dados para o gráfico de rentabilidade dos investimentos.
	 * 
	 * @return O conjunto de dados para o gráfico de rentabilidade
	 */
	private CategoryDataset createDatasetProfitability() {
		final String PROFITABILITY = "Rentabilidade";  
		
		final DefaultCategoryDataset dataset = new DefaultCategoryDataset( );      
		
		for(Investment investment : investmentList) 
			dataset.addValue(investment.getProfitability(), PROFITABILITY, String.format("%s - %d", investment.getGoal(), investment.getCode()));

		
		return dataset; 
	}
}
