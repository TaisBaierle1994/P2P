
package Client;

import javax.swing.*;
import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;
import fileshared.FileShared;
import static javax.swing.JOptionPane.ERROR_MESSAGE;


class Downloader implements Runnable{
    
  
    public String filename;
    public String host;
    public int port;
    public Socket socket;
    public String folder;
    
    public Downloader(String filename, String host, int port, String folder) throws Exception{
        this.filename = filename;
        this.host = host;
        this.port = port;
        this.folder = folder;
        System.out.println(filename + " " + host + " " + port);
        this.socket = new Socket(this.host, this.port);
        System.out.println("got socket");
        DataOutputStream output = new DataOutputStream(socket.getOutputStream());
        output.writeChars(filename + "\n");
        
    }

    @Override
    public void run() {
        try {
            
            InputStream in = socket.getInputStream();
            OutputStream out = new FileOutputStream(folder + "\\" + filename);  
            byte[] bytes = new byte[8*1024];
            int count;
            while ((count = in.read(bytes)) > 0) {
                out.write(bytes, 0, count);
            }
            
            JOptionPane.showMessageDialog(null, "DOWNLOAD FEITO COM SUCESSO\nSalvo em: "+ folder);
            out.close();
          
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}

class Wait implements Runnable{
    
    ServerSocket welcome;
    String folder;
    
    public Wait(ServerSocket welcomeSocket, String folder){
        this.welcome = welcomeSocket;
        this.folder = folder;
    }

    @Override
    public void run(){
        try {
            while (true){
                System.out.println("Aguardando na porta " + welcome.getLocalPort());
                Socket socket = welcome.accept();
                System.out.println("ACEITO!! ");
                BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-16")); 
                String s = input.readLine();
                new Thread(new Uploader(socket, s, folder)).start();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}


class Uploader implements Runnable{
    public Socket outSocket;
    public String filename;
    public String folder;
    
    public Uploader(Socket outSocket, String filename, String folder){
        this.outSocket = outSocket;
        this.filename = filename;
        this.folder = folder;
    }

    @Override
    public void run() {
        OutputStream out = null;
        try {
            
            File file = new File(folder + "\\" + filename);
            long length = file.length();
            byte[] bytes = new byte[8 * 1024];
            InputStream in = new FileInputStream(file);
            out = outSocket.getOutputStream();
            int count;
            while ((count = in.read(bytes)) > 0) {
                out.write(bytes, 0, count);
            }
            
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}

public class Frame extends javax.swing.JFrame {

    Cliente client;
    
  
    public Frame() {
        initComponents();
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        startDialog = new javax.swing.JDialog();
        hostPortLabel = new javax.swing.JLabel();
        hostnameTextField = new javax.swing.JTextField();
        portaText = new javax.swing.JTextField();
        directoryTextField = new javax.swing.JTextField();
        directoryButton = new javax.swing.JButton();
        okButton = new javax.swing.JButton();
        directoryChooser = new javax.swing.JFileChooser();
        buscaLabel = new javax.swing.JLabel();
        buscaTexto = new javax.swing.JTextField();
        busca = new javax.swing.JButton();
        foundLabel = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabela = new javax.swing.JTable();
        download = new javax.swing.JButton();

        startDialog.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        startDialog.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        startDialog.setMinimumSize(new java.awt.Dimension(296, 180));
        startDialog.setModalityType(java.awt.Dialog.ModalityType.APPLICATION_MODAL);
        startDialog.setResizable(false);
        startDialog.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                startDialogWindowClosed(evt);
            }
        });

        hostPortLabel.setText("Dgite o ip e porta do Servidor");

        hostnameTextField.setText("localhost");
        hostnameTextField.setToolTipText("Coloque o IP do Server");
        hostnameTextField.setName(""); // NOI18N

        portaText.setText("porta");

        directoryTextField.setEditable(false);

        directoryButton.setText("Buscar");
        directoryButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                directoryButtonActionPerformed(evt);
            }
        });

        okButton.setText("OK");
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout startDialogLayout = new javax.swing.GroupLayout(startDialog.getContentPane());
        startDialog.getContentPane().setLayout(startDialogLayout);
        startDialogLayout.setHorizontalGroup(
            startDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(startDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(startDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(hostPortLabel)
                    .addGroup(startDialogLayout.createSequentialGroup()
                        .addGroup(startDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(directoryTextField, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                            .addComponent(hostnameTextField, javax.swing.GroupLayout.Alignment.LEADING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(startDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(portaText)
                            .addComponent(directoryButton, javax.swing.GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE))))
                .addContainerGap(22, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, startDialogLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(okButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        startDialogLayout.setVerticalGroup(
            startDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(startDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(hostPortLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(startDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(hostnameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(portaText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(36, 36, 36)
                .addGroup(startDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(directoryButton, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(directoryTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(okButton)
                .addContainerGap())
        );

        hostnameTextField.getAccessibleContext().setAccessibleName("");
        hostnameTextField.getAccessibleContext().setAccessibleDescription("");

        directoryChooser.setCurrentDirectory(new java.io.File("C:\\Users\\Tais\\Desktop"));
        directoryChooser.setDialogTitle("");
        directoryChooser.setFileSelectionMode(javax.swing.JFileChooser.DIRECTORIES_ONLY);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));
        setMinimumSize(new java.awt.Dimension(640, 480));
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        buscaLabel.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        buscaLabel.setText("Buscar o caminho dos arquivos para updload:");

        buscaTexto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                buscaTextoKeyPressed(evt);
            }
        });

        busca.setText("Buscar");
        busca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buscaActionPerformed(evt);
            }
        });

        foundLabel.setText("Arquivos Encontrados:");

        tabela.setAutoCreateRowSorter(true);
        tabela.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nome Arquivo", "Tipo", "Tamanho", "Modificado", "IP", "Porta"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.Object.class, java.lang.String.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tabela.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        tabela.setName(""); // NOI18N
        jScrollPane1.setViewportView(tabela);

        download.setText("Download");
        download.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                downloadActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(buscaLabel)
                            .addComponent(foundLabel))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 528, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(buscaTexto, javax.swing.GroupLayout.PREFERRED_SIZE, 524, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(busca, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(download, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(buscaLabel)
                .addGap(11, 11, 11)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buscaTexto, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(busca))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(foundLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 357, Short.MAX_VALUE)
                        .addComponent(download, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        try {
            
            okButton.setEnabled(false);
            directoryChooser.approveSelection();
            client = new Cliente(hostnameTextField.getText(), Integer.parseInt(portaText.getText()));
            
            new Thread(new Wait(client.welcomeSocket, directoryChooser.getCurrentDirectory().getAbsolutePath())).start(); 
            
            ArrayList<FileShared> files =  client.criarLista(directoryChooser.getCurrentDirectory().getAbsolutePath());
            client.envia(files);
            startDialog.setVisible(false);
            this.requestFocus();
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(1);
        }
    }//GEN-LAST:event_okButtonActionPerformed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        
        directoryTextField.setText(directoryChooser.getCurrentDirectory().getAbsolutePath());
        startDialog.setVisible(true);
        startDialog.setLocationRelativeTo(null);
    }//GEN-LAST:event_formWindowOpened

    private void directoryButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_directoryButtonActionPerformed
       
   
        if (directoryChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) { 
            directoryChooser.setCurrentDirectory(directoryChooser.getSelectedFile());
            directoryTextField.setText(directoryChooser.getSelectedFile().getAbsolutePath());
        }
      else {
        JOptionPane.showMessageDialog(null, "Nenhum diretório selecionado", "ERROR", ERROR_MESSAGE);
        }
        
    }//GEN-LAST:event_directoryButtonActionPerformed

    
    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        try {
            
            client.desconecta();
        } catch (Exception ex) {
           ex.printStackTrace();
        }
    }//GEN-LAST:event_formWindowClosed

    private void startDialogWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_startDialogWindowClosed
        
        System.exit(1);
    }//GEN-LAST:event_startDialogWindowClosed

    
    private void downloadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_downloadActionPerformed
       
        int row = tabela.getSelectedRow();
        String filename = (String) tabela.getValueAt(row, 0);
        String ip = (String) tabela.getValueAt(row, 4);
        int port = (int) tabela.getValueAt(row, 5);
        File f = null;
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int opt = fileChooser.showOpenDialog(null);
        if(opt == JFileChooser.APPROVE_OPTION){
            f = fileChooser.getSelectedFile();
        }
        try {
            if(f!=null){
            new Thread(new Downloader(filename, ip, port, f.getAbsolutePath())).start();
            }else{
                JOptionPane.showMessageDialog(null,"SELECIONE UM DIRETÓRIO VÁLIDO","ERROR", ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }   
    }//GEN-LAST:event_downloadActionPerformed

    private void buscaTextoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_buscaTextoKeyPressed
      
        if (evt.getKeyCode() == 10){
            busca.doClick();
        }
    }//GEN-LAST:event_buscaTextoKeyPressed

    private void buscaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buscaActionPerformed
        try {
           
           String keyword = buscaTexto.getText();
            DefaultTableModel model = (DefaultTableModel) tabela.getModel();
            tabela.setModel(model);
            model.setRowCount(0);
            
            ArrayList<FileShared> entries = client.obterLista(keyword);
            for (FileShared entry: entries){
                model.addRow(new Object[]{entry.filename,entry.filetype,(long)entry.filesize, new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new java.util.Date (entry.lastmodified)),entry.ip,entry.port});
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_buscaActionPerformed

 
    public static void main(String args[]) {
       
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Frame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
      
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Frame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton busca;
    private javax.swing.JLabel buscaLabel;
    private javax.swing.JTextField buscaTexto;
    private javax.swing.JButton directoryButton;
    private javax.swing.JFileChooser directoryChooser;
    private javax.swing.JTextField directoryTextField;
    private javax.swing.JButton download;
    private javax.swing.JLabel foundLabel;
    private javax.swing.JLabel hostPortLabel;
    private javax.swing.JTextField hostnameTextField;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton okButton;
    private javax.swing.JTextField portaText;
    private javax.swing.JDialog startDialog;
    private javax.swing.JTable tabela;
    // End of variables declaration//GEN-END:variables
}
