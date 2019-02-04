package br.gov.ba.pge.validadoc.api.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.Scanner;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.gov.ba.pge.validadoc.api.util.DocumentumUtil;

@RestController
@RequestMapping("/validar")
public class ValidaDocumentoController {

	@Value("${documentum.authorization.basic}")
	private String authorization;
	
	// Obter a url com o código atual
	// Obter a url contents/contents
	// Obter a url content/media e passar para o método buscarDocumento

	@GetMapping("/{codigo}")
	public ResponseEntity<Byte[]> validarDocumentoPorCodigoValidacao(@PathVariable String codigo) {
		String codigoAtual = buscarIdDocumentoAtual(codigo);
		
		if (StringUtils.isEmpty(codigoAtual)) {
			return ResponseEntity.notFound().build();
		}
		
		System.out.println(codigoAtual);
		
		Byte[] documento = buscarDocumentoPeloCodigoAtual(codigoAtual);
		return documento != null ? ResponseEntity.ok(documento) : ResponseEntity.notFound().build();
	}

	private String buscarIdDocumentoAtual(String codigo) {
		HttpClient httpclient = HttpClients.createDefault();
		String url = "http://10.52.6.21:8080/dctm-rest/repositories/PGE_DEV1?dql=".concat(DocumentumUtil.criarDqlBuscarUltimoDocumentoPorId(codigo));
		HttpGet httpget = new HttpGet(url);
		
		try {
			httpget.setHeader("Authorization", authorization);
			HttpResponse response = httpclient.execute(httpget);
			
			if (response.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = response.getEntity();
				InputStream in = entity.getContent();
				
				System.out.println(in);
				
				Scanner scan = new Scanner(in);
				StringBuilder body = new StringBuilder();
				scan.forEachRemaining((s) -> {
					System.out.println(s);
					body.append(s);
				});
				scan.close();
				System.out.println(body.toString());
				return body.toString();
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			httpget.releaseConnection();
		}
		return null;
	}

	@GetMapping("/buscarDocumento")
	public byte[] buscarDocumento(String url) {
		try {
			url = "http://10.52.6.21:8080/dctm-rest/repositories/PGE_DEV1/objects/0901870e80191201/content-media?format=pdf&modifier=&page=0";
			url = "https://americalatina.dint.fgv.br/sites/americalatina.dint.fgv.br/files/teste33.pdf";
			HttpClient httpclient = HttpClients.createDefault();
			HttpGet httpget = new HttpGet(url);
			HttpResponse response = httpclient.execute(httpget);
			HttpEntity entity = response.getEntity();
			ByteArrayOutputStream baos = new ByteArrayOutputStream(); 
			entity.writeTo(baos);
			return baos.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@GetMapping("/buscarDocumento/base")
	public String obterArquivoBase64(String url) {
		try {
			url = "https://americalatina.dint.fgv.br/sites/americalatina.dint.fgv.br/files/teste33.pdf";
	    	HttpClient httpclient = HttpClients.createDefault();
	        HttpGet httpget = new HttpGet(url);
	        HttpResponse response = httpclient.execute(httpget);
	        HttpEntity entity = response.getEntity();
	        ByteArrayOutputStream baos = new ByteArrayOutputStream(); 
			entity.writeTo(baos);
			byte[] bytes = baos.toByteArray();
			return Base64.getEncoder().encodeToString(bytes);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private Byte[] buscarDocumentoPeloCodigoAtual(String url) {
		HttpClient httpclient = HttpClients.createDefault();
		HttpGet httpget = new HttpGet(url);
		
		try {
			httpget.setHeader("Authorization", authorization);
			HttpResponse response = httpclient.execute(httpget);
			HttpEntity entity = response.getEntity();

			InputStream in = entity.getContent();
			Scanner scan = new Scanner(in);
			StringBuilder body = new StringBuilder();
			scan.forEachRemaining((s) -> body.append(s));
			scan.close();
			
			return null;

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			httpget.releaseConnection();
		}
		
		return null;
	}
	
}
