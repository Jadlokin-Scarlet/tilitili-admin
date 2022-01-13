package com.tilitili.admin.controller;

import com.tilitili.common.mapper.mysql.BotTaskMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("api/botTask")
public class BotTaskController extends BaseController{
	private BotTaskMapper botTaskMapper;

	public BotTaskController(BotTaskMapper botTaskMapper) {
		this.botTaskMapper = botTaskMapper;
	}
}
