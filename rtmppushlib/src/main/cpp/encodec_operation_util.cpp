#include <jni.h>
#include <string>
#include <string.h>





/**
 * ===================================== NV21 =====================================
 */

extern "C"
JNIEXPORT void JNICALL
Java_com_onwardsmg_rtmppushlib_jniutil_EncodecOperationUtil_nv21ToYUV420SemiPlanarRotate0(
        JNIEnv *env,
        jclass type,
        jbyteArray input_,
        jbyteArray output_,
        jint width,
        jint height) {
    jbyte *input = env->GetByteArrayElements(input_, NULL);
    jbyte *output = env->GetByteArrayElements(output_, NULL);

    int nLenY = width * height;
    int nLenU = nLenY >> 2;

    memcpy(output, input, width * height);

    for (int i = 0; i < nLenU; i++) {
        int s = nLenY + 2 * i;
        output[s] = input[s + 1];
        output[s + 1] = input[s];
    }

    env->ReleaseByteArrayElements(input_, input, 0);
    env->ReleaseByteArrayElements(output_, output, 0);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_onwardsmg_rtmppushlib_jniutil_EncodecOperationUtil_nv21ToYUV420SemiPlanarRotate90(
        JNIEnv *env, jclass type, jbyteArray input_, jbyteArray output_, jint m_width,
        jint m_height) {
    jbyte *input = env->GetByteArrayElements(input_, NULL);
    jbyte *output = env->GetByteArrayElements(output_, NULL);

    int wh = m_width * m_height;

    //旋转Y
    int k = 0;

    for (int i = 0; i < m_width; i++) {
        for (int j = m_height - 1; j >= 0; j--) {
            output[k] = input[m_width * j + i];
            k++;
        }
    }

    //U/V
    for (int i = 0; i < m_width; i += 2) {
        int s = wh + i;
        int s1 = s + 1;
        for (int j = (m_height >> 1) - 1; j >= 0; j--) {
            int s3 = m_width * j;
            output[k] = input[s1 + s3];
            output[k + 1] = input[s + s3];
            k += 2;
        }
    }

    env->ReleaseByteArrayElements(input_, input, 0);
    env->ReleaseByteArrayElements(output_, output, 0);
}

/**
 * ===================================== YV12 =====================================
 */

extern "C"
JNIEXPORT void JNICALL
Java_com_onwardsmg_rtmppushlib_jniutil_EncodecOperationUtil_YV12toYUV420PlanarRotate0(JNIEnv *env,
                                                                                      jclass type,
                                                                                      jbyteArray input_,
                                                                                      jbyteArray output_,
                                                                                      jint width,
                                                                                      jint height) {
    jbyte *input = env->GetByteArrayElements(input_, NULL);
    jbyte *output = env->GetByteArrayElements(output_, NULL);

    /*
     * COLOR_FormatYUV420Planar is I420 which is like YV12, but with U and V
     * reversed. So we just have to reverse U and V.
     */
    int frameSize = width * height;
    int qFrameSize = frameSize / 4;

    memcpy(output, input, frameSize); // Y
    for (int i = frameSize; i < frameSize + qFrameSize; i++) {
        output[i + qFrameSize] = input[i];//V
        output[i] = input[i + qFrameSize];//U
    }

    env->ReleaseByteArrayElements(input_, input, 0);
    env->ReleaseByteArrayElements(output_, output, 0);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_onwardsmg_rtmppushlib_jniutil_EncodecOperationUtil_YV12toYUV420PlanarRotate90(JNIEnv *env,
                                                                                       jclass type,
                                                                                       jbyteArray input_,
                                                                                       jbyteArray output_,
                                                                                       jint width,
                                                                                       jint height) {
    jbyte *input = env->GetByteArrayElements(input_, NULL);
    jbyte *output = env->GetByteArrayElements(output_, NULL);

    int wh = width * height;
    //旋转Y
    int k = 0;
    for (int i = 0; i < width; i++) {
        for (int j = 0; j < height; j++) {
            output[k] = input[width * j + i];
            k++;
        }
    }

    for (int i = 0; i < width; i += 2) {
        for (int j = 0; j < height / 2; j++) {
            output[k] = input[wh + width * j + i];
            output[k + 1] = input[wh + width * j + i + 1];
            k += 2;
        }
    }

    env->ReleaseByteArrayElements(input_, input, 0);
    env->ReleaseByteArrayElements(output_, output, 0);
}

/**
 * ===================================== 合并两个视频 =====================================
 */

extern "C"
JNIEXPORT void JNICALL
Java_com_onwardsmg_rtmppushlib_jniutil_EncodecOperationUtil_mergeTwoYuv(JNIEnv *env, jclass type,
                                                                        jbyteArray input1_,
                                                                        jbyteArray input2_,
                                                                        jbyteArray output_,
                                                                        jint widthEach,
                                                                        jint heightEach) {
    jbyte *input1 = env->GetByteArrayElements(input1_, NULL);
    jbyte *input2 = env->GetByteArrayElements(input2_, NULL);
    jbyte *output = env->GetByteArrayElements(output_, NULL);

    jbyte *input1Temp = input1;
    jbyte *input2Temp = input2;
    jbyte *outputTemp = output;

    int height = heightEach * 3 / 2;

    for (int j = 0; j < height; j++) {
        memcpy(outputTemp, input1Temp, widthEach);
        input1Temp += widthEach;
        outputTemp += widthEach;
        memcpy(outputTemp, input2Temp, widthEach);
        outputTemp += widthEach;
        input2Temp += widthEach;
    }

    env->ReleaseByteArrayElements(input1_, input1, 0);
    env->ReleaseByteArrayElements(input2_, input2, 0);
    env->ReleaseByteArrayElements(output_, output, 0);
}