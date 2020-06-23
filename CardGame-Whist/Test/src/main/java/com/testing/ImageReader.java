package com.testing;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ImageReader {


    BufferedImage cards;
    final int cols = 13;
    final int rows = 4;
    final int width=43;
    final int height=64;
    int[] startPointX = {0,51,103,155,207,259,311,362,414,466,518,570,622};
    int[] startPointY = {0,72,145,217};
    int startPointBackCardX = 777;
    int startPointBackCardY = 0;

    Image[] images = new Image[cols*rows];
    Map<String,Image> iamges_map;
    public ImageReader() throws IOException {
        File f = new File("C:\\GitProjects\\whistGame\\CardGame-Whist\\Test\\src\\main\\resources\\cards.png");
        System.out.println(f);
        this.cards =  ImageIO.read(f);
        //width = cards.getWidth()/16;
        //height = cards.getHeight()/4;
        iamges_map = new HashMap<>();
        getSubImages();
    }

    private void getSubImages() throws IOException {
        String[] types = {"d","h","s","c"};
        int max_value = 14;
        for(int i = 0; i < rows; i++)
        {
            String currentType = types[i];
            int current_val = 14; // image is starting with ACE
            for(int j = 0; j< cols; j++){
                BufferedImage image = cards.getSubimage(startPointX[j],startPointY[i],width,height);
                //images[i * cols + j] = SwingFXUtils.toFXImage(image,null);
                String name = currentType + "-" + current_val;
                iamges_map.put(name,SwingFXUtils.toFXImage(image,null));
                current_val +=1;
                current_val = getResetedContor(current_val,max_value,2);
            }
        }
        BufferedImage image = cards.getSubimage(startPointBackCardX,startPointBackCardY,width,height);
        iamges_map.put("back",SwingFXUtils.toFXImage(image,null));

        BufferedImage bufferedImage1 = ImageIO.read(new File("C:\\GitProjects\\whistGame\\CardGame-Whist\\Test\\src\\main\\resources\\1-back (1).png"));
        BufferedImage bufferedImage2 = ImageIO.read(new File("C:\\GitProjects\\whistGame\\CardGame-Whist\\Test\\src\\main\\resources\\2-back (1).png"));
        BufferedImage bufferedImage3 = ImageIO.read(new File("C:\\GitProjects\\whistGame\\CardGame-Whist\\Test\\src\\main\\resources\\3-back.png"));
        BufferedImage bufferedImage4 = ImageIO.read(new File("C:\\GitProjects\\whistGame\\CardGame-Whist\\Test\\src\\main\\resources\\4-back (1).png"));
        BufferedImage bufferedImage5 = ImageIO.read(new File("C:\\GitProjects\\whistGame\\CardGame-Whist\\Test\\src\\main\\resources\\5-back.png"));
        BufferedImage bufferedImage6 = ImageIO.read(new File("C:\\GitProjects\\whistGame\\CardGame-Whist\\Test\\src\\main\\resources\\6-back (1).png"));

        iamges_map.put("back-1",SwingFXUtils.toFXImage(bufferedImage1,null));
        iamges_map.put("back-2",SwingFXUtils.toFXImage(bufferedImage2,null));
        iamges_map.put("back-3",SwingFXUtils.toFXImage(bufferedImage3,null));
        iamges_map.put("back-4",SwingFXUtils.toFXImage(bufferedImage4,null));
        iamges_map.put("back-5",SwingFXUtils.toFXImage(bufferedImage5,null));
        iamges_map.put("back-6",SwingFXUtils.toFXImage(bufferedImage6,null));
    }

    public Image[]  getImages(){
        return images;
    }

    public Image getCardImage(String name){
        return iamges_map.get(name);
    }

    public int getWidth(){
        return width;
    }

    public int getHeight(){
        return height;
    }


    private int getResetedContor(int x,int max,int min){
        if(x>max)
            return min;
        return x;
    }

}
