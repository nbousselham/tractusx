package com.csds.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConsumerOfferResource extends CommonDTO {

	@JsonProperty("ids:representation")
	private List<ConsumerOfferResourceRepresentation> representation;
	
	@JsonProperty("ids:contractOffer")
	private List<ConsumerOfferResourceContract> contractOffer;
	
}

