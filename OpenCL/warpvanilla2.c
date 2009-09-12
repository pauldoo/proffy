#include "warpvanilla2.h"

#include <stdlib.h>
#include <math.h>
#include <string.h>

static const short* const LookupShortBlock(const short* const volume, const int width, const int height, const int x, const int y, const int z)
{
    return volume + x + y * width + z * width * height;
}

static double const LinearInterpShortBlock(const short* const volume, const int width, const int height, const double x, const double y, const double z)
{
    const int x_int = (int)floor(x);
    const int y_int = (int)floor(y);
    const int z_int = (int)floor(z);
    const double x_frac = x - x_int;
    const double y_frac = y - y_int;
    const double z_frac = z - z_int;

    const double result =
        *LookupShortBlock(volume, width, height, x_int, y_int, z_int) * (1.0 - x_frac) * (1.0 - y_frac) * (1.0 - z_frac) +
        *LookupShortBlock(volume, width, height, x_int + 1, y_int, z_int) * (x_frac) * (1.0 - y_frac) * (1.0 - z_frac) +
        *LookupShortBlock(volume, width, height, x_int, y_int + 1, z_int) * (1.0 - x_frac) * (y_frac) * (1.0 - z_frac) +
        *LookupShortBlock(volume, width, height, x_int + 1, y_int + 1, z_int) * (x_frac) * (y_frac) * (1.0 - z_frac) +
        *LookupShortBlock(volume, width, height, x_int, y_int, z_int + 1) * (1.0 - x_frac) * (1.0 - y_frac) * (z_frac) +
        *LookupShortBlock(volume, width, height, x_int + 1, y_int, z_int + 1) * (x_frac) * (1.0 - y_frac) * (z_frac) +
        *LookupShortBlock(volume, width, height, x_int, y_int + 1, z_int + 1) * (1.0 - x_frac) * (y_frac) * (z_frac) +
        *LookupShortBlock(volume, width, height, x_int + 1, y_int + 1, z_int + 1) * (x_frac) * (y_frac) * (z_frac);
    return result;
}

static const float* const LookupFloatBlock(const float* const volume, const int width, const int height, const int x, const int y, const int z)
{
    return volume + x + y * width + z * width * height;
}

static double const LinearInterpFloatBlock(const float* const volume, const int width, const int height, const double x, const double y, const double z)
{
    const int x_int = (int)floor(x);
    const int y_int = (int)floor(y);
    const int z_int = (int)floor(z);
    const double x_frac = x - x_int;
    const double y_frac = y - y_int;
    const double z_frac = z - z_int;

    const double result =
        *LookupFloatBlock(volume, width, height, x_int, y_int, z_int) * (1.0 - x_frac) * (1.0 - y_frac) * (1.0 - z_frac) +
        *LookupFloatBlock(volume, width, height, x_int + 1, y_int, z_int) * (x_frac) * (1.0 - y_frac) * (1.0 - z_frac) +
        *LookupFloatBlock(volume, width, height, x_int, y_int + 1, z_int) * (1.0 - x_frac) * (y_frac) * (1.0 - z_frac) +
        *LookupFloatBlock(volume, width, height, x_int + 1, y_int + 1, z_int) * (x_frac) * (y_frac) * (1.0 - z_frac) +
        *LookupFloatBlock(volume, width, height, x_int, y_int, z_int + 1) * (1.0 - x_frac) * (1.0 - y_frac) * (z_frac) +
        *LookupFloatBlock(volume, width, height, x_int + 1, y_int, z_int + 1) * (x_frac) * (1.0 - y_frac) * (z_frac) +
        *LookupFloatBlock(volume, width, height, x_int, y_int + 1, z_int + 1) * (1.0 - x_frac) * (y_frac) * (z_frac) +
        *LookupFloatBlock(volume, width, height, x_int + 1, y_int + 1, z_int + 1) * (x_frac) * (y_frac) * (z_frac);
    return result;
}

