/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.breadthfirstpaths.client;

/**
 *
 * @author hieu0
 */
import com.mycompany.breadthfirstpaths.common.FileInfo;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
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
    private static final int PIECES_OF_FILE_SIZE = 1024 * 32;
    public static int destPort =1234;
    public static String hostname=  "localhost";
    public static void main(String[] args) throws SocketException, IOException {
        try {
            client cli =new client();
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
                cli.sendFile(tmp, "C:\\Users\\hieu0\\Desktop\\LTM_thay_Giang\\project\\BreadthFirstPaths\\src\\main\\java\\com\\mycompany\\breadthfirstpaths\\server\\file_text\\");
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
    
    private void sendFile(String sourcePath, String destinationDir) throws SocketException, UnknownHostException {
        DatagramSocket socket;
            socket =new DatagramSocket();
        InetAddress inetAddress;
        DatagramPacket sendPacket;

        try {
            File fileSend = new File(sourcePath);
            InputStream inputStream = new FileInputStream(fileSend);
            BufferedInputStream bis = new BufferedInputStream(inputStream);
            inetAddress = InetAddress.getByName(hostname);
            byte[] bytePart = new byte[PIECES_OF_FILE_SIZE];
            
            // get file size
            long fileLength = fileSend.length();
            int piecesOfFile = (int) (fileLength / PIECES_OF_FILE_SIZE);
            int lastByteLength = (int) (fileLength % PIECES_OF_FILE_SIZE);

            // check last bytes of file
            if (lastByteLength > 0) {
                piecesOfFile++;
            }

            // split file into pieces and assign to fileBytess
            byte[][] fileBytess = new byte[piecesOfFile][PIECES_OF_FILE_SIZE];
            int count = 0;
            while (bis.read(bytePart, 0, PIECES_OF_FILE_SIZE) > 0) {
                fileBytess[count++] = bytePart;
                bytePart = new byte[PIECES_OF_FILE_SIZE];
            }

            // read file info
            FileInfo fileInfo = new FileInfo();
            fileInfo.setFilename(fileSend.getName());
            fileInfo.setFileSize(fileSend.length());
            fileInfo.setPiecesOfFile(piecesOfFile);
            fileInfo.setLastByteLength(lastByteLength);
            fileInfo.setDestinationDirectory(destinationDir);

            // send file info
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(fileInfo);
            sendPacket = new DatagramPacket(baos.toByteArray(), baos.toByteArray().length,
                    inetAddress, 8800);
            socket.send(sendPacket);

            // send file content
            System.out.println("Sending file...");
            // send pieces of file
            for (int i = 0; i < (count - 1); i++) {
                sendPacket = new DatagramPacket(fileBytess[i], PIECES_OF_FILE_SIZE,
                        inetAddress, 8800);
                socket.send(sendPacket);
                waitMillisecond(40);
            }
            // send last bytes of file
            sendPacket = new DatagramPacket(fileBytess[count - 1], PIECES_OF_FILE_SIZE,
                    inetAddress, 8800);
            socket.send(sendPacket);
            waitMillisecond(40);

            // close stream
            bis.close();
            socket.close();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Sent.");
    }

    /**
     * sleep program in millisecond
     * 
     * @param millisecond
     */
    public void waitMillisecond(long millisecond) {
        try {
            Thread.sleep(millisecond);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
}

