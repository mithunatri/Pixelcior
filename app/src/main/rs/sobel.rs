#pragma version(1)
#pragma rs java_package_name(com.android.rs.sobel)

// Reference: https://github.com/jean80it/RSRTImgProc/blob/master/src/com/stareatit/RSRTImgProc/filter.rs
// http://angeljohnsy.blogspot.com/2011/12/sobel-edge-detection.html

rs_allocation gIn;
rs_allocation gOut;
rs_script gScript;

int32_t width;
int32_t height;


// xmask
//  1  2  1
//  0  0  0
// -1 -2 -1
//
void derivativeX(const uchar4 *v_in, uchar4 *v_out, uint32_t x, uint32_t y)
{

    uint32_t up = max(x - 1, (uint32_t) 0);
    uint32_t down = min(x + 1, (uint32_t) width);
    uint32_t left = max(y - 1, (uint32_t) 0);
    uint32_t right = min(y + 1, (uint32_t) height);

    const uchar *upLeft = rsGetElementAt(gIn, up, left);
    const uchar *upCur = rsGetElementAt(gIn, up, y);
    const uchar *upRight = rsGetElementAt(gIn, up, right);

    const uchar *downLeft = rsGetElementAt(gIn, down, left);
    const uchar *downCur = rsGetElementAt(gIn, down, y);
    const uchar *downRight = rsGetElementAt(gIn, down, right);

    float dx = ( 2 * (*upCur - *downCur)) + *upLeft + *upRight - *downLeft - *downRight;
    *v_out = dx;
}

// ymask
// 1 0 -1
// 2 0 -2
// 1 0 -1
//
void derivativeY(const uchar4 *v_in, uchar4 *v_out, uint32_t x, uint32_t y)
{

    uint32_t up = max(x - 1, (uint32_t) 0);
    uint32_t down = min(x + 1, (uint32_t) width);
    uint32_t left = max(y - 1, (uint32_t) 0);
    uint32_t right = min(y + 1, (uint32_t) height);


    const uchar *upLeft = rsGetElementAt(gIn, up, left);
    const uchar *curLeft = rsGetElementAt(gIn, x, left);
    const uchar *downLeft = rsGetElementAt(gIn, down, left);

    const uchar *upRight = rsGetElementAt(gIn, up, right);
    const uchar *curRight = rsGetElementAt(gIn, x, right);
    const uchar *downRight = rsGetElementAt(gIn, down, right);

    const float *dx = rsGetElementAt(gOut, x, y);

    float dy = (2 * (*curLeft - *curRight)) + (*upLeft - *upRight) + (*downLeft - *downRight);

    uchar magnitude = (uchar) clamp(sqrt(*dx * *dx + dy * dy), 0.0f, 255.0f);

    *v_out = (uchar4){magnitude, magnitude, magnitude, 255};
}


void init(){
    //rsForEach(gScript, gIn, gOut);
}
