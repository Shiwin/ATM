package bank;

import accounts.BankAccount;
import accounts.exceptions.AccountAccessException;
import accounts.exceptions.UnknownAccountType;
import bank.exceptions.BankIllegalOperation;
import bank.exceptions.PersonalInformationNotFound;
import com.sun.corba.se.spi.legacy.interceptor.UnknownType;
import connection.Connection;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.IllegalFormatException;

public class Bank {

    private static int numOfBranches = 0;

    public static final String BUSINESS_ACCOUNT = "business",
    SAVING_ACCOUNT = "saving",CHEQUING_ACCOUNT = "chequing";

    private ArrayList<Branch> branches;
    private HashMap<String, PersonalInformation> informationAboutCustomers;

    public Bank(ArrayList<Branch> branches, HashMap<String, PersonalInformation> informationAboutCustomers) {
        this.branches = branches;
        this.informationAboutCustomers = informationAboutCustomers;

        try {
            this.createBranch("Eric Dugle", "565-34-23", "Central square,21","8:00-18:00");
        } catch (BankIllegalOperation bankIllegalOperation) {
            bankIllegalOperation.printStackTrace();
        }
    }

    public Bank() {
        this(new ArrayList<Branch>(),new HashMap<>());
    }

    public void createBranch(String manager,String telephoneNum,String address,String businessHours) throws BankIllegalOperation {
        int size = branches.size();
        if (size<5){
            branches.add(new Branch(numOfBranches++,manager,telephoneNum,address, businessHours));
        }else {
            throw new BankIllegalOperation();
        }
    }


    /**
     * utility method for access to user's account
     * @param login format:"№branch_accountId"
     * @return
     */
    private BankAccount getAccount(String login)throws IllegalFormatException{
        String[] ids = login.split("_");
        Integer branchId = Integer.parseInt(ids[0]);
        Integer accountId = Integer.parseInt(ids[1]);

        Branch branch = branches.get(branchId);
        return branch.getAccount(accountId);
    }

    /**
     *
     * @param login
     * @param password
     * @return Connection
     * @throws IllegalFormatException
     * @throws AccountAccessException
     */
    public Connection createConnection(String login, String password)throws
            IllegalFormatException,AccountAccessException{

        BankAccount account = getAccount(login);
        if(account.checkPassword(password)){
            return new Connection(account);
        }else {
            throw new AccountAccessException();
        }
    }

    /**
     *
     * @param login1
     * @param password1
     * @param login2
     * @param password2
     * @return Connection
     * @throws IllegalFormatException
     * @throws AccountAccessException
     */
    public Connection createConnection(String login1,String password1,String login2,String password2)throws
            IllegalFormatException,AccountAccessException{
        BankAccount masterAccount = getAccount(login1);
        BankAccount slaveAccount = getAccount(login2);
        if(masterAccount.checkPassword(password1)&&slaveAccount.checkPassword(password2)){
            return new Connection(masterAccount,slaveAccount);
        }else {
            throw new AccountAccessException();
        }
    }

    /**
     *
     * @param branchId number of branch
     * @param typeOfAccount use constants BUSINESS_ACCOUNT,SAVING_ACCOUNT,CHEQUING_ACCOUNT
     * @return [login,password]
     */
    public String[] openAccount(String pasportSeriesAndNumber, int branchId, String typeOfAccount) throws UnknownAccountType, UnknownType, PersonalInformationNotFound {
        PersonalInformation persone = informationAboutCustomers.get(pasportSeriesAndNumber);

        if (persone == null) {
            throw new PersonalInformationNotFound();
        }

        Branch branch = branches.get(branchId);
        String[] result = branch.createAccount(typeOfAccount);
        String login = branchId + "_" + result[0];
        result[0] = login;
        persone.addAccountId(login);
        System.out.println(String.format("Login: %s\nPassword: %s",login,result[1]));
        return result;
    }

    public void createPersonalInformation(String pasportSeriesAndNumber, String name,
                                          String surname,String patronym, String gender, String telephone, String dateOfBorth){
        PersonalInformation personalInformation = new PersonalInformation( pasportSeriesAndNumber, name,
                surname, patronym, gender, telephone, dateOfBorth) ;
        this.informationAboutCustomers.put(pasportSeriesAndNumber,personalInformation);
    }

    public String reportForAccount(String login,String pasword) throws AccountAccessException {
        BankAccount ba = getAccount(login);
        if(ba.checkPassword(pasword)){
            return String.format("Transact report for %nLogin: %s %n%s",login,ba.report());
        }else{
            throw new AccountAccessException();
        }
    }

    public String reportForBranch(int branchId){
        return String.format("Report for branch: %d %n%s",branchId,this.branches.get(branchId).report());
    }


    public String reportForBank(){
        StringBuilder sb = new StringBuilder();
        branches.forEach((n)->{ sb.append(String.format("Branch: %d%n",n.getId()));
                sb.append(n.report());});
        return String.format("Report for bank. %n%s",sb.toString());
    }

    public void nextMonth(){
        System.out.println("Next Month");
        branches.forEach(Branch::nextMonth);
    }

    public void outReport(OutReport or,String filename){
        try {
            try(PrintWriter pw = new PrintWriter(new File(filename))){
                pw.print(or.out());
                pw.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
