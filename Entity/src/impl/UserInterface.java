/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package impl;

import dto.AddFriendDTO;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import model.User;

/**
 *
 * @author sonht
 */
public interface UserInterface extends Remote {
    public ArrayList<User> getUsers (User user) throws RemoteException;
    
    public User login(User user) throws RemoteException;

    public boolean signup(User user) throws RemoteException;

    public boolean triggerStatus(User user) throws RemoteException;
}
