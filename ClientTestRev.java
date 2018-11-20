/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tugasjarkom;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

/**
 *
 * @author Michael
 */
public class ClientTestRev {
    
    public static void main(String args[])
    {
        ClientTestRev ct = new ClientTestRev("127.0.0.1",6000);
    }
    
    //ATRIBUT
    Socket sk;
    BufferedReader sin;
    PrintStream sout;
    BufferedReader stdin;
    String s;
    boolean stop = false;
    
    // constructor 
    public ClientTestRev(String address, int port){ 
        try{
            this.sk = new Socket("127.0.0.1",6000);
            this.sin = new BufferedReader(new InputStreamReader(sk.getInputStream())); 
            this.sout = new PrintStream(sk.getOutputStream());
            this.stdin = new BufferedReader(new InputStreamReader(System.in));
            
            TerimaPesan tp = new TerimaPesan(sk);
            tp.start();
            
            while ( true ) {
                s=sin.readLine();
                System.out.print(s+"\n");
                if ( s.equalsIgnoreCase("BYE") ) {
                   System.out.println("Connection ended by client");
                   break;
                }
            }
            sk.close();
            sin.close();
            sout.close();
            stdin.close();
        }
        catch(Exception e){}
    } 
    
    ////////////////////*** FROM HERE TO BELOW IS THREAD ***////////////////////////////////////////////////////////////
    private class TerimaPesan extends Thread { //Thread untuk menunggu kalau ada client yang connect
        Socket socket=null;
        int jmlhClient;
        String show;
        
        TerimaPesan(Socket s) {
            this.socket = s;
            this.jmlhClient = 0;
        }

        @Override
        public void run() {
            try{
                while(true){
                    show=stdin.readLine();
                    if(!show.equalsIgnoreCase("")){
                        System.out.print(sk+" : ");
                        sout.println(show);
                    }
                }
            } 
            catch(Exception ex){
                System.out.println("Error : "+ex);
            }
        }
    }
}
