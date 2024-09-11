package me.cyberproton.ocean.features.history.controller;

import jakarta.validation.Valid;

import lombok.AllArgsConstructor;

import me.cyberproton.ocean.annotations.V1ApiRestController;
import me.cyberproton.ocean.config.AppUserDetails;
import me.cyberproton.ocean.domain.PaginationResponse;
import me.cyberproton.ocean.features.history.dto.CreateHistoryRequest;
import me.cyberproton.ocean.features.history.dto.HistoryQuery;
import me.cyberproton.ocean.features.history.dto.HistoryResponse;
import me.cyberproton.ocean.features.history.service.HistoryService;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@V1ApiRestController
@RequestMapping("/histories")
public class HistoryController {
    private final HistoryService historyService;

    @GetMapping
    public PaginationResponse<?> getHistories(
            @AuthenticationPrincipal AppUserDetails userDetails, @Valid HistoryQuery query) {
        return historyService.getHistories(userDetails.getUser(), query);
    }

    @GetMapping("{id}")
    public HistoryResponse getHistory(@PathVariable("id") Long id) {
        return historyService.getHistory(id);
    }

    @PostMapping
    public HistoryResponse createHistory(
            @Valid @RequestBody CreateHistoryRequest request,
            @AuthenticationPrincipal AppUserDetails userDetails) {
        return historyService.createHistory(userDetails.getUser(), request);
    }

    @DeleteMapping("{id}")
    public void deleteHistory(
            @PathVariable("id") Long id, @AuthenticationPrincipal AppUserDetails userDetails) {
        historyService.deleteHistory(id);
    }
}
