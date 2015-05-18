package edu.thu.ss.logserver.request;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import edu.thu.ss.logserver.request.WriteRequest.Type;
import edu.thu.ss.logserver.request.util.ResponseUtil;

public class ReadStatusRequest extends ReadRequest {
	private Map<String, String> info;
	
	public ReadStatusRequest(){
		this.info = new HashMap<String, String>();
	}
	
	
	public boolean ProcessData(long timestmp, String name, String roomId,
			Type type) {
		if (type == Type.Enter){
			info.put(name, roomId);
		}
		else{
			info.put(name, "Hall");
		}
		return true;
	}

	public boolean EndRequest() {
		String result = new String();
		Iterator<Map.Entry<String, String> > iter = info.entrySet().iterator();
		while (iter.hasNext()){
			Map.Entry<String, String> entry = iter.next();
			if (!entry.getValue().equals("Hall")){
				result += String.format("%s\t%s\n", entry.getValue(), entry.getKey());
			}
		}
		ResponseUtil.response(id, result);
		return true;
	}
}
