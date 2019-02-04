package br.gov.ba.pge.validadoc.api.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;

public class HttpDownloadUtil {
	
	private static final String HOME_DIRECTORY = System.getProperty("user.home");
    private static final String PASTA = "downloads";

	// método que realiza download de arquivo via método GET 
    public static void downloadByGet(HttpClient httpclient, String url) {
        HttpGet httpget = new HttpGet(url);
        try {
            HttpResponse response = httpclient.execute(httpget);
            HttpEntity entity = response.getEntity();
                                 
            if (entity != null) {
                /* salvar arquivo no disco */
                java.io.FileOutputStream fos = new java.io.FileOutputStream(getPathName("teste"));
                entity.writeTo(fos);
                fos.close();
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void download(HttpClient httpclient, String url, String authorization) {
        try {
            CloseableHttpClient client = HttpClientBuilder.create().build();
            HttpGet request = new HttpGet(url);
            request.setHeader("Authorization", authorization);
 
            HttpResponse response = client.execute(request);
            HttpEntity entity = response.getEntity();
            
 
            int responseCode = response.getStatusLine().getStatusCode();
 
            System.out.println("Request Url: " + request.getURI());
            System.out.println("Response Code: " + responseCode);  
 
            InputStream is = entity.getContent();
 
            String filePath = getPathName("documento.pdf");
            FileOutputStream fos = new FileOutputStream(new File(filePath));
 
            int inByte;
            while ((inByte = is.read()) != -1) {
                fos.write(inByte);
            }
            
            response.getEntity().writeTo(fos);
 
            is.close();
            fos.close();
 
            client.close();
            System.out.println("File Download Completed!!!");
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (UnsupportedOperationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static byte[] executeBinary(String url) throws IOException, ClientProtocolException {
    	HttpClient httpclient = HttpClients.createDefault();
        HttpGet httpget = new HttpGet(url);
        HttpResponse response = httpclient.execute(httpget);
        HttpEntity entity = response.getEntity();
        ByteArrayOutputStream baos = new ByteArrayOutputStream(); 
        entity.writeTo(baos);
        return baos.toByteArray();
    }
    
    private static String getPathName(String nomeAnexo) {
        return HOME_DIRECTORY.concat(File.separator).concat(PASTA).concat(File.separator).concat(nomeAnexo);
    }
}
