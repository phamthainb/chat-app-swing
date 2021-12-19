/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import javax.swing.JFrame;

/**
 *
 * @author phamthainb
 */
public class MulJFrame implements Runnable {

    JFrame theFrame;

    public MulJFrame(JFrame jFrame) {
        this.theFrame = jFrame;
    }

    @Override
    public void run() {
        theFrame.setVisible(true);
    }
}
