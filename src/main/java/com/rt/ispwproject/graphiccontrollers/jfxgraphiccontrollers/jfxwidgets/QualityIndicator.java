package com.rt.ispwproject.graphiccontrollers.jfxgraphiccontrollers.jfxwidgets;

import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.shape.SVGPath;

import static com.rt.ispwproject.graphiccontrollers.jfxgraphiccontrollers.jfxwidgets.QualitySelector.STAR_SHAPE;

// A row with some svg elements shaped as stars, it is used to represents a quality level in the range [0, maxQualityLevel]
public class QualityIndicator extends HBox {

    private static final String INACTIVE_STAR_STYLE = "-fx-border-color: #2B2B2B; -fx-background-color: #EBEBEB;";
    private static final String ACTIVE_STAR_STYLE = "-fx-border-color: #E69E0F; -fx-background-color: #E69E0F;";
    private int                 currQualityLevel;
    private final int           maxQualityLevel;


    public QualityIndicator(int startQualityLevel, int maxQualityLevel)
    {
        super();

        this.currQualityLevel = startQualityLevel;
        this.maxQualityLevel = maxQualityLevel;
        this.setAlignment(Pos.CENTER);
        this.setSpacing(2);
        SVGPath star = new SVGPath();
        star.setContent(STAR_SHAPE);

        for(int i = 1; i != maxQualityLevel; ++i)           // Create the star shaped images
        {
            if(i < startQualityLevel)
                star.setStyle(ACTIVE_STAR_STYLE);
            else
                star.setStyle(INACTIVE_STAR_STYLE);

            this.getChildren().add(star);
        }
    }


    public void setQualityLevel(int level)
    {
        ObservableList<Node> stars = this.getChildren();
        currQualityLevel = Math.clamp(level, 0, maxQualityLevel);

        for(int i = 0; i < stars.size(); ++i)
        {
            if(i < currQualityLevel)
                stars.get(i).setStyle(ACTIVE_STAR_STYLE);
            else
                stars.get(i).setStyle(INACTIVE_STAR_STYLE);
        }
    }


    public int getQualityLevel()    { return this.currQualityLevel; }

}
