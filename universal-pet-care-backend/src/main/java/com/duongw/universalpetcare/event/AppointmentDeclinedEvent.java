package com.duongw.universalpetcare.event;

import com.duongw.universalpetcare.model.Appointment;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter

public class AppointmentDeclinedEvent extends ApplicationEvent {
    private Appointment appointment;

    public AppointmentDeclinedEvent(Appointment appointment ) {
        super(appointment);
        this.appointment = appointment;
    }
}
