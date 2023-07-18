package vjps.bloomcapital.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import vjps.bloomcapital.finance.expense.Expense;
import vjps.bloomcapital.finance.expense.FormPayment;

public class ExpenseDAO implements DataAccessObject<Expense> {

	private Connection connection;
	
	public ExpenseDAO(Connection connection) {
		this.connection = connection;
	}

	@Override
	public boolean insert(Expense data) {
		final String query = "INSERT INTO orcamento (mes_ano, cod_despesa, data_despesa, data_pagamento, cod_forma_pagamento, valor, situacao) VALUES (?, ?, ?, ?, ?, ?, ?)";

	    if (data == null) return false;

	    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {

	        // Insere a descrição da despesa na tabela "despesa"
	        Long code = getExpenseCodeByDescription(data.getDescription());

	        if (code == null)
	            if ((code = insertExpense(data.getDescription(), data.getCategory())) == null)
	                return false;

	        // Insere os detalhes da despesa na tabela "orcamento"
	        preparedStatement.setString(1, data.getPayDay().format(DateTimeFormatter.ofPattern("MM/yyyy")));
	        preparedStatement.setLong(2, code);
	        preparedStatement.setObject(3, data.getExpenseDate());
	        preparedStatement.setObject(4, data.getPayDay());
	        preparedStatement.setLong(5, getFormPaymentCodeByDescription(data.getFormPayment().getDescription()));
	        preparedStatement.setFloat(6, data.getValue());
	        preparedStatement.setBoolean(7, data.isPaid());
	        preparedStatement.executeUpdate();

	        // Atualiza o código da despesa com o valor gerado
	        data.setCode(code);

	        return true;
	    } catch (SQLException e) {
	        return false;
	    }
	}

	@Override
	public boolean delete(Expense data) {
		String query = "DELETE FROM orcamento WHERE cod_despesa=? AND mes_ano=?";
		
		try(PreparedStatement preparedStatement = connection.prepareStatement(query)) {
			
			// Insere a descrição da receita na tabela "renda"
	        Long code = getExpenseCodeByDescription(data.getDescription());
	        if(code == null) return false;
	        
			preparedStatement.setLong(1, code);
			preparedStatement.setObject(2, data.getExpenseDate().format(DateTimeFormatter.ofPattern("MM/yyyy")));
			
			return preparedStatement.execute();
			
		} catch (SQLException e) {
			return false;
		}
	}

