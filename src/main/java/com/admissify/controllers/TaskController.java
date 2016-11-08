/**
 * 
 */
package com.admissify.controllers;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.admissify.models.AppHolder;
import com.admissify.models.KillCommand;
import com.admissify.models.Status;
import com.admissify.models.Task;
import com.admissify.models.TaskConstant;

/**
 * @author braj.kishore
 *
 */

@RestController
public class TaskController {

	
	@Resource(name="appHolder")
	private AppHolder appHolder;
	
	@RequestMapping("/")
	public String welcome(){
		return "Welcome to admissify services.";
	}
	
	
	@RequestMapping(value="/api/request",method=RequestMethod.GET,produces=MediaType.APPLICATION_JSON_VALUE)	
	public ResponseEntity<?> submitTask(@RequestParam("connId") String connId,@RequestParam("timeout") long timeout){
		
		
		Status status=new Status();
		if(appHolder.tasks.containsKey(connId)
				&& (TaskConstant.RUNNABLE.equals(appHolder.tasks.get(connId).getStatus())
						||TaskConstant.RUNNING.equals(appHolder.tasks.get(connId).getStatus()))){
			status.setStatus("Task already running");
			return ResponseEntity.ok(status);
		}
		
		Task task=new Task(connId,timeout);		
		
		Thread t=new Thread(task);
		t.start();
		try {
			appHolder.tasks.put(task.getConnId(), task);			
			t.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			status.setStatus(""+e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(status); 
		}
				
		status.setStatus(task.getStatus());		
		return ResponseEntity.ok(status); 
	}
	
	@RequestMapping(value="/api/serverStatus",method=RequestMethod.GET,produces=MediaType.APPLICATION_JSON_VALUE)	
	public ResponseEntity<?> taskStatus(){
		
		Map<String,Long> tasks=new HashMap<String,Long>();
		for(Entry<String,Task> task:appHolder.tasks.entrySet()){			
			if(task.getValue().getRemTime()>0 && TaskConstant.RUNNING.equals(task.getValue().getStatus()))
				tasks.put(task.getValue().getConnId(), task.getValue().getRemTime());				
		}		
		if(tasks.size()>0)		
			return ResponseEntity.ok(tasks);
		else{
			Status status=new Status();
			status.setStatus("No running tasks");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(status);
		}
	}
	
	@RequestMapping(value="/api/kill",method=RequestMethod.PUT,produces=MediaType.APPLICATION_JSON_VALUE,
			consumes=MediaType.APPLICATION_JSON_VALUE)	
	public ResponseEntity<?> taskStatus(@RequestBody KillCommand killCommand){
		
		Status status=new Status();
		if(!appHolder.tasks.containsKey(killCommand.getConnId())
				||!(TaskConstant.RUNNABLE.equals(appHolder.tasks.get(killCommand.getConnId()).getStatus())
						||TaskConstant.RUNNING.equals(appHolder.tasks.get(killCommand.getConnId()).getStatus()))){
			status.setStatus("Invalid connection Id:"+killCommand.getConnId());
			return ResponseEntity.ok(status);
		}
		
		Task task=appHolder.tasks.get(killCommand.getConnId());
		task.setStatus(TaskConstant.KILLED);		
		status.setStatus(TaskConstant.OK);
		return ResponseEntity.ok(status);
	}
	
}
