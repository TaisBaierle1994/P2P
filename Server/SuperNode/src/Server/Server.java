package Server;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import fileshared.FileShared;



public class Server implements Runnable{
    Socket csocket;
    
    static volatile ArrayList<FileShared> arquivos = new ArrayList<>();
    
    public Server(Socket csocket){
        this.csocket = csocket;
    }

    public static void main(String[] args) throws Exception{
        ServerSocket welcomeSocket = new ServerSocket(0);
        System.out.println("Listening on port " + welcomeSocket.getLocalPort());
        
        while(true){
            Socket connectionSocket = welcomeSocket.accept();
            new Thread(new Server(connectionSocket)).start();
        }
    }

    @Override
    public void run() {
        ArrayList<FileShared> thing = new ArrayList<>();
        try {
            System.out.println("thread separated");
            BufferedInputStream inFromClient = new BufferedInputStream(csocket.getInputStream());
            DataOutputStream outToClient = new DataOutputStream(csocket.getOutputStream());
            ObjectInputStream objectInput = new ObjectInputStream(csocket.getInputStream());
            ObjectOutputStream objectOutput = new ObjectOutputStream(csocket.getOutputStream());
            
            while (true){
                String s = "";
                for (int i=0; i<4; i++){ 
                    int c = inFromClient.read();
                    s = s + (char)c;
                }
                System.out.println(s);
                if (s.equals("SEND")){
                    //System.out.println("getting fmts!");
                    thing = (ArrayList<FileShared>) objectInput.readObject();
                    for (FileShared file: thing){
                        file.ip = csocket.getLocalAddress().toString().substring(1);
                    }
                    
                    arquivos.addAll(thing);
                } else if (s.equals("DESCONNECT")){
                    arquivos.removeAll(thing);
                    
                } else if (s.equals("LIST")){
                    objectOutput.reset();
                    objectOutput.writeObject(arquivos);
                    
                    for (FileShared f : arquivos){
                        System.out.println(f);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e);
            arquivos.removeAll(thing);
        }
    }
    
}
