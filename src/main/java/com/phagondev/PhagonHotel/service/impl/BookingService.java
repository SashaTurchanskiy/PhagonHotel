package com.phagondev.PhagonHotel.service.impl;

import com.phagondev.PhagonHotel.dto.BookingDto;
import com.phagondev.PhagonHotel.dto.Response;
import com.phagondev.PhagonHotel.entity.Booking;
import com.phagondev.PhagonHotel.entity.Room;
import com.phagondev.PhagonHotel.entity.User;
import com.phagondev.PhagonHotel.exception.OurException;
import com.phagondev.PhagonHotel.repo.BookingRepo;
import com.phagondev.PhagonHotel.repo.RoomRepo;
import com.phagondev.PhagonHotel.repo.UserRepo;
import com.phagondev.PhagonHotel.service.interfac.IBookingService;
import com.phagondev.PhagonHotel.service.interfac.IRoomService;
import com.phagondev.PhagonHotel.utils.Utils;
import jdk.jshell.execution.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService implements IBookingService {

    private final BookingRepo bookingRepo;
    private final IRoomService roomService;
    private final RoomRepo roomRepo;
    private final UserRepo userRepo;

    @Override
    public Response saveBooking(Long roomId, Long userId, Booking bookingRequest) {
        Response response = new Response();
        try{
            if (bookingRequest.getCheckOutDate().isBefore(bookingRequest.getCheckInDate())){
                throw new OurException("Check out date cannot be before check in date");
            }
            Room room = roomRepo.findById(roomId).orElseThrow(()-> new OurException("Room not found"));
            User user = userRepo.findById(userId).orElseThrow(()-> new OurException("User not found"));

            List<Booking> existingBookings = room.getBookings();
            
            if (!roomAvailable(existingBookings, bookingRequest)){
                throw new OurException("Room is not available for booking");
            }

            bookingRequest.setRoom(room);
            bookingRequest.setUser(user);
            String bookingConfirmationCode = Utils.generateRandomConfirmationCode(10);
            bookingRequest.setBookingConfirmationCode(bookingConfirmationCode);
            bookingRepo.save(bookingRequest);
            response.setStatusCode(200);
            response.setMessage("Booking saved successfully");
            response.setBookingConfirmationCode(bookingConfirmationCode);

        }catch (OurException e){
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        }catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error saving booking " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response findBookingByConfirmationCode(String confirmationCode) {
        Response response = new Response();
        try{
            Booking booking = bookingRepo.findByBookingConfirmationCode(confirmationCode).orElseThrow(()-> new OurException("Booking not found"));
            BookingDto bookingDto = Utils.mapBookingEntityToBookingDTO(booking);
            response.setStatusCode(200);
            response.setMessage("Booking saved successfully");
            response.setBooking(bookingDto);

        }catch (OurException e){
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        }catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error finding a booking " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getAllBookings() {
        Response response = new Response();
        try{
            List<Booking> booking = bookingRepo.findAll(Sort.by(Sort.Direction.DESC, "id"));
            List<BookingDto> bookingDto = Utils.mapBookingListEntityToBookingListDTO(booking);
            response.setStatusCode(200);
            response.setMessage("Booking saved successfully");
            response.setBookingList(bookingDto);

        }catch (OurException e){
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        }catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error getting all booking " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response cancelBooking(Long bookingId) {
        Response response = new Response();
        try{
            bookingRepo.findById(bookingId).orElseThrow(()-> new OurException("Booking not found"));
            bookingRepo.deleteById(bookingId);
            response.setStatusCode(200);
            response.setMessage("Booking cancelled successfully");

        }catch (OurException e){
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        }catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error cancelling a bookings " + e.getMessage());
        }
        return response;
    }

    private boolean roomAvailable(List<Booking> existingBookings, Booking bookingRequest) {
        return existingBookings.stream()
                .noneMatch(existingBooking ->
                        bookingRequest.getCheckInDate().equals(existingBooking.getCheckInDate())
                                || bookingRequest.getCheckOutDate().isBefore(existingBooking.getCheckOutDate())
                                || (bookingRequest.getCheckInDate().isAfter(existingBooking.getCheckInDate())
                                && bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckOutDate()))
                                || (bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckInDate())

                                && bookingRequest.getCheckOutDate().equals(existingBooking.getCheckOutDate()))
                                || (bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckInDate())

                                && bookingRequest.getCheckOutDate().isAfter(existingBooking.getCheckOutDate()))

                                || (bookingRequest.getCheckInDate().equals(existingBooking.getCheckOutDate())
                                && bookingRequest.getCheckOutDate().equals(existingBooking.getCheckInDate()))

                                || (bookingRequest.getCheckInDate().equals(existingBooking.getCheckOutDate())
                                && bookingRequest.getCheckOutDate().equals(bookingRequest.getCheckInDate()))
                );
    }
}
