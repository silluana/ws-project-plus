package com.client.ws.projectplus.service;

import com.client.ws.projectplus.model.SubscriptionType;

import java.util.List;

public interface SubcriptionTypeService {

    List<SubscriptionType> findAll();

    SubscriptionType findById(Long id);

    SubscriptionType create(SubscriptionType subscriptionType);

    SubscriptionType update(Long id, SubscriptionType subscriptionType);

    void delete(Long id);
}
