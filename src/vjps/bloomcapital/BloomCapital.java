package vjps.bloomcapital;

import static vjps.bloomcapital.utility.Constants.ERR_BD_ACCESS;
import static vjps.bloomcapital.utility.Constants.FINANCIAL_PLANNING;

import java.sql.SQLException;

import mos.io.InputOutput;
import vjps.bloomcapital.db.DataBase;
import vjps.bloomcapital.gui.IgBloomCapital;

/**
 * Classe responsável por inicializar a execução do software.
 * 
 * @author Vinicius J P Silva
 *
 */
public class BloomCapital {
	
	/* 
	 * Define a URL do banco de dados.
	 */
	private final String URL_BD = "jdbc:postgresql:financialplanning";

	/*
	 * Construtor
	 */
	public BloomCapital() {
		try {
			DataBase database = new DataBase(URL_BD, "dba", "fpdb@");
			new IgBloomCapital(database);
		} catch (SQLException e) {
			InputOutput.showError(ERR_BD_ACCESS, FINANCIAL_PLANNING);
			System.exit(0);			
		}
	} // BloomCapital()
	
	/*
	 * Inicializa o software
	 */
	public static void main(String[] args) {
		new BloomCapital();
	}

} // class BloomCapital
