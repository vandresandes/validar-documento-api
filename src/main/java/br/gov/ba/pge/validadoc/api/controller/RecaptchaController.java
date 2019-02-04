package br.gov.ba.pge.validadoc.api.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/recaptcha")
public class RecaptchaController {

private static final String GOOGLE_RECAPTCHA_VERIFY_URL = "https://www.google.com/recaptcha/api/siteverify";
	
	@Autowired
	private RestTemplateBuilder restTemplateBuilder;
	
	@Value("${google.recaptcha.secret}")
	private String recaptchaSecret;
	@Value("${google.recaptcha.secret.invisible}")
	private String recaptchaSecretInvisible;


	@SuppressWarnings({ "unchecked", "rawtypes" })
	@PostMapping({"/{recaptchaResponse}", "/{recaptchaResponse}/{visible}"})
	public Map<String, Object> verifyRecaptcha(@PathVariable String recaptchaResponse, @PathVariable Optional<Boolean> visible, HttpServletRequest request) {
		Map<String, String> body = new HashMap<>();
		body.put("secret", getRecaptchaSecret(visible));
		body.put("response", recaptchaResponse);
		body.put("remoteip", request.getRemoteAddr());
		
		ResponseEntity<Map> recaptchaResponseEntity = restTemplateBuilder.build().postForEntity(GOOGLE_RECAPTCHA_VERIFY_URL + "?secret={secret}&response={response}&remoteip={remoteip}", body, Map.class, body);
		Map<String, Object> responseBody = recaptchaResponseEntity.getBody();
		return responseBody;
	}

	private String getRecaptchaSecret(Optional<Boolean> visible) {
		return visible.isPresent() && !visible.get() ? this.recaptchaSecretInvisible : this.recaptchaSecret;
	}

}

