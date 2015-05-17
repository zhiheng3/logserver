package edu.thu.ss.logserver.request;

import java.util.LinkedList;
import java.util.List;

import edu.thu.ss.logserver.request.WriteRequest.Type;
import edu.thu.ss.logserver.request.util.ResponseUtil;

public final class ReadEmployeeRequest extends ReadRequest {
	public String name;

	private List<String> roomList;

	public ReadEmployeeRequest() {
		this.roomList = new LinkedList<String>();
	}

	public boolean ProcessData(long timestmp, String name, String roomId,
			Type type) {
		if (!name.equals(this.name)) {
			return true;
		}
		if (type == Type.Enter) {
			if (!roomList.contains(roomId)) {
				roomList.add(roomId);
			}
		}
		return true;
	}

	public boolean EndRequest() {
		String[] list = (String[]) roomList
				.toArray(new String[roomList.size()]);
		String result = new String();
		for (int i = 0; i < list.length; ++i) {
			if (i != 0)
				result += ",";
			result += list[i];
		}
		ResponseUtil.response(id, result);
		return true;
	}
}
