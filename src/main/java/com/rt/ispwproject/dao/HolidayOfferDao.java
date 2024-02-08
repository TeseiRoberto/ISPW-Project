package com.rt.ispwproject.dao;

import com.rt.ispwproject.exceptions.DbException;
import com.rt.ispwproject.model.HolidayOffer;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;

public class HolidayOfferDao {


    // Stores given holiday offer in db
    public void postOffer(int bidderId, int announcementId, HolidayOffer offer) throws DbException
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

            createOfferProc.setInt("requirementsId_in", announcementId);
            createOfferProc.setInt("bidderAgencyId_in", bidderId);
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

}
