package com.phagondev.PhagonHotel.utils;

import com.phagondev.PhagonHotel.dto.BookingDto;
import com.phagondev.PhagonHotel.dto.RoomDto;
import com.phagondev.PhagonHotel.dto.UserDto;
import com.phagondev.PhagonHotel.entity.Booking;
import com.phagondev.PhagonHotel.entity.Room;
import com.phagondev.PhagonHotel.entity.User;

import java.security.SecureRandom;
import java.util.List;

public class Utils {

    private static final String ALPHANUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final SecureRandom secureRandom = new SecureRandom();

    public static String generateRandomAlphanumeric(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int randomIndex = secureRandom.nextInt(ALPHANUMERIC_STRING.length());
            char randomChar = ALPHANUMERIC_STRING.charAt(randomIndex);
            sb.append(randomChar);
        }
        return sb.toString();
    }

    public static UserDto mapUserEntityToUserDTO(User user){
        UserDto userDto = new UserDto();

        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        userDto.setPassword(user.getPassword());
        userDto.setRole(user.getRole());
        return userDto;
    }
    public static RoomDto mapRoomEntityToRoomDTO(Room room) {
        RoomDto roomDto = new RoomDto();

        roomDto.setId(room.getId());
        roomDto.setRoomType(room.getRoomType());
        roomDto.setRoomPhotoUrl(room.getRoomPhotoUrl());
        roomDto.setRoomPrice(room.getRoomPrice());
        roomDto.setRoomDescription(room.getRoomDescription());

        return roomDto;
    }
    public static BookingDto mapBookingEntityToBookingDTO(Booking booking) {
        BookingDto bookingDto = new BookingDto();

        bookingDto.setId(booking.getId());
        bookingDto.setCheckInDate(booking.getCheckInDate());
        bookingDto.setCheckOutDate(booking.getCheckOutDate());
        bookingDto.setNumOfAdults(booking.getNumberOfAdults());
        bookingDto.setNumOfChildren(booking.getNumberOfChildren());
        bookingDto.setTotalNumOfGuest(booking.getTotalNumberOfGuests());
        bookingDto.setBookingConfirmationCode(booking.getBookingConfirmationCode());

        return bookingDto;
    }

    public static RoomDto mapRoomEntityToRoomDTOPlusBookings(Room room) {
        RoomDto roomDto = new RoomDto();

        roomDto.setId(room.getId());
        roomDto.setRoomType(room.getRoomType());
        roomDto.setRoomPhotoUrl(room.getRoomPhotoUrl());
        roomDto.setRoomPrice(room.getRoomPrice());
        roomDto.setRoomDescription(room.getRoomDescription());

        if (room.getBookings() != null) {
            roomDto.setBookings(room.getBookings().stream()
                    .map(Utils::mapBookingEntityToBookingDTO)
                    .toList());
        }

        return roomDto;
    }

    public static UserDto mapUserEntityToUserDTOPlusUserBookingsAndRoom(User user) {
        UserDto userDto = new UserDto();

        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        userDto.setPassword(user.getPassword());
        userDto.setRole(user.getRole());

        if (!user.getBookings().isEmpty()) {
            userDto.setBookings(user.getBookings().stream()
                    .map(booking -> mapBookingEntityToBookingDTOPlusBookedRoom(booking, false))
                    .toList());

        }
        return userDto;
    }

    public static BookingDto mapBookingEntityToBookingDTOPlusBookedRoom(Booking booking, boolean mapUser){
        BookingDto bookingDto = new BookingDto();

        bookingDto.setId(booking.getId());
        bookingDto.setCheckInDate(booking.getCheckInDate());
        bookingDto.setCheckOutDate(booking.getCheckOutDate());
        bookingDto.setNumOfAdults(booking.getNumberOfAdults());
        bookingDto.setNumOfChildren(booking.getNumberOfChildren());
        bookingDto.setTotalNumOfGuest(booking.getTotalNumberOfGuests());
        bookingDto.setBookingConfirmationCode(booking.getBookingConfirmationCode());

        if (mapUser){
            bookingDto.setUser(Utils.mapUserEntityToUserDTO(booking.getUser()));
        }
        if (booking.getRoom() != null) {
            RoomDto roomDto = new RoomDto();

            roomDto.setId(booking.getRoom().getId());
            roomDto.setRoomType(booking.getRoom().getRoomType());
            roomDto.setRoomPhotoUrl(booking.getRoom().getRoomPhotoUrl());
            roomDto.setRoomPrice(booking.getRoom().getRoomPrice());
            roomDto.setRoomDescription(booking.getRoom().getRoomDescription());

            bookingDto.setRoom(roomDto);
        }
        return bookingDto;

    }

    public static List<UserDto> mapUserListEntityToUserListDTO(List<User> users) {
        return users.stream().map(Utils::mapUserEntityToUserDTO).toList();
    }
    public static List<RoomDto> mapRoomListEntityToRoomListDTO(List<Room> rooms) {
        return rooms.stream().map(Utils::mapRoomEntityToRoomDTO).toList();
    }
    public static List<BookingDto> mapBookingListEntityToBookingListDTO(List<Booking> bookings) {
        return bookings.stream().map(Utils::mapBookingEntityToBookingDTO).toList();
    }
}
