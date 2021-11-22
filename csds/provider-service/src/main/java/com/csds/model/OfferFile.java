package com.csds.model;

import java.io.InputStream;

import lombok.Data;

@Data
public class OfferFile {
	private String title;
	private InputStream stream;
}
