package me.cyberproton.ocean.features.user;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class UserEvent extends ApplicationEvent {
    private final UserEntity user;
    private final Type type;

    public UserEvent(Type type, UserEntity user) {
        super(user);
        this.user = user;
        this.type = type;
    }

    @Override
    public UserEntity getSource() {
        return user;
    }

    public enum Type {
        CREATE,
        //        UPDATE,
        //        DELETE
    }
}
