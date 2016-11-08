/**
 * 
 */
package com.admissify.models;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.stereotype.Component;

/**
 * @author braj.kishore
 *
 */

@Component("appHolder")
public class AppHolder {

	public ConcurrentMap<String,Task> tasks=new ConcurrentHashMap<String,Task>();
	
}
