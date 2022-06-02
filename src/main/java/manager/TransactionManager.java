package manager;

import java.util.ArrayList;
import java.util.HashMap;

import manager.TransactionConverter.Pair;

public class TransactionManager {
    private static TransactionManager manager = new TransactionManager();

    public HashMap<Integer, Transaction> curTransaction = new HashMap<Integer, Transaction>();
    public HashMap<Integer, Transaction> finishedTransaction = new HashMap<Integer, Transaction>();
    public HashMap<Integer, Transaction> validatedTransaction = new HashMap<Integer, Transaction>();
//    public ArrayList<Transaction> curTransactionList = new ArrayList<>();
//    public ArrayList<Transaction> finishedTransactionList = new ArrayList<>();
//    public ArrayList<Transaction> validatedTransactionList = new ArrayList<>();
    public int curTransactionNum = 0;

    public TransactionManager() {}

    public static TransactionManager getInstance() {
        if (manager == null) {
            manager = new TransactionManager();
        }
        return manager;
    }

    public synchronized HashMap<Integer, Transaction> getCurTransaction() {
        return curTransaction;
    }

    public synchronized int getCurTransactionNum() {
        return curTransactionNum;
    }

    public synchronized HashMap<Integer, Transaction> getValidatedTransaction() {
        return validatedTransaction;
    }

    public synchronized void addTransaction(ArrayList<Pair<String,ArrayList<String>>> operationList) {
        Transaction transaction = new Transaction(operationList, ++curTransactionNum);
        curTransaction.put(curTransactionNum, transaction);
    }

    public synchronized void setValidatedTransaction(Transaction transaction) {
        int id = transaction.getTransactionId();
        if (curTransaction.containsKey(id)) {
            curTransaction.remove(id);
        }
        validatedTransaction.put(id, transaction);
    }
}
