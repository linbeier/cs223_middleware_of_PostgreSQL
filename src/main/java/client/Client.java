package client;


import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.core.util.KeyValuePair;

import java.util.ArrayList;
import java.util.List;

public class Client {
    private final String operations;

    public Client(String operations) {
        this.operations = operations;
    }

    public ArrayList<ImmutablePair<String, String>> getOperations() {
        String[] opList = this.operations.split(";");
        ArrayList<ImmutablePair<String, String>> arr = new ArrayList<ImmutablePair<String, String>>();
        for (String op : opList) {
            String first = op.split("=")[0];
            String second  = op.split("=")[1];
            ImmutablePair<String, String> pair = new ImmutablePair<String, String>(first, second);
            arr.add(pair);
        }
        return arr;
    }
}
