package com.phagondev.PhagonHotel.repo;

import com.phagondev.PhagonHotel.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookingRepo extends JpaRepository<Booking, Long> {

    List<Booking> findByRoomId(Long roomId);

    Optional<Booking> findByBookingConfirmationCode(String confirmationCode);

    List<Booking> findByUserId(Long userId);
}
