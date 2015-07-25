package connection;

import accounts.BankAccount;
import connection.transactions.AbstractMoneyTransaction;

/**
 * Created by ivan on 25.07.15.
 */
public class InternalTransaction extends AbstractMoneyTransaction{
    public InternalTransaction(double money, long id, BankAccount masterAccount, BankAccount slaveAccount) {
        super(money,id,masterAccount,slaveAccount);
    }

    @Override
    protected double action() {
        masterAccount.withdraw(getMoney());
        slaveAccount.deposit(getMoney());
        return getBalance();
    }
}
