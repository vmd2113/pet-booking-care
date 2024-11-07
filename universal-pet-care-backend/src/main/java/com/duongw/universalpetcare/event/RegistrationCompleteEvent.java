package com.duongw.universalpetcare.event;

import com.duongw.universalpetcare.model.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter

public class RegistrationCompleteEvent extends ApplicationEvent {

    private User user;
    public RegistrationCompleteEvent(User user) {
        super(user);
        this.user = user;
    }

}
