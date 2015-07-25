package bank;

import accounts.AccountFactory;
import accounts.BankAccount;
import accounts.exceptions.UnknownAccountType;

import java.util.ArrayList;

/**
 * Created by ivan on 22.07.15.
 */
public class Branch {

    private static int nextAccountId = 0;

    private AccountFactory accountFactory;
    private ArrayList<BankAccount> accounts;
    private int id;
    private String manager,address,phoneNum,businessHours;

    public Branch(ArrayList<BankAccount> accounts, int id, String manager, String address, String phoneNum, String businessHours) {
        this.accounts = accounts;
        this.id = id;
        this.manager = manager;
        this.address = address;
        this.phoneNum = phoneNum;
        this.businessHours = businessHours;
        this.accountFactory = new AccountFactory();
    }

    public Branch(int id, String manager, String address, String phoneNum, String businessHours) {
        this(new ArrayList<>(), id, manager, address, phoneNum, businessHours);
    }

    public BankAccount getAccount(Integer accountId) {
        return accounts.get(accountId);
    }

    public String report(){
        StringBuilder sb = new StringBuilder();
        accounts.forEach((n)->{
            sb.append(String.format("For %d account:%n",n.getId()));
            sb.append(n.report());
        });
        return sb.toString();
    }

    public int getId() {
        return id;
    }

    public void nextMonth(){
        accounts.forEach((n)->{
            n.clearReport();
            n.changesInMonth();
        });
    }

    public String[] createAccount(String typeOfAccount) throws UnknownAccountType {
        BankAccount account = accountFactory.createAccount(typeOfAccount, nextAccountId);
        accounts.add(account);

        return new String[]{String.format("%s", nextAccountId++),account.getPassword()};
    }


}
