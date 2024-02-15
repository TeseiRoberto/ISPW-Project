package com.rt.ispwproject.dao;

import com.rt.ispwproject.exceptions.DbException;
import com.rt.ispwproject.model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HolidayOfferDao {


    // Stores given holiday offer in db
    public void postOffer(HolidayOffer offer) throws DbException
    {
        Connection connection = DbConnection.getInstance().getConnection();

        AccommodationOfferDao accommodationOfferDao = new AccommodationOfferDao();
        int accommodationOfferId = 0;

        TransportOfferDao transportOfferDao = new TransportOfferDao();
        int transportOfferId = 0;

        try (CallableStatement createOfferProc = connection.prepareCall("call createHolidayOffer(?, ?, ?, ?, ?, ?, ?)"))
        {
            // Use accommodation offer dao to post the accommodation offer
            accommodationOfferId = accommodationOfferDao.postOffer(offer.getAccommodation());

            // Use transport offer dao to post the accommodation offer
            transportOfferId = transportOfferDao.postOffer(offer.getTransport());

            createOfferProc.setInt("requirementsId_in", offer.getMetadata().getRelativeRequirementsId());
            createOfferProc.setInt("bidderAgencyId_in", offer.getMetadata().getBidderAgency().getUserId());
            createOfferProc.setInt("holidayPrice_in", offer.getPrice());
            createOfferProc.setDate("holidayStartDate_in", Date.valueOf(offer.getDepartureDate()));
            createOfferProc.setDate("holidayEndDate_in", Date.valueOf(offer.getReturnDate()));
            createOfferProc.setInt("accommodationOfferId_in", accommodationOfferId);
            createOfferProc.setInt("transportOfferId_in", transportOfferId);

            createOfferProc.execute();
        } catch(SQLException e)
        {
            if(accommodationOfferId != 0)
                accommodationOfferDao.removeOffer(accommodationOfferId);

            if(transportOfferId != 0)
                transportOfferDao.removeOffer(transportOfferId);

            throw new DbException("Failed to invoke the \"createHolidayOffer\" stored procedure:\n\"" + e.getMessage());
        } catch (DbException e)
        {
            if(accommodationOfferId != 0)
                accommodationOfferDao.removeOffer(accommodationOfferId);

            if(transportOfferId != 0)
                transportOfferDao.removeOffer(transportOfferId);

            throw new DbException("Cannot post holiday offer:\n" + e.getMessage());
        }
    }


    // Retrieves all the holiday offers made by travel agencies to the holiday requirements associated to the given holidayReqId
    public List<HolidayOffer> getOffersForHolidayRequirements(int holidayReqId) throws DbException
    {
        List<HolidayOffer> holidayOffers = null;
        Connection connection = DbConnection.getInstance().getConnection();

        try (CallableStatement getOffersProc = connection.prepareCall("call getHolidayOffersForRequirements(?)"))
        {
            getOffersProc.setInt("requirementsId_in", holidayReqId);
            boolean status = getOffersProc.execute();
            if(status)                                      // If the stored procedure returned a result set
            {
                ResultSet rs = getOffersProc.getResultSet();
                holidayOffers = createHolidayOffersFromResultSet(rs);
                rs.close();
            }

        } catch(SQLException e)
        {
            throw new DbException("Failed to invoke the \"getHolidayOffersForRequirements\" stored procedure:\n\"" + e.getMessage());
        } catch (DbException e)
        {
            throw new DbException("Cannot load holiday offers for the given holiday requirements:\n" + e.getMessage());
        }

        return holidayOffers;
    }


    // Retrieves all the holiday offers made by the user associated to the given userId
    public List<HolidayOffer> getOffersMadeByUser(int userId) throws DbException
    {
        List<HolidayOffer> holidayOffers = null;
        Connection connection = DbConnection.getInstance().getConnection();

        try (CallableStatement getOffersProc = connection.prepareCall("call getHolidayOffersPostedByUser(?)"))
        {
            getOffersProc.setInt("userId_in", userId);
            boolean status = getOffersProc.execute();
            if(status)                                      // If the stored procedure returned a result set
            {
                ResultSet rs = getOffersProc.getResultSet();
                holidayOffers = createHolidayOffersFromResultSet(rs);
                rs.close();
            }

        } catch(SQLException e)
        {
            throw new DbException("Failed to invoke the \"getHolidayOffersPostedByUser\" stored procedure:\n\"" + e.getMessage());
        } catch (DbException e)
        {
            throw new DbException("Cannot load holiday offers made by the given user:\n" + e.getMessage());
        }

        return holidayOffers;
    }


    // Updates the state of the holiday offer associated to the given offerId
    public void updateOfferState(int offerId, HolidayOfferState newState) throws DbException
    {
        Connection connection = DbConnection.getInstance().getConnection();

        try (CallableStatement updateOfferProc = connection.prepareCall("call updateHolidayOfferState(?, ?)"))
        {
            updateOfferProc.setInt("offerId_in", offerId);
            updateOfferProc.setString("newState_in", newState.toPersistenceType());

            updateOfferProc.execute();
        } catch(SQLException e)
        {
            throw new DbException("Failed to invoke the \"updateHolidayOfferState\" stored procedure:\n\"" + e.getMessage());
        }
    }


    // Creates a list of HolidayOffer using data contained in the given result set.
    private List<HolidayOffer> createHolidayOffersFromResultSet(ResultSet rs) throws SQLException, IllegalArgumentException, DbException
    {
        ArrayList<HolidayOffer> result = new ArrayList<>();

        ProfileDao profileDao = new ProfileDao();
        AccommodationOfferDao accommodationOfferDao = new AccommodationOfferDao();
        TransportOfferDao transportOfferDao = new TransportOfferDao();

        while(rs.next())
        {
            // Retrieve the user that made the offer and the one to which the offer is intended to
            Profile bidderAgency = profileDao.getProfile(rs.getInt("bidderAgencyId"));
            Profile relativeReqOwner = profileDao.getProfile(rs.getInt("relativeRequirementsOwnerId"));

            HolidayOfferMetadata metadata = new HolidayOfferMetadata(
                    rs.getInt("id"),
                    bidderAgency,
                    HolidayOfferState.fromPersistenceType(rs.getString("offerState")),
                    rs.getInt("relativeRequirementsId"),
                    relativeReqOwner
            );

            DateRange duration = new DateRange(
                    rs.getDate("holidayStartDate").toLocalDate(),
                    rs.getDate("holidayEndDate").toLocalDate()
            );

            int accommodationOfferId = rs.getInt("accommodationOfferId");
            int transportOfferId = rs.getInt("transportOfferId");
            AccommodationOffer accommodationOffer = accommodationOfferDao.getOffer(accommodationOfferId);
            TransportOffer transportOffer = transportOfferDao.getOffer(transportOfferId);

            Location destination = transportOffer.getArrivalLocation();
            result.add(new HolidayOffer(metadata, destination, duration, rs.getInt("holidayPrice"), accommodationOffer, transportOffer));
        }

        return result;
    }

}
