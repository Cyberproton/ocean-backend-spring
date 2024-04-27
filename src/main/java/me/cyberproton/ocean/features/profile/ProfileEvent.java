package me.cyberproton.ocean.features.profile;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class ProfileEvent extends ApplicationEvent {
    private final Type type;
    private final Profile profile;

    public ProfileEvent(Type type, Profile profile) {
        super(profile);
        this.type = type;
        this.profile = profile;
    }

    public enum Type {
        CREATE,
        UPDATE,
        DELETE
    }
}
