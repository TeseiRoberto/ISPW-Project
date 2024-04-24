package com.rt.ispwproject.graphiccontrollers.cmdgraphiccontrollers;

import com.rt.ispwproject.CmdApplication;
import com.rt.ispwproject.beans.ChangesOnOffer;
import com.rt.ispwproject.beans.Offer;
import com.rt.ispwproject.beans.Session;
import com.rt.ispwproject.cmdview.BaseViewCmd;
import com.rt.ispwproject.cmdview.OfferDetailsViewCmd;
import com.rt.ispwproject.exceptions.DbException;
import com.rt.ispwproject.graphiccontrollers.cmdgraphiccontrollers.makeoffercontroller.MakeCounterofferGfxControllerCmd;
import com.rt.ispwproject.logiccontrollers.ChangesManager;

import java.util.ArrayList;
import java.util.List;

// Graphic controller that display the details of an offer that was made by the "TRAVEL_AGENCY"
// and the details of the changes requested by the "SIMPLE_USER" (if a request of changes was made)
public class OfferDetailsGfxControllerCmd extends BaseGfxControllerCmd {

    private boolean                     runLoop;
    private final OfferDetailsViewCmd   view;
    private final Offer                 currOffer;
    private ChangesOnOffer              requestedChanges;


    public OfferDetailsGfxControllerCmd(Session session, Offer offer)
    {
        super(session);
        currOffer = offer;
        view = new OfferDetailsViewCmd();
        requestedChanges = null;
    }


    // Loads the changes requested on the offer (if any) and starts the menu
    public void start()
    {
        try {
            ChangesManager changesManager = new ChangesManager();
            requestedChanges = changesManager.getRequestedChangesOnOffer(currSession, currOffer);
        } catch (DbException | IllegalArgumentException | IllegalCallerException e)
        {
           view. showErrorDialog(e.getMessage());
        }

        menu();
    }


    private void menu()
    {
        runLoop = true;
        ArrayList<String> possibilities = new ArrayList<>(List.of( "back to my offers", "reject requested changes", "make counteroffer" ));

        do {
            // Remove menu entries to respond to the request of changes (if there are no changes requested)
            if(requestedChanges == null)
            {
                possibilities.removeLast();
                possibilities.removeLast();
            }

            view.showScreenTitle();
            view.showOffer(currOffer);
            view.showRequestedChangesOnOffer(requestedChanges);

            int choice = view.getChoiceFromUser(possibilities);
            switch(choice)
            {
                case 1:     onBackSelected(); break;
                case 2:     onRejectChangesSelected(); break;
                case 3:     onMakeCounterofferSelected(); break;
                default:    view.showErrorDialog(BaseViewCmd.INVALID_OPTION_MSG); break;
            }
        } while(runLoop);
    }


    // Invoked when "reject requested changes" is selected, deletes the request from db and updates the offer state
    private void onRejectChangesSelected()
    {
        if(requestedChanges == null)
            return;

        // Ask confirm to the user
        if(!view.showConfirmDialog("Do you really want to reject the changes requested by the user?"))
            return;

        try {
            ChangesManager changesManager = new ChangesManager();
            changesManager.rejectChangesRequest(currSession, requestedChanges);

            view.showInfoDialog("Request rejected successfully!");
            currOffer.setOfferStatus("Requested changes has been rejected, waiting for user to evaluate the offer.");
            requestedChanges = null;
        } catch (DbException | IllegalCallerException e) {
            view.showErrorDialog(e.getMessage());
        }
    }


    // Invoked when "make counteroffer" is selected, switches to the "make counteroffer" view
    private void onMakeCounterofferSelected()
    {
        if(requestedChanges == null)
            return;

        runLoop = false;
        CmdApplication.changeScreen( new MakeCounterofferGfxControllerCmd(currSession, currOffer, requestedChanges) );
    }


    // Invoked when "back to my offers" is selected, switches to the "my offers" view
    private void onBackSelected()
    {
        runLoop = false;
        CmdApplication.changeScreen( new MyOffersGfxControllerCmd(currSession) );
    }

}
