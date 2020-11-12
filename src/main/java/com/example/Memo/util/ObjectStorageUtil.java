package com.example.Memo.util;

import java.io.InputStream;

public interface ObjectStorageUtil {
    // store 관련 이미지(섬네일, 배너, 상세이미지)
    String STORE_IMAGE_BUCKET = "store/%d/";
    // store 관련 이미지(메뉴)
    String STORE_MENU_IMAGE_BUCEKT = "store/%d/menu/";
    // store 관련 이미지(리뷰)
    String STORE_REVIEW_IMAGE_BUCKET = "store/%d/review/";
    // member 관련 이미지
    String MEMBER_IMAGE = "member/%d/";

    String putObject(String path, String objectName, InputStream inputStream);

    Boolean removeObject(String objectPath);
}