package connection.transactions;

import accounts.BankAccount;

/**
 * Created by ivan on 23.07.15.
 */
public class WithdrawTransaction extends AbstractMoneyTransaction {


    public WithdrawTransaction(double money, Long id, BankAccount account) {
        super(money, id, account);
    }

    @Override
    protected double action() {
        return super.masterAccount.withdraw(super.getMoney());
    }
}
