package com.phagondev.PhagonHotel.service.interfac;

import com.phagondev.PhagonHotel.dto.Response;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

public interface IRoomService {
    Response addNewRoom(MultipartFile photo, String roomType, BigDecimal roomPrice, String description);
    List<String> getAllRoomTypes();
    Response getAllRooms();
    Response deleteRoom(Long roomId);
    Response updateRoom(Long roomId, MultipartFile photo, String roomType, BigDecimal roomPrice);
    Response getRoomById(Long roomId);
    Response getAvailableRoomsByDataAndType(String checkInDate, String checkOutDate, String roomType);
    Response getAllAvailableRooms();
}
