package com.duongw.universalpetcare.event;

import com.duongw.universalpetcare.model.Appointment;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Setter
@Getter

public class AppointmentBookingEvent extends ApplicationEvent {

    private Appointment appointment;
    public AppointmentBookingEvent(Appointment appointment ) {
        super(appointment);
        this.appointment = appointment;
    }
}
