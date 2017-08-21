package com.quest.forge.rest.tunnel.client.controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
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
public class TunnelClientController extends AbstractController {
	
	private static final Logger logger = LoggerFactory.getLogger(TunnelClientController.class);
	
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
		if (tunnelClientInfo != null && 
				!StringUtils.isEmpty(tunnelClientInfo.getCustomCode()) && 
				!StringUtils.isEmpty(tunnelClientInfo.getConnectionToken()) && 
				!StringUtils.isEmpty(tunnelClientInfo.getFoglightUrl()) && 
				!StringUtils.isEmpty(tunnelClientInfo.getAuthToken()) && 
				!StringUtils.isEmpty(tunnelClientInfo.getAccessKey())) {
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
						return "qr";
					} else {
						logger.error(getMessage("client.request.loginFailed.debug", 
								tunnelClientInfo.getFoglightUrl(), 
								tunnelClientInfo.getAuthToken()));
						model.addAttribute("ResultMessage", getMessage("client.request.loginFailed"));
					}
				} catch (Exception e) {
					logger.error(getMessage("client.request.accessTokenFailed"));
					model.addAttribute("ResultMessage", getMessage("client.request.accessTokenFailed"));
				}
			} else {
				logger.error(getMessage("client.start.failed.debug", 
						tunnelClientInfo.getCustomCode(), 
						tunnelClientInfo.getConnectionToken(), 
						tunnelClientInfo.getFoglightUrl()));
				model.addAttribute("ResultMessage", getMessage("client.start.failed"));
			}
		} else {
			logger.error(getMessage("request.parameter.invalid"));
			model.addAttribute("ResultMessage", getMessage("request.parameter.invalid"));
		}
		return "main";
	}
	
	@RequestMapping(value="/qr", method = RequestMethod.GET)
    public ResponseEntity<byte[]> requestQRCode(@RequestParam("key") String accessKey, 
    		@RequestParam("token") String accessToken, 
    		Model model) {
    	HttpHeaders headers = new HttpHeaders();
    	headers.setContentType(MediaType.IMAGE_JPEG);
    	if (!StringUtils.isEmpty(accessKey) && !StringUtils.isEmpty(accessToken)) {
			try {
				String qrCodeContent = clientConfig.getMobileWebProtocol() + "://" + 
						clientConfig.getMobileWebUrl() + clientConfig.getMobileWebPath() + 
						"?key=" + URLEncoder.encode(accessKey, "UTF-8") + "&token=" + URLEncoder.encode(accessToken, "UTF-8");
				InputStream in = new FileInputStream(QRCode.from(qrCodeContent).to(ImageType.JPG).file());
				byte[] content = IOUtils.toByteArray(in);
				return new ResponseEntity<byte[]> (content, headers, HttpStatus.CREATED);
			} catch (FileNotFoundException e) {
				logger.error(getMessage("client.qr.failed.debug", e.getMessage()));
			} catch (IOException e) {
				logger.error(getMessage("client.qr.failed.debug", e.getMessage()));
			}
    	} else {
    		logger.error(getMessage("request.parameter.invalid"));
    	}
    	return new ResponseEntity<byte[]> (null, headers, HttpStatus.CREATED);
	}
}
