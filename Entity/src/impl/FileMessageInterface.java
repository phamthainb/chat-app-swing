/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package impl;

import java.lang.reflect.Array;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import model.Conversation;
import model.FileMessage;
import model.Message;

/**
 *
 * @author phamthainb
 */
public interface FileMessageInterface extends Remote{

    public boolean saveFile(FileMessage f) throws RemoteException;

    public ArrayList<FileMessage> getListFile(Conversation c) throws RemoteException;
}
