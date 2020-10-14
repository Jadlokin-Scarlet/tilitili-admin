import VideoInfoManager from "../pages/VideoInfoManager";
import VideoDataManager from "../pages/VideoDataManager";

export const menuList = [
    { title: "视频信息管理", key: "/videoInfoManager", icon: "video-camera", component: VideoInfoManager},
    { title: "视频排行管理", key: "/videoDataManager", icon: "database", component: VideoDataManager},
]
