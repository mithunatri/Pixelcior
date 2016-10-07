#pragma version(1)
#pragma rs java_package_name(com.android.rs.sobel)

//#include "rs_cl.rsh"

rs_allocation gIn;
rs_allocation gTmp;
rs_allocation gOut;
rs_script gScript;

int32_t width;
int32_t height;



// 1 0 -1
// 2 0 -2
// 1 0 -1
//
void SobelFirstPass(const uchar4 *v_in, uchar4 *v_out, uint32_t x, uint32_t y)
{

/*
    float4 in = rsUnpackColor8888(* v_in);

    //Convert image to grayscale
    float value = (in.r + in.g + in.b)/3;
    in.r = in.b = in.g = value;
*/
    uint32_t n = max(y - 1, (uint32_t)0);
    uint32_t s = min(y + 1, (uint32_t)height);
    uint32_t e = min(x + 1, (uint32_t)width);
    uint32_t w = max(x - 1, (uint32_t)0);

    const uchar *e11 = rsGetElementAt(gIn, w, n);
    const uchar *e12 = rsGetElementAt(gIn, w, y);
    const uchar *e13 = rsGetElementAt(gIn, w, s);

    const uchar *e31 = rsGetElementAt(gIn, e, n);
    const uchar *e32 = rsGetElementAt(gIn, e, y);
    const uchar *e33 = rsGetElementAt(gIn, e, s);

    *v_out =(*e12 - *e32)*2 + *e11 + *e13 - *e31 - *e33;
}

//  1  2  1
//  0  0  0
// -1 -2 -1
//

void SobelSecondPass(const uchar4 *v_in, uchar4 *v_out, uint32_t x, uint32_t y)
{
    uint32_t n = max(y - 1, (uint32_t)0);
    uint32_t s = min(y + 1, (uint32_t)height);
    uint32_t e = min(x + 1, (uint32_t)width);
    uint32_t w = max(x - 1, (uint32_t)0);

    const uchar *e11 = rsGetElementAt(gIn, w, n);
    const uchar *e21 = rsGetElementAt(gIn, x, n);
    const uchar *e31 = rsGetElementAt(gIn, e, n);

    const uchar *e13 = rsGetElementAt(gIn, w, s);
    const uchar *e23 = rsGetElementAt(gIn, x, s);
    const uchar *e33 = rsGetElementAt(gIn, e, s);

    const float *lastPassResult = rsGetElementAt(gOut, x, y);

    float tmp = (*e21 - *e23)*2 + *e11 - *e13 + *e31 - *e33;

    uchar res =  (uchar)clamp(sqrt(tmp * tmp + *lastPassResult * *lastPassResult), 0.0f, 255.0f);

    *v_out = (uchar4){res, res, res, 255};
}


void init(){
    //rsForEach(gScript, gIn, gOut);
}
