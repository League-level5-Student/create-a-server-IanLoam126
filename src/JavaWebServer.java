import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.activation.MimetypesFileTypeMap;

public class JavaWebServer {
	private static final int NUMBER_OF_THREADS = 100;
	private static final Executor THREAD_POOL = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

	public static void main(String[] args) throws IOException {
		ServerSocket socket = new ServerSocket(8080);

		// Waits for a connection request
		while (true) {
			final Socket connection = socket.accept();
			Runnable task = new Runnable() {
				@Override
				public void run() {
					HandleRequest(connection);
				}
			};
			THREAD_POOL.execute(task);

		}
		

	}

    private static void HandleRequest(Socket s)
    {
        BufferedReader in;
        PrintWriter out;
        String request;
 
        try
        {
            String webServerAddress = s.getInetAddress().toString();
            System.out.println("New Connection:" + webServerAddress);
            in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            
            request = in.readLine();
            
            System.out.println("--- Client request: " + request);
 
            out = new PrintWriter(s.getOutputStream(), true);
            out.println("HTTP/1.0 200");
            if(request.contains(".html")) {
            		out.println("Content-type: text/html");
            }else if(request.contains(".css")) {
            	out.println("Content-type: text/css");
            	
            }else {
            		out.println("Content-type: text/html");
            }
            out.println("Server-name: myserver");
            
            
            
            		
           
            /*	String response = "<html>"
                        + "<head>"
                        + "<title>My Web Server</title></head>"
                        + "<h1>Change the server code so that it can read files!</h1>"
                        + "</html>";*/
            String response;
           try {
            		response = "";
            		File file = new File("//Users//league//git//create-a-server-IanLoam126//" + request.substring(5,request.length()-9));
            	 
            	    BufferedReader br = new BufferedReader(new FileReader(file));
            	 
            	    String st;
            	    while ((st = br.readLine()) != null) {
            	    		response += st +"\n";
            	    }
            	    br.close();
        		}finally {
        	
        		}
           
            	
            out.println("Content-length: " + response.length());
            out.println("");
            out.println(response);
            out.flush();
            out.close();
            s.close();
        }
        catch (IOException e)
        {
            System.out.println("Failed respond to client request: " + e.getMessage());
        }
        finally
        {
            if (s != null)
            {
                try
                {
                    s.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
        return;
    }
 
}