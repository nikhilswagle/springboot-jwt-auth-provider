package com.incomm.dds.crypto.client;


import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.Date;
import java.util.Properties;

public class HttpRestClient {
	
	// Get the connect timeout value from application config
	private int connTimeOut;
	
	// Get the connect timeout value from application config
	private int connTimeOutOnRetry;
	
	// Get the read timeout value from application config
	private int readTimeOut;

	// Get the connection attempt count value from application config
	private int maxRetryCount;	
	
	public int getConnTimeOut() {
		return connTimeOut;
	}

	public void setConnTimeOut(int connTimeOut) {
		this.connTimeOut = connTimeOut;
	}

	public int getConnTimeOutOnRetry() {
		return connTimeOutOnRetry;
	}

	public void setConnTimeOutOnRetry(int connTimeOutOnRetry) {
		this.connTimeOutOnRetry = connTimeOutOnRetry;
	}

	public int getReadTimeOut() {
		return readTimeOut;
	}

	public void setReadTimeOut(int readTimeOut) {
		this.readTimeOut = readTimeOut;
	}

	public int getMaxRetryCount() {
		return maxRetryCount;
	}

	public void setMaxRetryCount(int maxRetryCount) {
		this.maxRetryCount = maxRetryCount;
	}

	/**
	 * 
	 * @param strUrl
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	public InputStream sendHttpRequest(final String strUrl, final Properties properties, final String httpMethod, final Object request) throws Exception {
		// Response
		InputStream resp = null;
		
		// Initialize the start time for the DDP Post.
		Date startTrip = new Date();
		
		// Get connection instance
		HttpURLConnection con = getConnectionInstance(strUrl, properties, httpMethod);
		
		// Connection retry mechanism in case of connection timeouts
		boolean isConnSuccess = connect(con);
		
		if (isConnSuccess) { // If connection is successful
			resp = sendRequest(con, request);
			System.out.println("Total DDP Request/Response trip time is "+(new Date().getTime() - startTrip.getTime())+" ms");
		}
		else{
			System.out.println("Could not establish connection to "+strUrl);
		}
		
		return resp;
	}	
	
	private boolean connect(HttpURLConnection con){
		// Connection retry mechanism in case of connection timeouts
		int attemptCount = 0;
		boolean isConnSuccess = false;
		Date start = null;
		while (maxRetryCount > attemptCount) {
			try {
				// Initialize the start time
				start = new Date();
				
				// Connection attempt
				con.connect();
				System.out.println("Connection established successfully in "+(new Date().getTime() - start.getTime())+" ms");
				isConnSuccess = true;
				break;
			} catch (SocketTimeoutException stex) {
				// Connection timeout has occured.
				con.setConnectTimeout(connTimeOutOnRetry);
				attemptCount++;
				System.out.println("Connection attempt " + attemptCount + " failed due to Connection Timeout");
			} catch (ConnectException cex) {
				// Failed to get connection to the server. This is not a timeout scenario but is to be treated as DDPConnectionTimeoutException
				con.setConnectTimeout(connTimeOutOnRetry);
				attemptCount++;
				System.out.println("Connection attempt " + attemptCount + " failed due an unexpected exception");
			}
			catch (Exception ex) {
				// Read Timeout has occurred
				System.out.println("Unknown exception occured");
				ex.printStackTrace();
			}
		}
		return isConnSuccess;		
	}
	
	private HttpURLConnection getConnectionInstance(final String strUrl, final Properties properties, final String httpMethod) throws IOException{
		// Build the URL
		URL url = new URL(strUrl);

		// Open Connection
		HttpURLConnection con = (HttpURLConnection) url.openConnection();

		// Add request parameters
		for(Object key : properties.keySet()){
			con.setRequestProperty(key.toString(),properties.get(key).toString());
		}		
		con.setRequestMethod(httpMethod);
		con.setDoOutput(true);

		// Set timeout
		con.setConnectTimeout(connTimeOut);
		con.setReadTimeout(readTimeOut);
		
		return con;
	}
	
	private InputStream sendRequest(HttpURLConnection con, Object request) throws Exception{
		InputStream resp;
		try {
			// Initialize the start time
			Date startTrip = new Date();
			
			if(null != request){
				// Write the bytes to the connection
				con.getOutputStream().write(request.toString().getBytes("UTF-8"));
			}

			// Retrieve the webservice response from DDP			
			resp = con.getInputStream();
			System.out.println("Response received in "+(new Date().getTime() - startTrip.getTime())+" ms");			
		} catch (SocketTimeoutException stex) {
			// Read Timeout has occurred
			System.out.println("Read Timeout occurred");
			stex.printStackTrace();
			throw stex;
		}
		catch (Exception ex) {
			// Read Timeout has occurred
			System.out.println("Unknown exception occured");
			ex.printStackTrace();
			throw ex;
		}
		
		return resp;
	}
}
