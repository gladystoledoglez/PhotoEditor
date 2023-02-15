#include <jni.h>
#include <android/bitmap.h>
#include <sys/types.h>
#include <ctime>

jobject applyFilter(JNIEnv *env, jfloatArray matrix, jfloat filter[20]);

extern "C"
JNIEXPORT jobject JNICALL
Java_com_gladystoledoglez_photoeditor_presenter_viewModels_FilterViewModel_getCyanFilter(JNIEnv *env,
                                                                                jobject thiz) {
    jfloatArray cyanoMatrix = env->NewFloatArray(20);
    jfloat cyanoFilter[20] = {
            0.222f, 0.222f, 0.222f, 0, 61.0f,
            0.222f, 0.222f, 0.222f, 0, 87.0f,
            0.222f, 0.222f, 0.222f, 0, 136.0f,
            0, 0, 0, 1, 0
    };
    return applyFilter(env, cyanoMatrix, cyanoFilter);
}

extern "C"
JNIEXPORT jobject JNICALL
Java_com_gladystoledoglez_photoeditor_presenter_viewModels_FilterViewModel_getGrainFilter(JNIEnv *env,
                                                                                 jobject thiz) {

    jfloatArray grainMatrix = env->NewFloatArray(20);
    jfloat grainFilter[20] = {
            0.9f, 0.1f, 0.1f, 0, 10.0f,
            0.1f, 0.9f, 0.1f, 0, 10.0f,
            0.1f, 0.1f, 0.9f, 0, 10.0f,
            0, 0, 0, 1, 0
    };

    return applyFilter(env, grainMatrix, grainFilter);
}

jobject applyFilter(JNIEnv *env, jfloatArray matrix, jfloat filter[20]) {

    char androidGraphicsColorMatrix[] = "android/graphics/ColorMatrix";
    char androidGraphicsColorMatrixColorFilter[] = "android/graphics/ColorMatrixColorFilter";
    char init[] = "<init>";
    char sigV[] = "()V";
    char set[] = "set";
    char sigFV[] = "([F)V";
    char sigColorMatrix[] = "(Landroid/graphics/ColorMatrix;)V";

    jsize start = 0;
    jsize len = 20;

    jclass colorMatrixClass = env->FindClass(androidGraphicsColorMatrix);
    jmethodID colorMatrixConstructor = env->GetMethodID(colorMatrixClass, init, sigV);
    jobject colorMatrix = env->NewObject(colorMatrixClass, colorMatrixConstructor);

    env->SetFloatArrayRegion(matrix, start, len, filter);

    jmethodID setValuesMethod = env->GetMethodID(colorMatrixClass, set, sigFV);
    env->CallVoidMethod(colorMatrix, setValuesMethod, matrix);

    jclass colorMatrixColorFilterClass = env->FindClass(androidGraphicsColorMatrixColorFilter);
    jmethodID colorMatrixColorFilterConstructor = env->GetMethodID(
            colorMatrixColorFilterClass, init, sigColorMatrix
    );
    jobject colorMatrixColorFilter = env->NewObject(
            colorMatrixColorFilterClass, colorMatrixColorFilterConstructor, colorMatrix
    );

    return colorMatrixColorFilter;
}