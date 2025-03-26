package com.client.ws.projectplus.service.impl;

import com.client.ws.projectplus.dto.SubscriptionTypeDto;
import com.client.ws.projectplus.exception.BadRequestException;
import com.client.ws.projectplus.exception.NotFoundException;
import com.client.ws.projectplus.model.SubscriptionType;
import com.client.ws.projectplus.repository.SubcriptionTypeRepository;
import com.client.ws.projectplus.service.SubscriptionTypeService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class SubscriptionTypeServiceImpl implements SubscriptionTypeService {

    private final SubcriptionTypeRepository subcriptionTypeRepository;

    SubscriptionTypeServiceImpl(SubcriptionTypeRepository subcriptionTypeRepository) {
        this.subcriptionTypeRepository = subcriptionTypeRepository;
    }

    @Override
    public List<SubscriptionType> findAll() {
        return subcriptionTypeRepository.findAll();
    }

    @Override
    public SubscriptionType findById(Long id) {
        return getSubscriptionType(id);
    }

    @Override
    public SubscriptionType create(SubscriptionTypeDto dto) {
        if(Objects.nonNull(dto.getId())) {
            throw new BadRequestException("Id deve ser nulo");
        }
        return subcriptionTypeRepository.save(SubscriptionType.builder()
                        .id(dto.getId())
                        .name(dto.getName())
                        .accessMonth(dto.getAccessMonth())
                        .price(dto.getPrice())
                        .productKey(dto.getProductKey())
                .build());
    }

    @Override
    public SubscriptionType update(Long id, SubscriptionTypeDto dto) {
        getSubscriptionType(id);

        return subcriptionTypeRepository.save(SubscriptionType.builder()
                .id(id)
                .name(dto.getName())
                .accessMonth(dto.getAccessMonth())
                .price(dto.getPrice())
                .productKey(dto.getProductKey())
                .build());
    }

    @Override
    public void delete(Long id) {
        getSubscriptionType(id);
        subcriptionTypeRepository.deleteById(id);
    }

    private SubscriptionType getSubscriptionType(Long id) {
        Optional<SubscriptionType> optionalSubscriptionType = subcriptionTypeRepository.findById(id);
        if (optionalSubscriptionType.isEmpty())
            throw new NotFoundException("SubscriptionType não encontrado");
        return optionalSubscriptionType.get();
    }
}
