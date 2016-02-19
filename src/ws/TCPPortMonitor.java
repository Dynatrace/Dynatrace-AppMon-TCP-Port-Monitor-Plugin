package ws;


import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.net.Socket;

import com.dynatrace.diagnostics.pdk.PluginEnvironment;
import com.dynatrace.diagnostics.pdk.Status;

public class TCPPortMonitor {

	private static final String CONFIG_PORT = "port";
	
	private boolean matchRuleSuccess;
	private String server = null;
	String portString = null;
	long responseTime;

	
	private static final Logger log = Logger.getLogger(TCPPortMonitor.class.getName());

	protected Status setup(PluginEnvironment env) throws Exception {
		Status result = new Status(Status.StatusCode.Success);
        String host = env.getHost().getAddress();
		portString = env.getConfigString(CONFIG_PORT);
		
			if (portString != null && host != null) {
				server = host;
			}
			
		return result;
	}
	
	public long getResponseTime() {
	return responseTime;
}

	protected Status execute(PluginEnvironment env) throws Exception {
		Status result = new Status(Status.StatusCode.Success);
		responseTime = System.currentTimeMillis();
		String output = executeCommand(result);
		responseTime = System.currentTimeMillis() - responseTime;
		if (log.isLoggable(Level.FINE)) log.fine("Command output was: " + output);
		
		matchRuleSuccess = false;
		if (output != null) {
			if(output.contains("is open on")) {
				result.setMessage("port is open");
				matchRuleSuccess = true;
			}
			
			else if(output.contains("is not open on")) {
				result = new Status(Status.StatusCode.PartialSuccess);
				result.setMessage("port is not open");
				matchRuleSuccess = false;
			}
			else {
			    result = new Status(Status.StatusCode.ErrorInternalUnauthorized);
				result.setMessage("Error occurred");
			}
		}

		return result;
	}
	
	protected boolean isMatchRuleSuccess() {
		return matchRuleSuccess;
	}

	protected void teardown(PluginEnvironment env) throws Exception {
	}

	private String executeCommand(Status status) {
				 
        int port = Integer.parseInt(portString);
        String message = null;

            try { 

                Socket socket = new Socket(server, port); 
                message = port + " is open on " + server; 
                socket.close(); 

            	} 
            
            catch (IOException e) { 

            	message = port + " is not open on " + server; 

            	}
		
		return message;

	}
	
}
