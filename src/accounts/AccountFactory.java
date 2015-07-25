package accounts;

import accounts.exceptions.UnknownAccountType;

import java.util.Random;

/**
 * Created by ivan on 22.07.15.
 */
public class AccountFactory {

    private static final int DEFAULT_PASSWORD_LENGTH = 10;

    private int passwordLength;
    private Random rd = new Random();

    /**
     *
     * @param passwordLength length of generating passwords
     */
    public AccountFactory(int passwordLength) {
        this.passwordLength = passwordLength;
    }

    public AccountFactory(){
        this(DEFAULT_PASSWORD_LENGTH);
    }

    public BankAccount createAccount(String type,Integer id) throws UnknownAccountType {
        String password = passwordGenerator();
        BankAccount account = new BankAccount(0,id,password);
        setType(account,type);
        account.setBalance(getMinOpenningBalance(type));
        return account;
    }

    public static BankAccount setType(BankAccount account, String type) throws UnknownAccountType {
        switch (type){
            case "business":{
                account.setType("Business");
                account.setPercent(1);
                return account;
            }
            case "saving":{
                account.setType("Saving");
                account.setPercent(5);
                return account;
            }
            case "chequing":{
                account.setType("Chequing");
                account.setPercent(0);
                return account;
            }
            default:{
                throw new UnknownAccountType();
            }
        }
    }

    public static double getMinOpenningBalance(String type) throws UnknownAccountType{
        switch (type){
            case "business":{
                return 5_000_000;
            }
            case "saving":{
                return 50_000;
            }
            case "chequing":{
                return 5_000;
            }
            default:{
                throw new UnknownAccountType();
            }
        }
    }

    public int getPasswordLength() {
        return passwordLength;
    }

    public void setPasswordLength(int passwordLength) {
        this.passwordLength = passwordLength;
    }

    /**
     *
     * @return password created from "ABCDEFGHIJKLMNOPQRSTUVWXYZ[\]^_`abcdefghijklmnopqrstuvwxyz" characters
     */
    private String passwordGenerator(){
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < passwordLength; i++) {
            sb.append(new Character((char) (rd.nextInt(57)+65)));
        }
        return sb.toString();
    }

}
