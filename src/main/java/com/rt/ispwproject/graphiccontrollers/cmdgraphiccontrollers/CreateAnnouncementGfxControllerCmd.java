package com.rt.ispwproject.graphiccontrollers.cmdgraphiccontrollers;

import com.rt.ispwproject.CmdApplication;
import com.rt.ispwproject.beans.Accommodation;
import com.rt.ispwproject.beans.Announcement;
import com.rt.ispwproject.beans.Session;
import com.rt.ispwproject.beans.Transport;
import com.rt.ispwproject.cmdview.BaseViewCmd;
import com.rt.ispwproject.cmdview.CreateAnnouncementViewCmd;
import com.rt.ispwproject.exceptions.DbException;
import com.rt.ispwproject.logiccontrollers.AnnouncementManager;

import java.time.LocalDate;
import java.util.List;

// Graphic controller used by the "SIMPLE_USER" to create and post a new announcement
public class CreateAnnouncementGfxControllerCmd extends BaseGfxControllerCmd {

    private final CreateAnnouncementViewCmd view;
    private final Announcement              newAnnounce;
    private boolean                         runLoop;


    public CreateAnnouncementGfxControllerCmd(Session session)
    {
        super(session);
        view = new CreateAnnouncementViewCmd();
        newAnnounce = new Announcement();
    }


    public void start()
    {
        menu();
    }


    private void menu()
    {
        runLoop = true;
        List<String> possibilities = List.of( "edit announcement field", "post announcement", "back to my announcements" );

        do {
            view.showScreenTitle();
            view.showAnnouncementDetails(newAnnounce, false, true);
            view.print("\n");

            int choice = view.getChoiceFromUser(possibilities);
            switch(choice)
            {
                case 1:     onEditFieldSelected(); break;
                case 2:     onPostAnnouncementSelected(); break;
                case 3:     this.runLoop = false; break;
                default:    view.showErrorDialog(BaseViewCmd.INVALID_OPTION_MSG); break;
            }

        } while(runLoop);
        CmdApplication.changeScreen( new MyAnnouncementsGfxControllerCmd(currSession) );
    }


    // Invoked when "edit announcement field" is selected. It converts the int inserted
    // by the user to a string and asks the user to insert a new value for the announcement field associated to that string
    private void onEditFieldSelected()
    {
        view.print("Which field do you want to edit? ==> ");
        int choice = view.getIntFromUser(1, BaseViewCmd.ANNOUNCEMENT_FIELDS.size());

        try{
            switch(BaseViewCmd.ANNOUNCEMENT_FIELDS.get(choice))
            {
                case "holiday description":
                    view.print("Insert a description: ");
                    newAnnounce.setHolidayDescription(view.getStringFromUser());
                    break;

                case "destination":
                    view.print("Insert your destination: ");
                    newAnnounce.getTransportRequirements().setArrivalLocation(view.getStringFromUser());
                    break;

                case "available budget":
                    view.print("Insert your budget: ");
                    newAnnounce.setAvailableBudget(view.getIntFromUser(1, Integer.MAX_VALUE));
                    break;

                case "departure date":
                    view.print("Insert the departure date: ");
                    newAnnounce.getHolidayDuration().setDepartureDate(view.getDateFromUser());
                    break;

                case "return date":
                    view.print("Insert the return date: ");
                    newAnnounce.getHolidayDuration().setReturnDate(view.getDateFromUser());
                    break;

                case "accommodation type": {
                    List<String> types = Accommodation.getAvailableTypes();
                    view.printList(types, true);

                    view.print("Choose your accommodation type: ");
                    int chosenType = view.getIntFromUser(1, types.size());
                    newAnnounce.getAccommodationRequirements().setType(types.get(chosenType - 1));
                }   break;

                case "accommodation quality":
                    view.print("Insert the accommodation quality: ");
                    newAnnounce.getAccommodationRequirements().setQuality(view.getIntFromUser(1, 5));
                    break;

                case "number of rooms":
                    view.print("Insert the number of rooms required: ");
                    newAnnounce.getAccommodationRequirements().setNumOfRooms(view.getIntFromUser(1, Integer.MAX_VALUE));
                    break;

                case "transport type": {
                    List<String> types = Transport.getAvailableTypes();
                    view.printList(types, true);

                    view.print("Choose your transport type: ");
                    int chosenType = view.getIntFromUser(1, types.size());
                    newAnnounce.getTransportRequirements().setType(types.get(chosenType - 1));
                }   break;

                case "transport quality":
                    view.print("Insert the transport quality: ");
                    newAnnounce.getTransportRequirements().setQuality(view.getIntFromUser(1, 5));
                    break;

                case "departure location":
                    view.print("Insert the departure location: ");
                    newAnnounce.getTransportRequirements().setDepartureLocation(view.getStringFromUser());
                    break;

                case "number of travelers":
                    view.print("Insert the number of travelers: ");
                    newAnnounce.getTransportRequirements().setNumOfTravelers(view.getIntFromUser(1, Integer.MAX_VALUE));
                    break;

                default:
                    view.showErrorDialog(BaseViewCmd.INVALID_OPTION_MSG);
                    break;
            }
        } catch(IllegalArgumentException e)
        {
            view.showErrorDialog(e.getMessage());
        }
    }


    // Invoked when "post announcement" is selected, invokes the logic controller functionalities
    private void onPostAnnouncementSelected()
    {
        try {
            newAnnounce.setDateOfPost(LocalDate.now());
            AnnouncementManager annManager = new AnnouncementManager();
            annManager.postAnnouncement(currSession, newAnnounce);
            view.showInfoDialog("Announcement posted correctly");
            runLoop = false;
        } catch(IllegalArgumentException | DbException e)
        {
            view.showErrorDialog(e.getMessage());
        }
    }

}
