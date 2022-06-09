package agent;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.FutureTask;

import static java.lang.Integer.parseInt;

public class Agent {

    public  Map<String, Map<String, String>> configurations;
    //todo: implement agents communication and agent roles(leader, follower), read-only(client connect to follower and directly send transactions)
    public Agent() {
        this.configurations = new HashMap<>();
        Map<String, String> agent1 = new HashMap<String, String>();
        agent1.put("port", "6666");
        agent1.put("log", "log1");
        configurations.put("agent1", agent1);
        Map<String, String> agent2 = new HashMap<String, String>();
        agent1.put("port", "6667");
        agent1.put("log", "log2");
        configurations.put("agent2", agent2);
        Map<String, String> agent3 = new HashMap<String, String>();
        agent1.put("port", "6668");
        agent1.put("log", "log3");
        configurations.put("agent3", agent3);
    }

    public void Listen_handle(String agent) throws IOException {
        int port = parseInt(configurations.get(agent).get("port"));
        ServerSocket ss = new ServerSocket(port); // 监听指定端口
        System.out.println("server is running...");
        for (; ; ) {
            Socket sock = ss.accept();
            System.out.println("connected from " + sock.getRemoteSocketAddress());
            Thread t = new Handler(sock);
            t.start();
        }

    }

}
