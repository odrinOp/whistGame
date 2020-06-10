package com.utils;

import com.AppException;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

public class SceneManager {

    private Stage mainStage;
    private Map<ApplicationState, Scene> scenes;
    private ApplicationState currentState;

    public SceneManager(Stage stage,Map<ApplicationState, Scene> scenes) {
        this.mainStage = stage;
        this.scenes = scenes;
        currentState = null;
    }

    public SceneManager(Stage stage){
        this.mainStage = stage;
        this.scenes = new HashMap<>();
    }


    public void addScene(ApplicationState state, Parent view){
        scenes.put(state,new Scene(view));
        if(currentState == null)
            currentState = state;
    }

    public void dropScene(ApplicationState state){
        scenes.remove(state);
        if(currentState == state)
            currentState = null;
    }

    public void changeActiveScene(ApplicationState state) throws AppException {
        Scene newScene = scenes.get(state);
        if(newScene == null)
            throw new AppException("Scene with title '" +state.toString() + "' doesn't exists!" );

        mainStage.setScene(newScene);
        currentState = state;
    }

    public void show(){
        mainStage.show();
    }

    public void hide(){
        mainStage.hide();
    }

    public void setOnCloseRequest(EventHandler event){
        mainStage.setOnCloseRequest(event);
    }

    public ApplicationState getCurrentScene(){
        return currentState;
    }

    public void setTitle(String nickname) {
        mainStage.setTitle(nickname);
    }
}
