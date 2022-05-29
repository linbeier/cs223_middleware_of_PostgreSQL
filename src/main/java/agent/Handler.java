package agent;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

class Handler implements Callable<String> {
    Socket sock;

    public Handler(Socket sock) {
        this.sock = sock;
    }

    @Override
    public String call() throws Exception {
        try (InputStream input = this.sock.getInputStream()) {
            try (OutputStream output = this.sock.getOutputStream()) {
                return handle(input, output);
            }
        } catch (Exception e) {
            try {
                this.sock.close();
            } catch (IOException ioe) {
            }
            System.out.println("client disconnected.");
        }
        return null;
    }

    //todo: handle sock messages
    private String handle(InputStream input, OutputStream output) throws IOException {
        var writer = new BufferedWriter(new OutputStreamWriter(output, StandardCharsets.UTF_8));
        var reader = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8));
//            writer.write("hello\n");
//            writer.flush();
//            for (;;) {
//                String s = reader.readLine();
//                if (s.equals("bye")) {
//                    writer.write("bye\n");
//                    writer.flush();
//                    break;
//                }
//                writer.write("ok: " + s + "\n");
//                writer.flush();
//            }
        return reader.readLine();
    }
}
