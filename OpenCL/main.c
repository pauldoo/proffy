#include "common.h"
#include "warpopencl.h"
#include "warpvanilla.h"
#include "warpvanilla2.h"
#include "warpvanillaf.h"
#include "warpopenmp.h"

#include <stdlib.h>
#include <stdio.h>
#include <math.h>
#include <time.h>


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

static void InitializeShortVolume(const ShortVolume* const volume)
{
    int x, y, z;

    for (z = 0; z < volume->m_count; z++) {
        for (y = 0; y < (volume->m_images + z)->m_height; y++) {
            for (x = 0; x < (volume->m_images + z)->m_width; x++) {
                *LookupShortVolume(volume, x, y, z) = (short)floor(cos(sqrt(x*x + y*y + z*z) * 0.2) * 20000.0 + 0.5);
            }
        }
    }
}

static void InitializeFloatVolume(
    const FloatVolume* const volume,
    const double frequencyInRadiansPerPixel,
    const double magnitude,
    const double scale)
{
    int x, y, z;
    if (frequencyInRadiansPerPixel * magnitude >= 1.0) {
        Bailout("Warp is too sharp");
    }
    for (z = 0; z < volume->m_count; z++) {
        for (y = 0; y < (volume->m_images + z)->m_height; y++) {
            for (x = 0; x < (volume->m_images + z)->m_width; x++) {
                *LookupFloatVolume(volume, x, y, z) = (float)(cos((x + y + z) * frequencyInRadiansPerPixel * scale) * magnitude);
            }
        }
    }
}

static void InitializeShortVolumeExpectedResult(
    const ShortVolume* const volume,
    const double frequencyInRadiansPerPixelX,
    const double frequencyInRadiansPerPixelY,
    const double frequencyInRadiansPerPixelZ,
    const double magnitude)
{
    int x, y, z;

    for (z = 0; z < volume->m_count; z++) {
        for (y = 0; y < (volume->m_images + z)->m_height; y++) {
            for (x = 0; x < (volume->m_images + z)->m_width; x++) {
                const double dx = cos((x + y + z) * frequencyInRadiansPerPixelX) * magnitude;
                const double dy = cos((x + y + z) * frequencyInRadiansPerPixelY) * magnitude;
                const double dz = cos((x + y + z) * frequencyInRadiansPerPixelZ) * magnitude;
                const double tx = x + dx;
                const double ty = y + dy;
                const double tz = z + dz;
                *LookupShortVolume(volume, x, y, z) = (short)floor(cos(sqrt(tx*tx + ty*ty + tz*tz) * 0.2) * 20000.0 + 0.5);
            }
        }
    }
}

static double MeasureRmsError(
    const ShortVolume* const volumeA,
    const ShortVolume* const volumeB,
    const int ignoreRegionSize)
{
    double sumSquared = 0.0;
    int count = 0;
    int x, y, z;

    for (z = ignoreRegionSize; z < (volumeA->m_count - ignoreRegionSize); z++) {
        for (y = ignoreRegionSize; y < ((volumeA->m_images + z)->m_height - ignoreRegionSize); y++) {
            for (x = ignoreRegionSize; x < ((volumeA->m_images + z)->m_width - ignoreRegionSize); x++) {
                int difference =
                    ((int)*LookupShortVolume(volumeA, x, y, z)) -
                    ((int)*LookupShortVolume(volumeB, x, y, z));
                sumSquared += difference*difference;
                count++;
            }
        }
    }
    return sqrt(sumSquared / count);
}

static void Benchmark(
    const char* const name,
    const ShortVolume* const input_volume,
    const Warpfield* const warpfield,
    const ShortVolume* const output_volume,
    const ShortVolume* const expected_volume,
    const WarperFunc func)
{
    clock_t begin, end;
    double timeSingle;
    double timeMulti;
    double setupTime;
    double computeTime;
    double rmsError;

    begin = clock();
    func(input_volume, warpfield, output_volume, 1);
    end = clock();
    timeSingle = ((double)(end - begin)) / CLOCKS_PER_SEC;

    begin = clock();
    func(input_volume, warpfield, output_volume, 10);
    end = clock();
    timeMulti = ((double)(end - begin)) / CLOCKS_PER_SEC;

    rmsError = MeasureRmsError(output_volume, expected_volume, 10);
    printf("%s: %f rms error.\n", name, rmsError);

    computeTime = (timeMulti - timeSingle) / 9.0;
    setupTime = timeSingle - computeTime;

    printf("%s: Setup: %f seconds.\n", name, setupTime);
    printf("%s: Compute: %f seconds.\n", name, computeTime);
    printf("\n");
}

int main(void)
{
    const int width = 256;
    const int height = 256;
    const int depth = 256;
    const double warpScale = 3;
    const double warpFrequencyInRadiansPerPixelX = 0.1;
    const double warpFrequencyInRadiansPerPixelY = 0.2;
    const double warpFrequencyInRadiansPerPixelZ = 0.3;
    const double warpMagnitude = 3.0;
    ShortVolume input_volume, output_volume, expected_volume;
    FloatVolume warp_x, warp_y, warp_z;
    Warpfield warpfield;

    setvbuf(stdout, NULL, _IONBF, 0);

    warpfield.m_scale = warpScale;
    warpfield.m_warp_x = &warp_x;
    warpfield.m_warp_y = &warp_y;
    warpfield.m_warp_z = &warp_z;

    AllocateShortVolume(&input_volume, width, height, depth);
    AllocateShortVolume(&output_volume, width, height, depth);
    AllocateShortVolume(&expected_volume, width, height, depth);
    AllocateFloatVolume(&warp_x, (int)ceil(width / warpScale) + 1, (int)ceil(height / warpScale) + 1, (int)ceil(depth / warpScale) + 1);
    AllocateFloatVolume(&warp_y, (int)ceil(width / warpScale) + 1, (int)ceil(height / warpScale) + 1, (int)ceil(depth / warpScale) + 1);
    AllocateFloatVolume(&warp_z, (int)ceil(width / warpScale) + 1, (int)ceil(height / warpScale) + 1, (int)ceil(depth / warpScale) + 1);

    InitializeShortVolume(&input_volume);
    InitializeShortVolumeExpectedResult(
            &expected_volume,
            warpFrequencyInRadiansPerPixelX,
            warpFrequencyInRadiansPerPixelY,
            warpFrequencyInRadiansPerPixelZ,
            warpMagnitude);
    InitializeFloatVolume(warpfield.m_warp_x, warpFrequencyInRadiansPerPixelX, warpMagnitude, warpScale);
    InitializeFloatVolume(warpfield.m_warp_y, warpFrequencyInRadiansPerPixelY, warpMagnitude, warpScale);
    InitializeFloatVolume(warpfield.m_warp_z, warpFrequencyInRadiansPerPixelZ, warpMagnitude, warpScale);

    Benchmark("OpenCL", &input_volume, &warpfield, &output_volume, &expected_volume, WarpOpenCL);
    Benchmark("OpenMP", &input_volume, &warpfield, &output_volume, &expected_volume, WarpOpenMP);
    Benchmark("Vanilla", &input_volume, &warpfield, &output_volume, &expected_volume, WarpVanilla);
    Benchmark("Vanilla2", &input_volume, &warpfield, &output_volume, &expected_volume, WarpVanilla2);
    Benchmark("VanillaF", &input_volume, &warpfield, &output_volume, &expected_volume, WarpVanillaF);

    return 0;
}

