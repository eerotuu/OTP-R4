/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.r4.matkapp.mvc.controller;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;

/**
 *
 * @author Eero
 */
public abstract class AbstractSceneController implements SceneController {

    protected AbstractSceneController owner;
    
    public AbstractSceneController(AbstractSceneController owner) {
        this.owner = owner;
    }
    
    protected Parent loadFXML(String fileName, SceneController controller) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/" + fileName + ".fxml"));
        
        if(controller != null) {
            loader.setController(controller);
        }

        return loader.load();
    }
    
    protected abstract void update();
}