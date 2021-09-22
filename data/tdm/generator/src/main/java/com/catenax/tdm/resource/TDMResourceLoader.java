package com.catenax.tdm.resource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

public class TDMResourceLoader {
	
	private static final Logger log = LoggerFactory.getLogger(TDMResourceLoader.class);

	public static InputStream loadResource(String path) throws IOException {
        try {
        	ResourceLoader loader = new DefaultResourceLoader();
            Resource resource = loader.getResource("classpath:" + path);
            
            // new ClassPathResource(filename).getInputStream();
            
            // File file = resource.getFile(); //.getFile("classpath:" + path);
            InputStream in = resource.getInputStream(); // FileInputStream(file);
            return in;
        } catch (IOException e) {
            log.error(e.getMessage());
            throw e;
        }
    }
	
	public static String resourceToString(String path) {
		String result = "";
		
		try {
			InputStream in = loadResource(path);
			 
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
	        StringBuilder out = new StringBuilder();
	        String line;
	        while ((line = reader.readLine()) != null) {
	            out.append(line);
	        }
	        
	        result = out.toString();
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
		
		return result;
	}
	
}
