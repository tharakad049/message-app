package controller;

import com.madeorsk.emojisfx.EmojisLabel;
import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.NodeOrientation;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ResourceBundle;

public class ChatRoomBoxController extends Thread implements Initializable {
    public Label clientName;
    public TextArea msgRoom;
    public TextField msgField;
    public Pane chat;
    public VBox vbox;
    public File file;

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
                HBox hBox = new HBox();
                hBox.setSpacing(10);
                AnchorPane anchorPane = new AnchorPane();
                anchorPane.setStyle("-fx-background-radius: 10px; -fx-background-color: #d3d9d3;");

                EmojisLabel label = new EmojisLabel();

                label.setText(msg);

                label.setMaxWidth(200);
                String mgsText = msg;
                Text text = new Text(mgsText);
                double width = text.getLayoutBounds().getWidth();
                label.setPrefWidth(width + 25);
                label.setStyle("-fx-alignment: center ; -fx-font-size: 16 ; -fx-font-family: 'Arial';");
                label.setPadding(new Insets(5, 5, 5, 5));
                anchorPane.getChildren().add(label);

                anchorPane.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
                hBox.getChildren().add(anchorPane);

                Platform.runLater(
                        () -> {
                            vbox.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
                            vbox.getChildren().add(hBox);
                        }
                );
            }
            reader.close();
            writer.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void selectImage(MouseEvent mouseEvent) {
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        final FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image");
        file = fileChooser.showOpenDialog(stage);

        Label label = new Label();
        Text text = new Text("Me");
        Image image = new Image("assets/icons8-camera-96.png");
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(100);
        imageView.setPreserveRatio(true);
        label.setGraphic(imageView);

        vbox.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
        vbox.getChildren().add(imageView);
        writer.println(LoginAndRegFormController.username + ": " + vbox.getChildren().add(text));

    }

    public void send() {
        String msg = msgField.getText();
        writer.println(LoginAndRegFormController.username + ": " + msg);
        HBox hBox = new HBox();
        hBox.setSpacing(10);
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setStyle("-fx-background-radius: 10px; -fx-background-color: #5fd3dc;");

        EmojisLabel label = new EmojisLabel();

        label.setText(msg);

        label.setMaxWidth(300);
        String mgsText = msgField.getText();
        final Text text = new Text(mgsText);
        final double width = text.getLayoutBounds().getWidth();
        System.out.println(width);
        label.setPrefWidth(width + 50);
        label.setStyle("-fx-alignment: center ; -fx-font-size: 16 ; -fx-font-family: 'Arial';");
        label.setPadding(new Insets(5, 5, 5, 5));
        anchorPane.getChildren().add(label);

        anchorPane.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
        hBox.setPrefWidth(476);

        Pane pane = new Pane();
        pane.setPrefSize(400, 20);

        hBox.getChildren().addAll(pane, anchorPane);
        vbox.getChildren().add(hBox);
        msgField.clear();
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

    public void handleSendImojiEvent(MouseEvent mouseEvent) {
        byte[] emojiByteCode = new byte[]{(byte) 0xF0, (byte) 0x9F, (byte) 0x98, (byte) 0x81};
        String emoji = new String(emojiByteCode, StandardCharsets.UTF_8);
        msgField.setText(msgField.getText() + " " + emoji);
    }
}
