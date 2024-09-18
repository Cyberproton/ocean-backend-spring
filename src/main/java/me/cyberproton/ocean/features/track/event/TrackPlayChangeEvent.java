package me.cyberproton.ocean.features.track.event;

import lombok.Getter;

import me.cyberproton.ocean.features.track.dto.TrackPlayDto;

import org.springframework.context.ApplicationEvent;

import java.util.List;

@Getter
public class TrackPlayChangeEvent extends ApplicationEvent {
    private final List<TrackPlayDto> trackPlayDtos;

    public TrackPlayChangeEvent(List<TrackPlayDto> source) {
        super(source);
        this.trackPlayDtos = source;
    }
}
