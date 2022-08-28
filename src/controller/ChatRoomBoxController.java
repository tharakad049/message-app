package controller;

import javafx.fxml.Initializable;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class ChatRoomBoxController extends Thread implements Initializable {
    public Label clientName;
    public TextArea msgRoom;
    public TextField msgField;
    public Pane chat;

    BufferedReader reader;
    PrintWriter writer;
    Socket socket;

    public void connectSocket() {
        try {
            socket = new Socket("localhost", 8003);
            System.out.println("Socket is connected with server!");
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);
            this.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                String msg = reader.readLine();
                String[] tokens = msg.split(" ");
                String cmd = tokens[0];
                System.out.println(cmd);
                StringBuilder fullMessage = new StringBuilder();
                for(int i = 1; i < tokens.length; i++) {
                    fullMessage.append(tokens[i]);
                }
                System.out.println(fullMessage);
                if (cmd.equalsIgnoreCase(LoginAndRegFormController.username + ":")) {
                    continue;
                }else if(fullMessage.toString().equalsIgnoreCase("bye")) {
                    break;
                }
                msgRoom.appendText(msg + "\n");
            }
            reader.close();
            writer.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void selectImage(MouseEvent mouseEvent) {
    }

    public void send() {
        String msg = msgField.getText();
        writer.println(LoginAndRegFormController.username + ": " + msg);
        msgRoom.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
        msgRoom.appendText("Me: " + msg + "\n");
        msgField.setText("");
        if(msg.equalsIgnoreCase("BYE") || (msg.equalsIgnoreCase("logout"))) {
            System.exit(0);
        }
    }


    public void handleSendEvent(MouseEvent mouseEvent) {
        send();
        for(User user : LoginAndRegFormController.users) {
            System.out.println(user.name);
        }
    }

    public void sendMessageByKey(KeyEvent keyEvent) {
        if (keyEvent.getCode().toString().equals("ENTER")) {
            send();
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        clientName.setText(LoginAndRegFormController.username);
        connectSocket();
    }
}
