package com.interview.skeletons.mappers;

import com.interview.skeletons.dtos.items.ItemDTO;
import com.interview.skeletons.dtos.safes.CreateSafeBoxDTO;
import com.interview.skeletons.dtos.safes.SafeBoxDTO;
import com.interview.skeletons.models.Item;
import com.interview.skeletons.models.SafeBox;
import org.springframework.util.CollectionUtils;

import java.util.List;

public class SafeBoxMapper {

    public static SafeBox toSafeBox(CreateSafeBoxDTO createSafeBox) {
        if (createSafeBox == null) {
            return null;
        }
        return new SafeBox(createSafeBox.name(), createSafeBox.password());
    }

    public static SafeBoxDTO toSafeBoxDTO(SafeBox safeBox) {
        if (safeBox == null) {
            return null;
        }

        List<ItemDTO> itemDTOs = toItemsDto(safeBox.getItems());

        return new SafeBoxDTO(
            safeBox.getId(),
            safeBox.getName(),
            safeBox.getPassword(),
            itemDTOs
        );
    }

    public static SafeBoxDTO fromSafeBox(SafeBox safeBox) {
        if (safeBox == null) {
            return null;
        }

        return toSafeBoxDTO(safeBox);

    }

    public static Item fromItemDto(ItemDTO in) {
        if (in == null) {
            return null;
        }
        return new Item(in.description());
    }

    public static List<Item> fromItemsDTO(List<ItemDTO> in) {
        if (in == null || CollectionUtils.isEmpty(in)) {
            return null;
        }
        return in.stream().map(SafeBoxMapper::fromItemDto).toList();
    }

    private static ItemDTO toItemDto(Item item) {
        if (item == null) {
            return null;
        }
        return new ItemDTO(item.getDescription());
    }

    public static List<ItemDTO> toItemsDto(List<Item> items) {
        if (items == null || CollectionUtils.isEmpty(items)) {
            return null;
        }
        return items.stream().map(SafeBoxMapper::toItemDto).toList();
    }

}
