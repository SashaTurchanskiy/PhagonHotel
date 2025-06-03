package com.phagondev.PhagonHotel.controller;

import com.phagondev.PhagonHotel.dto.Response;
import com.phagondev.PhagonHotel.service.interfac.IBookingService;
import com.phagondev.PhagonHotel.service.interfac.IRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/room")
@RequiredArgsConstructor
public class RoomController {

    private final IRoomService roomService;
    private final IBookingService bookingService;

    public ResponseEntity<Response> addNewRoom(
            @RequestParam(value = "photo", required = false)MultipartFile photo,
            @RequestParam(value = "roomType", required = false) String roomType,
            @RequestParam(value = "roomPrice", required = false) BigDecimal roomPrice,
            @RequestParam(value = "roomDescription", required = false) String roomDescription
            ){
        if (photo == null || photo.isEmpty() || roomType == null || roomType.isBlank() || roomPrice == null || roomType.isBlank()){
            Response response = new Response();
            response.setStatusCode(400);
            response.setMessage("All fields are required");
            return ResponseEntity.status(response.getStatusCode()).body(response);
        }
        Response response = roomService.addNewRoom(photo, roomType, roomPrice, roomDescription);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
    @GetMapping("/all")

    public ResponseEntity<Response> getAllRooms(){
        Response response = roomService.getAllRooms();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
    @GetMapping("/types")
    public List<String> getRoomsTypes(){
        return roomService.getAllRoomTypes();
    }
    @GetMapping("/room-by-id/{roomId}")
    public ResponseEntity<Response> getRoomById(@PathVariable Long roomId){
        Response response = roomService.getRoomById(roomId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
    @GetMapping("/all-available-room")
    public ResponseEntity<Response> getAvailableRooms(){
        Response response = roomService.getAllAvailableRooms();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }


}
