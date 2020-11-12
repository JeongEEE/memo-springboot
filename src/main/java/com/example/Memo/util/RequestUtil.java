package com.example.Memo.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RequestUtil {

    public static final Integer DEFAULT_PAGE_SIZE = 6;
    public static final Integer MAX_PAGE_SIZE = 50;
    public static final String REQUEST_SUCCESS_MSG = "request success";
    public static final Integer REQUEST_SUCCESS_CODE = 0;
    public static final String MEMBER_AUTHENTICATE_ERROR_MSG = "사용자 권한이 올바르지 않습니다";
    public static final Integer MEMBER_AUTHENTICATE_ERROR_CODE = 1000;
    public static final String MEMBER_EMAIL_EXIST_MSG = "사용자 이메일이 이미 존재합니다";
    public static final Integer MEMBER_EMAIL_EXIST_CODE = 1001;
    public static final String MERCHANT_CODE_INEXACT_MSG = "판매자 코드가 일치하지 않습니다";
    public static final Integer MERCHANT_CODE_INEXACT_CODE = 1002;
    public static final String MEMBER_LOGIN_INFO_INVALID_MSG = "로그인 계정 정보가 일치하지 않습니다";
    public static final Integer MEMBER_LOGIN_INFO_INVALID_CODE = 1003;
    public static final String MEMBER_ACCOUNT_DISABLE_MSG = "비활성화된 계정입니다";
    public static final Integer MEMBER_ACCOUNT_DISABLE_CODE = 1004;
    public static final String MEMBER_PASSWORD_EXPIRED_MSG = "비밀번호가 만료되었습니다";
    public static final Integer MEMBER_PASSWORD_EXPIRED_CODE = 1005;
    public static final String MEMBER_EMAIL_UNAVAILABLE_MSG = "유효히지 않은 이메일입니다";
    public static final Integer MEMBER_EMAIL_UNAVAILABLE_CODE = 1006;
    public static final String MEMBER_ACCOUNT_DELETED_MSG = "유효히지 않은 이메일입니다";
    public static final Integer MEMBER_ACCOUNT_DELETED_CODE = 1006;
    public static final String MEMBER_FCM_TOKEN_UNEXIST_MSG = "fcm token 이 존재하지 않습니다";
    public static final Integer MEMBER_FCM_TOKEN_UNEXIST_CODE = 1007;
    public static final String SERVER_ERROR_MSG = "서버에서 알수없는 오류가 발생했습니다";
    public static final Integer SERVER_ERROR_CODE = 2000;
    public static final String IMAGE_SAVE_ERROR_MSG = "이미지 IO 에러";
    public static final Integer IMAGE_SAVE_ERROR_CODE = 2001;
    public static final String IMAGE_OVER_MAX_SIZE_ERROR_MSG = "이미지 저장 최대 크기(10MB) 초과 에러";
    public static final Integer IMAGE_OVER_MAX_SIZE_ERROR_CODE = 2002;
    public static final String SERVER_URL_NOT_SUPPORT_MSG = "서버에서 지원하지 않는 url 입니다";
    public static final Integer SERVER_URL_NOT_SUPPORT_CODE = 2003;

    public static final String REQUEST_PARAMETER_FORM_ERROR_MSG = "요청 파라미터 형식이 올바르지 않습니다";
    public static final Integer REQUEST_PARAMETER_FORM_ERROR_CODE = 3000;
    public static final String REQUEST_PARAMETER_ILLEGAL_ERROR_MSG = "요청 파라미터가 거부되었습니다";
    public static final Integer REQUEST_PARAMETER_ILLEGAL_ERROR_CODE = 3001;
    public static final String REQUEST_OBJECT_NONEXISTENT_ERROR_MSG = "해당 데이터가 존재하지 않습니다";
    public static final Integer REQUEST_OBJECT_NONEXISTENT_ERROR_CODE = 3002;
    public static final String REQUEST_OBJECT_INACCESSIBLE_ERROR_MSG = "해당 데이터의 접근권한이 없습니다";
    public static final Integer REQUEST_OBJECT_INACCESSIBLE_ERROR_CODE = 3003;

    public static final String NETWORK_OAUTH2_ERROR_MSG = "인증 서버와의 연결에서 오류가 발생하였습니다";
    public static final Integer NETWORK_OAUTH2_ERROR_CODE = 4001;

    public static final String PAYMENT_ERROR_MSG = "주문 정보가 존재하지 않습니다";
    public static final Integer PAYMENT_ERROR_CODE = 5000;
    public static final String PAYMENT_AMOUNT_ERROR_MSG = "결제 결과가 없습니다";
    public static final Integer PAYMENT_AMOUNT_ERROR_CODE = 5001;
    public static final String PAYMENT_UID_ERROR_MSG = "uid가 일치하지 않습니다";
    public static final Integer PAYMENT_UID_ERROR_CODE = 5002;
    public static final String PAYMENT_AUTHENTICATE_ERROR_MSG = "아임포트 인증 오류가 발생하였습니다";


    public static final String DELIVERY_ALREADY_SHIPPING_MSG = "이미 배송중인 주문입니다";
    public static final Integer DELIVERY_ALREADY_SHIPPING_CODE = 6000;

    public static String FILE_SERVER_URL;

//    @Value("${object.storage.url}")
//    public void setProjectUrl(String projectUrl) {
//        FILE_SERVER_URL = projectUrl+"/beydrologis";
//    }

    /**
     * 파일 저장후 파일 확장자 반환
     *
     * @return : 파일 확장자 (jpeg, png, img 등)
     * @Param filepath : 파일 확장자를 제외한 저장 path
     * public static String uploadImage(MultipartFile multipartFile, String filepath) throws IOException {
     * try (InputStream inputStream = new BufferedInputStream(multipartFile.getInputStream())) {
     * String mimeType = URLConnection.guessContentTypeFromStream(inputStream);
     * if (!mimeType.startsWith(MIME_TYPE_IMAGE_INDEX)) {
     * log.error(multipartFile.getOriginalFilename() + " 파일은 Image 형식이 아닙니다, mimeType:" + mimeType);
     * throw new IllegalArgumentException("해당 파일은 Image 형식이 아닙니다, mimeType:" + mimeType);
     * }
     * String extension = mimeType.substring(MIME_TYPE_IMAGE_INDEX.length());
     * File file = new File(filepath + "." + extension);
     * file.getParentFile().mkdirs(); // 부모 디렉토리 생성
     * FileOutputStream fileOutputStream = new FileOutputStream(file);
     * FileCopyUtils.copy(inputStream, fileOutputStream);
     * fileOutputStream.close();
     * return extension;
     * } catch (Exception e) {
     * log.error("uploadImage failed. filepath:" + filepath);
     * throw e;
     * }
     * }
     * <p>
     * public static boolean deleteImage(String filepath) {
     * File file = new File(filepath);
     * boolean delYn = file.delete();
     * if (!delYn) {
     * log.error("deleteImage failed. filepath:" + filepath);
     * }
     * return delYn;
     * }
     */

    public static String modifyImageUrl(String dbPath) {
        if (dbPath == null)
            return null;
        return FILE_SERVER_URL + "/" + dbPath;
    }
}
