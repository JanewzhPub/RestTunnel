package com.quest.forge.rest.tunnel.client.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.quest.forge.rest.tunnel.client.bean.TunnelClientInfo;
import com.quest.forge.rest.tunnel.client.service.TunnelClientService;

@Controller
public class TunnelClientController {
	
	@Autowired
	private TunnelClientService tunnelClientService;
	
	@RequestMapping(value="/client", method = RequestMethod.GET)
    public String client(Model model) {
		model.addAttribute("ClientInfo", new TunnelClientInfo());
		return "startclient";
	}
	
	@RequestMapping(value="/startclient", method = RequestMethod.POST)
    public String startClient(@ModelAttribute("ClientInfo") TunnelClientInfo tunnelClientInfo,
			Model model) {
		if (tunnelClientInfo != null) {
			if (tunnelClientService.startClient(
					tunnelClientInfo.getCustomCode(), 
					tunnelClientInfo.getConnectionToken(), 
					tunnelClientInfo.getFoglightUrl())) {
				model.addAttribute("ResultMessage", "Start client successed!");
			} else {
				model.addAttribute("ResultMessage", "Start client failed!");
			}
			return "startclient";
		} else {
			return "error";
		}
	}
}
