import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

public class JobCreator {
   static LinkedList<ServerThread> connections;
   static void menu() throws Exception
   {
    BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
    System.out.println("Creator menu\n-----------------\n1.Get all active coonnections\n2.Assign a job to a seeker\n3.Terminate a connection\n9.Refresh");
    int choice=-1;
    try{
    choice=Integer.parseInt(br.readLine());}
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
    if(choice==2)
    {
        //assign a job
		
		for(int i=0;i<connections.size();i++)
		{
			System.out.println("Client("+i+") is Free? "+connections.get(i).isFree);
		}
	
		System.out.println("Enter client id to send: ");
        int clId=Integer.parseInt(br.readLine());
		if(connections.get(clId)!=null && connections.get(clId).isFree)
        {
            try{
				System.out.println("1) send \"Done\" after 5 seconds");
				System.out.println("2) send \"Done\" after 10 seconds");
				
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
				connections.get(clId).isFree = false;
				
            }
            catch(Exception e)
            {
                System.out.println("error while sending the task");
            }
        }
        menu();
    }
    if(choice==3)
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
            }
        }
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
        ss2 = new ServerSocket(4445); // can also use static final PORT_NUM , when defined

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
            //menu();
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

class ServerThread extends Thread{  
    int id;
    boolean isActive = true;
	boolean isFree = true;
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
			
			
            line=is.readLine();
        }   
    } catch (IOException e) {

        line=this.getName(); //reused String line for getting thread name
        System.out.println("IO Error/ Client ("+ id+ ") terminated abruptly");
        isActive=false;
    }
    catch(NullPointerException e){
        line=this.getName(); //reused String line for getting thread name
        System.out.println("Client id("+ id+") Closed");
        isActive=false;
    }

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
