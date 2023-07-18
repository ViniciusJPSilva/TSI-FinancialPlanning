package vjps.bloomcapital.gui;

import static mos.io.InputOutput.getTextArea;
import static mos.io.InputOutput.showError;
import static mos.io.InputOutput.writeTextArea;
import static vjps.bloomcapital.io.FinancialDataInterpreter.getExpenseFromline;
import static vjps.bloomcapital.io.FinancialDataInterpreter.getInvestmentFromline;
import static vjps.bloomcapital.io.FinancialDataInterpreter.getRevenueFromline;
import static vjps.bloomcapital.io.FinancialDataInterpreter.identifyDataByLine;
import static vjps.bloomcapital.io.FinancialDataInterpreter.identifyFileDataTypeByHeader;
import static vjps.bloomcapital.utility.Constants.*;

import java.awt.BorderLayout;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ContainerAdapter;
import java.awt.event.ContainerEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;

import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;

import mos.reader.Line;
import mos.reader.Reader;
import vjps.bloomcapital.db.DataBase;
import vjps.bloomcapital.db.dao.ExpenseDAO;
import vjps.bloomcapital.db.dao.InvestmentDAO;
import vjps.bloomcapital.db.dao.RevenueDAO;
import vjps.bloomcapital.finance.FinancesList;
import vjps.bloomcapital.finance.expense.Expense;
import vjps.bloomcapital.finance.expense.FormPayment;
import vjps.bloomcapital.finance.investment.Investment;
import vjps.bloomcapital.finance.revenue.Revenue;
import vjps.bloomcapital.gui.chart.PieChartPanel;
import vjps.bloomcapital.gui.chart.VerticalBarChartPanel;
import vjps.bloomcapital.io.FinancialDataInterpreter;
import vjps.bloomcapital.io.FinancialDataInterpreter.FILE;
import vjps.bloomcapital.io.ObjectCreationException;
import vjps.bloomcapital.utility.Month;

/**
 * Janela principal do software.
 * 
 * @author Vinícius J P Silva
 */
public class IgBloomCapital extends JFrame {

	public final Font STD_FONT = new Font("Tahoma", Font.PLAIN, 12),
			HEADER_TEXT_FONT = new Font("Arial", Font.BOLD, 16),
			HEADER_VALUE_FONT = new Font("Arial", Font.BOLD, 20)
			;
	
	public final Color STD_BG_COLOR = new Color(255, 255, 255),
			HEADER_FOREGROUND_COLOR = new Color(0, 0, 160)
			;

	public final int WINDOW_POSITION_X = 100,
			WINDOW_POSITION_Y = 100,
			WINDOW_WIDTH = 1207, 
			WINDOW_HEIGTH = 521,
			
			HEADER_PANELS_HORIZONTAL_GAP = 40,
			HEADER_ELEMENTS_VERTICAL_GAP = 10,
			HEADER_GROUPS_HORIZONTAL_GAP = 80
			;
	
	
	private static final long serialVersionUID = 1L;
	
	
	private UIManager.LookAndFeelInfo lookAndFeelInfo[];
	
	private DataBase dataBase;
	private FinancesList financesList;
	List<Expense> expensesInTable;
	
	private JPanel contentPane;
	private JPanel graphPanel;
	
	private Choice monthChoice;
	private JTable table;
	DefaultTableModel defaultTableModel;
	String lastChoiceMonth;
	String lastChoiceCategory;
	boolean isPieChart = true;
	int editingRow;
	
	private PieChartPanel pieChart;
	private VerticalBarChartPanel barChart;
	private JLabel revenueValueLabel;
	private JLabel expensesValueLabel;
	private JLabel balanceValueLabel;
	private JLabel totalPaidValueLabel;
	private JLabel totalPayableValueLabel;
	private JLabel investmentValueLabel;
	private Choice categoryChoice;

