package edu.thu.ss.logserver;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;

import edu.thu.ss.logserver.request.ReadEmployeeRequest;
import edu.thu.ss.logserver.request.ReadIntervalRequest;
import edu.thu.ss.logserver.request.ReadStatusRequest;
import edu.thu.ss.logserver.request.ReadTimeRequest;
import edu.thu.ss.logserver.request.Request;
import edu.thu.ss.logserver.request.WriteRequest;
import edu.thu.ss.logserver.request.WriteRequest.Type;

public class LogClient implements Runnable {

	private BlockingQueue<Request> queue;
	
	public LogClient(BlockingQueue<Request> queue) {
		this.queue = queue;
	}

	@Override
	public void run() {
		//send request here
		//queue.add(...)
		try {
			Scanner inScanner = new Scanner(new File("example/request.txt"));
			while (inScanner.hasNextLine()) {
                String str = inScanner.nextLine();
                String[] __request_string = splitString(str);
                switch(__request_string[0]){
	            	case "0":
	            		long __timestmp = Long.parseLong(__request_string[1]);
	            		Type __Type;
	            		if(__request_string[4].equals("0")){
	            			__Type = Type.Enter;
	            		}
	            		else {
							__Type = Type.Leave;
						}
	            		WriteRequest __w_request = new WriteRequest(__timestmp, __request_string[2], __request_string[3], __Type);
	            		queue.add(__w_request);
	            		break;
	            	case "1":
	            		queue.add(new ReadStatusRequest());
	            		break;
	            	case "2":
	            		ReadEmployeeRequest __employRequest = new ReadEmployeeRequest();
	            		__employRequest.name = __request_string[1];
	            		queue.add(__employRequest);
	            		break;
	            	case "3":
	            		ReadTimeRequest __timeRequest = new ReadTimeRequest();
	            		__timeRequest.name = __request_string[1];
	            		__timeRequest.roomId = __request_string[2];
	            		queue.add(__timeRequest);
	            		break;
	            	case "4":
	            		ReadIntervalRequest __intervalRequest = new ReadIntervalRequest();
	            		__intervalRequest.low = Long.parseLong(__request_string[1]);
	            		__intervalRequest.up = Long.parseLong(__request_string[2]);
	            		queue.add(__intervalRequest);
	            		break;
	            	default:
	            		break;
                };
            }
			inScanner.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	public String[] splitString(String str){
		String __str = str.trim();
        String[] __request_string = __str.split("[\\p{Space}]+");
//        int len = logInfo.length;
//        for(int i = 0;i < len-1;i++){
//        	System.out.print(logInfo[i]+" ");
//        }
//        System.out.println(logInfo[len-1]);
        return __request_string;
	}
}
