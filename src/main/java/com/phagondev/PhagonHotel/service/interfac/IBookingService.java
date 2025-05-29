package com.phagondev.PhagonHotel.service.interfac;

import com.phagondev.PhagonHotel.dto.Response;
import com.phagondev.PhagonHotel.entity.Booking;

public interface IBookingService {
    Response saveBooking(Long roomId, Long userId, Booking bookingRequest);
    Response findBookingByConfirmationCode(String confirmationCode);
    Response getAllBookings();
    Response cancelBooking(Long bookingId);
}
