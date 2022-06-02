package agent;

import manager.TransactionConverter;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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
        this.WriteSQL = "update table_test set object_value = ? where object_name = ?;\n";
    }

    @Override
    public void run() {
        try (InputStream input = this.sock.getInputStream()) {
            try (OutputStream output = this.sock.getOutputStream()) {
                handle(input, output);
            }
        } catch (Exception e) {
            System.out.println(e);
            try {
                this.sock.close();
            } catch (IOException ioe) {
            }
            System.out.println("client disconnected.");
        }
    }

    //todo: handle sock messages
    private String handle(InputStream input, OutputStream output) throws IOException, SQLException, ClassNotFoundException {
        var writer = new BufferedWriter(new OutputStreamWriter(output, StandardCharsets.UTF_8));
        var reader = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8));
            for (;;) {
                String s = reader.readLine();
                System.out.println(s);
                if(s.equals("end")){
                    break;
                }
                ArrayList<TransactionConverter.Pair<String,ArrayList<String>>> arr = new ArrayList<>();
                arr = TransactionConverter.splitInput(s);
                validate();
                s = connect_sql(arr);
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
//        StringBuilder sql_query = new StringBuilder("BEGIN;\n");
        StringBuilder result = new StringBuilder();
        for (TransactionConverter.Pair<String,ArrayList<String>> op:arr) {
            StringBuilder sql_query = new StringBuilder();
            ArrayList<String> params = new ArrayList<String>();
            if(op.first.equals("R")){
                sql_query.append(this.ReadSQL);
                params.add(op.second.get(0));
            }else{
                sql_query.append(this.WriteSQL);
                params.add(op.second.get(1));
                params.add(op.second.get(0));
            }
            Object[] params_obj = params.toArray();
            ResultSet rs = null;
            try {
                rs = manager.executeQuery(sql_query.toString(), params_obj);
            }catch (SQLException e){
                System.out.println(e);
            }

            while(rs != null && rs.next()){
                String obj_name = rs.getString("object_name");
                String obj_value = rs.getString("object_value");
                if(obj_name != null)result.append(obj_name);
                result.append(":");
                if(obj_value != null) {
                    result.append(obj_value);
                    result.append(";");
                }
            }
        }
//        sql_query.append("END;\n");

        return result.toString();
    }

}
