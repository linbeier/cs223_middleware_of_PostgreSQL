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
            Thread t = new Handler(sock);
            t.start();
        }

    }

}
