package com.rt.ispwproject.graphiccontrollers.cmdgraphiccontrollers;

import com.rt.ispwproject.CmdApplication;
import com.rt.ispwproject.beans.Announcement;
import com.rt.ispwproject.beans.Offer;
import com.rt.ispwproject.beans.Session;
import com.rt.ispwproject.cmdview.AnnouncementDetailsViewCmd;
import com.rt.ispwproject.cmdview.BaseViewCmd;
import com.rt.ispwproject.exceptions.DbException;
import com.rt.ispwproject.logiccontrollers.OfferManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// Graphic controller used to display to the "SIMPLE_USER" the details of a posted announcement
public class AnnouncementDetailsGfxControllerCmd extends BaseGfxControllerCmd {

    private boolean                         runLoop;
    private final AnnouncementDetailsViewCmd view;
    private final Announcement              currAnnounce;
    private List<Offer>                     offers;        // Offers received for the currAnnounce
    private int                             offerIndex;     // Index in the offers list for the offer that is currently being shown


    public AnnouncementDetailsGfxControllerCmd(Session session, Announcement announce)
    {
        super(session);
        this.view = new AnnouncementDetailsViewCmd();
        this.currAnnounce = announce;
        this.offerIndex = 0;
    }


    // Load the offers made for currAnnounce and starts the menu
    public void start()
    {
        try {
            OfferManager offerManager = new OfferManager();
            offers = offerManager.getOffersForAnnouncement(currSession, currAnnounce);
        } catch (DbException | IllegalArgumentException e)
        {
            offers.clear();
            view.showErrorDialog(e.getMessage());
        }

        menu();
    }


    // Main menu to interact with the system
    private void menu()
    {
        runLoop = true;
        ArrayList<String> possibilities = new ArrayList<>(Arrays.asList(
                "edit announcement", "remove announcement", "back to my announcements",
                "reject offer", "request changes on offer", "accept offer", "show previous offer", "show next offer"
        ));

        do {
            // Remove the possibilities related to offers from menu if there are no offers
            if( (offers == null || offers.isEmpty()) && possibilities.size() > 3)
            {
                while(possibilities.size() > 3)
                    possibilities.removeLast();
            }

            view.showScreenTitle();
            view.printSubtitle("Your request");
            view.showAnnouncementDetails(currAnnounce, true, false);
            view.print("\n");

            if((offers == null || offers.isEmpty()))
                view.print("No offer has been received yet...\n");
            else
                view.showOffer(offers.get(offerIndex));

            int choice = view.getChoiceFromUser(possibilities);
            switch(choice)
            {
                case 1:     onEditAnnouncementSelected(); break;
                case 2:     onDeleteAnnouncementSelected(); break;
                case 3:     onBackSelected(); break;
                case 4:     onRejectOfferSelected(); break;
                case 5:     onRequestChangesSelected(); break;
                case 6:     onAcceptOfferSelected(); break;
                case 7:     onShowPrevOfferSelected(); break;
                case 8:     onShowNextOfferSelected(); break;
                default:    view.showErrorDialog(BaseViewCmd.INVALID_OPTION_MSG); break;
            }

        } while(runLoop);
    }


    // Invoked when "edit announcement" is selected
    private void onEditAnnouncementSelected()
    {
        view.showErrorDialog("Edit announcement functionality is not available yet...");
    }


    // Invoked when "delete announcement" is selected
    private void onDeleteAnnouncementSelected()
    {
        view.showErrorDialog("Delete announcement functionality is not available yet...");
    }


    // Invoked when "back to my announcements" is selected, quits the menu loop and switches to the "my announcements" view
    private void onBackSelected()
    {
        runLoop = false;
        CmdApplication.changeScreen( new MyAnnouncementsGfxControllerCmd(currSession) );
    }


    // Invoked when "show previous offer" is selected
    private void onShowPrevOfferSelected()
    {
        if(offers == null || offers.isEmpty())
            return;

        if(offerIndex <= 0)                                     // Wrap around the index
            offerIndex = offers.size();

        offerIndex--;
    }


    // Invoked when "show next offer" is selected
    private void onShowNextOfferSelected()
    {
        if(offers == null || offers.isEmpty())
            return;

        if(offerIndex + 1 >= offers.size())                        // Wrap around the index
            offerIndex = -1;

        offerIndex++;
    }


    // Invoked when "reject offer" is selected, informs travel agency that the offer was rejected
    private void onRejectOfferSelected()
    {
        if(offers == null || offers.isEmpty())
            return;

        if(!view.showConfirmDialog("Do you really want to reject this offer?"))
            return;

        try {
            Offer currOffer = offers.get(offerIndex);

            OfferManager offerManager = new OfferManager();
            offerManager.rejectOffer(currSession, currOffer);

            offers.remove(currOffer);                       // Delete current offer from the offers list
            if(offers.isEmpty())
                offerIndex = 0;
            else
                offerIndex = (offerIndex + 1) % offers.size();

            view.showInfoDialog("Offer rejected correctly");

        } catch(IndexOutOfBoundsException e)
        {
            view.showErrorDialog("No offer has been selected");
        }
        catch (DbException | IllegalCallerException | IllegalArgumentException e) {
            view.showErrorDialog(e.getMessage());
        }
    }


    // Invoked when "request changes on offer" is selected, quits the menu loop and switches to the "request changes" view
    private void onRequestChangesSelected()
    {
        if(offers == null || offers.isEmpty())
            return;

        runLoop = false;
        CmdApplication.changeScreen( new RequestChangesGfxControllerCmd(currSession, currAnnounce, offers.get(offerIndex)) );
    }


    // Invoked when "accept offer" is selected
    private void onAcceptOfferSelected()
    {
        if(offers == null || offers.isEmpty())
            return;

        /* TODO: Add implementation...
        *   Note: If changes were requested on the offer but now the user is accepting it
            then the request must be canceled from the system*/
        view.showErrorDialog("Accept offer functionality is not implemented yet...");
    }

}
