//Question:- bankY is a simulation of a basic banking system that allows users to create accounts deposit and
//        withdraw funds, and transfer funds between accounts. This project provides an opportunity  to explore
//        fundamental concepts of object-oriented programming.data persistence using files or databases, and basic
//        transaction handling.




import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


public class Banky {


    // Account class represents a bank account
    static class Account implements Serializable {    //serializable is a interface
        private final String accountNo, accountHolder;
        private double balance;


        public Account(String accountNo, String accountHolder, double initialBalance) {
            this.accountNo = accountNo;
            this.accountHolder = accountHolder;
            this.balance = initialBalance;
        }
        public String getAccountNumber() {
            return accountNo;
        }
        public String getAccountHolder() {
            return accountHolder;
        }
        public double getBalance() {
            return balance;
        }


        public void setBalance(double balance) {
            this.balance = balance;
        }
    }


    static class ServiceOfBank {
        private Map<String, Account> accounts = new HashMap<>();


        //method for create account in the bank
        public void createAccount(String accountNo, String accountHolder, double initialBalance) {
            Account account = new Account(accountNo, accountHolder, initialBalance);
            accounts.put(accountNo, account);
        }


        //method for deposit money in bank account
        public void deposit(String accountNo, double amount) {
            Account account = accounts.get(accountNo);
            if (account != null) {
                account.setBalance(account.getBalance() + amount);
            } else {
                System.out.println("account not available in the bank.");
            }
        }


        //method for withdraw money
        public void withdraw(String accountNo, double amount) {
            Account account = accounts.get(accountNo);
            if (account != null) {
                if (account.getBalance() >= amount) {
                    account.setBalance(account.getBalance() - amount);
                } else {
                    System.out.println("no money");
                }
            } else {
                System.out.println("account not available in the bank.");
            }
        }


        //create transfer method to transfer the money from one account to another account
        public void transfer(String fromAccount, String toAccount, double amount) {
            Account from = accounts.get(fromAccount);
            Account to = accounts.get(toAccount);
            if (from != null && to != null) {
                if (from.getBalance() >= amount) {
                    from.setBalance(from.getBalance() - amount);
                    to.setBalance(to.getBalance() + amount);
                } else {
                    System.out.println("no money.");
                }
            } else {
                System.out.println("account not available in the bank.");
            }
        }


        public double getBalance(String accountNumber) {
            Account account = accounts.get(accountNumber);
            return account != null ? account.getBalance() : 0.0;
        }


        public Map<String, Account> getAccounts() {
            return accounts;
        }


        public void setAccounts(Map<String, Account> accounts) {
            this.accounts = accounts;
        }
    }


    // DAO class to handle data persistence
    static class AccountDao {
        private static final String DATA_FILE = "bank_accounts.dat";


        public void saveAccounts(Map<String, Account> accounts) {
            try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
                out.writeObject(accounts);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        @SuppressWarnings("unchecked")
        public Map<String, Account> loadAccounts() {
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(DATA_FILE))) {
                return (Map<String, Account>) in.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                return new HashMap<>();
            }
        }
    }


    //main method
    public static void main(String[] args) {
        ServiceOfBank accountService = new ServiceOfBank();
        AccountDao accountDao = new AccountDao();


        // Load existing accounts
        accountService.setAccounts(accountDao.loadAccounts());


        Scanner sc = new Scanner(System.in);
        boolean bankOn = true;


        while (bankOn) {   //initially true, which means the loop will start running.
            System.out.println("1. Create account");
            System.out.println("2. Deposit money");
            System.out.println("3. Withdraw money");
            System.out.println("4. Transfer money");
            System.out.println("5. Check total balance");
            System.out.println("6. Exit from all process");


            System.out.print("Select the above choice: ");
            int choice = sc.nextInt();


            String accountNo;
            switch (choice) {
                case 1 -> {
                    // Create account of customer
                    System.out.print("Please enter the account number of customer: ");
                    accountNo = sc.next();
                    System.out.print("Enter account holder name: ");
                    String accountHolder = sc.next();
                    System.out.print("Enter initial balance of account: ");
                    double initialBalance = sc.nextDouble();
                    accountService.createAccount(accountNo, accountHolder, initialBalance);
                }
                case 2 -> {
                    // Deposit money in bank account
                    System.out.print("Please enter your account number: ");
                    accountNo = sc.next();
                    System.out.print("Enter amount to deposit: ");
                    double depositAmount = sc.nextDouble();
                    accountService.deposit(accountNo, depositAmount);
                }
                case 3 -> {
                    // Withdraw money from bank account
                    System.out.print("Please enter your account number: ");
                    accountNo = sc.next();
                    System.out.print("Enter amount to withdraw: ");
                    double withdrawAmount = sc.nextDouble();
                    accountService.withdraw(accountNo, withdrawAmount);
                }
                case 4 -> {
                    // Transfer money
                    System.out.print("Please enter account number from which we want to transfer money: ");
                    String fromAccount = sc.next();
                    System.out.print("Enter the account number where we want to deposit money: ");
                    String toAccount = sc.next();
                    System.out.print("Enter the amount: ");
                    double transferAmount = sc.nextDouble();
                    accountService.transfer(fromAccount, toAccount, transferAmount);
                }
                case 5 -> {
                    // Check balance
                    System.out.print("Please enter your account number: ");
                    accountNo = sc.next();
                    double balance = accountService.getBalance(accountNo);
                    System.out.println("total money available is : " + balance);
                }
                case 6 -> bankOn = false;
                default -> System.out.println("Invalid choice");
            }
            accountDao.saveAccounts(accountService.getAccounts());
        }
    }
}

