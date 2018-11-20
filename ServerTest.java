package Jarkom;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author SHUBHAM
 */
public class ServerTest {

    int port;
    ServerSocket server=null;
    Socket[] client=null;
    ExecutorService pool = null;
    int clientcount=0;
    
    public static void main(String[] args) throws IOException {
        ServerTest serverobj=new ServerTest(5000);
        serverobj.startServer();
    }
    
    ServerTest(int port){
        this.port=port;
        pool = Executors.newFixedThreadPool(5);
    }

    public void startServer() throws IOException {
        
        server=new ServerSocket(5000);
        Scanner sc = new Scanner(System.in);
        System.out.println("Server Booted");
        System.out.println("Any client can stop the server by sending -1");
        System.out.print("Jumlah client : ");
        int jumlah_client = sc.nextInt();
        this.client = new Socket[jumlah_client];
        while(true)
        {
            client[clientcount]=server.accept();
            clientcount++;
//            ServerThread runnable= new ServerThread(client,clientcount,this);
//            pool.execute(runnable);
            if (clientcount==jumlah_client){
                break;
            }
        }
        for(int i = 0 ; i<jumlah_client ; i++){
            ServerThread runnable= new ServerThread(client,i,this);
            pool.execute(runnable);
        }
        
    }

    private static class ServerThread implements Runnable {
        
        ServerTest server=null;
        Socket[] client=null;
        BufferedReader cin;
        PrintStream cout;
        Scanner sc=new Scanner(System.in);
        int id;
        String s;
        
        ServerThread(Socket[] client, int count ,ServerTest server ) throws IOException {
            
            this.client=client;
            this.server=server;
            this.id=count;
            System.out.println("Connection "+id+" established with client "+client[id]);
            
            
        }

        @Override
        public void run() {
            int x=1;
         try{
         while(true){
               cin=new BufferedReader(new InputStreamReader(client[id].getInputStream()));
               s=cin.readLine();
//               cout=new PrintStream(client.getOutputStream());
  			
			System. out.print("Client("+id+") :"+s+"\n");
//			System.out.print("Server : ");
			//s=stdin.readLine();
//                            s=sc.nextLine();
                        if (s.equalsIgnoreCase("bye"))
                        {
                            cout.println("BYE");
                            x=0;
                            System.out.println("Connection ended by server");
                            break;
                        }
                        for (Socket record : client){
                            cout=new PrintStream(record.getOutputStream());
                            cout.print("Client : "+record);
                            cout.println(s);
                        }
		}
		
                for (Socket record : client){
                    record.close();
                }
                cin.close();
                cout.close();
                if(x==0) {
			System.out.println( "Server cleaning up." );
			System.exit(0);
                }
         } 
         catch(IOException ex){
             System.out.println("Error : "+ex);
         }
            
 		
        }
    }
    
}