	/**
	 * Cria e exibe a GUI.
	 * 
	 * @param dataBase objeto de acesso ao banco de dados.
	 */
	public IgBloomCapital(DataBase dataBase) {
		this.dataBase = dataBase;
		financesList = new FinancesList();
		
		lookAndFeelInfo = UIManager.getInstalledLookAndFeels();
		
		addWindowListener(new WindowAdapter() {	
			@Override
			public void windowClosing(WindowEvent e) {
				finalizeFinancialPlanning();
			}

			@Override
			public void windowOpened(WindowEvent e) {
				changeLookAndFeel("Nimbus");
			}
		});
		
		contentPane = new JPanel();
		contentPane.setBackground(STD_BG_COLOR);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(WINDOW_POSITION_X, WINDOW_POSITION_Y, WINDOW_WIDTH, WINDOW_HEIGTH);
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		/*
		 * Cabeçalho
		 */
		JPanel headerPanel = new JPanel();
		headerPanel.setBackground(STD_BG_COLOR);
		contentPane.add(headerPanel, BorderLayout.NORTH);
		headerPanel.setLayout(new BorderLayout(0, 0));
		
		JPanel financesPanel = new JPanel();
		headerPanel.add(financesPanel, BorderLayout.WEST);
		financesPanel.setBackground(STD_BG_COLOR);
		financesPanel.setLayout(new GridLayout(2, 3, HEADER_PANELS_HORIZONTAL_GAP, HEADER_ELEMENTS_VERTICAL_GAP));
		financesPanel.setBorder(BorderFactory.createEmptyBorder(0, HEADER_PANELS_HORIZONTAL_GAP, 0, 0));
		
		JLabel revenueTextLabel = new JLabel(LB_REVENUE);
		revenueTextLabel.setForeground(HEADER_FOREGROUND_COLOR);
		revenueTextLabel.setFont(HEADER_TEXT_FONT);
		revenueTextLabel.setHorizontalAlignment(SwingConstants.CENTER);
		financesPanel.add(revenueTextLabel);
		
		JLabel expensesTextLabel = new JLabel(LB_EXPENSE);
		expensesTextLabel.setForeground(HEADER_FOREGROUND_COLOR);
		expensesTextLabel.setFont(HEADER_TEXT_FONT);
		expensesTextLabel.setHorizontalAlignment(SwingConstants.CENTER);
		financesPanel.add(expensesTextLabel);
		
		JLabel balanceTextLabel = new JLabel(LB_BALANCE);
		balanceTextLabel.setForeground(HEADER_FOREGROUND_COLOR);
		balanceTextLabel.setFont(HEADER_TEXT_FONT);
		balanceTextLabel.setHorizontalAlignment(SwingConstants.CENTER);
		financesPanel.add(balanceTextLabel);
		
		
		revenueValueLabel = new JLabel("R$ 0,00");
		revenueValueLabel.setHorizontalAlignment(SwingConstants.CENTER);
		revenueValueLabel.setFont(HEADER_VALUE_FONT);
		financesPanel.add(revenueValueLabel);
		
		expensesValueLabel = new JLabel("R$ 0,00");
		expensesValueLabel.setHorizontalAlignment(SwingConstants.CENTER);
		expensesValueLabel.setFont(HEADER_VALUE_FONT);
		financesPanel.add(expensesValueLabel);
		
		balanceValueLabel = new JLabel("R$ 0,00");
		balanceValueLabel.setHorizontalAlignment(SwingConstants.CENTER);
		balanceValueLabel.setFont(HEADER_VALUE_FONT);
		financesPanel.add(balanceValueLabel);
		
		
		
		JPanel totalPaidPanel = new JPanel();
		headerPanel.add(totalPaidPanel, BorderLayout.CENTER);
		totalPaidPanel.setBackground(STD_BG_COLOR);
		totalPaidPanel.setBorder(BorderFactory.createEmptyBorder(0, HEADER_PANELS_HORIZONTAL_GAP, 0, HEADER_PANELS_HORIZONTAL_GAP));
		totalPaidPanel.setLayout(null);
		
		JLabel totalPaidTextLabel = new JLabel(LB_TOTAL_PAID);
		totalPaidTextLabel.setBounds(166, 0, 102, 24);
		totalPaidTextLabel.setForeground(HEADER_FOREGROUND_COLOR);
		totalPaidTextLabel.setFont(HEADER_TEXT_FONT);
		totalPaidTextLabel.setHorizontalAlignment(SwingConstants.CENTER);
		totalPaidPanel.add(totalPaidTextLabel);
		
		JLabel totalPayableTextLabel = new JLabel(LB_TOTAL_PAYABLE);
		totalPayableTextLabel.setBounds(310, 0, 124, 24);
		totalPayableTextLabel.setForeground(HEADER_FOREGROUND_COLOR);
		totalPayableTextLabel.setFont(HEADER_TEXT_FONT);
		totalPayableTextLabel.setHorizontalAlignment(SwingConstants.CENTER);
		totalPaidPanel.add(totalPayableTextLabel);
		
		totalPaidValueLabel = new JLabel("R$ 0,00");
		totalPaidValueLabel.setBounds(152, 34, 133, 24);
		totalPaidValueLabel.setHorizontalAlignment(SwingConstants.CENTER);
		totalPaidValueLabel.setFont(HEADER_VALUE_FONT);
		totalPaidPanel.add(totalPaidValueLabel);
		
		totalPayableValueLabel = new JLabel("R$ 0,00");
		totalPayableValueLabel.setBounds(307, 34, 133, 24);
		totalPayableValueLabel.setHorizontalAlignment(SwingConstants.CENTER);
		totalPayableValueLabel.setFont(HEADER_VALUE_FONT);
		totalPaidPanel.add(totalPayableValueLabel);
		
		
		
		JPanel investmentPanel = new JPanel();
		headerPanel.add(investmentPanel, BorderLayout.EAST);
		investmentPanel.setBackground(STD_BG_COLOR);
		investmentPanel.setLayout(new GridLayout(2, 1, 0, HEADER_ELEMENTS_VERTICAL_GAP));
		investmentPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, HEADER_PANELS_HORIZONTAL_GAP));
		
		JLabel investmentTextLabel = new JLabel(LB_INVESTMENT);
		investmentTextLabel.setForeground(HEADER_FOREGROUND_COLOR);
		investmentTextLabel.setFont(HEADER_TEXT_FONT);
		investmentTextLabel.setHorizontalAlignment(SwingConstants.CENTER);
		investmentPanel.add(investmentTextLabel);
		
		investmentValueLabel = new JLabel("R$ 0,00");
		investmentValueLabel.setHorizontalAlignment(SwingConstants.CENTER);
		investmentValueLabel.setFont(HEADER_VALUE_FONT);
		investmentPanel.add(investmentValueLabel);
		

		/*
		 * Painel principal
		 */
		
		JPanel mainPanel = new JPanel();
		mainPanel.setBorder(new TitledBorder(new LineBorder(new Color(200, 200, 200)), BORDER_TITLE_BUDGET, TitledBorder.LEADING, TitledBorder.TOP, STD_FONT, null));
		mainPanel.setBackground(STD_BG_COLOR);
		contentPane.add(mainPanel, BorderLayout.CENTER);
		mainPanel.setLayout(null);
		
		JLabel monthLabel = new JLabel(LB_MONTH);
		monthLabel.setBounds(21, 24, 25, 14);
		monthLabel.setFont(STD_FONT);
		monthLabel.setToolTipText(TIP_LB_MONTH);
		monthLabel.setDisplayedMnemonic(KeyEvent.VK_M);
		mainPanel.add(monthLabel);
		
		monthChoice = new Choice();
		monthChoice.setBounds(55, 21, 91, 22);
		monthChoice.setFont(STD_FONT);
		mainPanel.add(monthChoice);
		monthLabel.setLabelFor(monthChoice);
		
		
		JLabel categoryLabel = new JLabel(LB_CATEGORY);
		categoryLabel.setBounds(160, 24, 61, 14);
		categoryLabel.setFont(STD_FONT);
		categoryLabel.setToolTipText(TIP_LB_MONTH);
		categoryLabel.setDisplayedMnemonic(KeyEvent.VK_C);
		mainPanel.add(categoryLabel);
		
		categoryChoice = new Choice();
		categoryChoice.setBounds(223, 21, 120, 22);
		categoryChoice.setFont(STD_FONT);
		mainPanel.add(categoryChoice);
		categoryLabel.setLabelFor(categoryChoice);
		

		defaultTableModel = new DefaultTableModel(TABLE_COLUMNS, 1) {
		    private static final long serialVersionUID = 1L;
		    
		    @Override
		    public Class<?> getColumnClass(int columnIndex) {
		        if (columnIndex == getColumnCount() - 1)
		            return Boolean.class;
		        return super.getColumnClass(columnIndex);
		    }
		};
		
		table = new JTable(defaultTableModel);
		table.setBounds(21, 62, 630, 250);
		table.setShowVerticalLines(true);
		table.setShowHorizontalLines(true);
		table.setBackground(STD_BG_COLOR);
		
		table.setDefaultRenderer(Boolean.class, new CheckBoxCell());
		
		table.getTableHeader().setReorderingAllowed(false);
		table.getTableHeader().setResizingAllowed(false);
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
		
		table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
			private static final long serialVersionUID = 1L;
			
			private Border cellBorder = BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(Color.GRAY, 1), // Utiliza a borda InternalFrameBorder para obter uma linha mais fina
		        BorderFactory.createEmptyBorder(1, 1, 1, 1) // Adiciona um espaçamento interno para a borda
		    );

		    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		        Component cellComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		        
		        setBorder(cellBorder); 

		        return cellComponent;
		    }
			
		});
		
		table.getColumnModel().getColumn(0).setPreferredWidth(100);
		table.getColumnModel().getColumn(1).setPreferredWidth(40);
		table.getColumnModel().getColumn(2).setPreferredWidth(70);
		table.getColumnModel().getColumn(3).setPreferredWidth(300);
		table.getColumnModel().getColumn(4).setPreferredWidth(70);
		table.getColumnModel().getColumn(5).setPreferredWidth(50);
		
		// Adiciona a tabela ao painel.
		JScrollPane tableScrollPane = new JScrollPane(table);
		tableScrollPane.setBorder(null);
		tableScrollPane.setBounds(21, 62, 632, 250);
		tableScrollPane.getViewport().setBackground(new Color(240, 240, 240));
		mainPanel.add(tableScrollPane, BorderLayout.CENTER);
		
		
		JButton searchExpenseButton = new JButton(BTN_SEARCH_EXPENSE);
		searchExpenseButton.setBounds(21, 321, 150, 28);
		mainPanel.add(searchExpenseButton);
		searchExpenseButton.setFont(STD_FONT);
		searchExpenseButton.setBackground(STD_BG_COLOR);
		searchExpenseButton.setMnemonic(KeyEvent.VK_P);
		searchExpenseButton.setToolTipText(TIP_BTN_SEARCH_EXPENSE);
		
		
		JButton showBarGraphButton = new JButton(BTN_BAR_GRAPH);
		showBarGraphButton.setBounds(183, 321, 122, 28);
		mainPanel.add(showBarGraphButton);
		showBarGraphButton.setFont(STD_FONT);
		showBarGraphButton.setBackground(STD_BG_COLOR);
		showBarGraphButton.setMnemonic(KeyEvent.VK_B);
		showBarGraphButton.setToolTipText(TIP_BTN_BAR_GRAPH);
		
		JButton showPieChartButton = new JButton(BTN_PIE_CHART);
		showPieChartButton.setBounds(317, 321, 122, 28);
		mainPanel.add(showPieChartButton);
		showPieChartButton.setFont(STD_FONT);
		showPieChartButton.setBackground(STD_BG_COLOR);
		showPieChartButton.setMnemonic(KeyEvent.VK_G);
		showPieChartButton.setToolTipText(TIP_BTN_PIE_CHART);
		
		pieChart = null;
		barChart = null;
		
		graphPanel = new JPanel();
		graphPanel.setBounds(665, 62, 499, 250);
		graphPanel.setBackground(STD_BG_COLOR);
		updateGraphPanel(null);
		mainPanel.add(graphPanel);
		
		/*
		 * Rodapé
		 */
		JPanel footerPanel = new JPanel();
		footerPanel.setBackground(STD_BG_COLOR);
		contentPane.add(footerPanel, BorderLayout.SOUTH);
		footerPanel.setLayout(new BoxLayout(footerPanel, BoxLayout.X_AXIS));
		
		JPanel containerFooterPanel = new JPanel();
		containerFooterPanel.setBackground(STD_BG_COLOR);
		footerPanel.add(containerFooterPanel);
		containerFooterPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));
		
		JButton importButton = new JButton(BTN_IMPORT);
		containerFooterPanel.add(importButton);
		importButton.setFont(STD_FONT);
		importButton.setBackground(STD_BG_COLOR);
		importButton.setMnemonic(KeyEvent.VK_O);
		importButton.setToolTipText(TIP_BTN_IMPORT);
		
		JButton investmentButton = new JButton(BTN_INVESTMENT);
		containerFooterPanel.add(investmentButton);
		investmentButton.setFont(STD_FONT);
		investmentButton.setBackground(STD_BG_COLOR);
		investmentButton.setMnemonic(KeyEvent.VK_I);
		investmentButton.setToolTipText(TIP_BTN_INVESTMENT);
		
		
		/**
		 * Tratamento dos eventos
		 */
		// Choice's
		monthChoice.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					lastChoiceMonth = monthChoice.getSelectedItem();
					updateInterface();
                }
			}
		});
		
		categoryChoice.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					lastChoiceCategory = categoryChoice.getSelectedItem();
					updateInterface();
                }
			}
		});
		
		// Buttons
		searchExpenseButton.addActionListener((e) -> new IgSearchExpense(this, table));
		showPieChartButton.addActionListener((e) -> updateGraphPanel(getPieChart()));
		showBarGraphButton.addActionListener((e) -> updateGraphPanel(getBarChart()));
		
		importButton.addActionListener((e) -> importFiles());
		investmentButton.addActionListener((e) -> new IgInvestments(this, financesList));
		
		// Tabela
		table.addContainerListener(new ContainerAdapter() {
			@Override
			public void componentRemoved(ContainerEvent event) {
				if(table.getSelectedRow() != -1)
					changedRow();
			}
		});
		
		table.addKeyListener(new KeyAdapter() {
		    @Override
		    public void keyPressed(KeyEvent e) {
		        int keyCode = e.getKeyCode();
		        int selectedRow = table.getSelectedRow();
		        int selectedColumn = table.getSelectedColumn();

		        if (keyCode == KeyEvent.VK_ENTER || keyCode == KeyEvent.VK_TAB) {
		            e.consume(); // Evita que o evento seja processado pela tabela

		            if (selectedRow != -1 && selectedColumn != -1) {
		                TableCellEditor editor = table.getCellEditor(selectedRow, selectedColumn);
		                if (editor != null) {
		                    editor.stopCellEditing();
		                }

		                if (keyCode == KeyEvent.VK_ENTER) {
		                    if (selectedColumn == table.getColumnCount() - 1) {
		                        if (selectedRow == table.getRowCount() - 1) {
		                            // Última célula da tabela, não há próxima célula
		                            return;
		                        } else {
		                            // Próxima linha, primeira célula
		                            table.changeSelection(selectedRow + 1, 0, false, false);
		                        }
		                    } else {
		                        // Mesma linha, próxima célula
		                        table.changeSelection(selectedRow, selectedColumn + 1, false, false);
		                    }
		                } else if (keyCode == KeyEvent.VK_TAB) {
		                    if (selectedColumn == table.getColumnCount() - 1) {
		                        if (selectedRow == table.getRowCount() - 1) {
		                            // Última célula da tabela, não há próxima célula
		                            return;
		                        } else {
		                            // Próxima linha, primeira célula
		                            table.changeSelection(selectedRow + 1, 0, false, false);
		                        }
		                    } else {
		                        // Mesma linha, próxima célula
		                        table.changeSelection(selectedRow, selectedColumn + 1, false, false);
		                    }
		                }

		                SwingUtilities.invokeLater(() -> {
		                    table.editCellAt(table.getSelectedRow(), table.getSelectedColumn());
		                    table.requestFocus();
		                });
		            }
		        }
		    }
		});
		
		
		/*
		 * Definições finais e exebição da interface. 
		 */
		setTitle(FINANCIAL_PLANNING);
		setResizable(false);

		updateInterface();
		
		setVisible(true);
		importButton.requestFocusInWindow();
		
		
	} // IgFinancialPlanning()
	
	/**
	 * Notifica a mudança em uma linha da tabela.
	 * 
	 * Verifica se todos os campos da linha foram preenchidos corretamente e, em caso afirmativo,
	 * realiza validações adicionais nos dados e atualiza a tabela de despesas.
	 */
	private void changedRow() {
	    editingRow = table.getEditingRow();
	    int editingColumn = table.getEditingColumn();
	    if (editingRow != -1 && editingColumn != -1) {
	        boolean allFieldsFilled = true;
	        for (int column = 0; column < table.getColumnCount(); column++) {
	            if (column != table.getColumnCount() - 1) { // Ignorar a última coluna (checkbox)
	                Object value = table.getValueAt(editingRow, column);
	                if (value == null || value.toString().isEmpty()) {
	                    allFieldsFilled = false;
	                    break;
	                }
	            }
	        }
	        if (allFieldsFilled) {
	        	String[] rowData = new String[table.getColumnCount() - 1];
	            for (int column = 0; column < table.getColumnCount() - 1; column++) {
	                Object value = table.getValueAt(editingRow, column);
	                rowData[column] = value.toString();
	            }
	            try {
	                validateNewExpense(rowData, editingRow);
	            } catch (ObjectCreationException e) {
	            	showError(ERR_INVALID_DATA, FINANCIAL_PLANNING);
	            	if(editingRow > expensesInTable.size())
	            		clearRowData(editingRow);
	            	else
	            		updateExpensesTable();
	            }
	        }
	    }		
	}

	/**
	 * Valida e cria uma nova despesa com base nos dados da linha da tabela.
	 *
	 * @param rowData Os dados da linha a serem utilizados na criação da despesa
	 * @param row O número da linha sendo validada
	 * 
	 * @throws ObjectCreationException Se ocorrer um erro na criação do objeto Expense
	 */
	private void validateNewExpense(String[] rowData, int row) throws ObjectCreationException {
		try {
			Expense expense = createExpenseFromRowData(rowData, row);
			if(expense != null) {
				ExpenseDAO dao = new ExpenseDAO(dataBase.getConnection());
				if(row < expensesInTable.size()) {
					if(!dao.update(expense))
						showError(ERR_UPDATE_EXPENSE, FINANCIAL_PLANNING);
				} else
					dao.insert(expense);
			} else
				showError(ERR_PAYDAY_MUST_BE_AFTER, FINANCIAL_PLANNING);
			
			if(row < expensesInTable.size() || expense != null)
				updateInterface();
		} catch (Exception e) {
			throw new ObjectCreationException();
		}
	}
	
	/**
	 * Cria um objeto Expense com base nos dados da linha.
	 *
	 * @param rowData Os dados da linha a serem utilizados na criação da despesa
	 * @param row O número da linha sendo processada
	 * 
	 * @return O objeto Expense criado com base nos dados da linha
	 */
	private Expense createExpenseFromRowData(String[] rowData, int row) {
	    String description = rowData[3];
	    float value = Float.parseFloat(FinancialDataInterpreter.standardizeFloatString(rowData[4]));
	    LocalDate expenseDate = LocalDate.parse(rowData[0], DateTimeFormatter.ofPattern("d[d]/M[M]/y[YYY]"));
	    LocalDate payDay = getNextDateWithDay(Integer.parseInt(rowData[1]), expenseDate);
	    FormPayment formPayment = FormPayment.getByName(rowData[2]);
	    
	    if(!isDateAfterOrEqual(payDay, expenseDate)) return null;
	    
	    String category = categoryChoice.getSelectedItem();
	    Long code = null;
	    
	    if(row < expensesInTable.size()) {
	    	code = expensesInTable.get(row).getCode();
	    	category = expensesInTable.get(row).getCategory();
	    }
	    
	    return new Expense(code, description, value, expenseDate, payDay, formPayment, (category.equalsIgnoreCase(FinancesList.ALL_CATEGORY_EXPENSES)) ? "Outros" : category, isCheckBoxSelected(row));
	}
	
	/**
	 * Verifica se a caixa de seleção está marcada para a linha especificada.
	 *
	 * @param row O número da linha a ser verificada
	 * 
	 * @return true se a caixa de seleção estiver marcada, false caso contrário
	 */
	private boolean isCheckBoxSelected(int row) {
	    Object value = table.getValueAt(row, table.getColumnCount() - 1);
	    return value instanceof Boolean && (boolean) value;
	}
	
	/**
	 * Obtém a próxima data com o dia especificado a partir de uma data de referência.
	 *
	 * @param day O dia desejado
	 * @param date A data de referência
	 * 
	 * @return A próxima data com o dia especificado
	 */
    private LocalDate getNextDateWithDay(int day, LocalDate date) {
        if(day <= 0 || day > 31) return null;
        
        return LocalDate.parse(
        		String.format("%d/%d/%d", day, Month.name(monthChoice.getSelectedItem()).getNumber(), date.getYear()), 
        		DateTimeFormatter.ofPattern("d[d]/M[M]/y[YYY]"));
    }
    
    /**
     * Verifica se uma data é posterior ou igual a outra.
     *
     * @param date1 A primeira data a ser comparada
     * @param date2 A segunda data a ser comparada
     * 
     * @return true se a primeira data for posterior ou igual à segunda, false caso contrário
     */
    public boolean isDateAfterOrEqual(LocalDate date1, LocalDate date2) {
        return !date1.isBefore(date2);
    }

    
    /**
     * Limpa os dados de uma linha na tabela.
     *
     * @param row O número da linha a ser limpa
     */
	private void clearRowData(int row) {
		if(row >=0 && row < defaultTableModel.getRowCount())
		    for (int column = 0; column < table.getColumnCount(); column++)
		        if (column != table.getColumnCount() - 1) // Ignorar a última coluna (checkbox)
		            table.setValueAt(null, row, column);
	}

	
	/**
	 * Atualiza a interface do usuário.
	 * 
	 * Atualiza os dados da base de dados, o cabeçalho, as opções de escolha (mês e categoria)
	 * e a tabela de despesas.
	 */
	private void updateInterface() {
		getDataFromDataBase();
		
		setChoiceOptions(monthChoice, MONTHS);
		monthChoice.select(lastChoiceMonth);
		setChoiceOptions(categoryChoice, financesList.getAllExpensesCategorys(true).stream().toArray(String[]::new));
		categoryChoice.select(lastChoiceCategory);
		
		updateHeader();
		updateExpensesTable();
	}

	/**
	 * Atualiza o cabeçalho da interface.
	 * 
	 * Atualiza os valores totais de receitas, despesas, saldo, valor total pago,
	 * valor total a pagar e valores totais de investimentos exibidos no cabeçalho.
	 */
	private void updateHeader() {
		Month currentMonth = Month.name(monthChoice.getSelectedItem());
		var totalRevenues = financesList.totalValuesRevenues(currentMonth);
		var totalExpenses = financesList.totalValuesExpenses(currentMonth);
		var totalPaid = financesList.totalPaid(currentMonth);
		
		revenueValueLabel.setText(formatMonetaryValues(totalRevenues));
		expensesValueLabel.setText(formatMonetaryValues(totalExpenses));
		balanceValueLabel.setText(formatMonetaryValues(totalRevenues - totalExpenses));
		
		totalPaidValueLabel.setText(formatMonetaryValues(totalPaid));
		totalPayableValueLabel.setText(formatMonetaryValues(totalExpenses - totalPaid));
		
		investmentValueLabel.setText(formatMonetaryValues(financesList.totalValuesInvestments()));
	}

	/**
	 * Atualiza a tabela de despesas.
	 * 
	 * Atualiza os dados da tabela com base no mês e categoria selecionados.
	 * Ordena as despesas pelo método de pagamento.
	 * Atualiza o modelo da tabela e preenche os dados.
	 * Atualiza o gráfico exibido no painel.
	 */
	private void updateExpensesTable() {
		expensesInTable = financesList.getExpensesByMonth(Month.name(monthChoice.getSelectedItem()));
		
		String category = categoryChoice.getSelectedItem();
		if(!category.equalsIgnoreCase(FinancesList.ALL_CATEGORY_EXPENSES))
			expensesInTable = financesList.getExpensesByMonthAndCategory(Month.name(monthChoice.getSelectedItem()), categoryChoice.getSelectedItem());
		
		expensesInTable.sort((e1, e2) -> Float.compare(e1.getValue(), e2.getValue()));
		
	    // Parar a edição da célula atual, se houver
	    if (table.isEditing()) {
	        TableCellEditor cellEditor = table.getCellEditor();
	        if (cellEditor != null)
	            cellEditor.stopCellEditing();
	    }

		defaultTableModel.setRowCount(0);
		defaultTableModel.setRowCount(expensesInTable.size() + 1);
		int line = 0;
		
		for(Expense expense : expensesInTable) {
			defaultTableModel.setValueAt(expense.getExpenseDate().format(Expense.EXPENSE_DATE_FORMAT), line, 0);
			defaultTableModel.setValueAt(String.format("%02d", expense.getPayDay().getDayOfMonth()), line, 1);
			defaultTableModel.setValueAt(expense.getFormPayment().getName(), line, 2);
			defaultTableModel.setValueAt(expense.getDescription(), line, 3);
			defaultTableModel.setValueAt(String.format("%,.2f", expense.getValue()), line, 4);
			defaultTableModel.setValueAt(expense.isPaid(), line++, 5);
		}
		
		pieChart = null;
		barChart = null;
		if(isPieChart)
			updateGraphPanel(getPieChart());
		else
			updateGraphPanel(getBarChart());
		table.setFocusable(true);
	}

	/**
	 * Formata um valor monetário (em reais) para uma string formatada.
	 *
	 * @param value o valor monetário a ser formatado
	 * @return a string formatada representando o valor monetário
	 */
	private String formatMonetaryValues(float value) {
		return String.format("R$ %,.2f", value);
	}

	/**
	 * Importa os dados dos arquivos CSV selecionados pelo usuário, valida e persiste os mesmo no Banco de Dados.
	 * Exibe um relatório ao final da importação.
	 * 
	 * @see {@link IgFileChooser}
	 * @see {@link Reader}
	 */
	private void importFiles() {
		List<String> files = IgFileChooser.openFileDialog(this, TITLE_FILE_CHOOSER);
		StringBuilder log = new StringBuilder();
		writeTextArea(null);
		
		if(files == null) return;
		
		for(String fileName : files) {
			List<Line> lines = Reader.read(fileName, SEMI_COLON);
			int index = 0,
					totalImported = 0;
			
			if(lines == null || lines.size() == 0) {
				log.append(String.format(LOG_EMPTY_FILE, fileName.substring(fileName.lastIndexOf(File.separator) + 1)));
				continue;
			}
			
			log.append(String.format(LOG_MAIN_STATUS, fileName.substring(fileName.lastIndexOf(File.separator) + 1)));
			
			if(identifyFileDataTypeByHeader(lines.get(0)) == null)
				log.append(LOG_INVALID_CSV_HEADER);
			else
				lines.remove(index); // Remove o cabeçalho

			int size = lines.size();
			for(; index < size; index++) {
				Line line = lines.get(index);
				FILE fileType = identifyDataByLine(line);
				if(fileType != null) {
					switch (fileType) {
						case REVENUE:
							totalImported += (checkAndImportRevenues(index + 1, line));
							break;
						case EXPENSE:
							totalImported += (checkAndImportExpenses(index + 1, line));
							break;
						case INVESTMENT:
							totalImported += (checkAndImportInvestments(index + 1, line));
							break;
					}
				} else
					writeTextArea(String.format(LOG_INVALID_CSV_LINE, index + 1));
			}
			
			log.append(String.format(LOG_STATUS, totalImported));
			log.append(String.format("%s\n\n", getTextArea().getText()));
			writeTextArea(null);
		}
		
		getTextArea().setText(log.toString());
		JOptionPane.showMessageDialog(null, new JScrollPane(getTextArea()), TITLE_IMPORT_LOGS, JOptionPane.INFORMATION_MESSAGE);
		writeTextArea(null);
		
		updateInterface();
	} // importFiles()


	/**
	 * Verifica e instancia receitas a partir de uma lista de linhas.
	 * 
	 * @param startLine O número da linha inicial para referência em logs
	 * @param lines As linhas contendo os dados a serem importados
	 * 
	 * @return O número de receitas importadas com sucesso
	 */
	private int checkAndImportRevenues(int startLine, Line... lines) {
		Revenue revenue;
		int count = 0;
		
		for(int i = 0; i < lines.length; i++) {
			if((revenue = getRevenueFromline(lines[i])) == null) {
				writeTextArea(String.format(LOG_INVALID_DATA, startLine++));
				continue;
			}
				
			if(persistRevenue(revenue) == 0)
				writeTextArea(String.format(LOG_DATA_ALREADY_EXISTS, startLine++));
			else
				count++;
		}
		
		return count;
	} // checkAndImportRevenues()
	
	/**
	 * Verifica e instancia despesas a partir de uma lista de linhas.
	 * 
	 * @param startLine O número da linha inicial para referência em logs
	 * @param lines As linhas contendo os dados a serem importados
	 * 
	 * @return O número de despesas importadas com sucesso
	 */
	private int checkAndImportExpenses(int startLine, Line... lines) {
		Expense expense;
		int count = 0;
		
		for(int i = 0; i < lines.length; i++) {
			if((expense = getExpenseFromline(lines[i])) == null) {
				writeTextArea(String.format(LOG_INVALID_DATA, startLine++));
				continue;
			}
				
			if(persistExpense(expense) == 0)
				writeTextArea(String.format(LOG_DATA_ALREADY_EXISTS, startLine++));
			else
				count++;
		}
		
		return count;
	} // checkAndImportExpenses()

	/**
	 * Verifica e instancia investimentos a partir de uma lista de linhas.
	 * 
	 * @param startLine O número da linha inicial para referência em logs
	 * @param lines As linhas contendo os dados a serem importados
	 * 
	 * @return O número de investimentos importados com sucesso
	 */
	private int checkAndImportInvestments(int startLine, Line... lines) {
		Investment investment;
		int count = 0;
		
		for(int i = 0; i < lines.length; i++) {
			if((investment = getInvestmentFromline(lines[i])) == null) {
				writeTextArea(String.format(LOG_INVALID_DATA, startLine++));
				continue;
			}
				
			if(persistInvestment(investment) == 0)
				writeTextArea(String.format(LOG_DATA_ALREADY_EXISTS, startLine++));
			else
				count++;
		}
		
		return count;
	} // checkAndImportExpenses()
	

	/**
	 * Persiste uma ou mais instâncias de {@code Revenue} (Receitas) no banco de dados.
	 * 
	 * @param revenues as receitas a serem persistidas
	 * 
	 * @return número de receitas persistidas com sucesso.
	 * 
	 * @throws SQLException caso ocorra algum erro ao persistir algum dos dados
	 */
	private long persistRevenue(Revenue... revenues) {
		RevenueDAO dao = new RevenueDAO(dataBase.getConnection());
		long count = 0;
		for(Revenue revenue : revenues)
			if(dao.insert(revenue)) count++;
		return count;
	}
	
	
	/**
	 * Persiste uma ou mais instâncias de {@code Expense} (Despesa) no banco de dados.
	 * 
	 * @param expenses as despesas a serem persistidas
	 * 
	 * @return número de despesas persistidas com sucesso.
	 * 
	 * @throws SQLException caso ocorra algum erro ao persistir algum dos dados
	 */
	private long persistExpense(Expense... expenses) {
		ExpenseDAO dao = new ExpenseDAO(dataBase.getConnection());
		long count = 0;
		for(Expense expense : expenses)
			if(dao.insert(expense)) count++;
		return count;
	}
	
	/**
	 * Persiste uma ou mais instâncias de {@code Investment} (Investimento) no banco de dados.
	 * 
	 * @param investments investimentos a serem persistidos
	 * 
	 * @return número de investimentos persistidos com sucesso.
	 * 
	 * @throws SQLException caso ocorra algum erro ao persistir algum dos dados
	 */
	private long persistInvestment(Investment... investments) {
		InvestmentDAO dao = new InvestmentDAO(dataBase.getConnection());
		long count = 0;
		for(Investment Investment : investments)
			if(dao.insert(Investment)) count++;
		return count;
	}

	/**
	 * Obtém os dados do banco de dados.
	 * 
	 * Obtém as receitas, despesas e investimentos do banco de dados
	 * e adiciona-os à lista de finanças.
	 */
	private void getDataFromDataBase() {
		financesList.addRevenue(true, new RevenueDAO(dataBase.getConnection()).selectAll());
		financesList.addExpense(true, new ExpenseDAO(dataBase.getConnection()).selectAll());
		financesList.addInvestment(true, new InvestmentDAO(dataBase.getConnection()).selectAll());
	}

	/**
	 * Retorna o painel do gráfico de pizza.
	 * 
	 * @return o painel do gráfico de pizza
	 * 
	 * @see {@link PieChartPanel}
	 */
	private PieChartPanel getPieChart() {
		pieChart = new PieChartPanel("", createPieChartDataset());
		isPieChart = true;
		return pieChart;
	}
	
	
	/**
	 * Retorna o painel do gráfico de pizza.
	 * 
	 * @return o painel do gráfico de pizza
	 * 
	 * @see {@link PieChartPanel}
	 */
	private VerticalBarChartPanel getBarChart() {
		barChart = new VerticalBarChartPanel("", createBarChartDataset());
		isPieChart = false;
		return barChart;
	}


	/**
	 * Atualiza o painel do gráfico com um novo painel.
	 * Remove todos os componentes do painel atual e adiciona o novo painel fornecido.
	 * Se o novo painel for nulo, nenhum painel será adicionado.
	 * 
	 * 
	 * @param <T> um tipo genérico que estende JPanel
	 * @param newPanel o novo painel a ser adicionado ao painel do gráfico
	 */
	private <T extends JPanel> void  updateGraphPanel(T newPanel) {
		graphPanel.removeAll();
		
		if(newPanel != null)
			graphPanel.add(newPanel);
		
		graphPanel.revalidate();
		graphPanel.repaint();
	}
	
	
	/**
	 * Gera um conjunto de dados para o gráfico de pizza.
	 * 
	 * @return o conjunto de dados
	 * 
	 * @see {@link PieDataset}
	 */
    private PieDataset createPieChartDataset() {
        final DefaultPieDataset dataset = new DefaultPieDataset();
        
        float total = financesList.totalValuesRevenues(Month.name(monthChoice.getSelectedItem())),
        		spending = 0;
        
        Map<String, Float> categories = financesList.getTotalValuesByCategories(Month.name(monthChoice.getSelectedItem()));
        for(String category : financesList.getAllExpensesCategorys(false)) {
        	float value = categories.get(category);
        	spending += value;
        	if(value > 0) {
        		if(total > 0)
        			dataset.setValue(category, value / total * 100);
        		else
        			dataset.setValue(category, value);
        	}
        }

        spending = total - spending;
        if(spending > 0)
        	dataset.setValue("Receita Restante", spending / total * 100);
        
        return dataset;
    }
    
	/**
	 * Gera um conjunto de dados para o gráfico de barras.
	 * 
	 * @return o conjunto de dados
	 * 
	 * @see {@link CategoryDataset}
	 */
    private CategoryDataset createBarChartDataset() {
        final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        
        float total = financesList.totalValuesRevenues(Month.name(monthChoice.getSelectedItem())),
        		spending = 0;
        
        Map<String, Float> categories = financesList.getTotalValuesByCategories(Month.name(monthChoice.getSelectedItem()));
        for(String category : financesList.getAllExpensesCategorys(false)) {
        	float value = categories.get(category);
        	spending += value;
        	if(value > 0) {
        		if(total > 0)
        			dataset.addValue(value / total * 100, category, "");
        		else
        			dataset.addValue(value, category, "");
        	}
        }
        
        spending = total - spending;
        if(spending > 0)
        	dataset.addValue(spending / total * 100, "Receita Restante", "");
        
        return dataset;
    }

	
	/**
 	 * Altera todas as opções de um {@code Choice}. As opções anteriores serão removidas, caso existam.
	 * 
	 * @param choice {@code Choice} que terá as opções alteradas.
	 * @param options opções que serão adicionadas.
	 * 
	 * @see {@link java.awt.Choice}
	 */
	private void setChoiceOptions(Choice choice, String... options) {
		choice.removeAll();
		
		for(String option : options)
			choice.add(option);
	}

	
	/**
	 * Muda a aparência da interface gráfica definindo um novo look-and-feel.
	 * 
	 * @param lookAndFeelName nome do novo look-and-feel.
	 * 
	 * @return {@code true} caso a mudança seja efetivada ou {@code false} caso não.
	 * 
	 * @see {@link javax.swing.UIManager.LookAndFeelInfo}
	 */
	private boolean changeLookAndFeel(String lookAndFeelName) {
		for (LookAndFeelInfo lookAndFeel : lookAndFeelInfo) 
			if (lookAndFeelName.equalsIgnoreCase(lookAndFeel.getName())) 
				try {  
					// Carrega o look-and-feel a ser usado pela GUI. 
					UIManager.setLookAndFeel(lookAndFeel.getClassName());

					// Ativa a aparência da GUI alterando o seu look-and-feel.
					SwingUtilities.updateComponentTreeUI(this);

				} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
					return false;
				}
		return true;
	}


	/**
	 * Finaliza o programa.
	 */
	private void finalizeFinancialPlanning() {
		try {
			dataBase.close();
		} catch (SQLException e) {}
		
		System.exit(0);
	} 
	
	/**
	 * Renderizador personalizado para exibir uma coluna de caixas de seleção em uma tabela.
	 * 
	 * @author Vinícius J P Silva
	 *
	 */
	private class CheckBoxCell extends DefaultTableCellRenderer {
	    private static final long serialVersionUID = 1L;

	    private final JCheckBox checkBox;

	    /**
	     * Cria um novo renderizador de caixa de seleção.
	     */
	    public CheckBoxCell() {
	        checkBox = new JCheckBox();
	        checkBox.setHorizontalAlignment(JCheckBox.CENTER);
	        checkBox.setBorderPainted(true);
	        checkBox.setBackground(STD_BG_COLOR);
	        checkBox.setBorder(BorderFactory.createCompoundBorder(
					BorderFactory.createLineBorder(Color.GRAY, 1), // Utiliza a borda InternalFrameBorder para obter uma linha mais fina
			        BorderFactory.createEmptyBorder(1, 1, 1, 1) // Adiciona um espaçamento interno para a borda
			    ));
	    }

	    @Override
	    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
	        if (column == table.getColumnCount() - 1) { // Última coluna
	            checkBox.setSelected(value != null && (boolean) value);
	            return checkBox;
	        } else {
	            return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
	        }
	    }
	}
} // class IgFinancialPlanning
