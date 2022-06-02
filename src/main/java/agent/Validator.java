package agent;

import manager.Transaction;
import manager.TransactionManager;

import java.util.HashMap;
import java.util.Set;

// Guarantee that it is called by concurrent threads, and be aware of thread safe.
public class Validator {

    public Set<String> readPhase(Transaction transaction) {
        Set<String> readSet = transaction.getReadSet();
        return readSet;
    }

    public boolean validatePhase(Transaction transaction) {
        boolean isValid = false;
        Set<String> writeSet = transaction.getWriteSet();
        Set<String> readSet = transaction.getReadSet();
        TransactionManager transactionManager = TransactionManager.getInstance();
        HashMap<Integer, Transaction>  curTransactions = transactionManager.getCurTransaction();

        for (int id : curTransactions.keySet()) {
            Transaction curTransaction = curTransactions.get(id);
            isValid = setIsDisjoint(readSet, curTransaction.getWriteSet());
            isValid = isValid && setIsDisjoint(writeSet, curTransaction.getWriteSet());
            if (!isValid) {break;}
        }
        transactionManager.setValidatedTransaction(transaction);
        return isValid;
    }

    public boolean setIsDisjoint(Set<String> setA, Set<String> setB) {
        for (String operationObj : setA) {
            if (setB.contains(operationObj)) {
                return false;
            }
        }
        return true;
    }

    public void OCC(Transaction transaction) {
        Set<String> _set = readPhase(transaction);
        boolean isValid = validatePhase(transaction);

    }
}