void WarpVanilla2(
    const ShortVolume* const input_volume,
    const Warpfield* const warpfield,
    const ShortVolume* const output_volume)
{
    const int width = output_volume->m_images[0].m_width;
    const int height = output_volume->m_images[0].m_height;
    const int depth = output_volume->m_count;
    const int warp_width = warpfield->m_warp_x->m_images[0].m_width;
    const int warp_height = warpfield->m_warp_x->m_images[0].m_height;
    const int warp_depth = warpfield->m_warp_x->m_count;
    int x, y, z;

    short* const block_input_volume = malloc(width * height * depth * sizeof(short));
    float* const block_warpfield_x = malloc(warp_width * warp_height * warp_depth * sizeof(float));
    float* const block_warpfield_y = malloc(warp_width * warp_height * warp_depth * sizeof(float));
    float* const block_warpfield_z = malloc(warp_width * warp_height * warp_depth * sizeof(float));
    short* const block_output_volume = malloc(width * height * depth * sizeof(short));
    if (block_input_volume == NULL ||
        block_warpfield_x == NULL ||
        block_warpfield_y == NULL ||
        block_warpfield_z == NULL ||
        block_output_volume == NULL) {
        Bailout("malloc failed.");
    }


    for (z = 0; z < depth; ++z) {
        const ShortImage* const input_image = input_volume->m_images + z;
        memcpy(block_input_volume + z * width * height, input_image->m_data, width * height * sizeof(short));
    }

    for (z = 0; z < warp_depth; ++z) {
        {
            const FloatImage* const input_image = warpfield->m_warp_x->m_images + z;
            memcpy(block_warpfield_x + z * warp_width * warp_height, input_image->m_data, warp_width * warp_height * sizeof(float));
        }
        {
            const FloatImage* const input_image = warpfield->m_warp_y->m_images + z;
            memcpy(block_warpfield_y + z * warp_width * warp_height, input_image->m_data, warp_width * warp_height * sizeof(float));
        }
        {
            const FloatImage* const input_image = warpfield->m_warp_z->m_images + z;
            memcpy(block_warpfield_z + z * warp_width * warp_height, input_image->m_data, warp_width * warp_height * sizeof(float));
        }
    }


    for (z = 0; z < depth; ++z) {
        for (y = 0; y < height; ++y) {
            for (x = 0; x < width; ++x) {
                short* const output_pixel = block_output_volume + (x + y * width + z * width * height);
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
                    const double dx = LinearInterpFloatBlock(block_warpfield_x, warp_width, warp_height, warp_x, warp_y, warp_z);
                    const double dy = LinearInterpFloatBlock(block_warpfield_y, warp_width, warp_height, warp_x, warp_y, warp_z);
                    const double dz = LinearInterpFloatBlock(block_warpfield_z, warp_width, warp_height, warp_x, warp_y, warp_z);

                    /* Coordinate from source volume. */
                    const double source_x = x + dx;
                    const double source_y = y + dy;
                    const double source_z = z + dz;

                    /* printf("was warped from (%f, %f, %f)\n", source_x, source_y, source_z); */

                    if (source_x >= 0.0 && source_x < (width - 1) &&
                        source_y >= 0.0 && source_y < (height - 1) &&
                        source_z >= 0.0 && source_z < (depth - 1)
                    ) {
                        output_value = (short)floor(LinearInterpShortBlock(block_input_volume, width, height, source_x, source_y, source_z) + 0.5);
                    }
                } else {
                    /* printf("was outside warpfield\n"); */
                }

                *output_pixel = output_value;
            }
        }

    }

    free(block_input_volume);
    free(block_warpfield_x);
    free(block_warpfield_y);
    free(block_warpfield_z);

    for (z = 0; z < depth; ++z) {
        const ShortImage* const output_image = output_volume->m_images + z;
        memcpy(output_image->m_data, block_output_volume + z * width * height, width * height * sizeof(short));
    }

    free(block_output_volume);
}