	@Override
	public boolean update(Expense data) {
		final String query = "UPDATE orcamento SET cod_despesa = ?, data_despesa = ?, data_pagamento = ?, cod_forma_pagamento = ?, valor = ?, situacao = ? WHERE mes_ano = ? AND cod_despesa = ?";

		try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
			
	        Long code = getExpenseCodeByDescription(data.getDescription());

	        if (code == null)
	            if ((code = insertExpense(data.getDescription(), data.getCategory())) == null)
	                return false;
			
	        preparedStatement.setLong(1, code);
		    preparedStatement.setObject(2, data.getExpenseDate());
		    preparedStatement.setObject(3, data.getPayDay());
		    preparedStatement.setLong(4, getFormPaymentCodeByDescription(data.getFormPayment().getDescription()));
		    preparedStatement.setFloat(5, data.getValue());
		    preparedStatement.setBoolean(6, data.isPaid());
		    preparedStatement.setString(7, data.getPayDay().format(DateTimeFormatter.ofPattern("MM/yyyy")));
		    preparedStatement.setLong(8, data.getCode());
		    
		    if(preparedStatement.executeUpdate() >= 1)
		    	return true;
		    else
		    	return false;
		    		
		} catch (SQLException e) {
			e.printStackTrace();
		    return false;
		}
	}

	@Override
	public Expense select(Long code) {
	    String query = "SELECT orcamento.*, despesa.descricao, categoria.descricao AS categoria, forma_pagamento.descricao AS descricao_forma_pagamento FROM orcamento JOIN despesa ON orcamento.cod_despesa = despesa.codigo JOIN categoria ON orcamento.cod_despesa = despesa.codigo AND despesa.cod_categoria = categoria.codigo JOIN forma_pagamento ON orcamento.cod_forma_pagamento = forma_pagamento.codigo WHERE orcamento.cod_despesa = ?";

	    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
	        preparedStatement.setLong(1, code);

	        ResultSet resultSet = preparedStatement.executeQuery();
	        if (resultSet.next())
	            return new Expense(code, resultSet.getString("descricao"), resultSet.getFloat("valor"), resultSet.getDate("data_despesa").toLocalDate(), 
	            		resultSet.getDate("data_pagamento").toLocalDate(), FormPayment.getByDescription(resultSet.getString("descricao_forma_pagamento")), 
	            		resultSet.getString("categoria"), resultSet.getBoolean("situacao"));
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return null;
	}

	@Override
	public List<Expense> selectAll() {
	    String query = "SELECT orcamento.*, despesa.descricao, categoria.descricao AS categoria, forma_pagamento.descricao AS descricao_forma_pagamento FROM orcamento JOIN despesa ON orcamento.cod_despesa = despesa.codigo JOIN categoria ON orcamento.cod_despesa = despesa.codigo AND despesa.cod_categoria = categoria.codigo JOIN forma_pagamento ON orcamento.cod_forma_pagamento = forma_pagamento.codigo";

		 List<Expense> expenses = new ArrayList<>();
		
		 try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
		     ResultSet resultSet = preparedStatement.executeQuery();
		     while (resultSet.next()) {
		         expenses.add(new Expense(resultSet.getLong("cod_despesa"), resultSet.getString("descricao"), resultSet.getFloat("valor"), resultSet.getDate("data_despesa").toLocalDate(), 
		            		resultSet.getDate("data_pagamento").toLocalDate(), FormPayment.getByDescription(resultSet.getString("descricao_forma_pagamento")), 
		            		resultSet.getString("categoria"), resultSet.getBoolean("situacao")));
		     }
		 } catch (SQLException | NullPointerException e) {
		 }
		
		 return expenses;
	}
	
	public Long insertExpense(String description, String category) {
	    if (description.isBlank() || category.isBlank()) return null;

	    String insertExpenseQuery = "INSERT INTO despesa (descricao, cod_categoria) VALUES (?, ?)";
	    String insertCategoryQuery = "INSERT INTO categoria (descricao) VALUES (?)";

	    Long categoryCode = getCategoryCodeByDescription(category);

	    // Insere a categoria na tabela "categoria" se ainda não existir
	    if (categoryCode == null) {
	        try (PreparedStatement categoryStatement = connection.prepareStatement(insertCategoryQuery, Statement.RETURN_GENERATED_KEYS)) {
	            categoryStatement.setString(1, category);
	            categoryStatement.executeUpdate();

	            ResultSet generatedCategoryCodes = categoryStatement.getGeneratedKeys();
	            if (generatedCategoryCodes.next())
	                categoryCode = generatedCategoryCodes.getLong(1);
	        } catch (SQLException e) {
	            return null;
	        }
	    }

	    // Insere a descrição da despesa na tabela "despesa"
	    try (PreparedStatement expenseStatement = connection.prepareStatement(insertExpenseQuery, Statement.RETURN_GENERATED_KEYS)) {
	        expenseStatement.setString(1, description);
	        expenseStatement.setLong(2, categoryCode);
	        expenseStatement.executeUpdate();

	        ResultSet generatedExpenseCodes = expenseStatement.getGeneratedKeys();
	        if (generatedExpenseCodes.next())
	            return generatedExpenseCodes.getLong(1);
	    } catch (SQLException e) {
	    }
	    return null;
	}

	public Long getExpenseCodeByDescription(String description) {
	    String query = "SELECT codigo FROM despesa WHERE descricao = ?";
	    try (PreparedStatement statement = connection.prepareStatement(query)) {
	        statement.setString(1, description);
	        ResultSet resultSet = statement.executeQuery();
	        if (resultSet.next())
	            return resultSet.getLong("codigo");
	    } catch (SQLException e) {
	    }
	    return null;
	}

	public Long getCategoryCodeByDescription(String description) {
	    String query = "SELECT codigo FROM categoria WHERE descricao = ?";
	    try (PreparedStatement statement = connection.prepareStatement(query)) {
	        statement.setString(1, description);
	        ResultSet resultSet = statement.executeQuery();
	        if (resultSet.next())
	            return resultSet.getLong("codigo");
	    } catch (SQLException e) {
	    }

	    // Caso a categoria não exista, cria uma nova
	    return insertCategory(description);
	}

	public Long getFormPaymentCodeByDescription(String description) {
	    String query = "SELECT codigo FROM forma_pagamento WHERE descricao = ?";
	    try (PreparedStatement statement = connection.prepareStatement(query)) {
	        statement.setString(1, description);
	        ResultSet resultSet = statement.executeQuery();
	        if (resultSet.next())
	            return resultSet.getLong("codigo");
	    } catch (SQLException e) {
	    }

	    // Caso a forma de pagamento não exista, cria uma nova
	    return insertFormPayment(description);
	}


	public Long insertCategory(String description) {
	    if (description.isBlank()) return null;

	    String query = "INSERT INTO categoria (descricao) VALUES (?)";

	    try (PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
	        preparedStatement.setString(1, description);
	        preparedStatement.executeUpdate();

	        ResultSet generatedCodes = preparedStatement.getGeneratedKeys();
	        if (generatedCodes.next())
	            return generatedCodes.getLong(1);
	    } catch (SQLException e) {
	    }
	    return null;
	}

	public Long insertFormPayment(String description) {
	    if (description.isBlank()) return null;

	    String query = "INSERT INTO forma_pagamento (descricao) VALUES (?)";

	    try (PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
	        preparedStatement.setString(1, description);
	        preparedStatement.executeUpdate();

	        ResultSet generatedCodes = preparedStatement.getGeneratedKeys();
	        if (generatedCodes.next())
	            return generatedCodes.getLong(1);
	    } catch (SQLException e) {
	    }
	    return null;
	}

} // class ExpenseDAO
