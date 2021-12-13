package com.csds.entity;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Document(collection = "provider_control_panel_connector_details")
public class ProviderControlPanelConnectorDetails {

	private String _id;
	private String connectorbaseURL;
	private String connectorUsername;
	private String connectorPassword;
}
