package com.phagondev.PhagonHotel.service.interfac;

import com.phagondev.PhagonHotel.dto.LoginRequest;
import com.phagondev.PhagonHotel.dto.Response;
import com.phagondev.PhagonHotel.entity.User;

public interface IUserService {
    Response register(User user);
    Response login(LoginRequest loginRequest);
    Response getAllUsers();
    Response getUserBookingHistory(String userId);
    Response deleteUser(String userId);
    Response getUserById(String userId);
    Response getMyInfo(String email);
}
