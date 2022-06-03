package manager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import manager.TransactionConverter.Pair;

public class Transaction {
    // Format of one operation: Pair<opetaion(R/W), ArrayList[object, value]>. eg. R=X;W=Y,V\n
    public ArrayList<Pair<String,ArrayList<String>>> operationList =  new ArrayList<>();
    public int transactionId = 0;
    public Set<String> readSet = new HashSet<>();
    public Set<String> writeSet = new HashSet<>();

    public Transaction() {
    }

    public Transaction(ArrayList<Pair<String, ArrayList<String>>> operationList) {
        UpdateOperationSet("R", this.readSet);
        UpdateOperationSet("W", this.writeSet);
        this.operationList = operationList;
    }

    public Transaction(ArrayList<Pair<String, ArrayList<String>>> operationList, int transactionId) {
        this.operationList = operationList;
        this.transactionId = transactionId;
    }

    public ArrayList<Pair<String, ArrayList<String>>> getOperationList() {
        return operationList;
    }

    public Set<String> getReadSet() {
        return readSet;
    }

    public Set<String> getWriteSet() {
        return writeSet;
    }

    public int getTransactionId() {
        return transactionId;
    }

    private void UpdateOperationSet(String type, Set<String> set) {
        for (Pair<String,ArrayList<String>> operation : operationList) {
            String operationType = operation.first; // Get the operation type (R/W)
            if (operationType.equals(type)) {
                String operationObject = operation.second.get(0); // Get the object of the operation
                set.add(operationObject);
            }
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Transaction)) return false;
        Transaction transaction = (Transaction)obj;
        return getTransactionId() == transaction.getTransactionId();
    }
}
