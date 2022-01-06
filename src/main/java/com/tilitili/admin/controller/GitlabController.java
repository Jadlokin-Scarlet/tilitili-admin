package com.tilitili.admin.controller;

import com.tilitili.common.entity.view.gitlab.GitlabContainer;
import com.tilitili.common.manager.GitlabManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/api/gitlab")
public class GitlabController {
	private final GitlabManager gitlabManager;

	@Autowired
	public GitlabController(GitlabManager gitlabManager) {
		this.gitlabManager = gitlabManager;
	}

	@RequestMapping("/pub/deleteContainer")
	public void deleteContainer(String projectName) {
		Long projectId = gitlabManager.getProjectIdByProjectName(projectName);
		List<GitlabContainer> containerList = gitlabManager.getContainerList(projectId);
		for (GitlabContainer gitlabContainer : containerList) {
			gitlabManager.deleteContainer(gitlabContainer);
		}
	}
}
