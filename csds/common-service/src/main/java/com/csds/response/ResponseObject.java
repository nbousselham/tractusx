/**
 * 
 */
package com.csds.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author sachinargade
 *
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ResponseObject {
	
	private String status;
	private Object data;
	private String message;
	private SmartPager page;
}
