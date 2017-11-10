package interfaces;

import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;

import bank.Transaction;

public interface OperationsInterface extends Remote {

    boolean login(String usr, String pass) throws RemoteException, SQLException, ClassNotFoundException;
    
    boolean checkFuncionario(String usr, String pass) throws RemoteException, SQLException;

    double deposit(double amt) throws RemoteException, SQLException, ClassNotFoundException;; //return new balance

    double withdraw(double amt) throws RemoteException, SQLException, ClassNotFoundException; // return new balance

    double getBalance() throws RemoteException;
    
    public ArrayList<Transaction> retrieveTransactionsHistory() throws SQLException, RemoteException;
    
    void exit() throws RemoteException, NotBoundException;

    public void insertNewAccount() throws SQLException, RemoteException, ClassNotFoundException;
    
}
