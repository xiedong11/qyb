package com.zhuandian.qxe.chat;

import java.util.List;

import cn.leancloud.chatkit.LCChatProfilesCallBack;

/**
 * 用户体系的接口，开发者需要实现此接口来接入 LCChatKit
 */
public interface LCChatProfileProvider {
  // 根据传入的 clientId list，查找、返回用户的 Profile 信息(id、昵称、头像)
  public void fetchProfiles(List<String> userIdList, LCChatProfilesCallBack profilesCallBack);
}