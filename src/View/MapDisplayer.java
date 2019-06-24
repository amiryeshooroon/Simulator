package View;

import Utilities.Math.ColoredRange;
import javafx.animation.AnimationTimer;
import javafx.animation.Timeline;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MapDisplayer extends Canvas {
    private List<List<Double>> map;
    private double maxHighet, minHighet;
    private double canvasWidth;
    private double canvasHighet;
    private double cellWidth;
    private double cellHighet;
    private Image xPhoto;
    private Image plane;
    private double planeX, planeY;
    private double prevXX, prevXY;
    private double startLongitude, startLatitude, area;
    private AnimationTimer timer;
    private GraphicsContext gc;
    private Pane planePane;
    private Region test;
    public void displayMap(List<List<Double>> newMap, double longitude, double latitude, double area){
        map = newMap;
        gc = null;
        prevXX = 0;
        prevXY = 0;
        xPhoto = new Image(getClass().getResource("Xphoto.png").toString());
        plane = new Image(getClass().getResource("plane.png").toString());
        startLongitude = longitude;
        startLatitude = latitude;
        planePane = new Pane();
        this.area = area;
        calculateMinMax(); //
        redraw();

    }

    private void calculateMinMax(){
        minHighet = Double.MAX_VALUE; maxHighet = Double.MIN_VALUE;
        for (List<Double> doubles : map) {
            for (Double d : doubles) {
                if (d > maxHighet) maxHighet = d;
                if (d < minHighet) minHighet = d;
            }
        }
    }

    public void redraw() {
        if(map == null){
            //draw enpty
            return;
        }
        canvasWidth = getWidth();
        canvasHighet = getHeight();
        cellWidth = canvasWidth / map.get(0).size();
        cellHighet = canvasHighet / map.size();
        gc = getGraphicsContext2D();
        for(int i=0;i<map.size(); i++)
            for(int j=0;j<map.get(0).size(); j++)
                redrawAt(i,j);
    }
    public void redrawAt(int i, int j){
        gc.setFill(Color.color( 1 - (map.get(i).get(j)-minHighet)/(maxHighet-minHighet), (map.get(i).get(j)-minHighet)/(maxHighet-minHighet),0));
        gc.fillRect(j*cellWidth, i*cellHighet, cellWidth, cellHighet);
    }
    public void drawX(double x, double y){
        if(gc != null) {
            redrawAt((int)(prevXY/cellHighet), (int)(prevXX / cellWidth));
            gc.drawImage(xPhoto, ((int)(x / cellWidth))*cellWidth, ((int)(y/cellHighet))*cellHighet, cellWidth, cellHighet);
            prevXX = x;
            prevXY = y;
        }
    }
    public void displayAirplane(ImageView p, double longitude , double latitude){
        double sqrtArea = Math.sqrt(area);
        double x = (((latitude - startLatitude + sqrtArea)/ sqrtArea))*cellWidth;
        double y = (((longitude - startLongitude + sqrtArea)/sqrtArea))*cellHighet;
//        System.out.println("latitude: " + latitude);
//        System.out.println("longtitude: " + longitude);
//        System.out.println("("+x+","+y+")");
        //gc.drawImage(plane, x, y, plane.getWidth(), plane.getHeight());
        planeX = x;
        planeY = y;
        p.setX(x);
        p.setY(y);
        p.setFitWidth(cellWidth);
        p.setFitHeight(cellHighet);
        p.setVisible(true);
    }
}
