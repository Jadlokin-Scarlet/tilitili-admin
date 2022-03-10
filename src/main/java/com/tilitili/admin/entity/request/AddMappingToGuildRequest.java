package com.tilitili.admin.entity.request;

public class AddMappingToGuildRequest {
	private String nick;
	private String guildId;

	public String getNick() {
		return nick;
	}

	public AddMappingToGuildRequest setNick(String nick) {
		this.nick = nick;
		return this;
	}

	public String getGuildId() {
		return guildId;
	}

	public AddMappingToGuildRequest setGuildId(String guildId) {
		this.guildId = guildId;
		return this;
	}
}
