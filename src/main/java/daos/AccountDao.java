package daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import bank.Account;
import config.ConnectionFactory;

public class AccountDao {

	public ArrayList<Account> listAll() throws SQLException, ClassNotFoundException {

		Connection con = null;
		ArrayList<Account> users = new ArrayList<>();

		try {
			con = new ConnectionFactory().getConnection();

			String sql = "SELECT * FROM account";

			Statement statement;
			statement = con.createStatement();

			ResultSet result = statement.executeQuery(sql);

			while (result.next()) {

				Account user = new Account();

				user.setName(result.getString(1));
				user.setPassword(result.getString(2));
				user.setAccountNum(result.getInt(3));
				user.setBalance(result.getDouble(4));
				user.setFuncionario(result.getBoolean(5));
				
				users.add(user);

			}
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
			con.close();
			return null;
		} 
		return users;
	}

	public Account insertNewAccount(String nome, String senha, double balance, boolean funcionario)
			throws SQLException, ClassNotFoundException {
		Account conta = new Account(nome, senha, balance, funcionario);

		Connection con = null;

		try {
			con = new ConnectionFactory().getConnection();

			String sql = "INSERT INTO account (accountNum, name, senha, balance, funcionario) VALUES (?, ?, ?, ?, ?)";

			PreparedStatement statement = con.prepareStatement(sql);

			statement.setInt(1, conta.getAccountNum());
			statement.setString(2, conta.getName());
			statement.setString(3, conta.getPassword());
			statement.setDouble(4, conta.getBalance());
			statement.setBoolean(5, conta.isFuncionario());

			int rowsInserted = statement.executeUpdate();
			if (rowsInserted > 0) {
				System.out.println("Novo usuario cadastrado com sucesso.");
			}
			statement.close();
		} catch (SQLException e) {
			System.out.println(e);
		} finally {
			con.close();

			}
			return conta;
		}
	}
