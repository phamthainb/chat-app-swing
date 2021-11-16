/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package impl;

import dto.AddFriendDTO;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 *
 * @author sonht
 */
public interface UserInterface extends Remote {
    public ArrayList<AddFriendDTO> getUsers (Long myId, String username) throws RemoteException;
}
