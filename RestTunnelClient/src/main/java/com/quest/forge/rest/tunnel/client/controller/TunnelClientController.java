package com.quest.forge.rest.tunnel.client.controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;

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

import com.quest.forge.rest.tunnel.client.bean.TunnelClientInfo;
import com.quest.forge.rest.tunnel.client.config.RestTunnelClientConfig;
import com.quest.forge.rest.tunnel.client.service.LoginRequestService;
import com.quest.forge.rest.tunnel.client.service.TunnelClientService;

import net.glxn.qrgen.core.image.ImageType;
import net.glxn.qrgen.javase.QRCode;

@Controller
public class TunnelClientController {
	
	@Autowired
	private RestTunnelClientConfig clientConfig;
	@Autowired
	private LoginRequestService loginRequestService;
	@Autowired
	private TunnelClientService tunnelClientService;
	
	@RequestMapping(value="/", method = RequestMethod.GET)
    public String home(Model model) {
		model.addAttribute("ClientInfo", new TunnelClientInfo());
		return "main";
	}
	
	@RequestMapping(value="/startclient", method = RequestMethod.POST)
    public String startClient(@ModelAttribute("ClientInfo") TunnelClientInfo tunnelClientInfo,
			Model model) {
		if (tunnelClientInfo != null) {
			if (tunnelClientService.startClient(
					tunnelClientInfo.getCustomCode(), 
					tunnelClientInfo.getConnectionToken(), 
					tunnelClientInfo.getFoglightUrl())) {
				try {
					String accessToken = loginRequestService.requestAccessToken(
							tunnelClientInfo.getFoglightUrl(), 
							tunnelClientInfo.getAuthToken());
					if (accessToken != null) {
						model.addAttribute("AccessKey", tunnelClientInfo.getAccessKey());
						model.addAttribute("AccessToken", accessToken);
						return "qrcode";
					}
				} catch (Exception e) {
					//TODO handle exception
					e.printStackTrace();
				}
				model.addAttribute("ResultMessage", "Login to foglight failed!");
			} else {
				model.addAttribute("ResultMessage", "Start client failed!");
			}
		} else {
			model.addAttribute("ResultMessage", "Invalid request parameters!");
		}
		return "main";
	}
	
	@RequestMapping(value="/requestQRCode", method = RequestMethod.GET)
    public ResponseEntity<byte[]> requestQRCode(@RequestParam("key") String accessKey, 
    		@RequestParam("token") String accessToken) {
    	final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        InputStream in;
		try {
			String qrCodeContent = clientConfig.getMobileWebProtocol() + "://" + 
					clientConfig.getMobileWebUrl() + clientConfig.getMobileWebPath() + 
					"?key=" + URLEncoder.encode(accessKey, "UTF-8") + "&token=" + URLEncoder.encode(accessToken, "UTF-8");
			in = new FileInputStream(QRCode.from(qrCodeContent).to(ImageType.JPG).file());
			return new ResponseEntity<byte[]> (IOUtils.toByteArray(in), headers, HttpStatus.CREATED);
		} catch (FileNotFoundException e) {
			//TODO handle exception
			e.printStackTrace();
		} catch (IOException e) {
			//TODO handle exception
			e.printStackTrace();
		}
        return new ResponseEntity<byte[]> (null, headers, HttpStatus.CREATED);
	}
}
