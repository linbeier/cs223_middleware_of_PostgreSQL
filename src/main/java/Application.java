import java.io.IOException;
import java.util.Objects;

import agent.Agent;
import client.Client;

public class Application {
    public static void main (String[] args)  {
        if(Objects.equals(args[0], "client")){
            int port_num = 6666;
            Client c = new Client();
            try {
                c.connect(port_num);
            }catch (IOException e){
                System.out.println(e);
            }

        }else{
            Agent a = new Agent();
            try {
                a.Listen_handle(6666);
            }catch (IOException e){
                System.out.println(e);
            }

        }
    }
}
