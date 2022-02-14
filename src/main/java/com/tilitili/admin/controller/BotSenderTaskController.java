package com.tilitili.admin.controller;

import com.google.common.collect.ImmutableMap;
import com.tilitili.common.entity.BotSenderTaskMapping;
import com.tilitili.common.entity.BotTask;
import com.tilitili.common.entity.dto.BotSenderDTO;
import com.tilitili.common.entity.query.BotSenderQuery;
import com.tilitili.common.entity.view.BaseModel;
import com.tilitili.common.entity.view.PageModel;
import com.tilitili.common.mapper.mysql.BotSenderTaskMappingMapper;
import com.tilitili.common.mapper.mysql.BotTaskMapper;
import com.tilitili.common.utils.Asserts;
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
	private final BotSenderTaskMappingMapper botSenderTaskMappingMapper;
	private final BotTaskMapper botTaskMapper;

	@Autowired
	public BotSenderTaskController(BotSenderTaskMappingMapper botSenderTaskMappingMapper, BotTaskMapper botTaskMapper) {
		this.botSenderTaskMappingMapper = botSenderTaskMappingMapper;
		this.botTaskMapper = botTaskMapper;
	}

	@RequestMapping("/list")
	@ResponseBody
	public BaseModel<PageModel<Map<String, Object>>> getBotSenderTaskByCondition(BotSenderQuery query) {
		int total = botSenderTaskMappingMapper.countSenderTaskTable(query);
		List<BotSenderDTO> senderWithTaskIdList = botSenderTaskMappingMapper.getSenderTaskTable(query);
		List<Map<String, Object>> result = senderWithTaskIdList.stream().map(s -> {
			ImmutableMap.Builder<String, Object> mapBuild = ImmutableMap.<String, Object>builder().put("name", s.getName()).put("id", s.getId()).put("sendType", s.getSendType());
			if (s.getTaskIdListStr() != null) {
				Arrays.asList(s.getTaskIdListStr().split(",")).forEach(taskId -> mapBuild.put(taskId, true));
			}
			return mapBuild.build();
		}).collect(Collectors.toList());
		return PageModel.of(total, query.getPageSize(), query.getPageNo(), result);
	}

	@RequestMapping("/update")
	@ResponseBody
	public BaseModel<?> updateBotSenderTask(Long id, Long taskId, Boolean checked) {
		BotSenderTaskMapping botSenderTaskMapping = botSenderTaskMappingMapper.getBotSenderTaskMappingBySenderIdAndTaskId(id, taskId);
		if (checked && botSenderTaskMapping == null) {
			botSenderTaskMappingMapper.addBotSenderTaskMappingSelective(new BotSenderTaskMapping().setTaskId(taskId).setSenderId(id));
		} else if (!checked && botSenderTaskMapping != null) {
			botSenderTaskMappingMapper.deleteBotSenderTaskMappingById(botSenderTaskMapping.getId());
		}
		return BaseModel.success();
	}

	@RequestMapping("/copyMapping")
	public BaseModel<?> copyMapping(Long fromTaskId, Long toTaskId) {
		Asserts.notNull(fromTaskId, "参数异常");
		Asserts.notNull(toTaskId, "参数异常");
		BotTask fromTask = botTaskMapper.getBotTaskById(fromTaskId);
		BotTask toTask = botTaskMapper.getBotTaskById(toTaskId);
		Asserts.notNull(fromTask, "参数异常");
		Asserts.notNull(toTask, "参数异常");

		List<BotSenderTaskMapping> fromMappingList = botSenderTaskMappingMapper.getBotSenderTaskMappingByTaskId(fromTaskId);
		List<BotSenderTaskMapping> toMappingList = botSenderTaskMappingMapper.getBotSenderTaskMappingByTaskId(fromTaskId);

		for (BotSenderTaskMapping toMapping : toMappingList) {
			botSenderTaskMappingMapper.deleteBotSenderTaskMappingById(toMapping.getId());
		}

		for (BotSenderTaskMapping fromMapping : fromMappingList) {
			botSenderTaskMappingMapper.deleteBotSenderTaskMappingById(fromMapping.getId());
		}

		return BaseModel.success();
	}
}
