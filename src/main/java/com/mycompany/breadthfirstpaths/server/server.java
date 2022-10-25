/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.breadthfirstpaths.server;

/**
 *
 * @author hieu0
 */


import com.mycompany.breadthfirstpaths.common.Graph;
import com.mycompany.breadthfirstpaths.client.client;
import com.mycompany.breadthfirstpaths.common.FileInfo;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
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
public class server {
    private static final int PIECES_OF_FILE_SIZE = 1024 * 32;
    public static int buffsize =9999;
    public static int port =1234;
    
    public static void main(String[] args) throws SocketException, IOException {
        
            DatagramSocket socket;
            DatagramPacket send, receive;
            String s = "";
            
            try{
            server sv=new server();   
            socket=new DatagramSocket(port);
            receive =new DatagramPacket(new byte[buffsize],buffsize);
            while(true){
                sv.receiveFile();  
                System.out.println("File transferred");                
                socket.receive(receive);
                String tmp=new String (receive.getData(),0,receive.getLength());
                System.out.println("Server receive: "+tmp+" from "+receive.getAddress().getHostAddress()+" at port "+socket.getLocalPort());
                if(tmp.equals("bye")){
                    System.out.println("Server socket closed");
                    socket.close();
                    break;                 
                }
                
                Graph grap;
                String path_file_name=sv.file_name(tmp);
                File myObj = new File("C:\\Users\\hieu0\\Desktop\\LTM_thay_Giang\\project\\BreadthFirstPaths\\src\\main\\java\\com\\mycompany\\breadthfirstpaths\\server\\file_text\\"+path_file_name);
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
    
    public void receiveFile() throws SocketException {
        byte[] receiveData = new byte[PIECES_OF_FILE_SIZE];
        DatagramPacket receivePacket;
        DatagramSocket socket;
            DatagramPacket send, receive;
            socket=new DatagramSocket(8800);
        
        try {
            // get file info
            receivePacket = new DatagramPacket(receiveData, receiveData.length);
            socket.receive(receivePacket);
            InetAddress inetAddress = receivePacket.getAddress();
            ByteArrayInputStream bais = new ByteArrayInputStream(receivePacket.getData());
            ObjectInputStream ois = new ObjectInputStream(bais);
            FileInfo fileInfo = (FileInfo) ois.readObject();
            // show file info
            if (fileInfo != null) {
                System.out.println("File name: " + fileInfo.getFilename());
                System.out.println("File size: " + fileInfo.getFileSize());
                System.out.println("Pieces of file: " + fileInfo.getPiecesOfFile());
                System.out.println("Last bytes length: " + fileInfo.getLastByteLength());
            }
            // get file content
            System.out.println("Receiving file...");
            File fileReceive = new File(fileInfo.getDestinationDirectory() 
                    + fileInfo.getFilename());
            BufferedOutputStream bos = new BufferedOutputStream(
                    new FileOutputStream(fileReceive));
            // write pieces of file
            for (int i = 0; i < (fileInfo.getPiecesOfFile() - 1); i++) {
                receivePacket = new DatagramPacket(receiveData, receiveData.length, 
                        inetAddress, port);
                socket.receive(receivePacket);
                bos.write(receiveData, 0, PIECES_OF_FILE_SIZE);
            }
            // write last bytes of file
            receivePacket = new DatagramPacket(receiveData, receiveData.length, 
                    inetAddress, port);
            socket.receive(receivePacket);
            bos.write(receiveData, 0, fileInfo.getLastByteLength());
            bos.flush();
            System.out.println("Done!");
            socket.close();

            // close stream
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    public String file_name(String path){
        String s="";
        for(int i=path.length()-1;i>=0;i--){
            if(path.charAt(i)=='\\'){
                while(true){  //lay ten file
                    i++;
                    try{
                      s+=path.charAt(i);  
                    }catch(Exception ex){
                        break;
                    }                 
                }
                break;             
                }
            }
        return s;
    }
}

