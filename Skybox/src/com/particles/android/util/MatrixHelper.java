package com.particles.android.util;

public class MatrixHelper {

	/**Android's Matrix class contains two methods for perspective projection:
	 * frustumM() and perspectiveM(). frustumM() has a bug that affects some types of projections,
	 * and perspectiveM() was only introduced in Android Ice Cream Sandwich and not available on
	 * earlier versions of Android. Therefore, we have to implement our own perspectiveM</br>
	 * @param m - 4x4 matrix(float[16])
	 * @param yFovInDegrees- field of view across y axis
	 * @param aspect- aspect ratio - width/height
	 * @param n- near
	 * @param f- far*/
	public static void perspectiveM(float[] m, float yFovInDegrees, float aspect, float n, float f){
		//calculate the focal length based on the field of view across y axis
		final float angleInRadians = (float) (yFovInDegrees * Math.PI / 180);
		final float a = (float) (1.0 / Math.tan(angleInRadians / 2.0));
		
		m[0] = a / aspect;		// column 1
		m[1] = 0f;
		m[2] = 0f;
		m[3] = 0f;
		
		m[4] = 0f;						// column 2
		m[5] = a;
		m[6] = 0f;
		m[7] = 0f;
		
		m[8] = 0f;
		m[9] = 0f;
		m[10] = -((f + n) / (f - n));
		m[11] = -1f;
		
		m[12] = 0f;
		m[13] = 0f;
		m[14] = -((2f * f * n) / (f - n));
		m[15] = 0f;
	}
}
