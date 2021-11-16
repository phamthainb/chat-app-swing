/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package impl;

import java.rmi.RemoteException;
import model.User;
import java.rmi.Remote;

/**
 *
 * @author sonht
 */
public interface AuthInterface extends Remote {
    public User login (User user) throws RemoteException;
    public boolean signup(User user) throws RemoteException;
    public void triggerStatus(Long id, int status) throws RemoteException;
}
