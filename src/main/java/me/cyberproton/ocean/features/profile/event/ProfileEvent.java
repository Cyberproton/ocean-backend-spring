package me.cyberproton.ocean.features.profile.event;

import lombok.Getter;

import me.cyberproton.ocean.features.profile.entity.ProfileEntity;

import org.springframework.context.ApplicationEvent;

@Getter
public class ProfileEvent extends ApplicationEvent {
    private final Type type;
    private final ProfileEntity profile;

    public ProfileEvent(Type type, ProfileEntity profile) {
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
