package me.cyberproton.ocean.features.track;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import me.cyberproton.ocean.annotations.V1ApiRestController;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@AllArgsConstructor
@V1ApiRestController
@RequestMapping("/tracks")
public class TrackController {
    private final TrackService trackService;

    @GetMapping
    public Set<TrackResponse> getTracks() {
        return trackService.getTracks();
    }

    @GetMapping("/{id}")
    public TrackResponse getTrack(@PathVariable Long id) {
        return trackService.getTrackById(id);
    }

    @PostMapping
    public TrackResponse createTrack(@Valid @RequestBody CreateOrUpdateTrackRequest request) {
        return trackService.createTrack(request);
    }

    @PutMapping("/{id}")
    public TrackResponse updateTrack(@PathVariable Long id, @Valid @RequestBody CreateOrUpdateTrackRequest request) {
        return trackService.updateTrack(id, request);
    }

    @DeleteMapping("/{id}")
    public Long deleteTrack(@PathVariable Long id) {
        return trackService.deleteTrack(id);
    }
}
