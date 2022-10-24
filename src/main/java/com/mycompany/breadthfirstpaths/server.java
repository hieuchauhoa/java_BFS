/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.breadthfirstpaths;

/**
 *
 * @author hieu0
 */


import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.UnknownHostException;

import java.util.Scanner;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hieu0
 */
public class server {
    public static int buffsize =9999;
    public static int port =1234;
    
    public static void main(String[] args) throws SocketException, IOException {
        
            DatagramSocket socket;
            DatagramPacket send, receive;
            String s = "";
            
            try{
            socket=new DatagramSocket(port);
            receive =new DatagramPacket(new byte[buffsize],buffsize);
            
            while(true){
                socket.receive(receive);
                String tmp=new String (receive.getData(),0,receive.getLength());
                System.out.println("Server receive: "+tmp+" from "+receive.getAddress().getHostAddress()+" at port "+socket.getLocalPort());
                if(tmp.equals("bye")){
                    System.out.println("Server socket closed");
                    socket.close();
                    break;                 
                }
                
                Graph grap;
                File myObj = new File(tmp);
                    Scanner myReader = new Scanner(myObj) ;
                    
                    //doc so node cua do thi
                    if(myReader.hasNextLine()){
                        try{
                            grap=new Graph(Integer.parseInt(myReader.nextLine())+1);
                        }catch(NumberFormatException ex){
                            s="File: loi kieu du lieu data cua file text";
                            System.out.println(s);
                            send=new DatagramPacket(s.getBytes(),s.getBytes().length,receive.getAddress(),receive.getPort());
                            System.out.println("Server sent back "+s+" to client");
                            socket.send(send);
                            continue;
                        }   
                    }
                    else{
                            s="File: trong hoac khong doc duoc so node cua do thi";
                            System.out.println(s);
                            send=new DatagramPacket(s.getBytes(),s.getBytes().length,receive.getAddress(),receive.getPort());
                            System.out.println("Server sent back "+s+" to client");
                            socket.send(send);
                            continue;
                    }
                    
                        while (myReader.hasNextLine()) {
                            String str=(myReader.nextLine()).toLowerCase(); 
                            try{
                                String[] arrSplit = str.split(",");
                                int tam1= Integer.parseInt(arrSplit[0]);
                                int tam2= Integer.parseInt(arrSplit[1]);
                                grap.addEdge(tam1, tam2);   
                            }catch(NumberFormatException e){
                                String[] arrSplit = str.split(":");
                                int tam1= Integer.parseInt(arrSplit[0]);
                                int tam2= Integer.parseInt(arrSplit[1]);
                                s=grap.BFS(tam1, tam2);
                            }
                            
                        }
                
                
                
                send=new DatagramPacket(s.getBytes(),s.getBytes().length,receive.getAddress(),receive.getPort());
                System.out.println("Server sent back "+s+" to client");
                socket.send(send);
            }
        } catch (UnknownHostException ex) {
            Logger.getLogger(client.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}

