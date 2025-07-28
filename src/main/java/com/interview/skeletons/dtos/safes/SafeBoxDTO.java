package com.interview.skeletons.dtos.safes;

import com.interview.skeletons.dtos.items.ItemDTO;

import java.io.Serializable;
import java.util.List;

public record SafeBoxDTO(
    Long id,
    String name,
    String password,
    List<ItemDTO> items
) implements Serializable {
}
