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
import java.util.concurrent.atomic.AtomicInteger;

class Handler extends Thread {
    Socket sock;
    private String ReadSQL;
    private String WriteSQL;
    private static final AtomicInteger ID_GENERATOR = new AtomicInteger();

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

        Connector manager = Connector.createInstance();
        manager.connectDB();

            for (;;) {
                String s = reader.readLine();
                System.out.println(s);
                if(s.equals("end")){
                    break;
                }
                ArrayList<TransactionConverter.Pair<String,ArrayList<String>>> arr = new ArrayList<>();
                int transaction_id = ID_GENERATOR.incrementAndGet();
                arr = TransactionConverter.splitInput(s);
                s = connect_sql(arr, manager);
                boolean b = validate();
                if(b){
                    manager.commitChange();
                }else{
                    manager.rollBack();
                }
                //todo: process with db, and write back to client
                writer.write("ok: " + s + "\n");
                writer.flush();
            }
        return "ok";
    }

    private boolean validate(){
        return false;
    }

    private String connect_sql(ArrayList<TransactionConverter.Pair<String,ArrayList<String>>> arr, Connector manager) throws IOException, ClassNotFoundException, SQLException {

        StringBuilder result = new StringBuilder();
        for (TransactionConverter.Pair<String,ArrayList<String>> op:arr) {
            StringBuilder sql_query = new StringBuilder();
            ArrayList<String> params = new ArrayList<String>();
            if(op.first.equals("R")){
                sql_query.append(this.ReadSQL);
                params.add(op.second.get(0));

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
            }else{
                sql_query.append(this.WriteSQL);
                params.add(op.second.get(1));
                params.add(op.second.get(0));

                Object[] params_obj = params.toArray();
                try {
                    boolean f = manager.executeUpdate(sql_query.toString(), params_obj);
                }catch (SQLException e){
                    System.out.println(e);
                }
            }

        }

        return result.toString();
    }

//    private String connect_sql(ArrayList<TransactionConverter.Pair<String,ArrayList<String>>> arr, int transaction_id) throws IOException, ClassNotFoundException, SQLException {
//        Connector manager = Connector.createInstance();
//        manager.connectDB();
//
//        StringBuilder sql_query = new StringBuilder("BEGIN;\n");
//        ArrayList<String> params = new ArrayList<String>();
//        StringBuilder result = new StringBuilder();
//
//        sql_query.append("PREPARE TRANSACTION '" + transaction_id + "' ;\n");
//        for (TransactionConverter.Pair<String,ArrayList<String>> op:arr) {
////            StringBuilder sql_query = new StringBuilder();
////            ArrayList<String> params = new ArrayList<String>();
//            if(op.first.equals("R")){
//                sql_query.append(this.ReadSQL);
//                params.add(op.second.get(0));
//            }else{
//                sql_query.append(this.WriteSQL);
//                params.add(op.second.get(1));
//                params.add(op.second.get(0));
//            }
//
//        }
//
//        sql_query.append("END;\n");
//
//        Object[] params_obj = params.toArray();
//        ResultSet rs = null;
//        try {
//            rs = manager.executeUpdate(sql_query.toString(), params_obj);
//        }catch (SQLException e){
//            System.out.println(e);
//        }
//
//        while(rs != null && rs.next()){
//            String obj_name = rs.getString("object_name");
//            String obj_value = rs.getString("object_value");
//            if(obj_name != null)result.append(obj_name);
//            result.append(":");
//            if(obj_value != null) {
//                result.append(obj_value);
//                result.append(";");
//            }
//        }
//        return result.toString();
//    }

//    private void commit(int transaction_id) throws IOException, ClassNotFoundException, SQLException {
//        Connector manager = Connector.createInstance();
//        manager.connectDB();
//        String sql_query = "COMMIT PREPARED '" + transaction_id + "' ;\n";
//        Object[] params_obj = new Object[]{};
//        ResultSet rs = null;
//        try {
//            rs = manager.executeQuery(sql_query, params_obj);
//        }catch (SQLException e){
//            System.out.println(e);
//        }
//    }
}
