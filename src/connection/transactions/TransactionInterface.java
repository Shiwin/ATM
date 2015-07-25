package connection.transactions;

import accounts.exceptions.LimitTransactionsSettled;
import accounts.exceptions.UnknownAccountType;
import connection.exceptions.TransactionFaildedException;

/**
 * Created by ivan on 25.07.15.
 */
public interface TransactionInterface {

    public void executeCommand() throws TransactionFaildedException, LimitTransactionsSettled, UnknownAccountType;
    public double getMoney();
    public String report(String condition);
    public double getBalance();

}
