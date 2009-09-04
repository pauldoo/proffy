#include <math.h>
#include <stdlib.h>
#include <stdio.h>

typedef struct
{
    float* m_data;
    int m_width;
    int m_height;
}  FloatImage;

typedef struct
{
    short* m_data;
    int m_width;
    int m_height;
} ShortImage;

typedef struct
{
    FloatImage* m_images;
    int m_count;
} FloatVolume;

typedef struct
{
    ShortImage* m_images;
    int m_count;
} ShortVolume;

typedef struct
{
    FloatVolume* m_warp_x;
    FloatVolume* m_warp_y;
    FloatVolume* m_warp_z;
} Warpfield;

typedef void (*WarperFunc)(
    const ShortVolume* /* input_data */,
    const Warpfield* /* warpfield */,
    const ShortVolume* /* output_data */);

static short* const LookupShortImage(const ShortImage* const image, const int x, const int y)
{
    return image->m_data + x + y * image->m_width;
}

static short* const LookupShortVolume(const ShortVolume* const volume, const int x, const int y, const int z)
{
    return LookupShortImage(volume->m_images + z, x, y);
}

static float* const LookupFloatImage(const FloatImage* const image, const int x, const int y)
{
    return image->m_data + x + y * image->m_width;
}

static float* const LookupFloatVolume(const FloatVolume* const volume, const int x, const int y, const int z)
{
    return LookupFloatImage(volume->m_images + z, x, y);
}


static void Vanilla(
    const ShortVolume* const input_volume,
    const Warpfield* const warpfield,
    const ShortVolume* const output_volume)
{
    const int width = output_volume->m_images[0].m_width;
    const int height = output_volume->m_images[0].m_height;
    const int depth = output_volume->m_count;

    int x, y, z;

    for (z = 0; z < depth; ++z) {
        const FloatImage* const warp_x = warpfield->m_warp_x->m_images + z;
        const FloatImage* const warp_y = warpfield->m_warp_y->m_images + z;
        const FloatImage* const warp_z = warpfield->m_warp_z->m_images + z;
        const ShortImage* const output_image = output_volume->m_images + z;

        for (y = 0; y < height; ++y) {
            for (x = 0; x < width; ++x) {
                const double dx = warp_x->m_data[x + y * warp_x->m_width];
                const double dy = warp_y->m_data[x + y * warp_y->m_width];
                const double dz = warp_z->m_data[x + y * warp_z->m_width];

                const double source_x = x + dx;
                const double source_y = y + dy;
                const double source_z = z + dz;

                printf("Pixel at location (%i, %i, %i) was warped from (%f, %f, %f)\n", x, y, z, source_x, source_y, source_z);

                if (source_x >= 0.0 && source_x < (width - 1) &&
                    source_y >= 0.0 && source_y < (height - 1) &&
                    source_z >= 0.0 && source_z < (depth - 1)
                ) {
                    short* const output_pixel = output_image->m_data + (x + y * output_image->m_width);

                    const int x_int = (int)floor(source_x);
                    const int y_int = (int)floor(source_y);
                    const int z_int = (int)floor(source_z);
                    const double x_frac = source_x - x_int;
                    const double y_frac = source_y - y_int;
                    const double z_frac = source_z - z_int;

                    const double output_value =
                        *LookupShortVolume(input_volume, x_int, y_int, z_int) * (1.0 - x_frac) * (1.0 - y_frac) * (1.0 - z_frac) +
                        *LookupShortVolume(input_volume, x_int + 1, y_int, z_int) * (x_frac) * (1.0 - y_frac) * (1.0 - z_frac) +
                        *LookupShortVolume(input_volume, x_int, y_int + 1, z_int) * (1.0 - x_frac) * (y_frac) * (1.0 - z_frac) +
                        *LookupShortVolume(input_volume, x_int + 1, y_int + 1, z_int) * (x_frac) * (y_frac) * (1.0 - z_frac) +
                        *LookupShortVolume(input_volume, x_int, y_int, z_int + 1) * (1.0 - x_frac) * (1.0 - y_frac) * (z_frac) +
                        *LookupShortVolume(input_volume, x_int + 1, y_int, z_int + 1) * (x_frac) * (1.0 - y_frac) * (z_frac) +
                        *LookupShortVolume(input_volume, x_int, y_int + 1, z_int + 1) * (1.0 - x_frac) * (y_frac) * (z_frac) +
                        *LookupShortVolume(input_volume, x_int + 1, y_int + 1, z_int + 1) * (x_frac) * (y_frac) * (z_frac);

                    *output_pixel = (short)floor(output_value + 0.5);
                }
            }
        }

    }
}

static void AllocateShortImage(ShortImage* const result, const int width, const int height)
{
    result->m_data = calloc(width * height, sizeof(short));
    result->m_width = width;
    result->m_height = height;
}

static void AllocateFloatImage(FloatImage* const result, const int width, const int height)
{
    result->m_data = calloc(width * height, sizeof(float));
    result->m_width = width;
    result->m_height = height;
}

static void AllocateShortVolume(ShortVolume* const result, const int width, const int height, const int depth)
{
    int z;
    result->m_images = calloc(depth, sizeof(ShortImage));
    result->m_count = depth;

    for (z = 0; z < depth; z++) {
        AllocateShortImage(result->m_images + z, width, height);
    }
}

static void AllocateFloatVolume(FloatVolume* const result, const int width, const int height, const int depth)
{
    int z;
    result->m_images = calloc(depth, sizeof(FloatImage));
    result->m_count = depth;

    for (z = 0; z < depth; z++) {
        AllocateFloatImage(result->m_images + z, width, height);
    }
}

static void InitializeFloatVolume(const FloatVolume* const volume, const double frequencyInRadiansPerPixel, const double magnitude)
{
    int x, y, z;
    if (frequencyInRadiansPerPixel * magnitude >= 1.0) {
        printf("ERROR: Warp is too sharp.\n");
        exit(EXIT_FAILURE);
    }
    for (z = 0; z < volume->m_count; z++) {
        for (y = 0; y < (volume->m_images + z)->m_height; y++) {
            for (x = 0; x < (volume->m_images + z)->m_width; x++) {
                *LookupFloatVolume(volume, x, y, z) = (float)(cos((x + y + z) * frequencyInRadiansPerPixel) * magnitude);
            }
        }
    }
}

int main(void)
{
    const int width = 10;
    const int height = 10;
    const int depth = 10;
    ShortVolume input_volume, output_volume;
    FloatVolume warp_x, warp_y, warp_z;
    Warpfield warpfield;

    warpfield.m_warp_x = &warp_x;
    warpfield.m_warp_y = &warp_y;
    warpfield.m_warp_z = &warp_z;

    AllocateShortVolume(&input_volume, width, height, depth);
    AllocateShortVolume(&output_volume, width, height, depth);
    AllocateFloatVolume(&warp_x, width, height, depth);
    AllocateFloatVolume(&warp_y, width, height, depth);
    AllocateFloatVolume(&warp_z, width, height, depth);

    InitializeFloatVolume(warpfield.m_warp_x, 0.1, 3.0);
    InitializeFloatVolume(warpfield.m_warp_y, 0.2, 3.0);
    InitializeFloatVolume(warpfield.m_warp_z, 0.3, 3.0);

    Vanilla(&input_volume, &warpfield, &output_volume);

    return 0;
}

