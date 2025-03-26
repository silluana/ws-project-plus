package com.client.ws.projectplus.service.impl;

import com.client.ws.projectplus.exception.NotFoundException;
import com.client.ws.projectplus.model.SubscriptionType;
import com.client.ws.projectplus.repository.SubcriptionTypeRepository;
import com.client.ws.projectplus.service.SubcriptionTypeService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SubcriptionTypeServiceImpl implements SubcriptionTypeService {

    private final SubcriptionTypeRepository subcriptionTypeRepository;

    SubcriptionTypeServiceImpl(SubcriptionTypeRepository subcriptionTypeRepository) {
        this.subcriptionTypeRepository = subcriptionTypeRepository;
    }

    @Override
    public List<SubscriptionType> findAll() {
        return subcriptionTypeRepository.findAll();
    }

    @Override
    public SubscriptionType findById(Long id) {
        Optional<SubscriptionType> optionalSubscriptionType = subcriptionTypeRepository.findById(id);
        if (optionalSubscriptionType.isEmpty())
            throw new NotFoundException("SubscriptionType não encontrado");

        return optionalSubscriptionType.get();
    }

    @Override
    public SubscriptionType create(SubscriptionType subscriptionType) {
        return null;
    }

    @Override
    public SubscriptionType update(Long id, SubscriptionType subscriptionType) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }
}
