package connection.transactions;

import accounts.BankAccount;
import connection.exceptions.TransactionFaildedException;
import accounts.exceptions.LimitTransactionsSettled;

public abstract class AbstractMoneyTransaction implements TransactionInterface {


    private final double money;

    protected BankAccount masterAccount,slaveAccount;
    protected long id;

    public AbstractMoneyTransaction(double money, long id,BankAccount mainAccount, BankAccount slaveAccount) {
        this.slaveAccount = slaveAccount;
        this.masterAccount = mainAccount;
        this.id = id;
        this.money = money;

    }

    public AbstractMoneyTransaction(double money, Long id,BankAccount account){
        this(money,id,account,null);
    }

    public void executeCommand() throws TransactionFaildedException, LimitTransactionsSettled {
        if (masterAccount.frozen()){
            masterAccount.addTransactionToReport(report("Fail"));
            throw new LimitTransactionsSettled();
        }
        if (slaveAccount != null && slaveAccount.frozen()) {
            slaveAccount.addTransactionToReport(report("Fail"));
            throw new LimitTransactionsSettled();
        }
        Double masterBalanceCopy = masterAccount.getBalance();
        Double slaveBalanceCopy = (slaveAccount!=null)?slaveAccount.getBalance():0;
        try{
            this.action();
            masterAccount.addTransactionToReport(report("OK"));
            if (slaveAccount != null) {
                slaveAccount.addTransactionToReport(report("OK"));
            }
        }catch (Exception e){
            masterAccount.setBalance(masterBalanceCopy);
            masterAccount.addTransactionToReport(report("Fail"));
            if (slaveAccount != null) {
                slaveAccount.setBalance(slaveBalanceCopy);
                slaveAccount.addTransactionToReport(report("Fail"));
            }
            e.printStackTrace();
            throw new TransactionFaildedException();
        }
    }

    protected abstract double action();

    public double getMoney() {
        return money;
    }

    public double getBalance(){
        return masterAccount.getBalance();
    }

    public String report(String condition) {
        String slave = (this.slaveAccount==null)?"":slaveAccount.toString();
        return String.format("%8d %.4f %s %s %s\n",id,money,masterAccount,slave,condition);
    }
}
