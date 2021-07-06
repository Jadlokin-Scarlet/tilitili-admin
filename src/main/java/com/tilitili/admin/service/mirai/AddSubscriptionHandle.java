package com.tilitili.admin.service.mirai;

import com.tilitili.common.emnus.TaskReason;
import com.tilitili.common.entity.Subscription;
import com.tilitili.common.entity.mirai.MiraiMessageView;
import com.tilitili.common.entity.mirai.Sender;
import com.tilitili.common.entity.view.SimpleTaskView;
import com.tilitili.common.manager.TaskManager;
import com.tilitili.common.mapper.SubscriptionMapper;
import com.tilitili.common.utils.Asserts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
public class AddSubscriptionHandle implements BaseMessageHandle {
    private final SubscriptionMapper subscriptionMapper;
    private final TaskManager taskManager;

    @Autowired
    public AddSubscriptionHandle(SubscriptionMapper subscriptionMapper, TaskManager taskManager) {
        this.subscriptionMapper = subscriptionMapper;
        this.taskManager = taskManager;
    }

    @Override
    public List<String> getKeyword() {
        return Arrays.asList("关注", "gz");
    }

    @Override
    public String handleMessage(MiraiMessageView message, Map<String, String> map) {
        String uid = map.get("uid");
        Sender sender = message.getSender();
        Long qq = sender.getId();
        Long group = Optional.ofNullable(sender.getGroup()).map(Sender::getId).orElse(null);

        Asserts.notBlank(uid, "格式错啦(uid)");

        int oldCount = subscriptionMapper.countSubscriptionByCondition(new Subscription().setType(1).setValue(uid).setSendQq(qq));
        Asserts.isTrue(oldCount == 0, "已经关注了哦。");

        Subscription add = new Subscription().setValue(uid).setType(1).setSendGroup(group).setSendQq(qq).setSendType("friend");
        subscriptionMapper.insertSubscription(add);

        SimpleTaskView simpleTaskView = new SimpleTaskView().setValue(uid).setReason(TaskReason.SUPPLEMENT_VIDEO_OWNER.value);
        taskManager.simpleSpiderVideo(simpleTaskView);
        return "关注成功！";
    }
}
