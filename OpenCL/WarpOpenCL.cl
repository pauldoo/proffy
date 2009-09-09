__global const short* LookupShortVolume(
    __global const short* volume,
    int sizex,
    int sizey,
    int x,
    int y,
    int z)
{
    return volume + (x + y * sizex + z * sizex * sizey);
}

double LinearInterpShortVolume(
    __global const short* volume,
    int sizex,
    int sizey,
    double x,
    double y,
    double z)
{
    const int x_int = (int)floor(x);
    const int y_int = (int)floor(y);
    const int z_int = (int)floor(z);
    const double x_frac = x - x_int;
    const double y_frac = y - y_int;
    const double z_frac = z - z_int;

    const double result =
        *LookupShortVolume(volume, sizex, sizey, x_int, y_int, z_int) * (1.0 - x_frac) * (1.0 - y_frac) * (1.0 - z_frac) +
        *LookupShortVolume(volume, sizex, sizey, x_int + 1, y_int, z_int) * (x_frac) * (1.0 - y_frac) * (1.0 - z_frac) +
        *LookupShortVolume(volume, sizex, sizey, x_int, y_int + 1, z_int) * (1.0 - x_frac) * (y_frac) * (1.0 - z_frac) +
        *LookupShortVolume(volume, sizex, sizey, x_int + 1, y_int + 1, z_int) * (x_frac) * (y_frac) * (1.0 - z_frac) +
        *LookupShortVolume(volume, sizex, sizey, x_int, y_int, z_int + 1) * (1.0 - x_frac) * (1.0 - y_frac) * (z_frac) +
        *LookupShortVolume(volume, sizex, sizey, x_int + 1, y_int, z_int + 1) * (x_frac) * (1.0 - y_frac) * (z_frac) +
        *LookupShortVolume(volume, sizex, sizey, x_int, y_int + 1, z_int + 1) * (1.0 - x_frac) * (y_frac) * (z_frac) +
        *LookupShortVolume(volume, sizex, sizey, x_int + 1, y_int + 1, z_int + 1) * (x_frac) * (y_frac) * (z_frac);
    return result;
}

__global const float* LookupFloatVolume(
    __global const float* volume,
    int sizex,
    int sizey,
    int x,
    int y,
    int z)
{
    return volume + (x + y * sizex + z * sizex * sizey);
}

double LinearInterpFloatVolume(
    __global const float* volume,
    int sizex,
    int sizey,
    double x,
    double y,
    double z)
{
    const int x_int = (int)floor(x);
    const int y_int = (int)floor(y);
    const int z_int = (int)floor(z);
    const double x_frac = x - x_int;
    const double y_frac = y - y_int;
    const double z_frac = z - z_int;

    const double result =
        *LookupFloatVolume(volume, sizex, sizey, x_int, y_int, z_int) * (1.0 - x_frac) * (1.0 - y_frac) * (1.0 - z_frac) +
        *LookupFloatVolume(volume, sizex, sizey, x_int + 1, y_int, z_int) * (x_frac) * (1.0 - y_frac) * (1.0 - z_frac) +
        *LookupFloatVolume(volume, sizex, sizey, x_int, y_int + 1, z_int) * (1.0 - x_frac) * (y_frac) * (1.0 - z_frac) +
        *LookupFloatVolume(volume, sizex, sizey, x_int + 1, y_int + 1, z_int) * (x_frac) * (y_frac) * (1.0 - z_frac) +
        *LookupFloatVolume(volume, sizex, sizey, x_int, y_int, z_int + 1) * (1.0 - x_frac) * (1.0 - y_frac) * (z_frac) +
        *LookupFloatVolume(volume, sizex, sizey, x_int + 1, y_int, z_int + 1) * (x_frac) * (1.0 - y_frac) * (z_frac) +
        *LookupFloatVolume(volume, sizex, sizey, x_int, y_int + 1, z_int + 1) * (1.0 - x_frac) * (y_frac) * (z_frac) +
        *LookupFloatVolume(volume, sizex, sizey, x_int + 1, y_int + 1, z_int + 1) * (x_frac) * (y_frac) * (z_frac);
    return result;
}

__kernel void warp(
    int width,
    int height,
    int depth,
    int warp_width,
    int warp_height,
    int warp_depth,
    double warp_scale,
    __global const short *input_volume,
    __global const float *warpfield_x,
    __global const float *warpfield_y,
    __global const float *warpfield_z,
    __global short* output_volume)
{
    const int x = get_global_id(0);
    const int y = get_global_id(1);
    const int z = get_global_id(2);

    /* Coordinate to lookup in downscaled warpfield. */
    const double warp_x = x / warp_scale;
    const double warp_y = y / warp_scale;
    const double warp_z = z / warp_scale;

    if (warp_x >= 0.0 && warp_x < (warp_width - 1) &&
        warp_y >= 0.0 && warp_y < (warp_height - 1) &&
        warp_z >= 0.0 && warp_z < (warp_depth - 1)
    ) {
        /* Warpfield value. */
        const double dx = LinearInterpFloatVolume(warpfield_x, warp_width, warp_height, warp_x, warp_y, warp_z);
        const double dy = LinearInterpFloatVolume(warpfield_y, warp_width, warp_height, warp_x, warp_y, warp_z);
        const double dz = LinearInterpFloatVolume(warpfield_z, warp_width, warp_height, warp_x, warp_y, warp_z);

        /* Coordinate from source volume. */
        const double source_x = x + dx;
        const double source_y = y + dy;
        const double source_z = z + dz;

        if (source_x >= 0.0 && source_x < (width - 1) &&
            source_y >= 0.0 && source_y < (height - 1) &&
            source_z >= 0.0 && source_z < (depth - 1)
        ) {
            __global short* const output_pixel = output_volume + (x + y * width + z * width * height);
            const double output_value = LinearInterpShortVolume(input_volume, width, height, source_x, source_y, source_z);
            *output_pixel = (short)floor(output_value + 0.5);
        }
    }
}

