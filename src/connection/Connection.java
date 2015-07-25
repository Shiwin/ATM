package connection;

import accounts.BankAccount;
import accounts.exceptions.LimitTransactionsSettled;
import accounts.exceptions.UnknownAccountType;
import connection.exceptions.TransactionFaildedException;
import connection.transactions.AbstractMoneyTransaction;
import connection.transactions.ChangeTypeTransaction;
import connection.transactions.DepositTransaction;
import connection.transactions.WithdrawTransaction;

/**
 * Created by ivan on 22.07.15.
 */
public class Connection {

    private static long nextId = 0;
    private BankAccount masterAccount,slaveAccount;

    public Connection(BankAccount masterAccount, BankAccount slaveAccount) {
        this.masterAccount = masterAccount;
        this.slaveAccount = slaveAccount;
    }

    public Connection(BankAccount account){
        this.masterAccount = account;
        this.slaveAccount = null;
    }

    public void externTransaction(BankAccount slaveAccount){
        if (this.slaveAccount == null) {
            this.slaveAccount = slaveAccount;
        }
    }

    public double deposit(double deposit){
        try {
            AbstractMoneyTransaction tr =  new DepositTransaction(deposit,nextId++,masterAccount);
            tr.executeCommand();

        } catch (TransactionFaildedException e) {
            e.printStackTrace();
        } catch (LimitTransactionsSettled limitTransactionsSettled) {
            limitTransactionsSettled.printStackTrace();
        }
        return masterAccount.getBalance();
    }

    public double withdraw(double withdraw){
        try {
            new WithdrawTransaction(withdraw,nextId++,masterAccount).executeCommand();
        } catch (TransactionFaildedException e) {
            e.printStackTrace();
        } catch (LimitTransactionsSettled limitTransactionsSettled) {
            limitTransactionsSettled.printStackTrace();
        }
        return masterAccount.getBalance();
    }

    public double internalTransaction(double money){
        try {
            new InternalTransaction(money,nextId++,masterAccount,slaveAccount).executeCommand();
        } catch (TransactionFaildedException e) {
            e.printStackTrace();
        } catch (LimitTransactionsSettled limitTransactionsSettled) {
            limitTransactionsSettled.printStackTrace();
        }
        return masterAccount.getBalance();
    }


    /**
     *
     * @param type use bank.Bank constants
     */
    public void changeType(String type){
        try {
            new ChangeTypeTransaction(nextId++,masterAccount,type).executeCommand();
            System.out.println(this.masterAccount);
        } catch (TransactionFaildedException e) {
            e.printStackTrace();
        } catch (LimitTransactionsSettled limitTransactionsSettled) {
            limitTransactionsSettled.printStackTrace();
        } catch (UnknownAccountType unknownAccountType) {
            unknownAccountType.printStackTrace();
        }
    }

}
