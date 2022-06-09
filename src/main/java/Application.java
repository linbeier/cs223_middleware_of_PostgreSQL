import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import agent.Agent;
import client.Client;

public class Application {

    public  Map<String, Map<String, String>> configurations;

    public Application(){
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

    public static void main (String[] args)  {
        String Agent = args[1];
        if(Objects.equals(args[0], "client")){
            Client c = new Client();
            try {
                c.connect(Agent);
            }catch (IOException e){
                System.out.println(e);
            }

        }else{
            Agent a = new Agent();
            try {
                a.Listen_handle(Agent);
            }catch (IOException e){
                System.out.println(e);
            }

        }
    }
}
