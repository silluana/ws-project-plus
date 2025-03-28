package com.client.ws.projectplus.service.impl;

import com.client.ws.projectplus.controller.SubscriptionTypeController;
import com.client.ws.projectplus.dto.SubscriptionTypeDto;
import com.client.ws.projectplus.exception.BadRequestException;
import com.client.ws.projectplus.exception.NotFoundException;
import com.client.ws.projectplus.mapper.SubscriptionTypeMapper;
import com.client.ws.projectplus.model.SubscriptionType;
import com.client.ws.projectplus.repository.SubcriptionTypeRepository;
import com.client.ws.projectplus.service.SubscriptionTypeService;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class SubscriptionTypeServiceImpl implements SubscriptionTypeService {

    private static final String UPDATE = "update";
    private static final String DELETE = "delete";

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
        return getSubscriptionType(id).add(WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(SubscriptionTypeController.class).findById(id)).withSelfRel()
        ).add(WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(SubscriptionTypeController.class).update(id, new SubscriptionTypeDto()))
                .withRel(UPDATE)
        ).add(WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(SubscriptionTypeController.class).delete(id))
                .withRel(DELETE)
        );
    }

    @Override
    public SubscriptionType create(SubscriptionTypeDto dto) {
        if(Objects.nonNull(dto.getId())) {
            throw new BadRequestException("Id deve ser nulo");
        }
        return subcriptionTypeRepository.save(SubscriptionTypeMapper.fromDtoToEntity(dto));
    }

    @Override
    public SubscriptionType update(Long id, SubscriptionTypeDto dto) {
        getSubscriptionType(id);
        dto.setId(id);
        return subcriptionTypeRepository.save(SubscriptionTypeMapper.fromDtoToEntity(dto));
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
