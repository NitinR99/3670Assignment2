
import java.lang.reflect.Method;
import java.net.Socket;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import javax.swing.*;
public class client extends JFrame
{
	static JPanel ans = new JPanel();
	String filenames[];
	int count=0;
	static DataOutputStream ii = null;
	static DataInputStream os = null;
	static Socket socket = null;
	String message = null;
	
	JLayeredPane lp;
	Object cls;
	Class newC;
	
	client() throws Exception
	{
		super("lab9");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		lp = new JLayeredPane();
    	JPanel query = new JPanel();
    	query.setBackground(Color.GRAY);
    	query.setBounds(0, 0, 500, 35);
    	lp.add(query,0,0);
    	lp.add(ans,1,0);
    	setLayout(new BorderLayout());
	
    	JTextField ipid = new JTextField();
		ipid.setColumns(20);
		ipid.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e)
			{
				
				String myip=ipid.getText();
				int mysock=6868;//Integer.parseInt(JOptionPane.showInputDialog("Which socket should I connect to ?"));
				
				FileOutputStream fos=null;
				
				try {
					socket = new Socket(myip, mysock);
				}
				catch(Exception ex)
				{}
				
				try {
					os = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
	            try {
					count = os.readInt();
					System.out.println("number of files: "+count);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	            
	            
	            for(int i=0;i<count;i++)
	            {
					byte[] b = new byte[1000];
					String FILE_TO_RECEIVE = ".\\";
	            	try {
						//get name
						message = os.readUTF();
						System.out.println("Received: "+ message);
						FILE_TO_RECEIVE += message;
						//get size
						int size = os.readInt();
						//get file
						b = new byte[size];
						os.read(b);
						
						File file = new File(FILE_TO_RECEIVE);
						FileOutputStream out =  new FileOutputStream(file);
						out.write(b);
						out.flush();
						
						out.close();
							
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
	            }
	           
			
			//update the panel
			
			try {
				String name = message.replace(".class","").trim();
				newC = Class.forName(name);
				cls = newC.newInstance();
				
				Method nfield = newC.getDeclaredMethod("getNameField");
				Method ffield = newC.getDeclaredMethod("getFileField");
				
				nfield.setAccessible(true);
				ffield.setAccessible(true);
				
				ans.removeAll();
				ans=(JPanel)cls;
				ans.revalidate();
				ans.setBounds(0,30,500,675);
				lp.add(ans,1,0);
				
				String Sname = null;
				String Fname = null;
				
				ii = new DataOutputStream(socket.getOutputStream());
				
				JTextField NF = (JTextField)nfield.invoke(cls);
				JTextField FF = (JTextField)ffield.invoke(cls); 
				
				NF.addActionListener(new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent e){
	
						enter1:try {
							
							System.out.println("working");
							
							Method setname = newC.getDeclaredMethod("setName");
							Method setfile = newC.getDeclaredMethod("setFile");
							
							setname.setAccessible(true);
							setfile.setAccessible(true);
							
							setname.invoke(cls);
							setfile.invoke(cls);
							
							Method getname = newC.getDeclaredMethod("getName");
							Method getfile = newC.getDeclaredMethod("getFile");
							
							getname.setAccessible(true);
							getfile.setAccessible(true);
							
							String name = "" + getname.invoke(cls);
							String file = "" + getfile.invoke(cls);
							
							if(name.equals("") || file.equals("")){
								JPanel p = new JPanel();
								JOptionPane.showMessageDialog(p,"Error: Please fill in both text fields"); 
								break enter1;
							}
							
							System.out.println("Sending name: "+name+".");
							System.out.println("Sending file: "+file+".");
							
							//send name of student
							ii.writeUTF(name);
							ii.flush();
							//send name of file
							ii.writeUTF(file);
							ii.flush();
							//find file
							File f = new File(file);
							//send file length
							ii.writeInt((int)f.length());
							ii.flush();
							//send actual file
							byte[] buf = new byte[(int) f.length()];
							FileInputStream fis = new FileInputStream(f);
							int n;
							while ((n = fis.read(buf)) != -1) {
								ii.write(buf, 0, n);
								System.out.println("file length: "+n);
								ii.flush();
							}
							ii.flush();
							
							//read incoming report
							String report = os.readUTF();
							System.out.println("Report Recieved. Displaying on screen now...");
							
							ans.removeAll();
							ans = new JPanel();
							
							JTextArea r = new JTextArea();
							ans.add(r);
							r.setText(report);			
							
							ans.revalidate();
							ans.setBounds(0,30,500,675);
							lp.add(ans,1,0);
							
							socket.close();
							ii.close();
							os.close();
							
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				});
				FF.addActionListener(new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent e){
	
						enter2:try {
							
							System.out.println("working");
							
							Method setname = newC.getDeclaredMethod("setName");
							Method setfile = newC.getDeclaredMethod("setFile");
							
							setname.setAccessible(true);
							setfile.setAccessible(true);
							
							setname.invoke(cls);
							setfile.invoke(cls);
							
							Method getname = newC.getDeclaredMethod("getName");
							Method getfile = newC.getDeclaredMethod("getFile");
							
							getname.setAccessible(true);
							getfile.setAccessible(true);
							
							
							String name = "" + getname.invoke(cls);
							String file = "" + getfile.invoke(cls);
							
							if(name.equals("") || file.equals("")){
								JPanel p = new JPanel();
								JOptionPane.showMessageDialog(p,"Error: Please fill in both text fields"); 
								break enter2;
							}
							
							System.out.println("Sending name: "+name+".");
							System.out.println("Sending file: "+file+".");
							
							//send name of student
							ii.writeUTF(name);
							ii.flush();
							//send name of file
							ii.writeUTF(file);
							ii.flush();
							//find file
							File f = new File(file);
							//send file length
							ii.writeInt((int)f.length());
							ii.flush();
							//send actual file
							byte[] buf = new byte[(int) f.length()];
							FileInputStream fis = new FileInputStream(f);
							int n;
							while ((n = fis.read(buf)) != -1) {
								ii.write(buf, 0, n);
								System.out.println("file length: "+n);
								ii.flush();
							}
							ii.flush();
							
							//read incoming report
							String report = os.readUTF();
							
							
							ans.removeAll();
							ans = new JPanel();
							
							JTextArea r = new JTextArea();
							ans.add(r);
							r.setText(report);			
							
							ans.revalidate();
							ans.setBounds(0,30,500,675);
							lp.add(ans,1,0);
							
							socket.close();
							ii.close();
							os.close();
							
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				});
				
				System.out.println("setup done");
				}
				catch(Exception ex)
				{
					System.out.println("help!");
					ex.printStackTrace();
				}
			
			
			}
		});
			
		query.add(ipid);
    	getContentPane().add(lp, BorderLayout.CENTER);
    	
    	setSize(500,700);
    	setVisible(true);
	}
	

	public void action(ActionEvent e){
		
		
		
	}
		
	
    public static void main(String[] args) throws Exception
    {	
    	client o1=new client();

    }
  
}