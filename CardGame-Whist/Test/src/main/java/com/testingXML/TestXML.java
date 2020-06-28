package com.testingXML;

import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class TestXML {

    public static void main(String[] args) {

        String ipAddress = null;
        try {
            ipAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        String port = "1234";
        String value = ipAddress + ":" + port;
        String propertyURL = "<property name=\"serviceUrl\" value=\"rmi://" + value + "/WhistGame\"/>";


        String finalString = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n" +
                "\n" +
                "<beans xmlns=\"http://www.springframework.org/schema/beans\"\n" +
                "       xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                "       xsi:schemaLocation=\"http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context.xsd\">\n" +
                "\n" +
                "    <bean id=\"appServer\" class=\"org.springframework.remoting.rmi.RmiProxyFactoryBean\">";

        finalString += propertyURL;

        finalString += "<property name=\"serviceInterface\" value=\"com.IServer\"/>\n" +
                "    </bean>\n" +
                "\n" +
                "</beans>";


        try {
            FileWriter writer = new FileWriter("C:\\GitProjects\\whistGame\\CardGame-Whist\\Test\\src\\main\\resources\\testXML.xml");
            writer.write(finalString);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
