package com.rt.ispwproject.graphiccontrollers.jfxgraphiccontrollers;

import com.rt.ispwproject.beans.Offer;
import com.rt.ispwproject.beans.Session;
import com.rt.ispwproject.exceptions.DbException;
import com.rt.ispwproject.graphiccontrollers.jfxgraphiccontrollers.jfxwidgets.DetailsBannerGfxElement;
import com.rt.ispwproject.logiccontrollers.OfferManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.util.List;

// Graphic controller that displays the list of offers made by the "TRAVEL_AGENCY"
public class MyOffersGfxControllerJfx extends BaseTravelAgencyGfxControllerJfx {

    private List<Offer>     offers = null;
    @FXML private VBox      offersVbox;


    public MyOffersGfxControllerJfx(Session session, Stage stage)
    {
        super(session, stage);
    }


    // Loads offers made by the travel agency and displays them
    @FXML void initialize()
    {
        try {
            OfferManager offerManager = new OfferManager();
            offers = offerManager.getMyOffers(currSession);

        } catch(DbException | IllegalArgumentException e)
        {
            offers.clear();
            displayErrorDialog(e.getMessage());
        }

        if(offers == null || offers.isEmpty())
        {
            Label infoMsg = new Label("No offer has been made yet.");
            infoMsg.setTextAlignment(TextAlignment.CENTER);
            infoMsg.setFont(new Font("System", 18));
            offersVbox.getChildren().add(infoMsg);
        } else {
            for(Offer o : offers)
            {
                DetailsBannerGfxElement gfxElement = new DetailsBannerGfxElement(o, e -> onOfferClick(o));
                offersVbox.getChildren().add(gfxElement);
            }
        }
    }


    // Invoked when one of the DetailsBannerGfxElement is clicked, switches to the "offer details" screen
    public void onOfferClick(Offer offer)
    {
        changeScreen(getClass().getResource(OFFER_DETAILS_SCREEN_NAME),
                c -> new OfferDetailsGfxControllerJfx(currSession, mainStage, offer));
    }


}
