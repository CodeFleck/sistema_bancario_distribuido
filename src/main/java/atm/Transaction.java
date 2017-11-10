package atm;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Transaction implements Serializable {


	private static final long serialVersionUID = 4438034788911002389L;
	double amount = 0;
    int accountNum = 0;
    String transactionType = "";
    Date transDate = new Date();

    public Transaction() {
    	super();
    }
    
    public Transaction(double amt, int accNum, String type){

        this.amount = amt;
        this.accountNum = accNum;
        this.transactionType = type;
    }
    
    public double getAmount() {
        return amount;
    }

    public void setAmount(double d) {
        this.amount = d;
    }

    public int getAccountNum() {
        return accountNum;
    }

    public void setAccountNum(int accountNum) {
        this.accountNum = accountNum;
    }

    public Date getTransDate() {
        return transDate;
    }

    public void setTransDate(Date transDate) {
        this.transDate = transDate;
    }

    public void setType(String type) {
        this.transactionType = type;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public String toString(){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        String info = "\nTransação: " + this.transactionType
                + "\nQuantidade: "+ this.amount
                + "\nData: "+ sdf.format(this.transDate) + "\n";

        return info;
    }

}
