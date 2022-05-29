package agent;

import org.apache.commons.lang3.tuple.ImmutablePair;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.FutureTask;

public class Agent {
    private String operations;
    ArrayBlockingQueue<FutureTask<String>> futureTaskQ;

    public Agent() {
        this.futureTaskQ = new ArrayBlockingQueue<FutureTask<String>>(100);
    }

    public void Listen_handle() throws IOException {
        ServerSocket ss = new ServerSocket(6666); // 监听指定端口
        System.out.println("server is running...");
        for (; ; ) {
            Socket sock = ss.accept();
            System.out.println("connected from " + sock.getRemoteSocketAddress());
            FutureTask<String> future = new FutureTask<String>(new Handler(sock));
            this.futureTaskQ.add(future);
            Thread t = new Thread(future);
            t.start();
        }

    }

    //todo: futureTaskQ has all connections from clients, result can be fetched from futureTask.get(). however, maybe directly process in handler class better;

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
