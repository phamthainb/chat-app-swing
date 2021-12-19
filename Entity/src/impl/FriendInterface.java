/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package impl;

import dto.RequestDTO;
import java.util.ArrayList;
import model.User;
import model.Friend;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author sonht
 */
public interface FriendInterface extends Remote {

    public ArrayList<Friend> getFriends(Long myId) throws RemoteException;

    public Long addFriend(Friend friend) throws RemoteException;

    public ArrayList<Friend> getRequests(Long id) throws RemoteException;

    public Long confirmFriend(Friend friend) throws RemoteException;

    public Long declineFriend(Friend friend) throws RemoteException;
    
    public Long cancelFriend(Friend friend) throws RemoteException;
    
    public Long deleteFriend(Friend friend) throws RemoteException;

    public Friend getFriend(Friend friend) throws RemoteException;
    
    // chat 
    public ArrayList<Friend> getChatFriend(User u) throws RemoteException;
}
