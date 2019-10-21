//
// Created by admin on 2017/10/17.
//
#include <jni.h>
#include <string>

#define TYPE_T_OPEN_ID 0
#define TYPE_W_OPEN_ID 1
#define TYPE_W_OPEN_SECRET 2
#define TYPE_MI_ID 3
#define TYPE_MI_KEY 4
#define TYPE_MZ_ID 5
#define TYPE_MZ_KEY 6
#define TYPE_TINKER_ID 7
#define TYPE_SIGN_KEY 8
static const char *T_OPEN_ID = "";
static const char *W_OPEN_ID = "";
static const char *W_OPEN_SECRET = "";
static const char *MI_ID = "";
static const char *MI_KEY = "";
static const char *MZ_ID = "";
static const char *MZ_KEY = "";
static const char *TINKER_ID = "";
static const char *SIGN_KEY = "c29mdGx1cXVAbzJv";
//static const char *SIGN_KEY = "YTEyMzQ1NjdsZWFuY2xvdWQ=";
extern "C"

JNIEXPORT jstring JNICALL
Java_com_taohdao_library_utils_SignatureUtil_getSignatureParam(
        JNIEnv *env,
        jobject instance,
        jint type) {
    std::string sign;
    switch (type){
        case TYPE_T_OPEN_ID:
            sign = T_OPEN_ID;
            break;
        case TYPE_W_OPEN_ID:
            sign = W_OPEN_ID;
            break;
        case TYPE_W_OPEN_SECRET:
            sign = W_OPEN_SECRET;
            break;
        case TYPE_MI_ID:
            sign = MI_ID;
            break;
        case TYPE_MI_KEY:
            sign = MI_KEY;
            break;
        case TYPE_MZ_ID:
            sign = MZ_ID;
            break;
        case TYPE_MZ_KEY:
            sign = MZ_KEY;
            break;
        case TYPE_TINKER_ID:
            sign = TINKER_ID;
            break;
        case TYPE_SIGN_KEY:
            sign = SIGN_KEY;
            break;
    }

    return env->NewStringUTF(sign.c_str());
}

