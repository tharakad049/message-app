package controller;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class LoginAndRegFormController {
    public Pane pnSignUp;
    public TextField txtRegname;
    public TextField txtRegEmail;
    public TextField txtRegPassword;
    public TextField txtRegPhoneNo;
    public Button getStarted;
    public RadioButton male;
    public ToggleGroup Gender;
    public RadioButton female;
    public TextField txtRegFirstname;
    public Label controlRegLabel;
    public Label success;
    public Label goBack;
    public Pane pnSignIn;
    public Label loginNotifier;
    public Button btnSignUp;
    public PasswordField txtPassword;
    public TextField txtUsername;

    public static String username, password, gender;
    public static ArrayList<User> loggedInUser = new ArrayList<>();
    public static ArrayList<User> users = new ArrayList<User>();

    public void forgetPassword(ActionEvent actionEvent) {
    }

    public void login(ActionEvent actionEvent) {
        username = txtUsername.getText();
        password = txtPassword.getText();
        boolean login = false;
        for (User x : users) {
            if (x.name.equalsIgnoreCase(username) && x.password.equalsIgnoreCase(password)) {
                login = true;
                loggedInUser.add(x);
                System.out.println(x.name);
                gender = x.gender;
                break;
            }
        }
        if (login) {
            changeWindow();
        } else {
            loginNotifier.setOpacity(1);
        }
    }

    public void handleButtonAction(ActionEvent actionEvent) {
        if (actionEvent.getSource().equals(btnSignUp)) {
            new FadeTransition().play();
            pnSignUp.toFront();
        }
        if (actionEvent.getSource().equals(getStarted)) {
            new FadeTransition().play();
            pnSignIn.toFront();
        }
        loginNotifier.setOpacity(0);
        txtUsername.setText("");
        txtPassword.setText("");
    }
    private void changeWindow() {
        try {
            Stage stage = (Stage) txtUsername.getScene().getWindow();
            URL resource = this.getClass().
                    getResource("ChatRoomBox.fxml");
            Parent load = FXMLLoader.load(resource);
            Scene scene= new Scene(load);

            stage.setScene(scene);
            stage.show();
            stage.setTitle(username + "");
            stage.setOnCloseRequest(event -> {
                System.exit(0);
            });
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void registration(ActionEvent actionEvent) {
        if (!txtRegname.getText().equalsIgnoreCase("")
                && !txtRegPassword.getText().equalsIgnoreCase("")
                && !txtRegEmail.getText().equalsIgnoreCase("")
                && !txtRegFirstname.getText().equalsIgnoreCase("")
                && !txtRegPhoneNo.getText().equalsIgnoreCase("")
                && (male.isSelected() || female.isSelected())) {
            if (checkUser(txtRegname.getText())) {
                if (checkEmail(txtRegEmail.getText())) {
                    User newUser = new User();
                    newUser.name = txtRegname.getText();
                    newUser.password = txtRegPassword.getText();
                    newUser.email = txtRegEmail.getText();
                    newUser.fullName = txtRegFirstname.getText();
                    newUser.phoneNo = txtRegPhoneNo.getText();
                    if (male.isSelected()) {
                        newUser.gender = "Male";
                    } else {
                        newUser.gender = "Female";
                    }
                    users.add(newUser);
                    goBack.setOpacity(1);
                    success.setOpacity(1);
                    makeDefault();
                }
            }
        }
    }
    private void makeDefault() {
        txtRegname.setText("");
        txtRegPassword.setText("");
        txtRegEmail.setText("");
        txtRegFirstname.setText("");
        txtRegPhoneNo.setText("");
        male.setSelected(true);

    }
    private boolean checkUser(String username) {
        for(User user : users) {
            if(user.name.equalsIgnoreCase(username)) {
                return false;
            }
        }
        return true;
    }
    private boolean checkEmail(String email) {
        for(User user : users) {
            if(user.email.equalsIgnoreCase(email)) {
                return false;
            }
        }
        return true;
    }
}
