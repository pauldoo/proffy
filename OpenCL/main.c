#include <math.h>
#include <stdlib.h>
#include <stdio.h>
#include <time.h>

#include <CL/cl.h>

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
    double m_scale;
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

static double const LinearInterpShortVolume(const ShortVolume* const volume, const double x, const double y, const double z)
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

static float* const LookupFloatImage(const FloatImage* const image, const int x, const int y)
{
    return image->m_data + x + y * image->m_width;
}

static float* const LookupFloatVolume(const FloatVolume* const volume, const int x, const int y, const int z)
{
    return LookupFloatImage(volume->m_images + z, x, y);
}

static double const LinearInterpFloatVolume(const FloatVolume* const volume, const double x, const double y, const double z)
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


static void WarpVanilla(
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

    for (z = 0; z < depth; ++z) {
        const ShortImage* const output_image = output_volume->m_images + z;

        for (y = 0; y < height; ++y) {
            for (x = 0; x < width; ++x) {
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
                        short* const output_pixel = output_image->m_data + (x + y * output_image->m_width);
                        const double output_value = LinearInterpShortVolume(input_volume, source_x, source_y, source_z);
                        *output_pixel = (short)floor(output_value + 0.5);
                    }
                } else {
                    /* printf("was outside warpfield\n"); */
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

static void Bailout(const char* const message)
{
    printf("ERROR: %s\n", message);
    exit(EXIT_FAILURE);
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

static const char* const ReadFileIntoString(
    const char* const filename)
{
    FILE* fp = fopen(filename, "rb");
    long size;
    char* result;

    if (fp == NULL) {
        Bailout("fopen failed");
    }
    if (fseek(fp, 0, SEEK_END) != 0) {
        Bailout("fseek failed");
    }
    size = ftell(fp);
    if (fseek(fp, 0, SEEK_SET) != 0) {
        Bailout("fseek failed");
    }
    result = calloc(size + 1, 1);
    if (fread(result, size, 1, fp) != 1) {
        Bailout("fread failed");
    }
    return result;
}

static void WarpOpenCL(
    const ShortVolume* const input_volume,
    const Warpfield* const warpfield,
    const ShortVolume* const output_volume)
{
    const char* const program_source = ReadFileIntoString("WarpOpenCL.cl");
    cl_int status;
    cl_context context;

    input_volume;
    warpfield;
    output_volume;
    program_source;

    context = clCreateContextFromType(0, CL_DEVICE_TYPE_CPU, 0, 0, &status);
    if(status != CL_SUCCESS) {
        Bailout("clCreateContextFromType failed");
	}

	status = clReleaseContext(context);
	if (status != CL_SUCCESS) {
	    Bailout("clReleaseContext failed");
	}
}

static void Benchmark(
    const char* const name,
    const ShortVolume* const input_volume,
    const Warpfield* const warpfield,
    const ShortVolume* const output_volume,
    void (*func)(const ShortVolume* const, const Warpfield* const, const ShortVolume* const))
{
    const int iterations = 1;
    clock_t begin, end;
    double secondsPerIteration;
    int i;

    begin = clock();
    for (i = 1; i <= iterations; i++) {
        printf(".");
        func(input_volume, warpfield, output_volume);
    }
    printf("\n");
    end = clock();

    secondsPerIteration = ((double)(end - begin)) / CLOCKS_PER_SEC / iterations;
    printf("%s: %f seconds.\n", name, secondsPerIteration);
}

int main(void)
{
    const int width = 256;
    const int height = 256;
    const int depth = 256;
    const double scale = 3;
    ShortVolume input_volume, output_volume;
    FloatVolume warp_x, warp_y, warp_z;
    Warpfield warpfield;

    warpfield.m_scale = scale;
    warpfield.m_warp_x = &warp_x;
    warpfield.m_warp_y = &warp_y;
    warpfield.m_warp_z = &warp_z;

    AllocateShortVolume(&input_volume, width, height, depth);
    AllocateShortVolume(&output_volume, width, height, depth);
    AllocateFloatVolume(&warp_x, (int)ceil(width / scale) + 1, (int)ceil(height / scale) + 1, (int)ceil(depth / scale) + 1);
    AllocateFloatVolume(&warp_y, (int)ceil(width / scale) + 1, (int)ceil(height / scale) + 1, (int)ceil(depth / scale) + 1);
    AllocateFloatVolume(&warp_z, (int)ceil(width / scale) + 1, (int)ceil(height / scale) + 1, (int)ceil(depth / scale) + 1);

    InitializeFloatVolume(warpfield.m_warp_x, 0.1, 3.0, scale);
    InitializeFloatVolume(warpfield.m_warp_y, 0.2, 3.0, scale);
    InitializeFloatVolume(warpfield.m_warp_z, 0.3, 3.0, scale);

    Benchmark("Vanilla", &input_volume, &warpfield, &output_volume, WarpVanilla);
    Benchmark("OpenCL", &input_volume, &warpfield, &output_volume, WarpOpenCL);

    return 0;
}

