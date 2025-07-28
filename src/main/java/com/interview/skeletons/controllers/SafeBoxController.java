package com.interview.skeletons.controllers;

import com.interview.skeletons.config.ApiConfig;
import com.interview.skeletons.dtos.safes.CreateSafeBoxDTO;
import com.interview.skeletons.dtos.safes.SafeBoxDTO;
import com.interview.skeletons.dtos.safes.UpdateSafeBoxDTO;
import com.interview.skeletons.services.SafeBoxService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = ApiConfig.BASE_PATH + "/safeboxes")
public class SafeBoxController {
    private final SafeBoxService safeBoxService;

    public SafeBoxController(final SafeBoxService safeBoxService) {
        this.safeBoxService = safeBoxService;
    }

    @PostMapping
    public final SafeBoxDTO createSafeBox(@RequestBody @Valid final CreateSafeBoxDTO request) {
        return safeBoxService.createSafeBox(request);
    }

    @GetMapping("/{id}")
    public final SafeBoxDTO getSafeBox(@PathVariable final Long id) {
        return safeBoxService.getSafeBox(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public final void updateSafeBox(@PathVariable final Long id, @RequestBody @Valid final UpdateSafeBoxDTO request) {
        safeBoxService.updateSafeBox(id, request);
    }

}
