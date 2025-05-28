package com.phagondev.PhagonHotel.service.impl;

import com.phagondev.PhagonHotel.dto.Response;
import com.phagondev.PhagonHotel.dto.RoomDto;
import com.phagondev.PhagonHotel.entity.Room;
import com.phagondev.PhagonHotel.exception.OurException;
import com.phagondev.PhagonHotel.repo.BookingRepo;
import com.phagondev.PhagonHotel.repo.RoomRepo;
import com.phagondev.PhagonHotel.service.AwsS3Service;
import com.phagondev.PhagonHotel.service.interfac.IRoomService;
import com.phagondev.PhagonHotel.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomService implements IRoomService {

    private final RoomRepo roomRepo;
    private final BookingRepo bookingRepo;
    private final AwsS3Service awsS3Service;

    @Override
    public Response addNewRoom(MultipartFile photo, String roomType, BigDecimal roomPrice, String description) {
        Response response = new Response();

        try{
            String imageUrl = awsS3Service.saveImageToS3(photo);
            Room room = new Room();
            room.setRoomPhotoUrl(imageUrl);
            room.setRoomType(roomType);
            room.setRoomPrice(roomPrice);
            room.setRoomDescription(description);
            Room savedRoom = roomRepo.save(room);
            RoomDto roomDto = Utils.mapRoomEntityToRoomDTO(savedRoom);
            response.setStatusCode(200);
            response.setMessage("Room added successfully");
            response.setRoom(roomDto);
        }catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error saving room " + e.getMessage());
        }
        return response;
    }

    @Override
    public List<String> getAllRoomTypes() {
        return roomRepo.findDistinctRoomType();
    }

    @Override
    public Response getAllRooms() {
        Response response = new Response();

        try{
            List<Room> roomList = roomRepo.findAll(Sort.by(Sort.Direction.DESC, "id"));
            List<RoomDto> roomDtoList = Utils.mapRoomListEntityToRoomListDTO(roomList);
            response.setStatusCode(200);
            response.setMessage("Rooms fetched successfully");
            response.setRoomList(roomDtoList);
        }catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error getting all rooms " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response deleteRoom(Long roomId) {
        Response response = new Response();

        try {
            roomRepo.findById(roomId).orElseThrow(() -> new OurException("Room not found"));
            response.setStatusCode(200);
            response.setMessage("Rooms fetched successfully");
        }catch (OurException e){
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        }catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error getting all rooms " + e.getMessage());
        }
        return response;

    }

    @Override
    public Response updateRoom(Long roomId, String roomDescription, MultipartFile photo, String roomType, BigDecimal roomPrice) {
        Response response = new Response();

        try {
            String imageUrl = null;
            if (photo != null && !photo.isEmpty()) {
                imageUrl = awsS3Service.saveImageToS3(photo);
            }
            Room room = roomRepo.findById(roomId).orElseThrow(()-> new OurException("Room not found"));
            if (roomType != null) room.setRoomType(roomType);
            if (roomPrice != null) room.setRoomPrice(roomPrice);
            if (roomDescription != null) room.setRoomDescription(roomDescription);
            if (imageUrl != null) room.setRoomPhotoUrl(imageUrl);

            Room updatedRoom = roomRepo.save(room);
            RoomDto roomDto = Utils.mapRoomEntityToRoomDTO(updatedRoom);

            response.setStatusCode(200);
            response.setMessage("Room updated successfully");
            response.setRoom(roomDto);

        }catch (OurException e){
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        }catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error getting all rooms " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getRoomById(Long roomId) {
        Response response = new Response();

        try {
            Room room = roomRepo.findById(roomId).orElseThrow(()-> new OurException("Room not found"));
            RoomDto roomDto = Utils.mapRoomEntityToRoomDTOPlusBookings(room);

            response.setStatusCode(200);
            response.setMessage("Rooms fetched successfully");
            response.setRoom(roomDto);

        }catch (OurException e){
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        }catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error getting all rooms " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getAvailableRoomsByDataAndType(LocalDate checkInDate, LocalDate checkOutDate, String roomType) {
        Response response = new Response();

        try {
            List<Room> availableRooms = roomRepo.findAvailableRoomsByDatesAndTypes(checkInDate, checkOutDate, roomType);
            List<RoomDto> roomDto = Utils.mapRoomListEntityToRoomListDTO(availableRooms);

            response.setStatusCode(200);
            response.setMessage("Rooms fetched successfully");
            response.setRoomList(roomDto);

        }catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error getting all rooms " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getAllAvailableRooms() {
        Response response = new Response();

        try {
            List<Room> roomList = roomRepo.getAllAvailableRooms();
            List<RoomDto> roomDto = Utils.mapRoomListEntityToRoomListDTO(roomList);

            response.setStatusCode(200);
            response.setMessage("Rooms fetched successfully");
            response.setRoomList(roomDto);

        }catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error getting all rooms " + e.getMessage());
        }
        return response;
    }
}
