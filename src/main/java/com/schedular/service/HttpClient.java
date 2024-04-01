package com.schedular.service;

import java.io.File;
import java.net.URL;

import javax.net.ssl.SSLContext;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.schedular.util.StackTrace;

public class HttpClient {
	Logger LOGGER = LogManager.getLogger(this.getClass());
	JSONObject finalJson = null;
	HttpUriRequest request = null;
	SSLConnectionSocketFactory sslConSocFactory=null;
	public JSONObject httpResponse(String ivrRequestID,JSONObject requestBody, JSONObject jsonArrayParse ) {
	    
         LOGGER.info(jsonArrayParse.get("apiurl").toString()+jsonArrayParse.get("methodName").toString()+"URL for Testing");
		try {

			RequestConfig config = RequestConfig.custom().setConnectTimeout(Integer.parseInt(jsonArrayParse.get("connectionTimeOut").toString()) * 1000)
					.setSocketTimeout(Integer.parseInt(jsonArrayParse.get("fetchTimeOut").toString()) * 1000).setConnectionRequestTimeout(Integer.parseInt(jsonArrayParse.get("readTimeOut").toString()) * 1000).build();

			HttpClientBuilder clientbuilder = HttpClients.custom();
			if ("https".equals(new URL(jsonArrayParse.get("apiurl").toString()+jsonArrayParse.get("methodName").toString()).getProtocol())) {
				SSLContextBuilder SSLBuilder = SSLContexts.custom();
				File file = new File(new File(jsonArrayParse.get("jksFilePath").toString()).getCanonicalPath());
				SSLBuilder = SSLBuilder.loadTrustMaterial(file, jsonArrayParse.get("jksPassword").toString().toCharArray());
				SSLContext sslcontext = SSLBuilder.build();
				sslConSocFactory = new SSLConnectionSocketFactory(sslcontext,new NoopHostnameVerifier());
				clientbuilder = clientbuilder.setSSLSocketFactory(sslConSocFactory);
			} 
			if (!"NA".equalsIgnoreCase(jsonArrayParse.get("apiUserName").toString()) && !"NA".equalsIgnoreCase(jsonArrayParse.get("apiPassword").toString())) {
				CredentialsProvider credentialsPovider = new BasicCredentialsProvider();
				Credentials credentials = new UsernamePasswordCredentials(jsonArrayParse.get("apiUserName").toString(), jsonArrayParse.get("apiPassword").toString());
				credentialsPovider.setCredentials(AuthScope.ANY, credentials);
				clientbuilder.setDefaultCredentialsProvider(credentialsPovider);
			} 
			if ("Get".equalsIgnoreCase( jsonArrayParse.get("methodType").toString())) {
				request = new HttpGet(jsonArrayParse.get("apiurl").toString()+jsonArrayParse.get("methodName").toString());
				request.setHeader("Content-Type", "application/json; charset=utf-8");
				request.setHeader("apirequestId", ivrRequestID);
			} else if ("Post".equalsIgnoreCase(jsonArrayParse.get("methodType").toString())) {
				request = new HttpPost(jsonArrayParse.get("apiurl").toString()+jsonArrayParse.get("methodName").toString());
				request.setHeader("Content-Type", "application/json; charset=utf-8");
				request.setHeader("apirequestId", ivrRequestID);
				((HttpPost) request).setEntity(new StringEntity(requestBody.toString(), "UTF-8"));
			} else {
				request = new HttpPut(jsonArrayParse.get("apiurl").toString()+jsonArrayParse.get("methodName").toString());
				request.setHeader("Content-Type", "application/json; charset=utf-8");
				request.setHeader("apirequestId", ivrRequestID);
				((HttpPut) request).setEntity(new StringEntity(requestBody.toString(), "UTF-8"));
			}

			CloseableHttpClient httpClient = clientbuilder.setDefaultRequestConfig(config).build();
					CloseableHttpResponse response = httpClient.execute(request);

				finalJson = (org.json.simple.JSONObject) new JSONParser()
						.parse(EntityUtils.toString(response.getEntity()));

			} catch (IllegalArgumentException e) {
				LOGGER.error("IllegalArgumentException Occured: "+StackTrace.getMessage(e));
			} catch (Exception e) {
			LOGGER.error("Exception while using Httpclient Method: "+StackTrace.getMessage(e));
		   }

		return finalJson;

	}
}
