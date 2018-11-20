/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tugasjarkom;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Michael
 */
public class ServerRev {

    public static void main(String[] args) {
        ServerRev server = new ServerRev(5000);
    }
    
    //declare attribute 
    //private Socket          socket   = null; 
    private ServerSocket    server   = null; 
    private DataInputStream in       =  null; 
    private DataOutputStream out     = null;
    private LinkedList<Socket> clientSocket = null;
    private LinkedList<DataInputStream> inList = null;
    private LinkedList<DataOutputStream> outList = null;
    private boolean stop = false;
    private String line = ""; 
  
    // constructor with port 
    public ServerRev(int port){ 
        try{ // starts server and waits for a connection 
            server = new ServerSocket(port); 
            System.out.println("Server started"); 
            System.out.println("Waiting for a client ..."); 

            //Inisialisasi atribut
            ServerThread serverThread = new ServerThread(this.server);
            this.clientSocket = new LinkedList<Socket>();
            this.inList = new LinkedList<DataInputStream>();
            this.outList = new LinkedList<DataOutputStream>();
            serverThread.start();
            
            // reads message from clients until "Over" is sent 
            while (!line.equalsIgnoreCase("Over")){ //System.out.println("MASUK");
                try{ 
                    int bnykSocket = this.clientSocket.size(); System.out.println(bnykSocket);
                    for(int i=0; i<bnykSocket; i++){
                        line = inList.get(i).readUTF(); System.out.println("\nAccepted from client: "+(i+1)+" : "+ line);
                        broadcast(i,line);
                    }
                } 
                catch(Exception i){ System.out.println(i); } 
            } 
             
            // close connection 
            System.out.println("Closing connection");
            stopAll();
        } 
        catch(Exception i){ System.out.println(i); }
    } 
    
    /////////////////////*** FROM HERE TO BELOW IS METHOD ***///////////////////////////////////////////////
    public void removeSocket(int i){
        try {
            this.clientSocket.get(i).close(); this.clientSocket.remove(i);
            this.inList.get(i).close(); this.inList.remove(i);
            this.outList.get(i).close(); this.outList.remove(i);
        } catch (Exception ex) {}
    }
    
    public void stopAll(){
        int bnykSocket = this.clientSocket.size();
        for(int i = 0;i<bnykSocket; i++){
            removeSocket(i);
        }
        this.stop = true;
    }
    
    public void addSocket(Socket s){
        try {
            // Input from the client socket
            inList.add(new DataInputStream(
                    new BufferedInputStream(s.getInputStream()) 
            ) );
            
            // Output to the socket 
            outList.add(new DataOutputStream(s.getOutputStream())
            ); 
        } catch (Exception ex) {}
        this.clientSocket.add(s);
    }
    
    public void broadcast(int idx, String pesan){ //idx = idx dari client yg mau nge broadcast
        for(int i=0; i<this.clientSocket.size(); i++){ 
            try {
                //ngirim ke smua, kecuali diri sendiri
                if(idx != i){ System.out.println("BROADCAST to idx: "+i);
                    outList.get(i).writeUTF(pesan); System.out.println("Finish broadcast to idx: "+i);
                } 
            } catch (Exception ex) {}
        }
    }
    
    ////////////////////*** FROM HERE TO BELOW IS THREAD ***////////////////////////////////////////////////////////////
    private class ServerThread extends Thread { //Thread untuk menunggu kalau ada client yang connect
        Socket client=null;
        private ServerSocket server = null; 
        int jmlhClient;
        
        ServerThread(ServerSocket ss) {
            this.server = ss;
            this.jmlhClient = 0;
        }

        @Override
        public void run() {
            try{
                while(true){
                    client = server.accept(); 
                    addSocket(client); //tambahkan client yang membuat sambungan/socketnya ke LinkedList
                    System.out.println("Client accepted");
                    jmlhClient++;
                    System.out.println("Number of clients: "+jmlhClient);
                    if(stop) break;
                }
            } 
            catch(Exception ex){
                System.out.println("Error : "+ex);
            }
        }
    }
}
