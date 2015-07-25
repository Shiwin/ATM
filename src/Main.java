import accounts.BankAccount;
import accounts.exceptions.AccountAccessException;
import accounts.exceptions.UnknownAccountType;
import bank.Bank;
import bank.exceptions.PersonalInformationNotFound;
import com.sun.corba.se.spi.legacy.interceptor.UnknownType;

import java.util.IllegalFormatException;
import java.util.Scanner;

/**
 * Created by ivan on 23.07.15.
 */
public class Main {

    public static void userInterface() {

        String help =
                "****************************************\n" +
                        "*                 HELP                 *  \n" +
                        "****************************************  \n" +
                        "*Commands:                             *\n" +
                        "*help - print this message             *  \n" +
                        "*create [$value] - create account with *  \n" +
                        "*   balance=$value                     *  \n" +
                        "*deposit $value - deposit account      *  \n" +
                        "*withdraw $value - withdraw account    *  \n" +
                        "*percent_month \\                       *  \n" +
                        "*percent_day   - rise account          *  \n" +
                        "*percent_week  /                       *  \n" +
                        "*quit - quit                           *  \n" +
                        "****************************************";

        System.out.println(help);

        Scanner sc = new Scanner(System.in);
        Boolean exit = false;
        BankAccount ba=null;

        while (!exit){
            System.out.print(">]");
            String input = sc.nextLine();
            input.trim();
            String[] argvs = input.split(" ");
            if (argvs.length>2){
                System.out.println("Illegal number of arguments");
                break;
            }
            Double money = 0d;
            if(argvs.length==2){
                try{
                    money = Double.parseDouble(argvs[1]);
                    if (!BankAccount.testArguments(money)){
                        System.out.println("Illegal value of $value");
                        break;
                    }
                }catch (IllegalFormatException e){
                    System.out.println("Illegal value of $value");
                    break;
                }
            }
            switch (argvs[0]){
                case "quit":{
                    exit = true;
                    break;
                }
                case "help" :{
                    System.out.println(help);
                    break;
                }
//                case "create":{
//                    ba = new BankAccount(money);
//                    break;
//                }
                case "deposit":{
                    if(argvs.length==1){
                        System.out.println("Illegal number of arguments");
                    }else{
                        if (ba != null) {
                            ba.deposit(money);
                        }else {
                            System.out.println("You must create balance before this operation");
                        }
                    }
                    break;
                }
                case "withdraw":{
                    if(argvs.length==1){
                        System.out.println("Illegal number of arguments");
                    }else{
                        if (ba != null) {
                            ba.withdraw(money);
                        }else {
                            System.out.println("You must create balance before this operation");
                        }
                    }
                    break;
                }
                case "percent_month":{
                    if (ba != null) {
                        ba.changesInMonth();
                    }else {
                        System.out.println("You must create balance before this operation");
                    }
                    break;
                }
                case "percent_day":{
                    if (ba != null) {
                        ba.percentDaily(30);
                    }else {
                        System.out.println("You must create balance before this operation");
                    }
                    break;
                }
                case "percent_week":{
                    if (ba != null) {
                        ba.percentWeekly();
                    }else {
                        System.out.println("You must create balance before this operation");
                    }
                    break;
                }
                default:{
                    System.out.println("Unknown command");
                }
            }
        }

    }

    public static void main(String[] args) {
        Bank b = new Bank();
        try {
            b.createPersonalInformation("8808 914324","Ivan","Sharavuev","Andrev", "m","89274055650","11.09.1994");
            String[] meAccount = b.openAccount("8808 914324", 0, Bank.BUSINESS_ACCOUNT);
            String[] meAccount2 = b.openAccount("8808 914324",0,Bank.SAVING_ACCOUNT);
            connection.Connection c = b.createConnection(meAccount[0],meAccount[1]);

            for (int i = 0; i < 40; i++) {
                c.deposit(Math.random()*10000);
                c.withdraw(Math.random()*10000);
            }
            c.changeType(Bank.CHEQUING_ACCOUNT);
            c = b.createConnection(meAccount[0],meAccount[1],meAccount2[0],meAccount2[1]);
            c.internalTransaction(1000);

            System.out.println(b.reportForAccount(meAccount[0],meAccount[1]));
            System.out.println();
            System.out.println(b.reportForBranch(0));
            System.out.println();
            System.out.println(b.reportForBank());

            b.nextMonth();

            System.out.println();

            for (int i = 0; i < 40; i++) {
                c.deposit(Math.random()*10000);
                c.withdraw(Math.random()*10000);
            }

            System.out.println(b.reportForBank());

            b.outReport(b::reportForBank,"BankReport.txt");

        } catch (UnknownAccountType unknownAccountType) {
            unknownAccountType.printStackTrace();
        } catch (UnknownType unknownType) {
            unknownType.printStackTrace();
        } catch (PersonalInformationNotFound personalInformationNotFound) {
            personalInformationNotFound.printStackTrace();
        } catch (AccountAccessException e) {
            e.printStackTrace();
        }




    }

}
