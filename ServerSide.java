    import java.net.InetAddress;
    import java.net.ServerSocket;
    import java.net.Socket;
    import java.net.UnknownHostException;
    import java.nio.file.FileSystems;
    import java.nio.file.Path;
    import java.nio.file.Paths;
    import java.nio.file.WatchEvent;
    import java.nio.file.WatchKey;
    import java.nio.file.WatchService;
    import java.util.concurrent.TimeUnit;
     
    import com.sun.jdi.InconsistentDebugInfoException;
    import static java.nio.file.StandardWatchEventKinds.*;
     
    import java.io.BufferedInputStream;
    import java.io.BufferedReader;
    import java.io.BufferedWriter;
    import java.io.DataInputStream;
    import java.io.DataOutputStream;
    import java.io.FileReader;
    import java.io.FileWriter;
    import java.io.IOException;
    import java.io.InputStreamReader;
     
    public class ServerSide {
    	static Socket s ;
    	static ServerSocket ss;
    	public static void main(String[] args) {
    		//WatchForOutput();
    		try {
    		ss = new ServerSocket(5000);
    		s = ss.accept();
    		}catch(Exception e) {
    			e.printStackTrace();
    		}
     
    				System.out.println("Running......");
    				while(true) {
    					try {
    					System.out.println("Connection Accepted");
    					DataInputStream DIS = new DataInputStream(s.getInputStream());
    					String IncomingSentence = DIS.readUTF();
    					IncomingSentence +='\n';
    					System.out.println(IncomingSentence);
    					BufferedWriter writer = new BufferedWriter(new FileWriter("/home/mohamed/Desktop/inputData/input.txt"));
    					writer.write(IncomingSentence);
    					writer.close();
    					WatchForOutput();
    				}catch (IOException e) {
    					try {
    					s = ss.accept();
    					}catch(Exception m) {
    						m.printStackTrace();
    					}
    					//e.printStackTrace();
    				}
    			}
     
    	}
     
    	static void WatchForOutput() {
    		System.out.println("Watching");
     
    		 try {
    	            // Creates a instance of WatchService.
    	            WatchService watcher = FileSystems.getDefault().newWatchService();
     
    	            // Registers the logDir below with a watch service.
    	            Path logDir = Paths.get("/home/mohamed/Desktop");
    	            logDir.register(watcher, ENTRY_CREATE, ENTRY_MODIFY, ENTRY_DELETE);
     
    	            // Monitor the logDir at listen for change notification.
    	            while (true) {
    	                WatchKey key = watcher.take();
    	                for (WatchEvent<?> event : key.pollEvents()) {
    	                    WatchEvent.Kind<?> kind = event.kind();
     
    	                      if (ENTRY_MODIFY.equals(kind)) {
    	                    	TimeUnit.SECONDS.sleep(1);
    	                        //System.out.println("Entry was modified on log dir.");
    	                        BufferedReader br = new BufferedReader(new FileReader("/home/mohamed/Desktop/output.txt"));
    	                        String lastLine=br.readLine();
    	                        br.close();
    	                        System.out.println(lastLine);
    	                        send(lastLine);
    	                        return ;
    	                    } 
    	                }
    	                key.reset();
    	            }
    	        } catch (IOException | InterruptedException e) {
    	            e.printStackTrace();
    	        }
     
    	}
     
    	static void send( String Respond ) {
    		try {
    			DataOutputStream DOS = new DataOutputStream(s.getOutputStream());
    			DOS.writeUTF(Respond);
    			DOS.flush();
    		}catch(IOException e){
    			e.printStackTrace();
    		}
    	}
    }
