package connection.transactions;

import accounts.AccountFactory;
import accounts.BankAccount;
import accounts.exceptions.LimitTransactionsSettled;
import accounts.exceptions.UnknownAccountType;
import connection.exceptions.TransactionFaildedException;

/**
 * Created by ivan on 23.07.15.
 */
public class ChangeTypeTransaction implements TransactionInterface {

    private final Long id;
    private BankAccount masterAccount;
    private String newType;

    /**
     * @param account
     * @param newType use constants define in bank.Bank
     */
    public ChangeTypeTransaction(Long id, BankAccount account, String newType) {
        this.masterAccount = account;
        this.newType = newType;
        this.id = id;
    }


    @Override
    public void executeCommand() throws TransactionFaildedException, LimitTransactionsSettled, UnknownAccountType {
        double minOpenningBalance = AccountFactory.getMinOpenningBalance(this.newType);
        if (masterAccount.getBalance() < minOpenningBalance) {
            masterAccount.addTransactionToReport(report("Fail"));
            throw new LimitTransactionsSettled();
        }
        try {
            AccountFactory.setType(this.masterAccount,newType);
            masterAccount.addTransactionToReport(report("OK"));
        } catch (Exception e) {
            e.printStackTrace();
            throw new TransactionFaildedException();
        }
    }


    @Override
    public double getMoney() {
        return 0;
    }

    @Override
    public String report(String condition) {
        return String.format("%8d  %s %s\n", id, masterAccount, condition);

    }

    @Override
    public double getBalance() {
        return masterAccount.getBalance();
    }
}
