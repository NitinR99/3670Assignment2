import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.LinkedList;
/*
This class is the server which is able to connect to multiple JobSeekers (clients)
*/
public class JobCreator {
   static LinkedList<ServerThread> connections; //each node in the linkedlist contains the thread of a jobseeker. Allowing this code to assign jobs to multiple clients simultaneously
   static void menu() throws Exception
   {
    BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
    System.out.println("Creator menu\n-----------------\n1.Get all active coonnections\n2.Assign a job to a seeker\n3.Terminate a connection\n4.One-to-many jobs\n9.wait for new connection");
    int choice=-1;
    try{
    choice=Integer.parseInt(br.readLine());} //reads choice the user selects
    catch(Exception e)
    {
        System.out.println("input error.");
    }
    if(choice==1)//lists all active connections
    {
        for(int i=0;i<connections.size();i++)
        {
            System.out.println("Client("+i+")= "+connections.get(i).isActive);
        }
        menu();
    }
    else if(choice==2)//assigns job to a client
    {
		//shows user the clients which are free and can accept a job
		for(int i=0;i<connections.size();i++)
		{
			System.out.println("Client("+i+") is Free? "+connections.get(i).isFree);
		}

		System.out.println("Enter client id to send: ");//gets the client id to assign a job to it
        int clId=Integer.parseInt(br.readLine());
		if(connections.get(clId)!=null && connections.get(clId).isFree)
        {
            try{//Asks user which of the 2 jobs should be assigned to the client
				System.out.println("1) send \"Done\" after 5 seconds");
				System.out.println("2) send \"Done\" after 10 seconds");
				System.out.println("3) Detect status of IP address\n4) Detect status of IP address and port");
				int choi = Integer.parseInt(br.readLine());

				if(choi == 1){
					String t = "done5";
					connections.get(clId).os.println(t); //sending task prompt
					connections.get(clId).os.flush();
				}
				else if(choi == 2){
					String t = "done10";
					connections.get(clId).os.println(t); //sending task prompt
					connections.get(clId).os.flush();
				}
        else if(choi == 3) 
        {
          System.out.println("Enter IP address to send to client: ");
          String df=br.readLine();
          connections.get(clId).os.println("justip");
          connections.get(clId).os.println(df);
          connections.get(clId).os.flush();
        }
        else if(choi == 4)
        {
          System.out.println("Enter IP address to send to client: ");
          String df=br.readLine();
          System.out.println("Enter the port number to send to the client: ");
          int dff=Integer.parseInt(br.readLine());
          connections.get(clId).os.println("portip");
          connections.get(clId).os.println(df);
          connections.get(clId).os.println(dff);
          connections.get(clId).os.flush();
        }
				connections.get(clId).isFree = false;

            }
            catch(Exception e)
            {
                System.out.println("error while sending the task");
            }
            menu();
        }
        else if(connections.get(clId)!=null && !connections.get(clId).isFree)
        {
                System.out.println("The selected client is not free. Try again.");
                menu();
        }
        else{
            System.out.println("Client not found. Try again.");
            menu();
        }
    }
    else if(choice==3)//Terminates the server side connection of the client. The client will not be able to send anything to the server.
    {
        System.out.println("Enter client id to terminate: ");
        int clId=Integer.parseInt(br.readLine());
        if(connections.get(clId)!=null)
        {
            try{
            connections.get(clId).is.close();
            connections.get(clId).os.close();
            connections.get(clId).s.close();
            connections.get(clId).isActive=false;
            connections.remove(clId);}
            catch(Exception e)
            {
                System.out.println("error while ending client connection");
                menu();
            }
        }
        menu();
    }
    else if(choice==4) //choice for one-to-many attacks
    {
      System.out.println("Choose the type of attack");
      System.out.println("1. ICMP attack for 5 seconds (Check pcap4j execution terminal)");
      System.out.println("2. TCP attack for 5 seconds");
      int chhh=Integer.parseInt(br.readLine());
      if(chhh==1) //tells every client to conduct an ICMP attack against specified target IP
      {
        System.out.println("Enter IP address to send to client: ");
        String df=br.readLine();
        for(int index=0;index<connections.size();index++)
        {
          connections.get(index).os.println("icmpattack");
          connections.get(index).os.println(df);
          connections.get(index).os.flush();
        }
        menu();
      }
      if(chhh==2) //tells every client to conduct an TDP attack against specified target IP and port
      {
        System.out.println("Enter IP address to send to client: ");
        String df=br.readLine();
        System.out.println("Enter the port number to send to the client: ");
        int dff=Integer.parseInt(br.readLine());

        for(int index=0;index<connections.size();index++)
        {
          connections.get(index).os.println("tcpattack");
          connections.get(index).os.println(df);
          connections.get(index).os.println(dff);
          connections.get(index).os.flush();
        }
        menu();
      }
    }
    else if(choice==9)
    {
        System.out.println("waiting for new connection...");
    }
    else{
        System.out.println("Invalid option. Try again.");
       menu();
    }

   }
public static void main(String args[]) throws Exception{

    int count =0; //id number of jobseekers
    Socket s=null;
    ServerSocket ss2=null;
     connections = new LinkedList<ServerThread>(); //keeps track of all current connections
    System.out.println("Server Listening......");
    try
    {
	InetAddress inetAddress = InetAddress.getLocalHost();
        ss2 = new ServerSocket(); // connection is through port 4445
	ss2.bind(new InetSocketAddress(inetAddress.getHostAddress(),4445));
	System.out.println("Address: "+inetAddress.getHostAddress());
	System.out.println("Port: "+4445);

    }
    catch(IOException e)
    {
	    e.printStackTrace();
	    System.out.println("Server error. Unable to start server.");

    }

    while(true)
    {
        try
	{
            s= ss2.accept(); //listens for new connection
            System.out.println("connection Established ("+count+")");
            ServerThread temp= new ServerThread(s, count); //assigns the connection a new serverthread
            temp.start();
            connections.add(temp); //adds it to the linkedlist
            count++;
        }

    catch(Exception e)
    {
        e.printStackTrace();
        System.out.println("Connection Error. Unable to connect to Job seekers.");

    }


    for(int i=0;i<connections.size();i++) //checks and removes inactive connections
    {
        if(connections.get(i).isActive==false)
        {
            connections.remove(i);
            System.out.println("removed a connection");
        }
    }
    menu();

    }

}

}
/*
This class defines the individual thread for each connection to the JobCreator
 */
