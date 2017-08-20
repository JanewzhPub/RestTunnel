package com.quest.forge.rest.tunnel.client.controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.quest.forge.rest.tunnel.client.bean.RequestLogin;
import com.quest.forge.rest.tunnel.client.service.LoginRequestService;

import net.glxn.qrgen.core.image.ImageType;
import net.glxn.qrgen.javase.QRCode;

@Controller
public class LoginRequestController {

	private final static String MFOGLIGHT_URL = "http://mfoglight.azurewebsites.net/RestTunnelApp";

	@Autowired
	private LoginRequestService loginRequestService;
	
	@RequestMapping(value="/", method = RequestMethod.GET)
    public String home(Model model) {
		model.addAttribute("RequestLogin", new RequestLogin());
		return "main";
	}
	
	@RequestMapping(value="/requestLogin", method = RequestMethod.POST)
    public String requestLogin(@ModelAttribute("RequestLogin") RequestLogin requestLogin,
			Model model) {
		if (requestLogin != null) {
			String accessToken = loginRequestService.requestAccessToken(
					requestLogin.getCustomCode(), requestLogin.getAuthToken());
			if (accessToken != null) {
				model.addAttribute("RequestLogin", 
						new RequestLogin(
								requestLogin.getCustomCode(), 
								requestLogin.getAccessKey(), 
								requestLogin.getAuthToken()));
				return "qrcode";
			} else {
				model.addAttribute("ResultMessage", "Request Login Failed: get access token failed!");
				return "main";
			}
		} else {
			model.addAttribute("ResultMessage", "Request Login Failed: invalid request parameter found!");
			return "main";
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
