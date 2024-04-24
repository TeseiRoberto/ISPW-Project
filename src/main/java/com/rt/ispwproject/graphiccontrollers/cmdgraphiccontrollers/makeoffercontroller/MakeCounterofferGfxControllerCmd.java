package com.rt.ispwproject.graphiccontrollers.cmdgraphiccontrollers.makeoffercontroller;

import com.rt.ispwproject.CmdApplication;
import com.rt.ispwproject.beans.ChangesOnOffer;
import com.rt.ispwproject.beans.Offer;
import com.rt.ispwproject.beans.Session;
import com.rt.ispwproject.cmdview.BaseViewCmd;
import com.rt.ispwproject.cmdview.makeofferview.MakeCounterofferViewCmd;
import com.rt.ispwproject.exceptions.DbException;
import com.rt.ispwproject.graphiccontrollers.cmdgraphiccontrollers.OfferDetailsGfxControllerCmd;
import com.rt.ispwproject.logiccontrollers.OfferManager;

import java.util.List;

public class MakeCounterofferGfxControllerCmd extends BaseMakeOfferGfxControllerCmd {

    private final Offer             originalOffer;
    private final ChangesOnOffer    requestedChanges;


    public MakeCounterofferGfxControllerCmd(Session session, Offer offer, ChangesOnOffer requestedChanges)
    {
        super(session, new MakeCounterofferViewCmd());
        currOffer = offer;
        this.requestedChanges = requestedChanges;

        // We clone the given offer so that we can keep track of how was the original offer, this is needed because
        // the changes that the travel agency makes on the offer (to build the counteroffer) are made on the given offer instance
        originalOffer = new Offer(offer);
    }


    public void start()
    {
        accommodationChosen = true; // The given offer already has accommodation and transport
        transportChosen = true;
        menu();
    }


    protected void menu()
    {
        runLoop = true;
        List<String> possibilities = List.of( "Edit counteroffer field", "make counteroffer to user", "back to offer details");

        do {
            view.showScreenTitle();
            view.showUserRequest(requestedChanges, originalOffer);
            view.showOffer(currOffer);

            int choice = view.getChoiceFromUser(possibilities);
            switch(choice)
            {
                case 1:     onEditOfferFieldSelected(); break;
                case 2:     onMakeCounterofferSelected(); break;
                case 3:     onBackSelected(); break;
                default:    view.showErrorDialog(BaseViewCmd.INVALID_OPTION_MSG); break;
            }
        } while(runLoop);
    }


    // Invoked when "make counteroffer to user" is selected, uses the logic controller
    // to update the original offer in the system with the new one
    private void onMakeCounterofferSelected()
    {
        try {
            if(!accommodationChosen)
                throw new IllegalArgumentException("No accommodation has been selected yet...");

            if(!transportChosen)
                throw new IllegalArgumentException("No transport has been selected yet...");

            OfferManager offerManager = new OfferManager();
            offerManager.makeCounteroffer(currSession, currOffer, requestedChanges);
        } catch(IllegalCallerException | IllegalArgumentException | DbException e)
        {
            view.showErrorDialog(e.getMessage());
            return;
        }

        runLoop = false;
        view.showInfoDialog("Counteroffer sent correctly!");
        CmdApplication.changeScreen( new OfferDetailsGfxControllerCmd(currSession, currOffer) );
    }


    // Invoked when "back to offer details" is selected, switches to the "offer details" view
    private void onBackSelected()
    {
        runLoop = false;
        CmdApplication.changeScreen( new OfferDetailsGfxControllerCmd(currSession, originalOffer) );
    }

}
