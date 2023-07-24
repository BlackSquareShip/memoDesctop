package com.example.demo4;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;

import java.io.IOException;

import static com.example.demo4.LoginController.*;

public class HelloController {

    @FXML
    private Label firstCard;

    @FXML
    private Label fourCard;

    @FXML
    private Label secondCard;

    @FXML
    private Label thirdCard;
    @FXML
    private Label welcomeText;
    @FXML
    private Label situationLabel;
    @FXML
    private HBox cardBox;

    @FXML
    void initialize() throws ParseException, IOException {
        class GetDataThread extends Thread {
            public void run() {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            doUpdate();
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
            }
        }
        GetDataThread getDataThread = new GetDataThread();
        getDataThread.start();
        AnimationTimer timerForTab = new AnimationTimer() {
            int frameCount = 0;
            @Override
            public void handle(long now) {
                if (frameCount % 100 == 0) getDataThread.run();
                frameCount++;
            }
        };
        timerForTab.start();
    }

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

    @FXML
    protected void sendCard(ActionEvent event) throws IOException {
        setPostRequest(servUrl + "/setCard","{\"id\": " + userId + ", \"name\" : \"" + ((Button)event.getSource()).getUserData() + "\"}" );
    }

    @FXML
    protected void requestCard(ActionEvent event) throws IOException, ParseException {
        String images = setPostRequest(servUrl + "/getMemes","{\"id\": " + userId + "}" );
        System.out.println(images);
        JSONParser parser = new JSONParser();
        JSONArray jsonArray = (JSONArray) parser.parse(images);
        int i = 0;
        for (Node node : cardBox.getChildren()){
            VBox card = (VBox) node;
            JSONObject jsonObject = (JSONObject)jsonArray.get(i);
            BackgroundImage myBI= new BackgroundImage(new Image((String) jsonObject.get("url"), 118, 177, false, true)
                    ,BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                    BackgroundSize.DEFAULT);
            System.out.println((String) jsonObject.get("url"));
            card.setBackground(new Background(myBI));
            i++;
            Button sendBtn = (Button) card.getChildren().get(0);
            sendBtn.setUserData(jsonObject.get("url"));
        }
    }

    @FXML
    protected void updatePage(ActionEvent event) throws IOException, ParseException {
       doUpdate();
    }

    void doUpdate() throws ParseException, IOException {
        String table = setPostRequest(servUrl + "/updateTable","{\"id\":" + userId + "}" );
        JSONParser parser = new JSONParser();
        JSONObject tableJson = (JSONObject) parser.parse(table);

        if(tableJson.get("firstCard") != null){
            BackgroundImage firstBI= new BackgroundImage(new Image((String) tableJson.get("firstCard"), 118, 177, false, true)
                    ,BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                    BackgroundSize.DEFAULT);
            firstCard.setBackground(new Background(firstBI));
        } else {
            firstCard.setBackground(null);
        }

        if(tableJson.get("secondCard") != null){
            BackgroundImage secBI= new BackgroundImage(new Image((String) tableJson.get("secondCard"), 118, 177, false, true)
                    ,BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                    BackgroundSize.DEFAULT);
            secondCard.setBackground(new Background(secBI));
        } else {
            secondCard.setBackground(null);
        }

        if(tableJson.get("thirdCard") != null){
            BackgroundImage thirdtBI= new BackgroundImage(new Image((String) tableJson.get("thirdCard"), 118, 177, false, true)
                    ,BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                    BackgroundSize.DEFAULT);
            thirdCard.setBackground(new Background(thirdtBI));
        } else {
            thirdCard.setBackground(null);
        }

        if(tableJson.get("forthCard") != null){
            BackgroundImage fourBI= new BackgroundImage(new Image((String) tableJson.get("forthCard"), 118, 177, false, true)
                    ,BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                    BackgroundSize.DEFAULT);
            fourCard.setBackground(new Background(fourBI));
        } else {
            fourCard.setBackground(null);
        }

        if(tableJson.get("situation") != null){
            situationLabel.setText((String) tableJson.get("situation"));
        } else {
            situationLabel.setBackground(null);
        }
    }
    @FXML
    protected void reload(ActionEvent event) throws IOException, ParseException {
        setPostRequest(servUrl + "/updateGame","{\"id\": " + userId + "}");
                welcomeText.setText("Welcome to JavaFX Application!");
        String images = setPostRequest(servUrl + "/getMemes","{\"id\": " + userId + "}" );
        System.out.println(images);
        JSONParser parser = new JSONParser();
        JSONArray jsonArray = (JSONArray) parser.parse(images);
        int i = 0;
        for (Node node : cardBox.getChildren()){
            VBox card = (VBox) node;
            JSONObject jsonObject = (JSONObject)jsonArray.get(i);
            BackgroundImage myBI= new BackgroundImage(new Image((String) jsonObject.get("url"), 118, 177, false, true)
                    ,BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                    BackgroundSize.DEFAULT);
            System.out.println((String) jsonObject.get("url"));
            card.setBackground(new Background(myBI));
            i++;
            Button sendBtn = (Button) card.getChildren().get(0);
            sendBtn.setUserData(jsonObject.get("url"));
        }

        String situation = setPostRequest(servUrl + "/getSituation","{\"id\": " + userId + "}" );
        updatePage(event);
        situationLabel.setText(situation);
    }
}