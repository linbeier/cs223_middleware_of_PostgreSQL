package client;


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static java.lang.Integer.parseInt;

public class Client {
    public  Map<String, Map<String, String>> configurations;

    public Client() {
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

    public void connect(String agent) throws IOException {
        int port = parseInt(configurations.get(agent).get("port"));
        Socket sock = new Socket("localhost", port); // 连接指定服务器和端口
        try (InputStream input = sock.getInputStream()) {
            try (OutputStream output = sock.getOutputStream()) {
                handle(input, output);
            }
        }
        sock.close();
        System.out.println("disconnected.");
    }

    private static void handle(InputStream input, OutputStream output) throws IOException {
        var writer = new BufferedWriter(new OutputStreamWriter(output, StandardCharsets.UTF_8));
        var reader = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8));
        Scanner scanner = new Scanner(System.in);
        System.out.println("server connected");
        for (;;) {
            System.out.print(">>> "); // 打印提示
            String s = scanner.nextLine(); // 读取一行输入
            if(s.equals("end"))break;
            writer.write(s);
            writer.newLine();
            writer.flush();
            String resp = reader.readLine();
            System.out.println("<<< " + resp);

        }
    }
}