class ServerThread extends Thread{
    int id; //unique id for the client, to be used by JobCreator
    boolean isActive = true; // boolean value to check if the connection is still active
	boolean isFree = true; // boolean value to check if the client is doing a task or whether it is free
    String line = null;
    BufferedReader  is = null;
    PrintWriter os = null;
    Socket s = null;
    String fromClient = null;
    public ServerThread(Socket s,int x){
        this.s = s;
        id = x;
    }

    public void run() {
    try{
	is=new BufferedReader(new InputStreamReader(s.getInputStream()));
        os=new PrintWriter(s.getOutputStream());

    }catch(IOException e){
        System.out.println("IO error in server thread");
    }
//to assign boolean value to isFree
    try {
        line=is.readLine();
        while(line.compareTo("QUIT")!=0){

            os.println(line);
            os.flush();
            System.out.println("Client "+id+"  :  "+line);
			if(line.equals("5 seconds have passed ~ DONE!")){
				isFree = true;
			}
			if(line.equals("10 seconds have passed ~ DONE!")){
				isFree = true;
			}
      if(line.equals("justip ~ DONE!"))
      {
        isFree=true;
      }
      if(line.equals("portip ~ DONE!"))
      {
        isFree=true;
      }
      if(line.equals("icmpattack ~ DONE!"))
      {
        isFree=true;
      }
      if(line.equals("tcpattack ~ DONE!"))
      {
        isFree=true;
      }
            line=is.readLine();
        }
    } catch (IOException e) {

        line=this.getName(); //reused String line for getting thread name
        System.out.println("IO Error/ Client ("+ id+ ") terminated abruptly");
        isActive=false; //since there is a connection error
    }
    catch(NullPointerException e){
        line=this.getName(); //reused String line for getting thread name
        System.out.println("Client id("+ id+") Closed");
        isActive=false; //since there is a connection error
    }
//to close the connection
    finally{
    try{
        if (is!=null){
            is.close();
            isActive=false;
        }

        if(os!=null){
            os.close();
            isActive=false;
        }
        if (s!=null){
        s.close();
	    isActive=false;
        }

        }
    catch(IOException ie){
        System.out.println("Socket Close Error");
    }
    }//end finally
    }
}
