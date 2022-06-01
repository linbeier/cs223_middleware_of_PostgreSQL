package agent;

import client.TransactionGenerator;
import converter.TransactionConverter;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.Callable;

class Handler extends Thread {
    Socket sock;
    private String ReadSQL;
    private String WriteSQL;

    public Handler(Socket sock) {
        this.sock = sock;
        this.ReadSQL = "select object_name, object_value from table_test where object_name = ?;\n";
//        this.WriteSQL = "If exists (select 1 from table_test where object_name = ?)\n" +
//                "Update table_test\n" +
//                "Set ?\n" +
//                "Where object_name = ?;\n" +
//                "Else\n" +
//                "Insert into table_test\n" +
//                "values (?, ?)\n";
        this.WriteSQL = "update table_test set ? where object_name = ?;\n";
    }

    @Override
    public void run() {
        try (InputStream input = this.sock.getInputStream()) {
            try (OutputStream output = this.sock.getOutputStream()) {
                handle(input, output);
            }
        } catch (Exception e) {
            try {
                this.sock.close();
            } catch (IOException ioe) {
            }
            System.out.println("client disconnected.");
        }
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
                TransactionConverter.splitInput(s);
                validate();

                //todo: process with db, and write back to client
                writer.write("ok: " + s + "\n");
                writer.flush();
            }
        return "ok";
    }

    private void validate(){

    }

    private String connect_sql(ArrayList<TransactionConverter.Pair<String,ArrayList<String>>> arr) throws IOException, ClassNotFoundException, SQLException {
        Connector manager = Connector.createInstance();
        manager.connectDB();
        StringBuilder sql_query = new StringBuilder("BEGIN\n");
        ArrayList<String> params = new ArrayList<String>();
        for (TransactionConverter.Pair<String,ArrayList<String>> op:arr) {
            if(op.first.equals("R")){
                sql_query.append(this.ReadSQL);
                params.add(op.second.get(0));
            }else{
                sql_query.append(this.WriteSQL);
                params.add(op.second.get(1));
                params.add(op.second.get(0));
            }
        }

        Object[] params_obj = params.toArray();
        ResultSet rs = null;
        try {
            rs = manager.executeQuery(sql_query.toString(), params_obj);
        }catch (SQLException e){
            System.out.println(e);
        }
        StringBuilder result = new StringBuilder();
        while(rs != null && rs.next()){
            String obj_name = rs.getString("object_name");
            String obj_value = rs.getString("object_value");
            if(obj_name != null)result.append(obj_name);
            if(obj_value != null) {
                result.append(obj_value);
                result.append("\n");
            }
        }
        return result.toString();
    }

}
