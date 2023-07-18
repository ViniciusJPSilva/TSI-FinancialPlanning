package vjps.bloomcapital.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Representa e estabelece conexões seguras com o Banco de Dados (BD), 
 * possui métodos de acesso ao mesmo: insert, select, update e delete.
 * 
 * @author Vinicius J P Silva
 *
 * @see {@link java.sql.ResultSet}
 * @see {@link java.sql.Statement}
 * @see {@link java.sql.Connection} 
 * @see {@link java.lang.AutoCloseable}
 */
public class DataBase implements AutoCloseable {
	private String url, user, password;
	
	private Connection connection;
	private Statement statement;
	private ResultSet resultSet; 
	
	/**
	  * Obtém uma conexão com o banco de dados.
	  */
	public DataBase(String url, String user, String password) throws SQLException {
		this.url = url;
		this.user = user;
		this.password = password;
		
		connection = DriverManager.getConnection(url, user, password);
		statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
	} 

	public String getUrl() {
		return url;
	}

	public String getUser() {
		return user;
	}

	public String getPassword() {
		return password;
	}

	public Connection getConnection() {
		return connection;
	}
	
	public Statement getStatement() {
		return statement;
	}

	public ResultSet getResultSet() {
		return resultSet;
	}

	/** 
	 * Fecha a conexão com o banco de dados. 
	 */
	@Override
	public void close() throws SQLException { 
		if (statement != null) statement.close();
		if (connection != null) connection.close();
		if (resultSet != null) resultSet.close();
	} 
	
	/**
	 * Executa uma consulta tipo SELECT no BD.
	 * 
	 * @param Instruction instrução a ser executada.
	 * @return {@link ResultSet} - Tabela de dados com o resultado da consulta.
	 * 
	 * @throws SQLException Exceção caso ocorra algum erro ao executar a consulta.
	 * 
	 * @see {@link ResultSet}
	 */
	public ResultSet select(String instruction) throws SQLException {
		try { 
			return statement.executeQuery(instruction);
		} catch (SQLException sqlException) {
			throw new SQLException("Erro ao executar a consulta SQL SELECT.", sqlException);
		}	
	} 
	
	/**
	 * Executa uma consulta tipo INSERT, UPDATE ou DELETE no BD.
	 * 
	 * @param instruction Instrução a ser executada.
	 * @return {@code int} - Números de linhas resultantes da consulta ou zero (0) para instruções que não retornam resultados.
	 * 
	 * @throws SQLException Exceção caso ocorra algum erro ao executar a consulta.
	 */
	public int query(String instruction) throws SQLException {
		try {  
			return statement.executeUpdate(instruction);
		} catch (SQLException sqlException) {
			throw new SQLException("Erro ao executar a consulta SQL INSERT, UPDATE ou DELETE.", sqlException);
		}
	}
	
} // class DataBase
