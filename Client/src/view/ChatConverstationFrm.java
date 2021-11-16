/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package view;

import control.ClientCtr;
import dto.SendMessageDTO;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Objects;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import model.Conversation;
import model.Message;
import model.ObjectWrapper;
import model.User;

/**
 *
 * @author hp
 */
public class ChatConverstationFrm extends javax.swing.JDialog {

    private ClientCtr mySocket;
    private User user;
    Conversation conversation;
    ArrayList<Conversation> listConverstation = new ArrayList<>();
    ArrayList<Message> messages = new ArrayList<>();
    private Frame parent;

    /**
     * Creates new form ChatConverstation
     */
    public ChatConverstationFrm(java.awt.Frame parent, boolean modal, ClientCtr mySocket, User user) {
        super(parent, modal);
        initComponents();
        this.mySocket = mySocket;
        this.user = user;
        this.parent = parent;

        // DEFINE LIST FUNCTION 
        this.mySocket.getActiveFunction().add(new ObjectWrapper(ObjectWrapper.REPLY_CHAT_GET_CONVERSTATION, this));
        this.mySocket.getActiveFunction().add(new ObjectWrapper(ObjectWrapper.REPLY_CHAT_GET_MESSAGE, this));
        this.mySocket.getActiveFunction().add(new ObjectWrapper(ObjectWrapper.REPLY_CHAT_CREATE_MESSAGE, this));
        this.mySocket.getActiveFunction().add(new ObjectWrapper(ObjectWrapper.REPLY_CHAT_CREATE_CONVERSTATION, this));

        // call
        getListConverstation();
    }

    public void getListConverstation() {
        this.mySocket.sendData(new ObjectWrapper(ObjectWrapper.CHAT_GET_CONVERSTATION, this.user.getId()));
    }

    public void receivedDataProcessing(ObjectWrapper data) {
        if (data.getPerformative() == ObjectWrapper.REPLY_CHAT_CREATE_CONVERSTATION) {
            getListConverstation();
        } else {

            ArrayList<Conversation> conversation = (ArrayList<Conversation>) data.getData();
            this.listConverstation = conversation;
            jLabel1.setText("Chat (" + user.getUsername() + ")");
        }

        mapListConverstation();
    }

    void mapListConverstation() {
        this.conversation = null;
        if (listConverstation.size() >= 0) {

            String columns[] = {"name", "type"};
            String[][] values = new String[listConverstation.size()][columns.length];

            for (int i = 0; i < listConverstation.size(); i++) {
                values[i][0] = listConverstation.get(i).getName() + "";
                values[i][1] = listConverstation.get(i).getUsers().size() > 2 ? "group" : "single";
            }

            DefaultTableModel table = new DefaultTableModel(values, columns) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

            jTable1.setModel(table);
        }
    }

    public void getDataMessage() {
        mySocket.sendData(new ObjectWrapper(ObjectWrapper.CHAT_GET_MESSAGE, conversation.getId()));
    }

    public void receivedDataMessageCheck(ObjectWrapper data) {
        // receive new message

        ArrayList<Message> messages = (ArrayList<Message>) data.getData();

        if (messages.size() > 0) {
            Conversation cId = messages.get(0).getConversation();
            if ((Objects.equals(cId.getId(), this.conversation.getId()))) {
                // current converstation 
                this.messages = messages;
                if (messages.size() > 0) {
                    this.conversation = cId;
                    nameConverstation.setText("Name converstation: " + cId.getName());
                }

                mapDataMessage();
            } else {
                // diff conversation
                System.out.println("Alert ");
                ArrayList<Conversation> temp = new ArrayList<Conversation>(this.listConverstation);

                for (int i = 0; i < temp.size(); i++) {
                    if (temp.get(i).getId() == cId.getId()) {
                        temp.remove(i);
                    }
                }
                temp.add(0, cId);
            }
        }

    }

    public void receivedDataMessage(ObjectWrapper data) {
        ArrayList<Message> messages = (ArrayList<Message>) data.getData();

        this.messages = messages;
        if (messages.size() > 0) {
            this.conversation = messages.get(0).getConversation();
            nameConverstation.setText("Name converstation: " + messages.get(0).getConversation().getName());
        }
        mapDataMessage();
    }

    void mapDataMessage() {
        clearMessageText();
        for (int i = 0; i < messages.size(); i++) {
            jTextArea1.append("" + messages.get(i).getUser().getUsername() + " > " + messages.get(i).getContent() + "\n");
            jTextArea1.setCaretPosition(jTextArea1.getDocument().getLength());
        }
    }

    public void sendMessage(String mess) {
        SendMessageDTO sendMessageDTO = new SendMessageDTO();
        sendMessageDTO.setContent(mess);
        sendMessageDTO.setConverstation_id(conversation.getId());
        sendMessageDTO.setUser_id(user.getId());

        mySocket.sendData(new ObjectWrapper(ObjectWrapper.CHAT_CREATE_MESSAGE, sendMessageDTO));
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

        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        nameConverstation = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setText("Chat");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "name", "type"
            }
        ));
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jLabel2.setText("List converstation");

        nameConverstation.setText("Pick one Converstation");

        jButton1.setText("send");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jTextArea1.setEditable(false);
        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane3.setViewportView(jTextArea1);

        jButton2.setText("create");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton2))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 253, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jTextField1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(nameConverstation)
                            .addComponent(jLabel1))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 357, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(nameConverstation)
                    .addComponent(jButton2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 264, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTextField1)
                            .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        // TODO add your handling code here:
        this.conversation = listConverstation.get(jTable1.getSelectedRow());
        nameConverstation.setText("Name converstation: " + conversation.getName());
        clearMessageText();
        getDataMessage();
    }//GEN-LAST:event_jTable1MouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        String messText = jTextField1.getText();

        if (!"".equals(messText) && this.conversation.getId() != null) {
            sendMessage(messText);
        } else {
            JOptionPane.showMessageDialog(rootPane, "Pls, typing or Pick one convertstation");
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        ChatCreateConvertstationFrm chatCreateConvertstationFrm = new ChatCreateConvertstationFrm(parent, rootPaneCheckingEnabled, mySocket, user);
        chatCreateConvertstationFrm.setVisible(true);
        chatCreateConvertstationFrm.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                getListConverstation();
                chatCreateConvertstationFrm.setVisible(false);
            }

        });
    }//GEN-LAST:event_jButton2ActionPerformed

    void clearMessageText() {
        jTextArea1.selectAll();
        jTextArea1.replaceSelection("");
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ChatConverstationFrm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ChatConverstationFrm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ChatConverstationFrm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ChatConverstationFrm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                ChatConverstationFrm dialog = new ChatConverstationFrm(new javax.swing.JFrame(), true, null, null);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JLabel nameConverstation;
    // End of variables declaration//GEN-END:variables
}
