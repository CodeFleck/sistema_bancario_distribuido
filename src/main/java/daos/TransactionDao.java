package daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import bank.Transaction;
import config.ConnectionFactory;

public class TransactionDao {

	public void saveTransaction(Transaction transaction) throws SQLException, ClassNotFoundException {

		Connection con = null;

		try {
			con = new ConnectionFactory().getConnection();

			String sql = "INSERT INTO transaction (amount, accountNum, transactionType, transDate) VALUES (?, ?, ?, ?)";

			PreparedStatement statement = con.prepareStatement(sql);

			statement.setDouble(1, transaction.getAmount());
			statement.setInt(2, transaction.getAccountNum());
			statement.setString(3, transaction.getTransactionType());

			java.util.Date dataUtil = new java.util.Date();
			java.sql.Date dataSql = new java.sql.Date(dataUtil.getTime());

			statement.setDate(4, dataSql);

			int rowsInserted = statement.executeUpdate();
			if (rowsInserted > 0) {
				System.out.println("Nova transacao efetuada.");
			}
			statement.close();
		} catch (SQLException e) {
			System.out.println(e);
		} finally {
			con.close();
		}
	}

	public void updateBalance(int accountNum, double ammount) throws SQLException, ClassNotFoundException {
		Connection con = null;

		try {
			con = new ConnectionFactory().getConnection();

			String sql = "UPDATE account SET balance=? WHERE accountNum=" + accountNum;
			
			PreparedStatement statement = con.prepareStatement(sql);

			statement.setDouble(1, ammount);

			int rowsInserted = statement.executeUpdate();
			if (rowsInserted > 0) {
				System.out.println("Saldo atualizado.");
			}
			statement.close();
		} catch (SQLException e) {
			System.out.println(e);
		} finally {
			con.close();
		}

	}

	public ArrayList<Transaction> retrieveTransactionsByAccountNum(int accountNum) throws SQLException, ClassNotFoundException {

		Connection con = null;
		ArrayList<Transaction> transacoes = new ArrayList<>();

		try {
			con = new ConnectionFactory().getConnection();

			String sql = "SELECT * FROM transaction WHERE accountNum=?";

			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, accountNum);

			ResultSet result = ps.executeQuery();

			while (result.next()) {

				Transaction transacao = new Transaction();

				transacao.setAmount(result.getDouble(1));
				transacao.setAccountNum(result.getInt(2));
				transacao.setType(result.getString(3));
				transacao.setTransDate(result.getDate(4));

				transacoes.add(transacao);

			}
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
			con.close();
			return null;
		} finally {
			con.close();
		}
		return transacoes;
	}

}