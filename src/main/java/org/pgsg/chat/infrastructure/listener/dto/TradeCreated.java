package org.pgsg.chat.infrastructure.listener.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TradeCreated(
        UUID tradeId,
        UUID productId,
        String productName,
        UUID sellerId,
        @JsonProperty("sellerNickName")
        String sellerNickname,
        UUID buyerId,
        @JsonProperty("buyerNickName")
        String buyerNickname) { }
