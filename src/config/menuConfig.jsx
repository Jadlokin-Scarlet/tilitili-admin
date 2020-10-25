import VideoInfoManager from "../pages/VideoInfoManager";
import VideoDataManager from "../pages/VideoDataManager";
import SpiderVideoManager from "../pages/SpiderVideoManager";

export const menuList = [
    { title: "视频信息管理", key: "/videoInfoManager", icon: "video-camera", component: VideoInfoManager},
    { title: "视频排行管理", key: "/videoDataManager", icon: "database", component: VideoDataManager},
    { title: "自定义爬取", key: "/spiderVideoManager", icon: "bug", component: SpiderVideoManager},
]
