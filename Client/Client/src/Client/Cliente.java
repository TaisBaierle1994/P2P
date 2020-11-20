package Client;

import java.io.*;
import java.net.*;
import java.util.*;
import fileshared.FileShared;

public class Cliente {

    public Socket clientSocket;
    public ObjectOutputStream objectWrite;
    public DataOutputStream outToServer;
    public BufferedReader inFromServer;
    public ObjectInputStream objectRead;
    public ServerSocket welcomeSocket;
    public int portNum;

    public Cliente(String serverHost, int serverPort) throws Exception {
        welcomeSocket = new ServerSocket(0);
        portNum = welcomeSocket.getLocalPort();
        this.clientSocket = new Socket(serverHost, serverPort);
        this.objectWrite = new ObjectOutputStream(clientSocket.getOutputStream());
        this.outToServer = new DataOutputStream(clientSocket.getOutputStream());
        this.inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        this.objectRead = new ObjectInputStream(clientSocket.getInputStream());
    }

    public void envia(ArrayList<FileShared> files) throws Exception {
        outToServer.writeBytes("SEND");
        objectWrite.writeObject(files);
    }

    public void desconecta() throws Exception {
        outToServer.writeBytes("DESCONNECT");

    }

    public ArrayList<FileShared> criarLista(String folderPath) {
        File[] files = new File(folderPath).listFiles();
        ArrayList<FileShared> arquivos = new ArrayList<>();
        for (File file : files) {
            if (file.isFile()) {
                long tamKB = file.length() / 1024;
                long tamMB= tamKB / 1024;
                if (tamMB < 4) {
                    String fileName = file.getName();
                    String extension = "";
                    int i = fileName.lastIndexOf('.');
                    if (i > 0) {
                        extension = fileName.substring(i + 1);
                    }
                    FileShared fmte = new FileShared(fileName, extension, file.length(), file.lastModified(), portNum);
                    arquivos.add(fmte);
                }
            }
        }
        return arquivos;
    }

    public ArrayList<FileShared> obterLista(String keyword) throws Exception {
        outToServer.writeBytes("LIST");

        ArrayList<FileShared> arquivoEntrada = new ArrayList<>();
        ArrayList<FileShared> arquivosFinal = new ArrayList<>();

        arquivoEntrada = (ArrayList<FileShared>) objectRead.readObject();

        for (FileShared i : arquivoEntrada) {
            if (i.filename.toLowerCase().contains(keyword.toLowerCase())) {
                arquivosFinal.add(i);
            }
            System.out.println(i);
        }

        return arquivosFinal;
    }
}
