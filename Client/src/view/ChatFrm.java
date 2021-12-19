/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package view;

import control.ClientCtr;
import dto.SendMessageDTO;
import dto.TypingDTO;
import java.awt.BorderLayout;
import java.awt.Button;
import java.util.ArrayList;
import java.util.Objects;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import model.Conversation;
import model.Message;
import model.ObjectWrapper;
import model.User;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import model.FileMessage;
import javax.swing.JButton;
import javax.swing.JLabel;

/**
 *
 * @author phamthainb
 */
public class ChatFrm extends javax.swing.JFrame {

    private ClientCtr mySocket;
    private User user;
    Conversation conversation;
    ArrayList<Message> messages = new ArrayList<>();
    ArrayList<FileMessage> fileMessages = new ArrayList<>();

    /**
     * Creates new form ChatFrm
     *
     * @param mySocket
     * @param user
     * @param conversation
     */
    public ChatFrm(ClientCtr mySocket, User user, Conversation conversation) {
        initComponents();
        this.mySocket = mySocket;
        this.user = user;
        this.conversation = conversation;
        jLabel1.setText("Chat (" + this.conversation.getName() + ") " + conversation.getId());

        // DEFINE LIST FUNCTION 
        this.mySocket.getActiveFunction().add(new ObjectWrapper(ObjectWrapper.REPLY_CHAT_GET_MESSAGE, this));
        this.mySocket.getActiveFunction().add(new ObjectWrapper(ObjectWrapper.REPLY_CHAT_CREATE_MESSAGE, this));
        this.mySocket.getActiveFunction().add(new ObjectWrapper(ObjectWrapper.REPLY_SEND_FILE, this));
        this.mySocket.getActiveFunction().add(new ObjectWrapper(ObjectWrapper.REPLY_GET_LIST_FILE, this));
        this.mySocket.getActiveFunction().add(new ObjectWrapper(ObjectWrapper.BOARD_TYPING, this));

        getDataMessage();
        getListFile();

        this.setTitle(user.getUsername());

        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                ArrayList<Conversation> list = getListOpen();
                System.out.println("list open size :" + list.size());
                for (int i = 0; i < list.size(); i++) {
                    Conversation get = list.get(i);
                    if (Objects.equals(get.getId(), conversation.getId())) {
                        list.remove(get);
                    }
                }

                updateListOpen(list);
            }
        });
    }

    public ArrayList<Conversation> getListOpen() {
        return this.mySocket.getOpenConversation();
    }

    public ArrayList<Conversation> updateListOpen(ArrayList<Conversation> l) {
        return this.mySocket.updateOpenConversation(l);
    }

    public void getDataMessage() {
        mySocket.sendData(new ObjectWrapper(ObjectWrapper.CHAT_GET_MESSAGE, conversation.getId()));
    }

    public void receivedDataMessage(ObjectWrapper data) {
        ArrayList<Message> mess = (ArrayList<Message>) data.getData();
        //System.out.println("list mess " + mess.size());
        if (mess.size() > 0 && Objects.equals(mess.get(0).getConversation().getId(), conversation.getId())) {
            this.messages = mess;
            mapDataMessage();
        }

    }

    void mapDataMessage() {
        clearMessageText();
        for (int i = 0; i < messages.size(); i++) {
            jTextArea1.append("" + messages.get(i).getUser().getUsername() + " > " + messages.get(i).getContent() + "\n");
            jTextArea1.setCaretPosition(jTextArea1.getDocument().getLength());
        }
    }

    void clearMessageText() {
        jTextArea1.selectAll();
        jTextArea1.replaceSelection("");
    }

    public void sendMessage(String mess) {

        Message m = new Message();
        m.setContent(mess);
        m.setConversation(conversation);
        m.setUser(user);

        mySocket.sendData(new ObjectWrapper(ObjectWrapper.CHAT_CREATE_MESSAGE, m));
        jTextField1.setText("");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenu1 = new javax.swing.JMenu();
        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList<>();
        jLabel2 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        fileInput = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();

        jMenu1.setText("jMenu1");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setText("Chat ()");

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jTextArea1.setEditable(false);
        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane3.setViewportView(jTextArea1);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 499, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 475, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 325, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(14, 14, 14)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 299, Short.MAX_VALUE)
                    .addContainerGap()))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jList1.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(jList1);

        jLabel2.setText("File uploaded");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel2)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addGap(1, 1, 1)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(16, Short.MAX_VALUE))
        );

        jTextField1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextField1FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField1FocusLost(evt);
            }
        });

        jButton1.setText("sent");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        fileInput.setText("file");
        fileInput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fileInputActionPerformed(evt);
            }
        });

        jButton2.setText("down file");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel3.setText("...");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(344, 344, 344)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 310, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(fileInput, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addGap(201, 201, 201)
                        .addComponent(jButton2)
                        .addContainerGap(200, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton2))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel3)))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fileInput, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        String messText = jTextField1.getText();

        if (!"".equals(messText) && this.conversation.getId() != null) {
            sendMessage(messText);
        } else {
            JOptionPane.showMessageDialog(rootPane, "Please typing...");
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void fileInputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fileInputActionPerformed
        // TODO add your handling code here:
        JFileChooser fileChooser = new JFileChooser();
        int option = fileChooser.showOpenDialog(this);

        if (option == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            System.out.println(" file: " + file.getName());
            String encodedFile = encodeFileToBase64(file);
            System.out.println("encodedFile: " + encodedFile);
            // send to server
            FileMessage f = new FileMessage();
            f.setBase64(encodedFile);
            f.setName(file.getName());
            f.setConversation(conversation);
            f.setUser(user);

            mySocket.sendData(new ObjectWrapper(ObjectWrapper.SEND_FILE, f));

        } else {
            System.out.println("Open command canceled");
        }
    }//GEN-LAST:event_fileInputActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        int[] listPicked = jList1.getSelectedIndices();

        if (listPicked.length == 1) {
            int index = listPicked[0];
            FileMessage f = fileMessages.get(index);
            byte[] decodedBytes = Base64.getDecoder().decode(f.getBase64());

            try {
                saveFiletoDisk("/home/phamthainb/Desktop/file-ltm/(" + f.getCreated_at() + ") - " + f.getName(), decodedBytes);
                JOptionPane.showMessageDialog(rootPane, "save file to disk done.");
            } catch (IOException ex) {
                Logger.getLogger(ChatFrm.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else {
            JOptionPane.showMessageDialog(rootPane, "Pick only 1 file.");
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jTextField1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField1FocusGained
        // TODO add your handling code here:
//        TypingDTO typingDTO = new TypingDTO();
//        typingDTO.setUser(user);
//        typingDTO.setConversation(conversation);
//        typingDTO.setTyping(true);

        mySocket.sendData(new ObjectWrapper(ObjectWrapper.GET_LIST_FILE, true));

    }//GEN-LAST:event_jTextField1FocusGained

    private void jTextField1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField1FocusLost
        // TODO add your handling code here:
//        TypingDTO typingDTO = new TypingDTO();
//        typingDTO.setUser(user);
//        typingDTO.setConversation(conversation);
//        typingDTO.setTyping(false);

        mySocket.sendData(new ObjectWrapper(ObjectWrapper.GET_LIST_FILE, false));
    }//GEN-LAST:event_jTextField1FocusLost
    private static void saveFiletoDisk(String fileOutput, byte[] bytes) throws IOException {

        try ( FileOutputStream fos = new FileOutputStream(fileOutput)) {
            fos.write(bytes);
            System.out.println("save file to disk done.");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void replySendFile(ObjectWrapper data) {
        boolean mess = (boolean) data.getData();
        //System.out.println("list mess " + mess.size());
        if (mess) {
            System.out.println("send success");
            getListFile();
        } else {
            System.out.println("send error");
        }
    }

    public void getListFile() {
        mySocket.sendData(new ObjectWrapper(ObjectWrapper.GET_LIST_FILE, conversation));
    }

    public void replyGetListFile(ObjectWrapper data) {

        ArrayList<FileMessage> mess = (ArrayList<FileMessage>) data.getData();
        //System.out.println("list file mess >>> " + mess.size());
        this.fileMessages = mess;

        DefaultListModel model = new DefaultListModel();
        jList1.setModel(model);
        model.removeAllElements();

        for (FileMessage mes : mess) {
            model.addElement(mes.getName() + " - " + mes.getUser().getUsername());
        }
    }

    public void boardTyping(ObjectWrapper data) {
        boolean res = (boolean) data.getData();
        System.out.println("----typing");
        if (res) {
            jLabel3.setText("Some one is typing...");
        } else {
            jLabel3.setText("");
        }
    }

    private static String encodeFileToBase64(File file) {
        String encodedfile = null;
        try {
            FileInputStream fileInputStreamReader = new FileInputStream(file);
            byte[] bytes = new byte[(int) file.length()];
            fileInputStreamReader.read(bytes);
            encodedfile = Base64.getEncoder().encodeToString(bytes);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("convert file got err");
        }

        return encodedfile;
    }
    /**
     * @param args the command line arguments
     */
//    public static void main(String args[]) {
//        /* Set the Nimbus look and feel */
//        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
//        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
//         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
//         */
//        try {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        } catch (ClassNotFoundException ex) {
//            java.util.logging.Logger.getLogger(ChatFrm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(ChatFrm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(ChatFrm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(ChatFrm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//
//        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new ChatFrm().setVisible(true);
//            }
//        });
//    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton fileInput;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JList<String> jList1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}