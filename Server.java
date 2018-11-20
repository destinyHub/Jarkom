/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tugasjarkom;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Michael
 */
public class Server {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Server server = new Server(5000);
    }
    
    
    //initialize socket and input stream 
    private Socket          socket   = null; 
    private ServerSocket    server   = null; 
    private DataInputStream in       =  null; 
    private DataOutputStream out     = null;
    private Socket[] clientSocket = null;
  
    // constructor with port 
    public Server(int port) 
    { 
        // starts server and waits for a connection 
        try
        { 
            server = new ServerSocket(port); 
            System.out.println("Server started"); 
  
            System.out.println("Waiting for a client ..."); 
  
            socket = server.accept(); 
            System.out.println("Client accepted");
            
            //TES
            int portClient = socket.getPort();
            int counter = 0;
            this.clientSocket = new Socket[2];
            this.clientSocket[counter] = this.socket; counter++;
  
            // takes input from the client socket 
            in = new DataInputStream( 
                new BufferedInputStream(socket.getInputStream())); 
  
            // TES sends output to the socket 
            out    = new DataOutputStream(clientSocket[0].getOutputStream()); 
            
            String line = ""; 
            
            //TES
            String temp = "";
  
            // reads message from client until "Over" is sent 
            while (!line.equalsIgnoreCase("Over")) 
            { 
                try
                { 
                    //Print incoming chat from client
                    line = in.readUTF(); 
                    System.out.println(line); 
                    
                    //Broadcast to clients
                    temp = "'"+line+"'";
                    out.writeUTF("Accepted ,U Sent: "+temp);
                } 
                catch(Exception i) 
                { 
                    System.out.println(i); 
                } 
            } 
            System.out.println("Closing connection"); 
  
            // close connection 
            socket.close(); 
            in.close(); 
        } 
        catch(Exception i) 
        { 
            System.out.println(i); 
        } 
    } 
}
