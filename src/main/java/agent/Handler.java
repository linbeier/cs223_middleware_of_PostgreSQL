package agent;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

class Handler implements Callable<String> {
    Socket sock;
    private String ReadSQL;
    private String WriteSQL;

    public Handler(Socket sock) {
        this.sock = sock;
        this.ReadSQL = "select object_value from table_test where object_name = ?";
        this.WriteSQL = "If exists (select 1 from table_test where object_name = ?)\n" +
                "Update table_test\n" +
                "Set ?\n" +
                "Where object_name = ?;\n" +
                "Else\n" +
                "Insert into table_test\n" +
                "values (?, ?)\n";
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
            for (;;) {
                String s = reader.readLine();
                if(s.equals("end")){
                    break;
                }
                transfer(s);
                writer.write("ok: " + s + "\n");
                writer.flush();
            }
        return "ok";
    }

    private void transfer(String str){

    }


}
