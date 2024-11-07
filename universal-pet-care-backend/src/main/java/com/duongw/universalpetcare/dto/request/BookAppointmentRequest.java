package com.duongw.universalpetcare.dto.request;

import com.duongw.universalpetcare.model.Appointment;
import com.duongw.universalpetcare.model.Pet;
import lombok.Data;

import java.util.List;

@Data

public class BookAppointmentRequest {
    private Appointment appointment;
    private List<Pet> pets;


}
