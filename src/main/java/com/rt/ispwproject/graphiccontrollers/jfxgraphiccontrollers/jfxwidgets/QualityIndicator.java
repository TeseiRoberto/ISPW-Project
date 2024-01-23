package com.rt.ispwproject.graphiccontrollers.jfxgraphiccontrollers.jfxwidgets;

import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.SVGPath;

import static com.rt.ispwproject.graphiccontrollers.jfxgraphiccontrollers.jfxwidgets.QualitySelector.STAR_SHAPE;


// A row with some svg elements shaped as stars, it is used to represents a quality level in the range [0, maxQualityLevel]
public class QualityIndicator extends HBox {

    private static final Paint  starColor = Paint.valueOf("#E69E0F");
    private int                 currQualityLevel;


    public QualityIndicator(int qualityLevel)
    {
        super();

        this.currQualityLevel = qualityLevel;
        this.setAlignment(Pos.CENTER);
        this.setSpacing(2);

        for(int i = 0; i < qualityLevel; ++i)           // Create the star shaped images
            this.getChildren().add(createStar());
    }


    public void setQualityLevel(int level)
    {
        ObservableList<Node> stars = this.getChildren();

        if(level < currQualityLevel)                            // If the given level is less than the current one
        {
            for(int i = level; i < currQualityLevel; ++i)       // Remove the not needed stars
                this.getChildren().remove(stars.get(i));
        } else {                                                // Otherwise
            for(int i = currQualityLevel; i < level; ++i)       // Add some stars
                this.getChildren().add(createStar());
        }

        this.currQualityLevel = level;
    }


    public int getQualityLevel()    { return this.currQualityLevel; }


    private SVGPath createStar()
    {
        SVGPath star = new SVGPath();
        star.setContent(STAR_SHAPE);
        star.setFill(starColor);
        star.setStroke(starColor);
        //star.setStyle(STAR_STYLE);

        return star;
    }
}
