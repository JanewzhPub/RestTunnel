package com.quest.forge.rest.tunnel.client.controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.websocket.server.PathParam;

import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.quest.forge.rest.tunnel.client.bean.RequestLogin;

import net.glxn.qrgen.core.image.ImageType;
import net.glxn.qrgen.javase.QRCode;

@Controller
public class LoginRequestController {
	
	/*
	 * Debug: will be replaced
	 */
	/**
	 * drmfm2os22dvmopt6fssgh8fr2
	 * drmfm2os22dvmopt6fssgh8fr2
	 */
	private final static String CUSTOM_CODE = "Quest";
	private final static String ACCESS_KEY = "SjmdqbeeqkvLV/SnpzGa8v0e5Us=";
	private final static String AUTH_TOKEN = "0084fdd581ca92:4776f91570c2ca456003e7bc";
	private final static String FOGLIGHT_URL = "10.30.154.102:8080";
	
	private final static String MFOGLIGHT_URL = "http://mfoglight.azurewebsites.net/RestTunnelApp";

	@RequestMapping(value="/", method = RequestMethod.GET)
    public String home(Model model) {
		model.addAttribute("RequestLogin", new RequestLogin());
		return "main";
	}
	
	@RequestMapping(value="/requestLogin", method = RequestMethod.POST)
    public String requestLogin(@ModelAttribute("RequestLogin") RequestLogin requestLogin,
			Model model) {
		if (requestLogin != null) {
			//get requested info from request, then start the client
			model.addAttribute("RequestLogin", new RequestLogin(requestLogin.getAccessKey(), requestLogin.getAuthToken()));
			return "qrcode";
		} else {
			return "error";
		}
	}
	
	@RequestMapping(value="/requestQRCode", method = RequestMethod.GET)
    public ResponseEntity<byte[]> requestQRCode(@RequestParam("key") String accessKey, @RequestParam("token") String authToken) {
    	final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        InputStream in;
		try {
			String qrCodeContent = MFOGLIGHT_URL + "?key=" + accessKey + "&token=" + authToken;
			in = new FileInputStream(QRCode.from(qrCodeContent).to(ImageType.JPG).file());
			return new ResponseEntity<byte[]> (IOUtils.toByteArray(in), headers, HttpStatus.CREATED);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
        return new ResponseEntity<byte[]> (null, headers, HttpStatus.CREATED);
	}
}
