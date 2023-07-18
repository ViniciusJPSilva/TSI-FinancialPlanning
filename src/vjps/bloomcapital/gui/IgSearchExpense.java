package vjps.bloomcapital.gui;


import static vjps.bloomcapital.utility.Constants.BTN_CLOSE;
import static vjps.bloomcapital.utility.Constants.BTN_NEXT_EXPENSE;
import static vjps.bloomcapital.utility.Constants.LB_SEARCH_FOR;
import static vjps.bloomcapital.utility.Constants.LB_SEARCH_ITEM;
import static vjps.bloomcapital.utility.Constants.MSG_DATA_NOT_FOUND;
import static vjps.bloomcapital.utility.Constants.RADIO_BTN_DATE;
import static vjps.bloomcapital.utility.Constants.RADIO_BTN_DESCRIPTION;
import static vjps.bloomcapital.utility.Constants.RADIO_BTN_VALUE;
import static vjps.bloomcapital.utility.Constants.TITLE_SEARCH_EXPENSE;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTable;
import javax.swing.JTextField;

import mos.io.InputOutput;

/**
 * Janela de busca de despesas.
 * Permite ao usuário realizar pesquisas e filtrar despesas com base em critérios específicos.
 * 
 * @author Vinícius J P Silva
 */
public class IgSearchExpense extends JDialog {
	
	public final Font STD_FONT = new Font("Tahoma", Font.PLAIN, 13)
			;
	
	public final Color STD_BG_COLOR = new Color(255, 255, 255),
			HEADER_FOREGROUND_COLOR = new Color(0, 0, 160)
			;
	
	private static final long serialVersionUID = 1L;
	
	private JTextField itemTextField;
	private int selectedRadioButton;
	private int selectedLine;
	private boolean find;
	
	private final ButtonGroup buttonGroup = new ButtonGroup();

	/**
	 * Cria e exibe a GUI
	 */
	public IgSearchExpense(Component component, JTable table) {
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				dispose();
			}
			
			@Override
			public void windowActivated(WindowEvent e) {
				itemTextField.requestFocus();
				changeParameters(selectedRadioButton);
			}
		});
		
		Container contentPane = getContentPane();
		contentPane.setLayout(null);
		contentPane.setBackground(STD_BG_COLOR);
		selectedRadioButton = 1;
		
		// Botões 
		JButton closeButton = new JButton(BTN_CLOSE);
		JButton nextExpenseButton = new JButton(BTN_NEXT_EXPENSE);
		
		closeButton.setFont(STD_FONT);
		nextExpenseButton.setFont(STD_FONT);
		
		closeButton.setMnemonic(KeyEvent.VK_F);
		nextExpenseButton.setMnemonic(KeyEvent.VK_P);
		
		closeButton.setBounds(358, 82, 90, 28);
		nextExpenseButton.setBounds(209, 82, 137, 28);
		
		closeButton.setBackground(STD_BG_COLOR);
		nextExpenseButton.setBackground(STD_BG_COLOR);
		
		contentPane.add(closeButton);
		contentPane.add(nextExpenseButton);
		
		// Definindo o botão default
		rootPane.setDefaultButton(nextExpenseButton);
		
		// RadioButtons
		JRadioButton dateRadioButton = new JRadioButton(RADIO_BTN_DATE);
		dateRadioButton.setBounds(97, 46, 52, 19);
		getContentPane().add(dateRadioButton);
		
		dateRadioButton.setFont(STD_FONT);
		
		buttonGroup.add(dateRadioButton);
		
		dateRadioButton.setMnemonic(KeyEvent.VK_D);
		JRadioButton descriptionRadioButton = new JRadioButton(RADIO_BTN_DESCRIPTION);
		descriptionRadioButton.setBounds(161, 46, 85, 19);
		getContentPane().add(descriptionRadioButton);
		descriptionRadioButton.setFont(STD_FONT);
		buttonGroup.add(descriptionRadioButton);
		descriptionRadioButton.setMnemonic(KeyEvent.VK_E);
		
		// Define o radioButton Descrição como o selecionado.
		descriptionRadioButton.setSelected(true);
		JRadioButton valueRadioButton = new JRadioButton(RADIO_BTN_VALUE);
		valueRadioButton.setBounds(258, 46, 55, 19);
		getContentPane().add(valueRadioButton);
		valueRadioButton.setFont(STD_FONT);
		buttonGroup.add(valueRadioButton);
		valueRadioButton.setMnemonic(KeyEvent.VK_V);
		
		// Labels
		JLabel itemLabel = new JLabel(LB_SEARCH_ITEM);
		itemLabel.setBounds(6, 10, 104, 19);
		getContentPane().add(itemLabel);
		itemLabel.setFont(STD_FONT);
		itemLabel.setDisplayedMnemonic(KeyEvent.VK_I);
		
		// TextFields
		itemTextField = new JTextField();
		itemTextField.setBounds(115, 6, 226, 28);
		getContentPane().add(itemTextField);
		itemTextField.setColumns(27);
		itemLabel.setLabelFor(itemTextField);
		
		JLabel findForLabel = new JLabel(LB_SEARCH_FOR);
		findForLabel.setFont(STD_FONT);
		findForLabel.setDisplayedMnemonic(KeyEvent.VK_I);
		findForLabel.setBounds(6, 46, 104, 19);
		getContentPane().add(findForLabel);
		valueRadioButton.addItemListener( (itemEvent)  -> changeParameters(2)); 
		descriptionRadioButton.addItemListener( (itemEvent)  -> changeParameters(1)); 
		
		// Trata a selecção de um radioButton
		dateRadioButton.addItemListener( (itemEvent)  -> changeParameters(0)); 
		

		// Tratando os eventos dos botões
		closeButton.addActionListener((event) -> dispose());
		nextExpenseButton.addActionListener((event) -> selectExpenseLine(table));
		
		// Janela
		setTitle(TITLE_SEARCH_EXPENSE);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		setSize(470, 155);
		setLocationRelativeTo(component);
		

		setResizable(false);
		setModal(true);
		setVisible(true);
	} // construtor

	
	/**
	 * Realiza uma varredura na tabela e seleciona (destaca) a linha correspondente ao conteúdo pesquisado, caso exista.
	 * @param table Tabela
	 */
	private void selectExpenseLine(JTable table) {
		String itemText = itemTextField.getText();
		if(itemText.isBlank()) return;
		
		int column = (selectedRadioButton == 0) ? 0 : selectedRadioButton + 2;
		
		for(int line = selectedLine + 1; line < table.getRowCount(); line++) {
			String tableText = (String) table.getValueAt(line, column);
			if(tableText != null)
				if(tableText.equalsIgnoreCase(itemText)) {
					table.setRowSelectionInterval(line, line);
					selectedLine = line;
					find = true;
					return;
				}
		}
		
		if(!find)
			InputOutput.showAlert(MSG_DATA_NOT_FOUND, TITLE_SEARCH_EXPENSE);
		
		table.clearSelection();
		changeParameters(selectedRadioButton);
	} // buscarDespesa()
	
	
	/**
	 * Altera os valores das variáveis utilizadas como parâmetros de pesquisa.
	 * @param valorRadioButton
	 */
	private void changeParameters(int valorRadioButton) {
		selectedRadioButton = valorRadioButton;
		selectedLine = -1;
		find = false;
		itemTextField.requestFocus();
	} // changeParameters()
} // class IgPesquisarDespesa