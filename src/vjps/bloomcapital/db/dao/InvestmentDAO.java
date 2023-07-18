package vjps.bloomcapital.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import vjps.bloomcapital.finance.investment.Investment;

public class InvestmentDAO implements DataAccessObject<Investment> {

	private Connection connection;
	
	public InvestmentDAO(Connection connection) {
		this.connection = connection;
	}

	@Override
	public boolean insert(Investment data) {
		String query = "INSERT INTO investimento (objetivo, estrategia, nome, valor_investido, posicao, rendimento_bruto, rentabilidade, vencimento) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
		if(data == null) return false;
		
		try(PreparedStatement preparedStatement = connection.prepareStatement(query)){
			
			preparedStatement.setString(1, data.getGoal());
			preparedStatement.setString(2, data.getStrategy());
			preparedStatement.setString(3, data.getName());
			preparedStatement.setFloat(4, data.getAmountInvested());
			preparedStatement.setFloat(5, data.getPosition());
			preparedStatement.setFloat(6, data.getGrossIncome());
			preparedStatement.setFloat(7, data.getProfitability());
			preparedStatement.setObject(8, data.getMaturityDate());
			
			preparedStatement.executeUpdate();
			return true;
		} catch (SQLException e) {
		}
		return false;
	}

	@Override
	public boolean delete(Investment data) {
		return false;
	}

	@Override
	public boolean update(Investment data) {
		return false;
	}

	@Override
	public Investment select(Long code) {
		return null;
	}

	@Override
	public List<Investment> selectAll() {
		String query = "SELECT * FROM investimento";
		
		List<Investment> investments = new ArrayList<>();
		
		 try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
		     ResultSet resultSet = preparedStatement.executeQuery();
		     while (resultSet.next()) {
		         investments.add(new Investment(resultSet.getLong("codigo"), resultSet.getString("objetivo"), resultSet.getString("estrategia"), resultSet.getString("nome"), 
		        		 resultSet.getFloat("valor_investido"), resultSet.getFloat("posicao"), resultSet.getFloat("rendimento_bruto"), resultSet.getFloat("rentabilidade"), 
		        		 resultSet.getDate("vencimento").toLocalDate()));
		     }
		 } catch (SQLException e) {
		 }
		
		 return investments;
	}
	
} // class InvestmentDAO
