package com.tilitili.admin.controller;

import com.google.common.collect.ImmutableMap;
import com.tilitili.common.entity.dto.BotSenderDTO;
import com.tilitili.common.entity.query.BotSenderQuery;
import com.tilitili.common.entity.view.BaseModel;
import com.tilitili.common.entity.view.PageModel;
import com.tilitili.common.mapper.mysql.BotSenderMapper;
import com.tilitili.common.mapper.mysql.BotSenderTaskMappingMapper;
import com.tilitili.common.mapper.mysql.BotTaskMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/api/botSenderTask")
public class BotSenderTaskController extends BaseController {
	private final BotTaskMapper botTaskMapper;
	private final BotSenderMapper botSenderMapper;
	private final BotSenderTaskMappingMapper botSenderTaskMappingMapper;

	@Autowired
	public BotSenderTaskController(BotTaskMapper botTaskMapper, BotSenderMapper botSenderMapper, BotSenderTaskMappingMapper botSenderTaskMappingMapper) {
		this.botTaskMapper = botTaskMapper;
		this.botSenderMapper = botSenderMapper;
		this.botSenderTaskMappingMapper = botSenderTaskMappingMapper;
	}

	@RequestMapping("/list")
	@ResponseBody
	public BaseModel<PageModel<Map<String, Object>>> getBotSenderTaskByCondition(BotSenderQuery query) {
		int total = botSenderTaskMappingMapper.countSenderTaskTable(query);
		List<BotSenderDTO> senderWithTaskIdList = botSenderTaskMappingMapper.getSenderTaskTable(query);
		List<Map<String, Object>> result = senderWithTaskIdList.stream().map(s -> {
			ImmutableMap.Builder<String, Object> mapBuild = ImmutableMap.<String, Object>builder().put("name", s.getName()).put("id", s.getId());
			Arrays.stream(s.getTaskIdListStr().split(",")).forEach(taskId -> mapBuild.put(taskId, true));
			return mapBuild.build();
		}).collect(Collectors.toList());
		return PageModel.of(total, query.getPageSize(), query.getPageNo(), result);
	}
}
