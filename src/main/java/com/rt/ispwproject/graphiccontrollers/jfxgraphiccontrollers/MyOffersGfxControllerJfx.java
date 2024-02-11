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

public class MyOffersGfxControllerJfx extends BaseGfxControllerJfx {

    private final Session   currSession;
    private List<Offer>     offers = null;
    @FXML private VBox      offersVbox;


    public MyOffersGfxControllerJfx(Session session)
    {
        this.currSession = session;
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
        // TODO: Need to switch to the "offer details" screen
        System.out.println("CLICKED ON OFFER!");
    }


    // Invoked when the "search announcements" button is clicked, switches to the "search announcements" screen
    public void onSearchAnnouncementsClick()
    {
        changeScreen(getClass().getResource("travelAgency/searchAnnouncementsScreen.fxml"),
                (Stage) offersVbox.getScene().getWindow(), e -> new SearchAnnouncementsGfxControllerJfx(currSession));
    }


}
