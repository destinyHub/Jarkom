/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tugasjarkom;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

/**
 *
 * @author Michael
 */
public class Client {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Client client = new Client("127.0.0.1", 5000); 
        
    }
    
    
    // initialize socket and input output streams 
    private Socket socket            = null; 
    private DataInputStream  input   = null; 
    private DataInputStream  in   = null; 
    private DataOutputStream out     = null; 
  
    // constructor to put ip address and port 
    public Client(String address, int port) 
    { 
        // establish a connection 
        try
        { 
            socket = new Socket(address, port); 
            System.out.println("Connected to server with address: "+address+" and port: "+port); 
  
            System.out.println("Chat to server: ");
            // takes input from terminal 
            input  = new DataInputStream(System.in); 
  
            // sends output to the socket 
            out    = new DataOutputStream(socket.getOutputStream()); 
            
            //Tes Receive From Server
            in = new DataInputStream( 
                new BufferedInputStream(socket.getInputStream())); 
        } 
        catch(Exception u) 
        { 
            System.out.println(u); 
        } 
        
  
        // string to read message from input 
        String line = ""; 
        String tes = "";
  
        // keep reading until "Over" is input 
        while (!line.equalsIgnoreCase("Over")) 
        { 
            try
            { 
                line = input.readLine(); 
                out.writeUTF(line); 
                
                //Tes receive
                tes = in.readUTF();
                if(!tes.equals("")) System.out.println(tes);
            } 
            catch(Exception i) 
            { 
                System.out.println(i); 
            } 
        } 
  
        // close the connection 
        try
        { 
            input.close(); 
            out.close(); 
            socket.close(); 
        } 
        catch(Exception i) 
        { 
            System.out.println(i); 
        } 
    } 
}
