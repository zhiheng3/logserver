package edu.thu.ss.logserver.request;

import edu.thu.ss.logserver.request.WriteRequest.Type;
import edu.thu.ss.logserver.request.util.ResponseUtil;

public final class ReadTimeRequest extends ReadRequest {
	public String name;
	public String roomId;
	
	private long enterTime;
	private long totalTime;
	
	public ReadTimeRequest(){
		super();
		this.enterTime = -1;
		this.totalTime = 0;
	}

	public boolean ProcessData(long timestmp, String name, String roomId,
			Type type) {
		if (!roomId.equals(this.roomId)){
			return true;
		}
		if (!name.equals(this.name)){
			return true;
		}
		if (type == Type.Enter){
			this.enterTime = timestmp;
		}
		if (type == Type.Leave){
			this.totalTime += timestmp - this.enterTime;
		}
		return true;
	}

	public boolean EndRequest() {
		ResponseUtil.response(id, String.format("%d", totalTime));
		return true;
	}
}
