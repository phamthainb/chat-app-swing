/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

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
import model.Friend;
import model.User;
import view.ServerMainFrm;
import dto.RequestDTO;
import dto.SendMessageDTO;
import dto.StatusDTO;
import dto.TypingDTO;
import java.util.List;
import java.util.Objects;
import model.Conversation;
import model.FileMessage;
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
            ArrayList<Friend> requests = new ArrayList<>();
            ArrayList<Friend> friends = new ArrayList<>();
            ArrayList<Conversation> cons = new ArrayList<>();
            ArrayList<Message> messList = new ArrayList<>();

            SendMessageDTO sendMessageDTO = null;
            User resUser = null;
            User inputUser = null;

            ArrayList<Long> ids = new ArrayList<>();
            Long fromId = null;
            Long toId = null;
            RequestDTO inputRequest = null;

            Friend fr = null;

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
                                inputUser = (User) data.getData();
                                users = (ArrayList<User>) rmiClient.remoteGetUsers(inputUser);
                                if (users.size() >= 0) {
                                    oos.writeObject(new ObjectWrapper(ObjectWrapper.REPLY_GET_LIST_USER, users));
                                }
                                break;

                            case ObjectWrapper.ADD_FRIEND:
                                fr = (Friend) data.getData();
                                Long userId2 = rmiClient.remoteAddFriend(fr);
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
                                requests = (ArrayList<Friend>) rmiClient.remoteGetRequests((Long) data.getData());
                                oos.writeObject(new ObjectWrapper(ObjectWrapper.REPLY_GET_REQUESTS, requests));
                                break;

                            case ObjectWrapper.CONFIRM_FRIEND:
                                fromId = rmiClient.remoteConfirmFriend((Friend) data.getData());
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
                                fr = (Friend) data.getData();
                                toId = rmiClient.remoteDeclineFriend(fr);
                                if (toId != null) {
                                    oos.writeObject(new ObjectWrapper(ObjectWrapper.REPLY_DECLINE_FRIEND, true));
                                    for (ServerProcessing sp : myProcess) {
                                        if (Objects.equals(sp.getUser().getId(), toId)) {
                                            sp.sendData(new ObjectWrapper(ObjectWrapper.REPLY_DECLINE_FRIEND, true));
                                        }
                                    }
                                }
                                break;

                            case ObjectWrapper.CANCEL_FRIEND:
                                fr = (Friend) data.getData();
                                toId = rmiClient.remoteCancelFriend(fr);
                                if (toId != null) {
                                    oos.writeObject(new ObjectWrapper(ObjectWrapper.REPLY_CANCEL_FRIEND, true));
                                    for (ServerProcessing sp : myProcess) {
                                        if (Objects.equals(sp.getUser().getId(), toId)) {
                                            sp.sendData(new ObjectWrapper(ObjectWrapper.REPLY_CANCEL_FRIEND, true));
                                        }
                                    }
                                }
                                break;

                            case ObjectWrapper.DELETE_FRIEND:
                                fr = (Friend) data.getData();
                                toId = rmiClient.remoteDeleteFriend(fr);
                                if (toId != null) {
                                    oos.writeObject(new ObjectWrapper(ObjectWrapper.REPLY_DELETE_FRIEND, true));
                                    for (ServerProcessing sp : myProcess) {
                                        if (Objects.equals(sp.getUser().getId(), toId)) {
                                            sp.sendData(new ObjectWrapper(ObjectWrapper.REPLY_DELETE_FRIEND, true));
                                        }
                                    }
                                }
                                break;

                            case ObjectWrapper.GET_LIST_FRIEND:
                                friends = (ArrayList<Friend>) rmiClient.remoteGetFriends((Long) data.getData());
                                oos.writeObject(new ObjectWrapper(ObjectWrapper.REPLY_GET_LIST_FRIEND, friends));
                                break;

                            case ObjectWrapper.GET_FRIEND_BY_USER:
                                fr = (Friend) rmiClient.remoteGetFriend((Friend) data.getData());
                                oos.writeObject(new ObjectWrapper(ObjectWrapper.REPLY_GET_FRIEND_BY_USER, fr));
                                break;

                            case ObjectWrapper.TRIGGER_STATUS:
                                inputUser = (User) data.getData();
                                booleanRes = rmiClient.remoteTriggerStatus(inputUser);
                                oos.writeObject(new ObjectWrapper(ObjectWrapper.REPLY_TRIGGER_STATUS, booleanRes));
                                if (booleanRes) {
                                    broadcastClientStatus(this.getUser().getId(), 1);
                                }
                                break;

                            // CHAT Key
                            case ObjectWrapper.CHAT_GET_CONVERSTATION:
                                List<Conversation> listCon;
                                listCon = rmiClient.remoteGetListConverstation((Long) data.getData());
                                System.out.println("conversation " + listCon.size());
                                oos.writeObject(new ObjectWrapper(ObjectWrapper.REPLY_CHAT_GET_CONVERSTATION, listCon));
                                break;

                            case ObjectWrapper.CHAT_GET_MESSAGE:
                                messList = rmiClient.remoteGetListMessage((Long) data.getData());

                                oos.writeObject(new ObjectWrapper(ObjectWrapper.REPLY_CHAT_GET_MESSAGE, messList));
                                break;

                            case ObjectWrapper.CHAT_CREATE_MESSAGE:
                                // save to db
                                booleanRes = rmiClient.remoteSendMessage((Message) data.getData());
                                Message m = (Message) data.getData();

                                ArrayList<Message> messages = new ArrayList<>();
                                if (booleanRes) {
                                    messages = rmiClient.remoteGetListMessage((m.getConversation().getId()));
                                    ArrayList<User> listUser = rmiClient.remoteGetAllUsersInConversation(m.getConversation().getId());

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
                                            // push open chatFrm
                                            for (User userMember : listUser) {
                                                if (Objects.equals(userMember.getId(), sp.user.getId())) {
                                                    sp.sendData(new ObjectWrapper(ObjectWrapper.REPLY_CHAT_CREATE_MESSAGE_DASHBOARD, messages));
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

                            case ObjectWrapper.CHAT_GET_LIST_FRIEND:
                                friends = rmiClient.remoteGetChatFriends((User) data.getData());
                                System.out.println("------get list friend done " + friends.size());
                                oos.writeObject(new ObjectWrapper(ObjectWrapper.REPLY_CHAT_GET_LIST_FRIEND, friends));
                                break;

                            case ObjectWrapper.CHAT_CREATE_CONVERSTATION:
                                List<User> us;
                                us = (List<User>) data.getData();
                                booleanRes = (boolean) rmiClient.remoteCreateConverstation(us);

                                if (booleanRes) {
                                    ids.clear();
                                    for (User u : us) {
                                        ids.add(u.getId());
                                    }

                                    for (ServerProcessing sp : myProcess) {
                                        // sent to other
                                        if (!sp.getUser().getId().equals(this.user.getId()) && ids.indexOf(sp.getUser().getId()) != -1) {
                                            sp.sendData(new ObjectWrapper(ObjectWrapper.REPLY_CHAT_CREATE_CONVERSTATION, booleanRes));
                                        }
                                    }
                                }

                                oos.writeObject(new ObjectWrapper(ObjectWrapper.REPLY_CHAT_CREATE_CONVERSTATION, booleanRes));
                                break;

                            // FILE ----
                            case ObjectWrapper.SEND_FILE:
                                FileMessage f = (FileMessage) data.getData();
                                System.out.println("file >>" + f.getName());
                                booleanRes = (boolean) rmiClient.remoteSaveFile(f);

                                oos.writeObject(new ObjectWrapper(ObjectWrapper.REPLY_SEND_FILE, booleanRes));
                                break;

                            case ObjectWrapper.GET_LIST_FILE:
                                Conversation c = (Conversation) data.getData();
                                ArrayList<FileMessage> fileMessages;
                                fileMessages = rmiClient.remoteGetListFile(c);

                                ArrayList<User> listUserCC = rmiClient.remoteGetAllUsersInConversation(c.getId());

                                for (ServerProcessing sp : myProcess) {
                                    if (sp.mySocket.getOutputStream() != mySocket.getOutputStream()) {
                                        for (User userMember : listUserCC) {
                                            if (Objects.equals(userMember.getId(), sp.user.getId())) {
                                                sp.sendData(new ObjectWrapper(ObjectWrapper.REPLY_GET_LIST_FILE, fileMessages));
                                            }
                                        }

                                    } else {
                                        oos.writeObject(new ObjectWrapper(ObjectWrapper.REPLY_GET_LIST_FILE, fileMessages));
                                    }
                                }
                                break;

                            case ObjectWrapper.SEND_TYPING:
                                TypingDTO typinggg = (TypingDTO) data.getData();

                                for (ServerProcessing sp : myProcess) {
                                    if (sp.mySocket.getOutputStream() != mySocket.getOutputStream()) {
                                        sp.sendData(new ObjectWrapper(ObjectWrapper.BOARD_TYPING, typinggg));
                                    } else {
                                        oos.writeObject(new ObjectWrapper(ObjectWrapper.BOARD_TYPING, typinggg));
                                    }
                                }
                                break;

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
                    this.user.setOnline(0);
                    boolean res = rmiClient.remoteTriggerStatus(this.user);
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
