/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import dto.AddFriendDTO;
import java.io.EOFException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import model.IPAddress;
import model.ObjectWrapper;
import model.User;
import view.ServerMainFrm;
import dto.RequestDTO;
import dto.SendMessageDTO;
import dto.StatusDTO;
import java.util.Objects;
import model.Conversation;
import model.Message;

public class ServerCtr {

    private ServerMainFrm view;
    private ServerSocket myServer;
    private ServerListening myListening;
    private ArrayList<ServerProcessing> myProcess;
    private IPAddress myAddress = new IPAddress("localhost", 8888);
    private RmiClient rmiClient;

    public ServerCtr(ServerMainFrm view) {
        myProcess = new ArrayList<ServerProcessing>();
        this.view = view;
        openServer();
    }

    public ServerCtr(ServerMainFrm view, int serverPort) {
        myProcess = new ArrayList<ServerProcessing>();
        this.view = view;
        myAddress.setPort(serverPort);
        openServer();
    }

    private void openServer() {
        try {
            myServer = new ServerSocket(myAddress.getPort());
            myListening = new ServerListening();
            myListening.start();
            myAddress.setHost(InetAddress.getLocalHost().getHostAddress());
            view.showServerInfor(myAddress);
            view.showMessage("TCP server is running at the port " + myAddress.getPort() + "...");
            rmiClient = new RmiClient(view);
            rmiClient.init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopServer() {
        try {
            for (ServerProcessing sp : myProcess) {
                sp.stop();
            }
            myListening.stop();
            myServer.close();
            view.showMessage("TCP server is stopped!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void publicClientNumber() {
        ObjectWrapper data = new ObjectWrapper(ObjectWrapper.SERVER_INFORM_CLIENT_NUMBER, myProcess.size());
        for (ServerProcessing sp : myProcess) {
            sp.sendData(data);
        }
    }

    public void broadcastClientStatus(Long id, int status) {
        ObjectWrapper data = new ObjectWrapper(ObjectWrapper.REPLY_TRIGGER_STATUS, new StatusDTO(id, status));
        for (ServerProcessing sp : myProcess) {
            if (sp.getUser() != null && !sp.getUser().getId().equals(id)) {
                sp.sendData(data);
            }
        }
    }

    /**
     * The class to listen the connections from client, avoiding the blocking of
     * accept connection
     *
     */
    class ServerListening extends Thread {

        public ServerListening() {
            super();
        }

        public void run() {
            view.showMessage("server is listening... ");
            try {
                while (true) {
                    Socket clientSocket = myServer.accept();
                    ServerProcessing sp = new ServerProcessing(clientSocket);
                    sp.start();
                    myProcess.add(sp);
                    view.showMessage("Number of client connecting to the server: " + myProcess.size() + " >" + sp.getName() + "> " + sp.getState() + " >" + sp.getId());
                    publicClientNumber();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * The class to treat the requirement from client
     *
     */
    class ServerProcessing extends Thread {

        private Socket mySocket;
        private User user;
        //private ObjectInputStream ois;
        //private ObjectOutputStream oos;

        public ServerProcessing(Socket s) {
            super();
            mySocket = s;
        }

        public void sendData(Object obj) {
            try {
                ObjectOutputStream oos = new ObjectOutputStream(mySocket.getOutputStream());
                oos.writeObject(obj);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void run() {
            String mess = "";
            boolean booleanRes = false;
            ArrayList<User> users = new ArrayList<>();
            ArrayList<RequestDTO> requests = new ArrayList<>();
            ArrayList<User> friends = new ArrayList<>();
            ArrayList<Conversation> cons = new ArrayList<>();
            ArrayList<Message> messList = new ArrayList<>();

            SendMessageDTO sendMessageDTO = null;
            User resUser = null;
            User inputUser = null;

            ArrayList<AddFriendDTO> userList = new ArrayList<>();
            ArrayList<Long> ids = new ArrayList<>();
            Long fromId = null;
            Long toId = null;
            RequestDTO inputRequest = null;

            try {
                while (true) {
                    view.showMessage("server reply: " + mySocket.getChannel());

                    ObjectInputStream ois = new ObjectInputStream(mySocket.getInputStream());
                    ObjectOutputStream oos = new ObjectOutputStream(mySocket.getOutputStream());
                    Object o = ois.readObject();

                    if (o instanceof ObjectWrapper) {

                        ObjectWrapper data = (ObjectWrapper) o;

                        System.out.print("SERVER_REPLY: ");
                        System.out.println(data.getPerformative());

                        switch (data.getPerformative()) {

                            case ObjectWrapper.LOGIN_USER:
                                resUser = rmiClient.remoteLogin((User) data.getData());
                                this.user = resUser;
                                oos.writeObject(new ObjectWrapper(ObjectWrapper.REPLY_LOGIN_USER, resUser));
                                break;

                            case ObjectWrapper.SIGNUP_USER:
                                booleanRes = rmiClient.remoteSignup((User) data.getData());
                                oos.writeObject(new ObjectWrapper(ObjectWrapper.REPLY_SIGNUP_USER, booleanRes));
                                break;

                            case ObjectWrapper.GET_LIST_USER:
                                resUser = (User) data.getData();
                                userList = (ArrayList<AddFriendDTO>) rmiClient.remoteGetUsers(resUser.getId(), resUser.getUsername());
                                if (userList.size() >= 0) {
                                    oos.writeObject(new ObjectWrapper(ObjectWrapper.REPLY_GET_LIST_USER, userList));
                                }
                                break;

                            case ObjectWrapper.ADD_FRIEND:
                                ids = (ArrayList<Long>) data.getData();
                                Long userId2 = rmiClient.remoteAddFriend(ids);
                                oos.writeObject(new ObjectWrapper(ObjectWrapper.REPLY_ADD_FRIEND, !Objects.equals(null, userId2)));

                                if (!Objects.equals(userId2, null)) {
                                    for (ServerProcessing sp : myProcess) {
                                        if (Objects.equals(sp.getUser().getId(), userId2)) {
                                            sp.sendData(new ObjectWrapper(ObjectWrapper.REPLY_ADD_FRIEND, true));
                                        }
                                    }
                                }
                                break;

                            case ObjectWrapper.GET_REQUESTS:
                                requests = (ArrayList<RequestDTO>) rmiClient.remoteGetRequests((Long) data.getData());
                                oos.writeObject(new ObjectWrapper(ObjectWrapper.REPLY_GET_REQUESTS, requests));
                                break;

                            case ObjectWrapper.CONFIRM_FRIEND:

                                fromId = rmiClient.remoteConfirmFriend((RequestDTO) data.getData());

                                oos.writeObject(new ObjectWrapper(ObjectWrapper.REPLY_CONFIRM_FRIEND, !Objects.equals(null, fromId)));
                                if (!Objects.equals(null, fromId)) {
                                    for (ServerProcessing sp : myProcess) {
                                        if (Objects.equals(sp.getUser().getId(), fromId)) {
                                            sp.sendData(new ObjectWrapper(ObjectWrapper.REPLY_CONFIRM_FRIEND, true));
                                        }
                                    }
                                }
                                break;

                            case ObjectWrapper.DECLINE_FRIEND:
                                inputRequest = (RequestDTO) data.getData();
                                toId = rmiClient.remoteDeclineFriend(inputRequest.getFriendId(), inputRequest.getToID());
                                if(toId != null) {
                                    oos.writeObject(new ObjectWrapper(ObjectWrapper.REPLY_DECLINE_FRIEND, true));
                                    for (ServerProcessing sp : myProcess) {
                                        if (Objects.equals(sp.getUser().getId(), toId)) {
                                            sp.sendData(new ObjectWrapper(ObjectWrapper.REPLY_DECLINE_FRIEND, true));
                                        }
                                    }
                                }
                                break;

                            case ObjectWrapper.GET_LIST_FRIEND:

                                friends = (ArrayList<User>) rmiClient.remoteGetFriends((Long) data.getData());
                                oos.writeObject(new ObjectWrapper(ObjectWrapper.REPLY_GET_LIST_FRIEND, friends));
                                break;

                            case ObjectWrapper.TRIGGER_STATUS:

                                inputUser = (User) data.getData();
                                booleanRes = rmiClient.remoteTriggerStatus(inputUser.getId(), inputUser.getOnline());
                                oos.writeObject(new ObjectWrapper(ObjectWrapper.REPLY_TRIGGER_STATUS, booleanRes));
                                if (booleanRes) {
                                    broadcastClientStatus(this.getUser().getId(), 1);
                                }
                                break;

                            // CHAT Key
                            case ObjectWrapper.CHAT_GET_CONVERSTATION: {

                                cons = rmiClient.remoteGetListConverstation((Long) data.getData());
                                System.out.println("conversation "+ cons.size());
                                oos.writeObject(new ObjectWrapper(ObjectWrapper.REPLY_CHAT_GET_CONVERSTATION, cons));
                                break;
                            }

                            case ObjectWrapper.CHAT_GET_MESSAGE: {
                                messList = rmiClient.remoteGetListMessage((Long) data.getData());

                                oos.writeObject(new ObjectWrapper(ObjectWrapper.REPLY_CHAT_GET_MESSAGE, messList));
                                break;
                            }

                            case ObjectWrapper.CHAT_CREATE_MESSAGE: {
                                booleanRes = rmiClient.remoteSendMessage((SendMessageDTO) data.getData());
                                sendMessageDTO = (SendMessageDTO) data.getData();

                                ArrayList<Message> messages = new ArrayList<>();
                                if (booleanRes) {
                                    messages = rmiClient.remoteGetListMessage((sendMessageDTO.getConverstation_id()));
                                    ArrayList<User> listUser = rmiClient.remoteGetAllUsersInConversation(sendMessageDTO.getConverstation_id());
                                    // loop all process connected
                                    for (ServerProcessing sp : myProcess) {
                                        // is other process
                                        if (sp.mySocket.getOutputStream() != mySocket.getOutputStream()) {
                                            // check process inside of converstation
                                            for (User userMember : listUser) {
                                                if (Objects.equals(userMember.getId(), sp.user.getId())) {
                                                    sp.sendData(new ObjectWrapper(ObjectWrapper.REPLY_CHAT_GET_MESSAGE, messages));
                                                }
                                            }
                                        } else {
                                            // is client call process
                                            oos.writeObject(new ObjectWrapper(ObjectWrapper.REPLY_CHAT_CREATE_MESSAGE, messages));
                                        }
                                    }
                                } else {
                                    System.out.println("Send message got errr");
                                }

                                break;
                            }

                            case ObjectWrapper.CHAT_GET_LIST_FRIEND: {
                                users = rmiClient.remoteGetFriends((Long) data.getData());

                                oos.writeObject(new ObjectWrapper(ObjectWrapper.REPLY_CHAT_GET_LIST_FRIEND, users));
                                break;
                            }

                            case ObjectWrapper.CHAT_CREATE_CONVERSTATION: {
                                users = (ArrayList<User>) data.getData();
                                ids.clear();
                                for (User u : users) {
                                    ids.add(u.getId());
                                }
                                booleanRes = (boolean) rmiClient.remoteCreateConverstation(users);
                                if (booleanRes) {
                                    for (ServerProcessing sp : myProcess) {
                                        if (!sp.getUser().getId().equals(this.user.getId()) && ids.indexOf(sp.getUser().getId()) != -1) {
                                            sp.sendData(new ObjectWrapper(ObjectWrapper.REPLY_CHAT_CREATE_CONVERSTATION, booleanRes));
                                        }
                                    }
                                }
                                oos.writeObject(new ObjectWrapper(ObjectWrapper.REPLY_CHAT_CREATE_CONVERSTATION, booleanRes));
                                break;
                            }
                        }

                    }
                    //ois.reset();
                    //oos.reset();
                }
            } catch (EOFException | SocketException e) {
                //e.printStackTrace();
                myProcess.remove(this);
                view.showMessage("Number of client connecting to the server: " + myProcess.size());
                publicClientNumber();
                if (this.getUser() != null) {
                    boolean res = rmiClient.remoteTriggerStatus(this.getUser().getId(), 0);
                    if (res) {
                        System.out.println("CLIENT DISCONNECTED: " + this.getUser().getId());
                        broadcastClientStatus(this.getUser().getId(), 0);
                        this.user = null;
                    }
                }
                try {
                    mySocket.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                this.stop();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }

    }
}
