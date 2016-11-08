/**
 * 
 */
package com.admissify.models;

/**
 * @author braj.kishore
 *
 */
public class Task implements Runnable{
	
	private String connId;
	private long timeout;
	private volatile String status;
	private volatile long remTime;
	
	
	public Task(String connId,long timeout){
		this.connId=connId;
		this.timeout=timeout;
		this.remTime=timeout;		
		this.status=TaskConstant.RUNNABLE;
	}
	
	
	/**
	 * @return the connId
	 */
	public String getConnId() {
		return connId;
	}
	
	/**
	 * @return the timeout
	 */
	public long getTimeout() {
		return timeout;
	}
	
	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * @param staus the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * @return the remTime
	 */
	public long getRemTime() {
		return remTime;
	}
	/**
	 * @param remTime the remTime to set
	 */
	public void setRemTime(long remTime) {
		this.remTime = remTime;
	}
	
	
	
	
	
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((connId == null) ? 0 : connId.hashCode());
		return result;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Task)) {
			return false;
		}
		Task other = (Task) obj;
		if (connId == null) {
			if (other.connId != null) {
				return false;
			}
		} else if (!connId.equals(other.connId)) {
			return false;
		}
		return true;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		this.status=TaskConstant.RUNNING;
		for(int i=0;i<timeout;i++){
			if(TaskConstant.KILLED.equalsIgnoreCase(status))
				break;
			
			try {				
				Thread.sleep(1000);
				--remTime;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}
		
		if(!TaskConstant.KILLED.equalsIgnoreCase(status) && remTime==0)
			status=TaskConstant.OK;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Task [connId=" + connId + ", timeout=" + timeout + ", status=" + status + ", remTime=" + remTime + "]";
	}
	
	
	
	
	
	
}
