package com.interview.skeletons.services;

import com.hazelcast.core.HazelcastInstance;
import com.interview.skeletons.cache.CacheUtils;
import com.interview.skeletons.dtos.items.ItemDTO;
import com.interview.skeletons.dtos.safes.CreateSafeBoxDTO;
import com.interview.skeletons.dtos.safes.SafeBoxDTO;
import com.interview.skeletons.dtos.safes.UpdateSafeBoxDTO;
import com.interview.skeletons.exceptions.SafeBoxException;
import com.interview.skeletons.exceptions.enums.SafeBoxErrorMessage;
import com.interview.skeletons.mappers.SafeBoxMapper;
import com.interview.skeletons.models.Item;
import com.interview.skeletons.models.SafeBox;
import com.interview.skeletons.repositories.SafeBoxRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@CacheConfig(cacheNames = CacheUtils.SAFE_BOXES)
public class SafeBoxServiceDefaultImpl implements SafeBoxService {
    private final SafeBoxRepository safeBoxRepository;
    private final HazelcastInstance hazelcastInstance;
    private static final Logger logger = LogManager.getLogger(SafeBoxServiceDefaultImpl.class);

    public SafeBoxServiceDefaultImpl(final SafeBoxRepository safeBoxRepository, final HazelcastInstance hazelcastInstance) {
        this.safeBoxRepository = safeBoxRepository;
        this.hazelcastInstance = hazelcastInstance;
    }

    @Override
    @Cacheable(key = "#safeBoxId")
    @Transactional(readOnly = true)
    public SafeBoxDTO getSafeBox(final Long safeBoxId) {
        final Optional<SafeBox> persistedSafeBox = safeBoxRepository.findById(safeBoxId);
        return persistedSafeBox.map(SafeBoxMapper::fromSafeBox)
            .orElseThrow(() -> {
                logger.error("[SAFE_BOX] : Safebox with {} id not found]", safeBoxId);
                return new SafeBoxException(SafeBoxErrorMessage.SAFE_BOX_NOT_FOUND);
            });
    }

    @Override
    @Transactional(readOnly = false)
    public SafeBoxDTO createSafeBox(final CreateSafeBoxDTO request) {
        final SafeBox createSafeBox = SafeBoxMapper.toSafeBox(request);
        final SafeBox persistedSafeBox = safeBoxRepository.save(createSafeBox);
        logger.info("[SAFE_BOX] : Safebox with {} id successfully created]", persistedSafeBox.getId());
        final SafeBoxDTO createdSafeBox = SafeBoxMapper.toSafeBoxDTO(persistedSafeBox);
        final String cacheName = CacheUtils.SAFE_BOXES;
        hazelcastInstance.getMap(cacheName).put(createdSafeBox.id(), createdSafeBox);
        return createdSafeBox;
    }

    @Override
    @CachePut(key = "#safeBoxId")
    @Transactional(readOnly = false)
    public List<ItemDTO> updateSafeBox(final Long safeBoxId, final UpdateSafeBoxDTO request) {
        final Optional<SafeBox> persistedSafeBox = safeBoxRepository.findById(safeBoxId);
        if (persistedSafeBox.isEmpty()) {
            logger.error("[SAFE_BOX] : Safebox with {} id not found]", safeBoxId);
            throw new SafeBoxException(SafeBoxErrorMessage.SAFE_BOX_NOT_FOUND);
        }
        final SafeBox updateSafeBox = persistedSafeBox.get();
        final List<Item> persistedItems = updateSafeBox.getItems();
        final List<Item> updateItemsCandidates = SafeBoxMapper.fromItemsDTO(request.items());
        persistedItems.addAll(updateItemsCandidates);
        safeBoxRepository.save(updateSafeBox);
        logger.info("[SAFE_BOX] : Safebox with {} successfully updated]", safeBoxId);

        final String cacheName = CacheUtils.SAFE_BOXES;
        hazelcastInstance.getMap(cacheName).put(safeBoxId, SafeBoxMapper.fromSafeBox(updateSafeBox));

        return SafeBoxMapper.toItemsDto(persistedItems);
    }
}
