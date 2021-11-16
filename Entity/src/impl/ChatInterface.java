package impl;

import dto.SendMessageDTO;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import model.Conversation;
import model.Message;
import model.User;

/**
 *
 * @author phamthainb
 */
public interface ChatInterface extends Remote {

    public ArrayList<Conversation> getConverstation(Long id) throws RemoteException;

    // get all user in one converstation
    public ArrayList<User> getUserConverstation(Long converstation_id) throws RemoteException;
    // 
    public boolean createConverstation(ArrayList<User> list) throws RemoteException;
    // message
    public ArrayList<Message> getMessageConverstation(Long converstation_id) throws RemoteException;

    public boolean sendMessage(SendMessageDTO sendMessageDTO) throws RemoteException;

}
