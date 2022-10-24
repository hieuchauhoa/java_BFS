/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.breadthfirstpaths;

/**
 *
 * @author hieu0
 */
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hieu0
 */
public class client {

    public static int destPort =1234;
    public static String hostname=  "localhost";
    public static void main(String[] args) throws SocketException, IOException {
        try {
            DatagramSocket socket;
            DatagramPacket send, receive;
            InetAddress add;
            Scanner stdIn;
            add= InetAddress.getByName(hostname);
            socket =new DatagramSocket();
            stdIn=new Scanner(System.in);
            while(true){
                System.out.print("Client input: ");
                String tmp =stdIn.nextLine();
                byte[] data=tmp.getBytes();
                send= new DatagramPacket(data,data.length,add,destPort);
                System.out.println("Client sent "+tmp+" to "+add.getHostAddress()+" form port "+socket.getLocalPort());
                socket.send(send);
                if(tmp.equals("bye")){
                    System.out.println("Client socket closed");
                    stdIn.close();
                    socket.close();
                    break;
                    
                }
                
                receive =new DatagramPacket(new byte[9999],9999);
                socket.receive(receive);
                tmp=new String (receive.getData(),0,receive.getLength());
                System.out.println("Client get: "+tmp+" from server");
            }
        } catch (UnknownHostException ex) {
            Logger.getLogger(client.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
}

