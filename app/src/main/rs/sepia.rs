#pragma version(1)
#pragma rs java_package_name(com.android.rs.sepia)

rs_allocation gIn;
rs_allocation gOut;
rs_script gScript;

float depth;            //To control the depth of Red and Green.
float intensity;         //To Control the intensity of Blue


void root(const uchar4 *v_in, uchar4 *v_out, uint32_t x, uint32_t y){

    float4 in = rsUnpackColor8888(* v_in);

    //Convert image to grayscale
    float value = (in.r + in.g + in.b)/3;
    in.r = in.b = in.g = value;

    //Convert image to Sepia
    in.r = in.r + (depth * 2);
    in.g = in.g + (depth);
    in.b = in.b - intensity;

    if(in.r <= ((depth*2)- 1)) in.r = 1.0f;
    if(in.g <= (depth - 1)) in.g = 1.0f;

    float3 sepia_vals = {in.r, in.g, in.b};

    *v_out = rsPackColorTo8888(sepia_vals);
}

void sepia(){
    rsForEach(gScript, gIn, gOut);
}
