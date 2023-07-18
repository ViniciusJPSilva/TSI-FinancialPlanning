package vjps.bloomcapital.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import vjps.bloomcapital.finance.revenue.Revenue;

public class RevenueDAO implements DataAccessObject<Revenue> {
	
	private Connection connection;

	public RevenueDAO(Connection connection) {
		this.connection = connection;
	}

	@Override
	public boolean insert(Revenue data){
	    String query = "INSERT INTO renda_mensal (cod_renda, data, valor) VALUES (?, ?, ?)";

	    if (data == null) return false;
	    
	    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {

	        // Insere a descrição da receita na tabela "renda"
	        Long code = getIncomeCodeByDescription(data.getDescription());
	        
	        if(code == null) 
	        	if((code = insertIncome(data.getDescription())) == null) 
	        		return false;

            // Insere os detalhes da receita na tabela "renda_mensal"
            preparedStatement.setLong(1, code);
            preparedStatement.setObject(2, data.getData());
            preparedStatement.setFloat(3, data.getValue());
            preparedStatement.executeUpdate();

            // Atualiza o código da receita com o valor gerado
            data.setCode(code);

            return true;
	    } catch (SQLException e) {
	    	return false;
		}
	}

	@Override
	public boolean delete(Revenue data) {
		if(data == null) return false;
		String query = "DELETE FROM renda_mensal WHERE cod_renda=? AND data=?";
		
		try(PreparedStatement preparedStatement = connection.prepareStatement(query)) {
			
			// Insere a descrição da receita na tabela "renda"
	        Long code = getIncomeCodeByDescription(data.getDescription());
	        if(code == null) return false;
	        
			preparedStatement.setLong(1, code);
			preparedStatement.setObject(2, data.getData());
			
			return preparedStatement.execute();
			
		} catch (SQLException e) {
			return false;
		}
	}

	@Override
	public boolean update(Revenue data) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Revenue select(Long code) {
		String query = "SELECT renda_mensal.*, renda.descricao FROM renda_mensal JOIN renda ON renda_mensal.cod_renda = renda.codigo WHERE renda_mensal.cod_renda = ?";
		
		try(PreparedStatement preparedStatement = connection.prepareStatement(query)) {
			preparedStatement.setLong(1, code);
			
			ResultSet resultSet = preparedStatement.executeQuery();
			if(resultSet.next())
				return new Revenue(resultSet.getLong("cod_renda"), 
						resultSet.getString("descricao"), 
						resultSet.getFloat("valor"), 
						resultSet.getDate("data").toLocalDate());
		} catch (SQLException e) {
		}
		return null;
	}

	@Override
	public List<Revenue> selectAll() {
		String query = "SELECT renda_mensal.*, ( SELECT descricao FROM renda WHERE codigo = renda_mensal.cod_renda ) AS descricao FROM renda_mensal";
		
		try(PreparedStatement preparedStatement = connection.prepareStatement(query)) {			
			ResultSet resultSet = preparedStatement.executeQuery();
			List<Revenue> revenueList = new ArrayList<>();
			while(resultSet.next())
				revenueList.add(new Revenue(resultSet.getLong("cod_renda"), 
							resultSet.getString("descricao"), 
							resultSet.getFloat("valor"), 
							resultSet.getDate("data").toLocalDate())
						);
			return revenueList;
		} catch (SQLException e) {
		}
		return null;
	}
	
	public Long insertIncome(String description) {
		if(description.isBlank()) return null;
		
		String query = "INSERT INTO renda (descricao) VALUES (?)";
		
		try(PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
			preparedStatement.setString(1, description);
			preparedStatement.executeUpdate();
			
			ResultSet generatedCodes = preparedStatement.getGeneratedKeys();
			if(generatedCodes.next())
				return generatedCodes.getLong(1);
		} catch (SQLException e) {
		}
		return null;
	}

    public Long getIncomeCodeByDescription(String description) {
    	String query = "SELECT codigo FROM renda WHERE descricao = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)){
            statement.setString(1, description);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next())
                return resultSet.getLong("codigo");
        } catch (SQLException e) {
        }
		return null;
    } // getIncomeCodeByDescription()

} // class RevenueDAO
