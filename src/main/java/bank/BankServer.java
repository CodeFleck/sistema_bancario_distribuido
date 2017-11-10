package bank;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

import daos.AccountDao;
import daos.TransactionDao;
import interfaces.OperationsInterface;

public class BankServer extends UnicastRemoteObject implements OperationsInterface {

	private static final long serialVersionUID = -5146216445089974573L;

	static String rmiName = "BankServer";
	static private int rmiPort;
	static Registry registry;
	private ArrayList<Account> users = new ArrayList<>();
	static Account activeAcc;
	private Scanner sc;

	AccountDao accountDao = new AccountDao();
	TransactionDao transactionDao = new TransactionDao();

	public BankServer() throws RemoteException, SQLException, ClassNotFoundException {
		super();

		// banco recebe uma lista de todas as contas existentes no banco
		users = accountDao.listAll();
	}

	public void insertNewAccount() throws SQLException, ClassNotFoundException {

		sc = new Scanner(System.in);
		String nome, senha;
		boolean funcionario = false;

		System.out.println("Digite o nome do cliente");
		nome = sc.nextLine();
		System.out.println("Digite uma senha: ");
		senha = sc.nextLine();
		System.out.println("É funcionario? [S]im [N]ao");
		String resp = sc.nextLine();
		if (resp.toLowerCase().startsWith("s")) {
			funcionario = true;
		}

		Account novaConta = accountDao.insertNewAccount(nome, senha, 0, funcionario);
		users.add(novaConta);
	}

	public boolean login(String usr, String pass) throws SQLException, ClassNotFoundException {

		for (Account a : users)
			if (a.getName().equals(usr) && a.getPassword().equals(pass)) {
				activeAcc = a;
				activeAcc.setTransactions(transactionDao.retrieveTransactionsByAccountNum(activeAcc.getAccountNum()));

				return true;
			}

		return false;
	}

	public boolean checkFuncionario(String usr, String pass) throws SQLException {
		boolean resp = false;
		for (Account a : users)
			if (a.getName().equals(usr) && a.getPassword().equals(pass)) {
				activeAcc = a;
				if (a.isFuncionario()) {
					resp = true;

				} else {
					resp = false;
				}
			}

		return resp;
	}

	public double deposit(double amt) throws SQLException, ClassNotFoundException {
		System.out.println("Deposit");

		Transaction t = new Transaction(amt, activeAcc.getAccountNum(), "Deposit");
		activeAcc.depositTransaction(t);
		activeAcc.saveTransaction(t);
		activeAcc.updateBalance(activeAcc, activeAcc.getBalance());
		return activeAcc.getBalance();
	}

	public double withdraw(double amt) throws SQLException, ClassNotFoundException {
		System.out.println("Withdraw");

		Transaction t = new Transaction(amt, activeAcc.getAccountNum(), "Withdraw");
		activeAcc.withdrawTransaction(t);
		activeAcc.saveTransaction(t);
		activeAcc.updateBalance(activeAcc, activeAcc.getBalance());
		return activeAcc.getBalance();
	}

	public double getBalance() {

		return activeAcc.getBalance();
	}

	public ArrayList<Transaction> retrieveTransactionsHistory() throws SQLException {
		
		return activeAcc.getTransactions();
	}


	/***
	 * Unbind our rmi Host from the registry, stop exporting and shut down the
	 * process
	 * 
	 * @throws RemoteException
	 * @throws NotBoundException
	 */
	public void exit() throws RemoteException, NotBoundException {

		// Unregister ourself
		registry.unbind(rmiName);
		
		// Unexport; this will also remove us from the RMI runtime
		UnicastRemoteObject.unexportObject(this, true);
		System.exit(0);
	}

	/**
	 * Starts up rmi Server on specified port. If none available it defaults to 1099
	 * 
	 * @param args
	 */
	public static void main(String[] args) { 

		// parse port
		try {
			rmiPort = Integer.parseInt(args[0]);
			System.out.println("RMI Port = " + rmiPort);
		} catch (ArrayIndexOutOfBoundsException e) {
			rmiPort = 1099; // Default to port 1099
			System.out.println("Default RMI Port = "+rmiPort);
		}

		try {

			// Set up RMI server

			OperationsInterface bank = new BankServer();
			registry = LocateRegistry.createRegistry(rmiPort);
			registry.rebind(rmiName, bank);
			System.out.println("Servidor iniciado...");

		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
}
