package View;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import java.util.List;

public class MapDisplayer extends Canvas {
    private List<List<Double>> map;

    public void displayMap(List<List<Double>> newMap){
        map = newMap;
        redraw();
    }

    public void redraw() {
        if(map == null){
            //draw enpty
            return;
        }
        double canvasWidth = getWidth();
        double canvasHighet = getHeight();
        double cellWidth = canvasWidth / map.get(0).size();
        double cellHighet = canvasHighet / map.size();

        GraphicsContext gc = getGraphicsContext2D();
        for(int i=0;i<map.size(); i++){
            for(int j=0;j<map.get(0).size(); j+=2){
                gc.setFill(Color.BLUE);
                gc.fillRect(j*cellWidth, i*cellHighet, cellWidth, cellHighet);
            }
        }
    }
}
