package com.interview.skeletons.dtos.safes;

import com.interview.skeletons.dtos.items.ItemDTO;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record UpdateSafeBoxDTO(@NotEmpty List<ItemDTO> items) {
}