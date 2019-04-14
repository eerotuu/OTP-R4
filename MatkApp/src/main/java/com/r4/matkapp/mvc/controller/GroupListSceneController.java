/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.r4.matkapp.mvc.controller;

import com.r4.matkapp.mvc.model.Group;
import com.r4.matkapp.mvc.view.alertfactory.AlertFactory;
import com.r4.matkapp.mvc.view.alertfactory.InformationAlert;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author Eero
 */
public class GroupListSceneController implements Initializable {
    
    // vanha toteutus ryhmä listalle, voinee poistaa joskus koko luokan
    
    @FXML
    VBox groupListPane;

    @FXML
    ScrollPane groupsScroll;

    @FXML
    Label text;

    private GroupSceneController expenseController;
    
    private Group selectedGroup;
    
    Set<Group> userGroups;

    /**
     *
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        generateGroupList();
    }

    private void generateGroupList() {

        userGroups = UserController.getLoggedUser().getGroup();
        for(Group g : userGroups) {
            Button b = new Button(g.getGroup_name());
            b.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {

                    BorderPane root = (BorderPane) ((Node) event.getSource()).getScene().getRoot();

                    try {
                        FXMLLoader loader = new FXMLLoader();
                        Node n = loader.load(getClass().getResource("/fxml/GroupScene.fxml").openStream());
                        expenseController = loader.getController();
                        root.setCenter(n);
                        expenseController.setActiveGroup(g);
                        expenseController.setTitleText(g.getGroup_name());
                        selectedGroup = g;
                    } catch (IOException ex) {
                        Logger.getLogger(GroupListSceneController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
            b.setMaxWidth(Double.MAX_VALUE);
            b.setMinHeight(50);

            groupListPane.getChildren().add(b);
        }
        // horisontaali scrollbar vittuun
        groupsScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    }

    @FXML
    private void createNewGroup() {
        
        Group g = inputDialog();
        String name = g.getGroup_name();
        if (name != null) {
            
            // Luo uuden buttonin ryhmälle -> voi muuttaa jos jaksaa ryhmien listat 
            // observable listaks, nii ei tartteis tätä.
            Button b = new Button(name);
            b.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    BorderPane root = (BorderPane) ((Node) event.getSource()).getScene().getRoot();

                    try {
                        FXMLLoader loader = new FXMLLoader();
                        Node n = loader.load(getClass().getResource("/fxml/GroupScene.fxml").openStream());
                        expenseController = loader.getController();
                        root.setCenter(n);
                        expenseController.setActiveGroup(g);
                        expenseController.setTitleText(name);
                    } catch (IOException ex) {
                        Logger.getLogger(GroupListSceneController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
            b.setMaxWidth(Double.MAX_VALUE);
            b.setMinHeight(50);
            groupListPane.getChildren().add(b);
        }

    }

    // kysy tiedot ryhmälle eli vain ryhmän nimi -> palauttaa inviten
    // vois joskus toteuttaa paremmin
    private Group inputDialog() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Luo uusi ryhmä");
        dialog.setContentText("Syötä ryhmän nimi:");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            UserController u = new UserController();
            Group g = u.createGroup(result.get());
            if (g.getInvite() != null) {
                AlertFactory f = new InformationAlert();
                f.createAlert(null, "Ryhmän luonti onnistui!", g.getInvite());
                return g;
            }
        }
        return null;
    }

}
