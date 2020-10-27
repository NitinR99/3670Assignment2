import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

public class JobSeeker {

public static void main(String args[]) throws Exception {


    InetAddress address=InetAddress.getLocalHost();
    Socket s1=null;
    String line=null;
    BufferedReader br=null;
    BufferedReader is=null;
    PrintWriter os=null;
    

    try {
        s1=new Socket(address, 4445); // You can use static final constant PORT_NUM
        br= new BufferedReader(new InputStreamReader(System.in));
        is=new BufferedReader(new InputStreamReader(s1.getInputStream()));
        os= new PrintWriter(s1.getOutputStream());
    }
    catch (IOException e){
        e.printStackTrace();
        System.err.print("IO Exception");
    }

    System.out.println("Client Address : "+address);
   
    System.out.println("Enter Data to echo Server ( Enter QUIT to end):");
    String response=null;
    try{
        //line=br.readLine(); 
        while(true)
        {
            
			//os.println(line);
            //os.flush();
            response=is.readLine();
			System.out.println(response);
			if(response.equals("done5")){
				TimeUnit.SECONDS.sleep(5);
				os.println("5 seconds have passed ~ DONE!");
				os.flush();
			}
			else if(response.equals("done10")){
				TimeUnit.SECONDS.sleep(10);
				os.println("10 seconds have passed ~ DONE!");
				os.flush();
			}
			else{
				System.out.println("No message received");
			}
            
			System.out.println("Server Response : "+response);
			
			
			
            //line=br.readLine();
        }
      }
    
    catch(IOException e)
    {
        e.printStackTrace();
        System.out.println("Socket read Error");
    }
    
    finally
    {
        is.close();os.close();br.close();s1.close();
        System.out.println("Connection Closed");
    }

}
}
