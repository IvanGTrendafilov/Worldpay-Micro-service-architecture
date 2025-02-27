package com.trendafilov.ivan.worldpay.offerservice.services.impl;

import com.trendafilov.ivan.worldpay.offerservice.dtos.requests.ProductItemRequest;
import com.trendafilov.ivan.worldpay.offerservice.dtos.response.ProductItemResponse;
import com.trendafilov.ivan.worldpay.offerservice.entities.Offer;
import com.trendafilov.ivan.worldpay.offerservice.entities.ProductItem;
import com.trendafilov.ivan.worldpay.offerservice.mappers.ProductItemMapper;
import com.trendafilov.ivan.worldpay.offerservice.repositories.ProductItemRepository;
import com.trendafilov.ivan.worldpay.offerservice.services.IProductService;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {

    private final ProductItemRepository productItemRepository;
    private final ProductItemMapper productItemMapper;

    @Override
    public List<ProductItemResponse> saveProductItemsForOffer(final Offer offer,
                                                              final List<ProductItemRequest> productItemRequests) {
        log.debug("Save Product Items for Offer: {}", offer.getOfferId());
        final List<ProductItemResponse> productItemResponses = new ArrayList<>();
        productItemRequests.stream()
                           .forEach(productItemRequest -> {
                               ProductItem
                                   dbProductItem =
                                   productItemMapper.convertProductItemRequestToProductItemEntity(
                                       productItemRequest, offer);
                               dbProductItem = productItemRepository.save(dbProductItem);
                               productItemResponses.add(
                                   productItemMapper.convertProductItemToResponse(dbProductItem));
                           });
        return productItemResponses;
    }

    @Override
    public List<ProductItemResponse> getAllProductItemsResponsesForOffer(final Offer offer) {
        log.debug("Get all product items for Offer: {}", offer.getOfferId());
        final List<ProductItem>
            allProductItemsByOffer = offer.getProductItems();
        return allProductItemsByOffer.stream()
                                     .map(
                                         productItem -> productItemMapper.convertProductItemToResponse(
                                             productItem))
                                     .collect(
                                         Collectors.toList());
    }
}
