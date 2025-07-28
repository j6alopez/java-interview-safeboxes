package com.interview.skeletons.services;

import com.interview.skeletons.dtos.items.ItemDTO;
import com.interview.skeletons.dtos.safes.CreateSafeBoxDTO;
import com.interview.skeletons.dtos.safes.SafeBoxDTO;
import com.interview.skeletons.dtos.safes.UpdateSafeBoxDTO;

import java.util.List;

public interface SafeBoxService {
    SafeBoxDTO getSafeBox(Long id);

    SafeBoxDTO createSafeBox(CreateSafeBoxDTO request);

    List<ItemDTO> updateSafeBox(Long safeBoxId, UpdateSafeBoxDTO request);
}
