package com.tilitili.admin.controller;

import com.tilitili.common.emnus.GuildEmum;
import com.tilitili.common.entity.TwitterChannelNameMapping;
import com.tilitili.common.entity.query.TwitterChannelNameMappingQuery;
import com.tilitili.common.entity.view.BaseModel;
import com.tilitili.common.entity.view.PageModel;
import com.tilitili.common.mapper.mysql.TwitterChannelNameMappingMapper;
import com.tilitili.common.utils.Asserts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/api/pub/chsChannel")
public class ChsChannelController extends BaseController {
    private static final String CAI_HONG_GUILD_ID = GuildEmum.Cai_Hong_Guild.guildId;
    private final TwitterChannelNameMappingMapper twitterChannelNameMappingMapper;

    @Autowired
    public ChsChannelController(TwitterChannelNameMappingMapper twitterChannelNameMappingMapper) {
        this.twitterChannelNameMappingMapper = twitterChannelNameMappingMapper;
    }

    @RequestMapping("/list")
    @ResponseBody
    public BaseModel<PageModel<TwitterChannelNameMapping>> listChsChannel(TwitterChannelNameMappingQuery query) {
        query.setGuildId(CAI_HONG_GUILD_ID).setStatus(0);
        int total = twitterChannelNameMappingMapper.countTwitterChannelNameMappingByCondition(query);
        List<TwitterChannelNameMapping> chsChannelList = twitterChannelNameMappingMapper.getTwitterChannelNameMappingByCondition(query);
        return PageModel.of(total, query.getPageSize(), query.getCurrent(), chsChannelList);
    }

    @RequestMapping("/add")
    @ResponseBody
    public BaseModel<?> addChsChannel(TwitterChannelNameMapping chsChannel) {
        Asserts.notNull(chsChannel, "参数异常");
        Asserts.notNull(chsChannel.getTwitterName(), "参数异常");
        Asserts.notNull(chsChannel.getChannelName(), "参数异常");
        Asserts.notNull(chsChannel.getGuildId(), "参数异常");
        Asserts.notNull(chsChannel.getChannelId(), "参数异常");

        List<TwitterChannelNameMapping> oldList = twitterChannelNameMappingMapper.getTwitterChannelNameMappingByCondition(new TwitterChannelNameMappingQuery().setTwitterName(chsChannel.getTwitterName()).setChannelName(chsChannel.getChannelName()).setStatus(0));
        Asserts.isTrue(oldList.isEmpty(), "名称重复");

        TwitterChannelNameMapping addChsChannel = new TwitterChannelNameMapping();
        addChsChannel.setTwitterName(chsChannel.getTwitterName());
        addChsChannel.setChannelName(chsChannel.getChannelName());
        addChsChannel.setGuildId(chsChannel.getGuildId());
        addChsChannel.setChannelId(chsChannel.getChannelId());
        twitterChannelNameMappingMapper.addTwitterChannelNameMappingSelective(addChsChannel);

        return BaseModel.success();
    }

    @RequestMapping("/edit")
    @ResponseBody
    public BaseModel<?> editChsChannel(TwitterChannelNameMapping chsChannel) {
        Asserts.notNull(chsChannel, "参数异常");
        Asserts.notNull(chsChannel.getId(), "参数异常");
        Asserts.notNull(chsChannel.getTwitterName(), "参数异常");
        Asserts.notNull(chsChannel.getChannelName(), "参数异常");
        Asserts.notNull(chsChannel.getGuildId(), "参数异常");
        Asserts.notNull(chsChannel.getChannelId(), "参数异常");

        TwitterChannelNameMapping old = twitterChannelNameMappingMapper.getTwitterChannelNameMappingById(chsChannel.getId());
        Asserts.notNull(old, "找不到ChsChannel");

        TwitterChannelNameMapping upd = new TwitterChannelNameMapping();
        upd.setId(old.getId());
        upd.setTwitterName(chsChannel.getTwitterName());
        upd.setChannelName(chsChannel.getChannelName());
        upd.setGuildId(chsChannel.getGuildId());
        upd.setChannelId(chsChannel.getChannelId());
        twitterChannelNameMappingMapper.updateTwitterChannelNameMappingSelective(upd);

        return BaseModel.success();
    }


    @RequestMapping("/delete")
    @ResponseBody
    public BaseModel<?> deleteChsChannel(TwitterChannelNameMapping chsChannel) {
        Asserts.notNull(chsChannel, "参数异常");
        Asserts.notNull(chsChannel.getId(), "参数异常");
        twitterChannelNameMappingMapper.updateTwitterChannelNameMappingSelective(new TwitterChannelNameMapping().setId(chsChannel.getId()).setStatus(-1));
        return BaseModel.success();
    }


}
