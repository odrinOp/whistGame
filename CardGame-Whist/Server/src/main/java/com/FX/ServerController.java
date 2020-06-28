package com.FX;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;


public class ServerController {
    @FXML
    private TextField ipAddr,port;
    @FXML
    private Label gameStatus,serverStatus;
    @FXML
    private Button openBtn;


    public ServerController(){

    }


    public void initState(){
        gameStatus.setText("CLOSED");
        serverStatus.setText("CLOSED");

        try {
            String ip = InetAddress.getLocalHost().getHostAddress();
            ipAddr.setText(ip);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

    }

    @FXML
    public void onRun(){
        if(port.getText().equals(""))
        {
            System.out.println("Enter a valid PORT!");
            return;
        }


        createXmlFile(port.getText());
        ApplicationContext factory = new ClassPathXmlApplicationContext("classpath:spring-server.xml");


        serverStatus.setText("Running");
        gameStatus.setText("Lobby");
        openBtn.setDisable(true);
        port.setDisable(true);

    }



    private void createXmlFile(String port) {
        String xmlFile = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n" +
                "\n" +
                "<beans xmlns=\"http://www.springframework.org/schema/beans\"\n" +
                "       xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                "       xsi:schemaLocation=\"http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context.xsd\">\n" +
                "\n" +
                "    <bean id=\"gameEngine\" class=\"com.GameEngine\"/>\n" +
                "\n" +
                "    <bean class=\"org.springframework.remoting.rmi.RmiServiceExporter\">\n" +
                "        <property name=\"serviceName\" value=\"WhistGame\"/>\n" +
                "        <property name=\"service\" ref=\"gameEngine\"/>\n" +
                "        <property name=\"serviceInterface\" value=\"com.IServer\"/>";

        xmlFile += "<property name=\"servicePort\" value=\"" + port +"\"/>\n";
        xmlFile += "</bean>\n" +
                "\n" +
                "\n" +
                "</beans>";

        try {
            FileWriter writer = new FileWriter("spring-server.xml");
            writer.write(xmlFile);
            System.out.println("XML File configured");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
