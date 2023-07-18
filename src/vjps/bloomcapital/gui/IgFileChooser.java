package vjps.bloomcapital.gui;

import static vjps.bloomcapital.utility.Constants.MSG_CSV_FILES;
import static vjps.bloomcapital.utility.Constants.MSG_OPEN_FILE;
import static vjps.bloomcapital.utility.Constants.TIP_BTN_OPEN_FILE;

import java.awt.Component;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Classe responsável por gerar uma interface gráfica que fornece ao usuário acesso aos arquivos CSV do sistema.
 * 
 * @author Vinícius José Pires Silva
 * 
 * @see {@link JFileChooser}
 */
public class IgFileChooser {
	
	private static JFileChooser fileChooser;
	
	
	/**
	 * Cria (se necessário) e retorna uma instância do JFileChooser para seleção de arquivos.
	 * 
	 * @return instância do JFileChooser
	 * 
	 * @see {@link JFileChooser}
	 */
	private static JFileChooser getFileChooser() {
		return(fileChooser == null) ? fileChooser = new JFileChooser() : fileChooser;
	}
	
	
	/**
	 * Exibe uma caixa de diálogo para seleção de arquivos CSV.
	 * 
	 * @param window a janela pai para exibição da caixa de diálogo
	 * @param title o título da caixa de diálogo
	 * @return uma lista de caminhos de arquivo selecionados pelo usuário, ou null se a operação for cancelada
	 * 
	 * @see {@link JFileChooser}
	 */
	public static List<String> openFileDialog(Component window, String title) {
		setProperties(title, TIP_BTN_OPEN_FILE, KeyEvent.VK_A);
		
		// Exibe a caixa de diálogo "Abrir Arquivo".
		int opcao = fileChooser.showDialog(window, MSG_OPEN_FILE);
		
		// Verifica se o usuário cancelou a operação; se não, obtém o nome do arquivo digitado ou selecionad pelo usuário na caixa de diálogo.
		try {
			 if (opcao == JFileChooser.APPROVE_OPTION) {
		            File[] selectedFiles = fileChooser.getSelectedFiles();
		            List<String> filePaths = new ArrayList<>();
		            for (File file : selectedFiles) {
		                filePaths.add(file.getCanonicalPath());
		            }
		            return filePaths;
		        } else {
		            return null;
		        }
		} catch(Exception e) {
			return null;
		}
	}

	
	/**
	 * Define as propriedades da caixa de diálogo JFileChooser.
	 * 
	 * @param title o título
	 * @param approveButtonToolTipText o texto de ajuda para o botão principal
	 * @param mnemonic a letra mnemônica para o botão principal da caixa de diálogo
	 * 
	 * @see {@link JFileChooser}
	 */
	private static void setProperties(String title, String approveButtonToolTipText, int mnemonic) {
		final String CURRENT_PATH = ".", CSV_EXTENSION = "csv";
		
		if(fileChooser == null) {
			fileChooser = getFileChooser();

			// Indica que o usuário poderá selecionar apenas nomes de arquivos.
			fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

			// Define qual é o diretório default.
			fileChooser.setCurrentDirectory(new File(CURRENT_PATH));

			// Cria os filtros de extensão de arquivo que serão usados pelo JFileChooser para filtrar os tipos de arquivos que serão exibidos na janela.
			FileNameExtensionFilter txtExtensionFilter = new FileNameExtensionFilter(MSG_CSV_FILES, CSV_EXTENSION);
			
			// O último tipo de arquivo acrescentado ao JFileChooser é considerado o filtro (ou tipo) default quando se usa o método abaixo.
			fileChooser.setFileFilter(txtExtensionFilter);
			
		}
		
		// Define o título da caixa de diálogo.
		fileChooser.setDialogTitle(title);
		
		// Define um texto de ajuda para o botão principal.
		fileChooser.setApproveButtonToolTipText(approveButtonToolTipText);
		
		// Define uma letra mnemônica texto de ajuda para o botão principal da caixa de diálogo.
		fileChooser.setApproveButtonMnemonic(mnemonic);
		
		// Permite seleção de vários arquivos
        fileChooser.setMultiSelectionEnabled(true);
		
	}// definirPropriedades()
	
}// class IgFileChooser