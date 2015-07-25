package accounts;

import java.util.ArrayList;
import java.util.Objects;

public class BankAccount {

    private  double percent = 7;//annual percentage rate
    protected static final int DAYS_IN_YEAR = 365;
    protected static final int DAYS_IN_MONTH = 30;
    protected static final int DAYS_IN_WEEK = 7;

    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private ArrayList<String> transactReport = new ArrayList<>();
    private final String password;
    private final  int id;

    private double balance;

    /**
     *
     * @param balance
     * @param id
     * @param password
     */
    public BankAccount(double balance,int id,String password) {
        this.id = id;
        this.password = password;
        this.balance = balance;
        System.out.println("Create account with balance "+balance);
    }

    /*public accounts.BankAccount(String balance){
        MyDecimail md = new MyDecimail(balance);
        this.balance = md;
        System.out.println("Create account with balance "+balance);
    }*/

    public boolean checkPassword(String password){
        return Objects.equals(password, this.password);
    }


    /**
     * Emulate depositing
     * @param refill
     * @return new value of balance after deposit
     */
    public double deposit(double refill){
//        MyDecimail md = new MyDecimail(refill);
        if (refill>=0) {
            this.balance+=refill;
            System.out.println("Deposit: "+refill+"\tBalance: "+balance);
        } else {
            System.out.println("Illegal refill. Please input positive number.");
        }
        return balance;
    }

    /**
     * Emulate withdraw
     *
     * @param indrawn
     * @return new value of balance after withdraw
     */
    public double withdraw(double indrawn){
//        MyDecimail md = new MyDecimail(indrawn);
        if (indrawn > balance){
            System.out.println("On account of insufficient funds");
        }else if (indrawn>=0) {
            this.balance-=indrawn;
            System.out.println("Withdraw: "+indrawn+"\tBalance: "+balance);
        } else {
            System.out.println("Illegal ithdrawals. Please input positive number.");
        }
        return balance;
    }

    /**
     * taxes or rise of balance on month. it depends on the type of account
     * @return value of balance after rising
     */
    public double changesInMonth(){
        if (balance<0){
            System.out.println("Your balance lower zero. Amount it.");
        }else{
            double percent = balance*this.percent /100;
            this.balance += percent;
            System.out.println("Add "+percent+" at the end of the month. Balance:"+balance);
        }
        return balance;
    }

    /**
     * taxes or rise of balance at the end of the day in depends on the type of account
     * @param daysInMonth
     * @return
     */
    public double percentDaily(int daysInMonth){
        if (balance<0){
            System.out.println("Your balance lower zero. Amount it.");
        }else{
            double percent = balance* this.percent /(100*daysInMonth);
            this.balance += percent;
            System.out.println("Add "+percent+" at the end of the day. Balance:"+balance);
        }
        return balance;
    }


    /**
     * taxes or rise of balance at the end of the week in depends on the type of account
     * @return
     */
    public double percentWeekly(){
        if (balance<0){
            System.out.println("Your balance lower zero. Amount it.");
        }else{
            double percent = balance* this.percent * DAYS_IN_WEEK /(100d*DAYS_IN_MONTH);
            this.balance += percent;
            System.out.println("Add "+percent+" at the end of the week. Balance:"+balance);
        }
        return balance;
    }

    public double getBalance() {
        return balance;
    }

    /**
     * utility function for input arguments
     * @param arg
     * @return false if Nan of Infinity
     */
    public static boolean testArguments(Double arg){
        return !(arg.isInfinite()||arg.isNaN());
    }

    @Override
    public String toString() {
        return String.format("%15s %15d %10.4f",type,id,balance);
    }

    public String getPassword() {
        return password;
    }


    /**
     * add transaction to report
     * limit 100 per month
     * @param transaction
     */
    public void addTransactionToReport(String transaction){
        if(!frozen()){
            transactReport.add(transaction);
        }
    }

    /**
     *
     * @return can make transactions?
     */
    public boolean frozen(){
        return transactReport.size()>=100;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public void clearReport(){
        transactReport.clear();
    }

    public String report(){
        StringBuilder sb = new StringBuilder();
        this.transactReport.forEach(sb::append);
        return sb.toString();
    }

    public double getPercent() {
        return percent;
    }

    public void setPercent(double percent) {
        this.percent = percent;
    }

    public ArrayList<String> getTransactReport() {
        return transactReport;
    }


    public int getId() {
        return id;
    }

}
