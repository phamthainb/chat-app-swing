/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package impl;

import dto.RequestDTO;
import java.util.ArrayList;
import model.User;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author sonht
 */
public interface FriendInterface extends Remote {

    public ArrayList<User> getFriends(Long myId) throws RemoteException;

    public Long addFriend(ArrayList<Long> ids) throws RemoteException;

    public ArrayList<RequestDTO> getRequests(Long id) throws RemoteException;

    public Long confirmFriend(RequestDTO requestDTO) throws RemoteException;

    public Long declineFriend(Long friendId, Long toId) throws RemoteException;
}
