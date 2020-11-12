package com.example.Memo.model.member;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
  GUEST("ROLE_GUEST", "손님"),
  USER("ROLE_USER", "일반 사용자"),
  ADMIN("ROLE_ADMIN", "관리자");
  private final String key;
  private final String title;

  public static Role getValue(String key) {
    for (Role value : values()) {
      if (value.getKey().equals(key))
        return value;
    }
    return null;
  }
}
