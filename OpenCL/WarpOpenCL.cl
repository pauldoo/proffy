__kernel void warp(
    int width,
    int height,
    int depth,
    int warp_width,
    int warp_height,
    int warp_depth,
    __global const short *input_volume,
    __global const float *warpfield_x,
    __global const float *warpfield_y,
    __global const float *warpfield_z,
    __global short* output_volume
    )
{
    const int x = get_global_id(0);
    const int y = get_global_id(1);
    const int z = get_global_id(2);

    short* const output_pixel = output_volume[x + y * width + z * width * height];
    *output_pixel = 9;
}

