import VideoInfoManager from "../pages/rank/VideoInfoManager";
import VideoDataManager from "../pages/rank/VideoDataManager";
import SpiderVideoManager from "../pages/rank/SpiderVideoManager";
import BatchSpiderVideoManager from "../pages/rank/BatchSpiderVideoManager";
import VideoDataFileCheck from "../pages/rank/VideoDataFileCheck";
import TagManager from "../pages/tilitili/TagManager";
import VideoTagManager from "../pages/tilitili/VideoTagManager";
import VideoResourceManager from "../pages/rank/VideoResourceManager";
import OwnerManager from "../pages/tilitili/OwnerManager";
import VideoInfoCount from "../pages/data/VideoInfoCount";
import TopTagCount from "../pages/data/TopTagCount";
import VideoDataCount from "../pages/data/VideoDataCount";
import RecommendPoolManager from "../pages/recommend/RecommendPoolManager";
import UseRecommendManager from "../pages/recommend/UseRecommendManager";
import RecommendVideoManager from "../pages/recommend/RecommendVIdeoManager";
import RecommendSelfManager from "../pages/recommend/RecommendSelfManager";
import RecommendSelfPoolManager from "../pages/recommend/RecommendSelfPoolManager";
import RecommendTalkManager from "../pages/recommend/RecommendTalkManager";

export const menuList = [
    {key: "/rank", title: "排行榜后台", children: [
        { title: "视频信息管理", key: "/rank/videoInfoManager", icon: "video-camera", component: VideoInfoManager},
        { title: "视频排行管理", key: "/rank/videoDataManager", icon: "database", component: VideoDataManager},
        { title: "视频排行文件检查", key: "/rank/videoDataFileCheck", icon: "database", component: VideoDataFileCheck},
        { title: "自定义爬取", key: "/rank/spiderVideoManager", icon: "bug", component: SpiderVideoManager},
        { title: "批量爬取", key: "/rank/batchSpiderVideoManager", icon: "bug", component: BatchSpiderVideoManager},
        { title: "视频资源下发管理", key: "/rank/videoResourceManager", icon: "arrow-down", component: VideoResourceManager},
    ]},
    {key: "/recommend", title: "推荐刊后台", children: [
        { title: "推荐池", key: "/recommend/RecommendPoolManager", icon: "funnel-plot", component: RecommendPoolManager},
        { title: "推荐归档", key: "/recommend/UseRecommendManager", icon: "folder-open", component: UseRecommendManager},
        { title: "自荐池", key: "/recommend/RecommendSelfPoolManager", icon: "funnel-plot", component: RecommendSelfPoolManager},
        { title: "自荐配置", key: "/recommend/RecommendSelfManager", icon: "smile", component: RecommendSelfManager},
        { title: "推荐刊配置", key: "/recommend/RecommendVideoManager", icon: "play-square", component: RecommendVideoManager},
        { title: "推荐刊对话配置", key: "/recommend/RecommendTalkManager", icon: "message", component: RecommendTalkManager},
    ]},
    {key: "/tilitili", title: "视频站后台", children: [
        { title: "作者管理", key: "/tilitili/ownerManager", icon: "user", component: OwnerManager},
        { title: "标签管理", key: "/tilitili/tagManager", icon: "tag", component: TagManager},
        { title: "视频标签管理", key: "/tilitili/videoTagManager", icon: "tag", component: VideoTagManager},
    ]},
    {key: "/data", title: "数据后台", children: [
        {title: "视频信息数据", key: "/video/info/count", icon: "bar-chart", component: VideoInfoCount},
        {title: "视频Tag数据", key: "/tag/count", icon: "bar-chart", component: TopTagCount},
        {title: "视频数据统计", key: "/video/data/count", icon: "bar-chart", component: VideoDataCount},
    ]}
]
