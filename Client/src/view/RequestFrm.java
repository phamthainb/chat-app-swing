/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package view;

import control.ClientCtr;
import dto.RequestDTO;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;
import model.ObjectWrapper;

/**
 *
 * @author sonht
 */
public class RequestFrm extends javax.swing.JDialog {

    /**
     * Creates new form RequestFrm
     */
    private ClientCtr mySocket;
    private Long id;
    private int selectedIndex = -1;
    private ArrayList<RequestDTO> requests = new ArrayList<>();

    public RequestFrm(java.awt.Frame parent, boolean modal, ClientCtr mySocket, Long id) {
        super(parent, modal);
        initComponents();
        this.mySocket = mySocket;
        this.id = id;
        acceptBtn.setEnabled(false);
        declineBtn.setEnabled(false);
        this.mySocket.getActiveFunction().add(new ObjectWrapper(ObjectWrapper.REPLY_CONFIRM_FRIEND, this));
        this.mySocket.getActiveFunction().add(new ObjectWrapper(ObjectWrapper.REPLY_GET_REQUESTS, this));
        this.mySocket.getActiveFunction().add(new ObjectWrapper(ObjectWrapper.REPLY_DECLINE_FRIEND, this));
        this.mySocket.getActiveFunction().add(new ObjectWrapper(ObjectWrapper.REPLY_ADD_FRIEND, this));

        getRequests();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    void getRequests() {
        this.mySocket.sendData(new ObjectWrapper(ObjectWrapper.GET_REQUESTS, id));
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        requestTbl = new javax.swing.JTable();
        usernameTxt = new javax.swing.JLabel();
        acceptBtn = new javax.swing.JButton();
        declineBtn = new javax.swing.JToggleButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setText("Requests");

        requestTbl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Requests"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        requestTbl.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                requestTblMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(requestTbl);

        acceptBtn.setText("Accept");
        acceptBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                acceptBtnActionPerformed(evt);
            }
        });

        declineBtn.setText("Decline");
        declineBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                declineBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(81, 81, 81)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(usernameTxt)
                            .addComponent(acceptBtn)
                            .addComponent(declineBtn))))
                .addContainerGap(109, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabel1)
                .addGap(41, 41, 41)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(usernameTxt)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(acceptBtn)))
                .addGap(18, 18, 18)
                .addComponent(declineBtn)
                .addContainerGap(246, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void acceptBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_acceptBtnActionPerformed
        if (selectedIndex != -1) {
            mySocket.sendData(new ObjectWrapper(ObjectWrapper.CONFIRM_FRIEND, requests.get(this.selectedIndex)));
        }
    }//GEN-LAST:event_acceptBtnActionPerformed

    private void requestTblMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_requestTblMouseClicked
        this.selectedIndex = requestTbl.getSelectedRow();
        acceptBtn.setEnabled(true);
        declineBtn.setEnabled(true);
        if (this.selectedIndex != -1 && this.requests.size() > 0) {
            usernameTxt.setText(requests.get(selectedIndex).getUsername());
        }
    }//GEN-LAST:event_requestTblMouseClicked

    private void declineBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_declineBtnActionPerformed
        if (selectedIndex != -1) {
            RequestDTO input = new RequestDTO();
            input.setFriendId(requests.get(this.selectedIndex).getFriendId());
            input.setToId(requests.get(this.selectedIndex).getFromId());
            mySocket.sendData(new ObjectWrapper(ObjectWrapper.DECLINE_FRIEND, input));
        }
    }//GEN-LAST:event_declineBtnActionPerformed

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
            java.util.logging.Logger.getLogger(RequestFrm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(RequestFrm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(RequestFrm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(RequestFrm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                RequestFrm dialog = new RequestFrm(new javax.swing.JFrame(), true, null, null);
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

    private void mapToTable() {
        acceptBtn.setEnabled(false);
        declineBtn.setEnabled(false);
        usernameTxt.setText("");

        if (requests.size() >= 0) {
            String columns[] = {"No.", "Username"};
            String[][] values = new String[requests.size()][columns.length];
            for (int i = 0; i < requests.size(); i++) {
                values[i][0] = requests.get(i).getFriendId() + "";
                values[i][1] = requests.get(i).getUsername() + "";
            }
            DefaultTableModel table = new DefaultTableModel(values, columns) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            requestTbl.setModel(table);
        }
    }

    public void receivedDataProcessing(ObjectWrapper data) {
        // reply get list requests
        if (data.getPerformative() == ObjectWrapper.REPLY_GET_REQUESTS) {
            this.requests = (ArrayList<RequestDTO>) data.getData();
            mapToTable();
        }
//        reply add friend
        if (data.getPerformative() == ObjectWrapper.REPLY_ADD_FRIEND) {
            getRequests();
            mapToTable();
        }
//        reply confirm
        if (data.getPerformative() == 8 || data.getPerformative() == 31) {
//            String mess = (String) data.getData();
            getRequests();
            mapToTable();
        }
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton acceptBtn;
    private javax.swing.JToggleButton declineBtn;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable requestTbl;
    private javax.swing.JLabel usernameTxt;
    // End of variables declaration//GEN-END:variables
}