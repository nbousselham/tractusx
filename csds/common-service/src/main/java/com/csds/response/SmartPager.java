package com.csds.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class SmartPager {

	private Integer currentPage;
	private Long totalItems;
	private Integer totalPages;
}
