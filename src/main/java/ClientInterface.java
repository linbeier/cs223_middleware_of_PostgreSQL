import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
//import java.sql.Types;
import java.util.Properties;
import java.util.Scanner;

//import org.postgresql.util.PSQLException;

import java.util.ArrayList;

import java.util.Scanner;
//import org.apache.commons.lang3.tuple.ImmutablePair;
//import org.apache.commons.lang3.tuple.ImmutablePair;





public class ClientInterface {
	class Pair<U, V>
	{
	    public final U first;       // the first field of a pair
	    public final V second;      // the second field of a pair
	 
	    // Constructs a new pair with specified values
	    private Pair(U first, V second)
	    {
	        this.first = first;
	        this.second = second;
	    }
	 
	 
	    @Override
	    public String toString() {
	        return "first element of pair : " + first + " , second element of pair : " + second;
	    }
	 

	}

	public void splitInput(String st){


		
        ArrayList<Pair<String,ArrayList<String>>> transaction = new ArrayList<>();
        String[] arrOfStr = st.split(";");

        for(String op : arrOfStr){
        	String[] arrOfOp= op.split("=");

        	String[] target = arrOfOp[1].split(",");

        	ArrayList<String> targetAL = new ArrayList<>();

        	for(String i : target){
        		targetAL.add(i);
        	}

        	Pair<String,ArrayList<String>> curPair = new Pair<String,ArrayList<String>>(arrOfOp[0],targetAL);
        	transaction.add(curPair);

        	//op[0]
        }



        for(int i = 0; i < transaction.size(); i++){
        	System.out.println("operation-- " + transaction.get(i).toString());
        }
  


	}
	
	
	///////////////////////////////////////
	//public static void main(String[] args) throws SQLException, PSQLException, ClassNotFoundException {
	public static void main(String[] args) throws ClassNotFoundException {

		Scanner sc = new Scanner(System.in);
		
		ClientInterface ci = new ClientInterface();
		
		//try {
			System.out.print("Enter the database username (usually postgres): ");
			String dbUsername = sc.nextLine();
			
			System.out.print("Enter the database password: ");
			String dbPassword = sc.nextLine();
			
			//Initialization of the JDBC code
			//Class.forName("org.postgresql.Driver");
	        //String url = "jdbc:postgresql://localhost:5432/";
	        //Properties props = new Properties();
	       // props.setProperty("user", dbUsername);
	        //props.setProperty("password", dbPassword);
	        //Connection conn = DriverManager.getConnection(url, props);
	        
	        
            
    		//End

    		System.out.println("Welcome to the client interface!");
    		
    		//System.out.print("What is customer login: ");
    		//String userLogin = sc.nextLine();
    		
    		//String option = "";
    		int choice = 0;
    		
    		while(true){
    			System.out.println("\n\n\nPlease enter your transaction:");

    			String input = sc.nextLine();
    			ci.splitInput(input);
    			

    			
    			//Add the code here

    			System.out.println("Would you like to continue? \n");    			
    			String option = sc.nextLine();



    			
    			if(option.equalsIgnoreCase("n") || option.equalsIgnoreCase("no") ) {
    				break;
    			}
    		} 
    		sc.close();
    		
       // }
        // catch(SQLException e) {
        // 	System.out.println("Incorrect username or password. Try again");
        //     while (e != null) {
        //         System.out.println("Message = " + e.getMessage());
        //         System.out.println("SQLState = "+ e.getSQLState());
        //         System.out.println("SQL Code = "+ e.getErrorCode());
        //         e = e.getNextException();
        //     }
        	
        // }
        
	}

}