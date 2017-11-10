package bank;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

import daos.TransactionDao;

public class Account implements Serializable {

    
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static Random rand = new Random();
	TransactionDao transactionDao = new TransactionDao();
	private int randomAccountNum = rand.nextInt(500) + 1;
    private String name, password;
    private double balance;
    private int accountNum;
    private boolean funcionario;

    private ArrayList<Transaction> transactions = new ArrayList<>();
    
    public Account() {
    	super();
    }

    public Account(String nme, String pass, double bal, boolean funcionario) {
    	
    	this.accountNum = randomAccountNum;  
        this.name = nme;
        this.password = pass;
        this.balance = bal;
        this.funcionario = funcionario;
        this.transactions = new ArrayList<Transaction>();
    }

    /**
     * Creates a Transaction which deposits an amount
     * @param t
     */
    public void depositTransaction(Transaction t) {
        this.balance = this.balance + t.getAmount();
        transactions.add(t);
    }

    /**
     * Creates a Transaction which withdraws an amount
     * @param t
     */
    public void withdrawTransaction(Transaction t) {
        this.balance = this.balance - t.getAmount();
        if (this.balance < 0)
            System.out.println("Voce esta negativo.");
        transactions.add(t);
    }


    public void saveTransaction(Transaction t) throws SQLException, ClassNotFoundException {
    	transactionDao.saveTransaction(t);
    }
    
    public ArrayList<Transaction> retrieveTransactionsHistory(Account activeAct) throws SQLException, ClassNotFoundException {
    	 activeAct.setTransactions(transactionDao.retrieveTransactionsByAccountNum(activeAct.getAccountNum()));
    	 return activeAct.getTransactions();
    }
    
    public void updateBalance(Account activeAct, double ammount) throws SQLException, ClassNotFoundException {
    	transactionDao.updateBalance(activeAct.getAccountNum(), ammount);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String pass) {
        this.password = pass;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double d) {
        this.balance = d;
    }

    public int getAccountNum() {
        return accountNum;
    }

    public void setAccountNum(int accountNum) {
        this.accountNum = accountNum;
    }

    public ArrayList<Transaction> getTransactions() {
   	
        return transactions;
    }

	public boolean isFuncionario() {
		return funcionario;
	}

	public void setFuncionario(boolean funcionario) {
		this.funcionario = funcionario;
	}

	public void setTransactions(ArrayList<Transaction> transactions) {
		this.transactions = transactions;
	}
    
}
