package manager;

import java.util.ArrayList;

public class Logger {
    public enum LogType {
        WRITE, COMMIT, ABORT
    }

    public class LogEntry {
        public LogType logType = null;
        public Integer transactionId = 0;
        public String object = null;
        public String newValue = null;

        public LogEntry() {
        }

        public LogEntry(LogType logType, Integer transactionId, String object, String newValue) {
            this.logType = logType;
            this.transactionId = transactionId;
            this.object = object;
            this.newValue = newValue;
        }
    }

//    ArrayList<Pair<String, ArrayList<String>>> logList =  new ArrayList<>();
    public ArrayList<LogEntry> logList = new ArrayList<>();
    public int logLen = 0;

    public synchronized ArrayList<LogEntry> getLogList() {
        return logList;
    }

    public synchronized void addWriteLogEntry(ArrayList<String> operation, Integer transactionId) {
        LogEntry logEntry = new LogEntry(LogType.WRITE, transactionId, operation.get(0), operation.get(1));
        logList.add(logEntry);
    }

    public synchronized void addCommitLog(Integer transactionId) {
        LogEntry logEntry = new LogEntry(LogType.COMMIT, transactionId, null, null);
        logList.add(logEntry);
    }

    public synchronized void addAbortLog(Integer transactionId) {
        LogEntry logEntry = new LogEntry(LogType.ABORT, transactionId, null, null);
        logList.add(logEntry);
    }

    public synchronized LogEntry getLastEntry() {
        if((logList.size() > 0) && (logList.size() >= logLen)) {
            return logList.get(logLen);
        }
        return null;
    }
}
