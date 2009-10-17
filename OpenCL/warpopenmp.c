#include "warpvanilla.h"

#include <math.h>

static double LinearInterpShortVolume(const ShortVolume* const volume, const double x, const double y, const double z)
{
    const int x_int = (int)floor(x);
    const int y_int = (int)floor(y);
    const int z_int = (int)floor(z);
    const double x_frac = x - x_int;
    const double y_frac = y - y_int;
    const double z_frac = z - z_int;

    const double result =
        *LookupShortVolume(volume, x_int, y_int, z_int) * (1.0 - x_frac) * (1.0 - y_frac) * (1.0 - z_frac) +
        *LookupShortVolume(volume, x_int + 1, y_int, z_int) * (x_frac) * (1.0 - y_frac) * (1.0 - z_frac) +
        *LookupShortVolume(volume, x_int, y_int + 1, z_int) * (1.0 - x_frac) * (y_frac) * (1.0 - z_frac) +
        *LookupShortVolume(volume, x_int + 1, y_int + 1, z_int) * (x_frac) * (y_frac) * (1.0 - z_frac) +
        *LookupShortVolume(volume, x_int, y_int, z_int + 1) * (1.0 - x_frac) * (1.0 - y_frac) * (z_frac) +
        *LookupShortVolume(volume, x_int + 1, y_int, z_int + 1) * (x_frac) * (1.0 - y_frac) * (z_frac) +
        *LookupShortVolume(volume, x_int, y_int + 1, z_int + 1) * (1.0 - x_frac) * (y_frac) * (z_frac) +
        *LookupShortVolume(volume, x_int + 1, y_int + 1, z_int + 1) * (x_frac) * (y_frac) * (z_frac);
    return result;
}

static double LinearInterpFloatVolume(const FloatVolume* const volume, const double x, const double y, const double z)
{
    const int x_int = (int)floor(x);
    const int y_int = (int)floor(y);
    const int z_int = (int)floor(z);
    const double x_frac = x - x_int;
    const double y_frac = y - y_int;
    const double z_frac = z - z_int;

    const double result =
        *LookupFloatVolume(volume, x_int, y_int, z_int) * (1.0 - x_frac) * (1.0 - y_frac) * (1.0 - z_frac) +
        *LookupFloatVolume(volume, x_int + 1, y_int, z_int) * (x_frac) * (1.0 - y_frac) * (1.0 - z_frac) +
        *LookupFloatVolume(volume, x_int, y_int + 1, z_int) * (1.0 - x_frac) * (y_frac) * (1.0 - z_frac) +
        *LookupFloatVolume(volume, x_int + 1, y_int + 1, z_int) * (x_frac) * (y_frac) * (1.0 - z_frac) +
        *LookupFloatVolume(volume, x_int, y_int, z_int + 1) * (1.0 - x_frac) * (1.0 - y_frac) * (z_frac) +
        *LookupFloatVolume(volume, x_int + 1, y_int, z_int + 1) * (x_frac) * (1.0 - y_frac) * (z_frac) +
        *LookupFloatVolume(volume, x_int, y_int + 1, z_int + 1) * (1.0 - x_frac) * (y_frac) * (z_frac) +
        *LookupFloatVolume(volume, x_int + 1, y_int + 1, z_int + 1) * (x_frac) * (y_frac) * (z_frac);
    return result;
}

void WarpOpenMP(
    const ShortVolume* const input_volume,
    const Warpfield* const warpfield,
    const ShortVolume* const output_volume,
    const int iterations)
{
    const int width = output_volume->m_images[0].m_width;
    const int height = output_volume->m_images[0].m_height;
    const int depth = output_volume->m_count;
    const int warp_width = warpfield->m_warp_x->m_images[0].m_width;
    const int warp_height = warpfield->m_warp_x->m_images[0].m_height;
    const int warp_depth = warpfield->m_warp_x->m_count;

    int i;
    for (i = 0; i < iterations; i++) {
        int z;
#pragma omp parallel for default(none)
        for (z = 0; z < depth; ++z) {
            const ShortImage* const output_image = output_volume->m_images + z;
            int y;
            for (y = 0; y < height; ++y) {
                int x;
                for (x = 0; x < width; ++x) {
                    short* const output_pixel = output_image->m_data + (x + y * output_image->m_width);
                    short output_value = -32768;

                    /* Coordinate to lookup in downscaled warpfield. */
                    const double warp_x = x / warpfield->m_scale;
                    const double warp_y = y / warpfield->m_scale;
                    const double warp_z = z / warpfield->m_scale;

                    /* printf("Pixel at location (%i, %i, %i) ", x, y, z); */

                    if (warp_x >= 0.0 && warp_x < (warp_width - 1) &&
                        warp_y >= 0.0 && warp_y < (warp_height - 1) &&
                        warp_z >= 0.0 && warp_z < (warp_depth - 1)
                    ) {
                        /* Warpfield value. */
                        const double dx = LinearInterpFloatVolume(warpfield->m_warp_x, warp_x, warp_y, warp_z);
                        const double dy = LinearInterpFloatVolume(warpfield->m_warp_y, warp_x, warp_y, warp_z);
                        const double dz = LinearInterpFloatVolume(warpfield->m_warp_z, warp_x, warp_y, warp_z);

                        /* Coordinate from source volume. */
                        const double source_x = x + dx;
                        const double source_y = y + dy;
                        const double source_z = z + dz;

                        /* printf("was warped from (%f, %f, %f)\n", source_x, source_y, source_z); */

                        if (source_x >= 0.0 && source_x < (width - 1) &&
                            source_y >= 0.0 && source_y < (height - 1) &&
                            source_z >= 0.0 && source_z < (depth - 1)
                        ) {
                            output_value = (short)floor(LinearInterpShortVolume(input_volume, source_x, source_y, source_z) + 0.5);
                        }
                    } else {
                        /* printf("was outside warpfield\n"); */
                    }

                    *output_pixel = output_value;
                }
            }

        }
    }
}

