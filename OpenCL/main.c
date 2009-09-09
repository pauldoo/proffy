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
                        *output_pixel = (short)round(output_value);
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

static void BailoutWithOpenClStatus(
    const char* const message,
    const cl_int status)
{
    printf("ERROR: %s\n", message);
    switch (status) {
    case CL_SUCCESS:
        break;
    case CL_INVALID_PROGRAM:
        printf("CL_INVALID_PROGRAM\n");
        break;
    case CL_INVALID_PROGRAM_EXECUTABLE:
        printf("CL_INVALID_PROGRAM_EXECUTABLE\n");
        break;
    case CL_INVALID_KERNEL_NAME:
        printf("CL_INVALID_KERNEL_NAME\n");
        break;
    case CL_INVALID_KERNEL_DEFINITION:
        printf("CL_INVALID_KERNEL_DEFINITION\n");
        break;
    case CL_INVALID_VALUE:
        printf("CL_INVALID_VALUE\n");
        break;
    case CL_OUT_OF_HOST_MEMORY:
        printf("CL_OUT_OF_HOST_MEMORY\n");
        break;
    default:
        printf("Unknown OpenCL error\n");
        break;
    }
    exit(EXIT_FAILURE);
}

static void Bailout(const char* const message)
{
    BailoutWithOpenClStatus(message, CL_SUCCESS);
}


static void InitializeShortVolume(const ShortVolume* const volume)
{
    int x, y, z;

    for (z = 0; z < volume->m_count; z++) {
        for (y = 0; y < (volume->m_images + z)->m_height; y++) {
            for (x = 0; x < (volume->m_images + z)->m_width; x++) {
                *LookupShortVolume(volume, x, y, z) = (short)round(cos(sqrt(x*x + y*y + z*z) * 0.2) * 20000.0);
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
                *LookupShortVolume(volume, x, y, z) = (short)round(cos(sqrt(tx*tx + ty*ty + tz*tz) * 0.2) * 20000.0);
            }
        }
    }
}

static const double MeasureRmsError(
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
    char* build_log;
    cl_command_queue command_queue;
    cl_context context;
    cl_device_id* devices;
    cl_int status;
    cl_mem input_volume_mem;
    cl_mem output_volume_mem;
    cl_mem warpfield_x_mem;
    cl_mem warpfield_y_mem;
    cl_mem warpfield_z_mem;
    cl_program program;
    cl_kernel kernel;
    const char* const program_source = ReadFileIntoString("WarpOpenCL.cl");
    const char* program_sources[] = { program_source };
    const cl_int width = output_volume->m_images[0].m_width;
    const cl_int height = output_volume->m_images[0].m_height;
    const cl_int depth = output_volume->m_count;
    const cl_int warp_width = warpfield->m_warp_x->m_images[0].m_width;
    const cl_int warp_height = warpfield->m_warp_x->m_images[0].m_height;
    const cl_int warp_depth = warpfield->m_warp_x->m_count;
    const cl_double warp_scale = warpfield->m_scale;
    int i;
    size_t device_list_size;
    size_t build_log_size;
    const size_t global_work_size[] = { width, height, depth };

    context = clCreateContextFromType(NULL, CL_DEVICE_TYPE_DEFAULT, NULL, NULL, &status);
    if (status != CL_SUCCESS) {
        Bailout("clCreateContextFromType failed");
	}

    status = clGetContextInfo(context, CL_CONTEXT_DEVICES, 0, NULL, &device_list_size);
    if (status != CL_SUCCESS) {
        Bailout("clGetContextInfo failed");
    }
    devices = calloc(1, device_list_size);
    status = clGetContextInfo(context, CL_CONTEXT_DEVICES, device_list_size, devices, NULL);
    if (status != CL_SUCCESS) {
        Bailout("clGetContextInfo failed");
    }

    for (i = 0; i < (int)(device_list_size / sizeof (cl_device_id)); i++) {
        size_t name_size;
        char* name;
        size_t image_support_size = sizeof(cl_bool);
        cl_bool image_support;


        clGetDeviceInfo(devices[i], CL_DEVICE_NAME, 0, NULL, &name_size);
        name = calloc(1, name_size);
        clGetDeviceInfo(devices[i], CL_DEVICE_NAME, name_size, name, NULL);

        clGetDeviceInfo(devices[i], CL_DEVICE_IMAGE_SUPPORT, image_support_size, &image_support, NULL);

        printf("Name: %s\n", name);
        printf("Image support: %i\n", (int)(image_support == CL_TRUE));
    }

    command_queue = clCreateCommandQueue(context, devices[0], CL_QUEUE_OUT_OF_ORDER_EXEC_MODE_ENABLE, &status);
    if (status != CL_SUCCESS) {
        Bailout("clCreateCommandQueue failed");
    }

    input_volume_mem = clCreateBuffer(context, CL_MEM_READ_ONLY, width * height * depth * sizeof(cl_short), NULL, &status);
    if (status != CL_SUCCESS) {
        Bailout("clCreateBuffer failed");
    }
    warpfield_x_mem = clCreateBuffer(context, CL_MEM_READ_ONLY, warp_width * warp_height * warp_depth * sizeof(cl_float), NULL, &status);
    if (status != CL_SUCCESS) {
        Bailout("clCreateBuffer failed");
    }
    warpfield_y_mem = clCreateBuffer(context, CL_MEM_READ_ONLY, warp_width * warp_height * warp_depth * sizeof(cl_float), NULL, &status);
    if (status != CL_SUCCESS) {
        Bailout("clCreateBuffer failed");
    }
    warpfield_z_mem = clCreateBuffer(context, CL_MEM_READ_ONLY, warp_width * warp_height * warp_depth * sizeof(cl_float), NULL, &status);
    if (status != CL_SUCCESS) {
        Bailout("clCreateBuffer failed");
    }
    output_volume_mem = clCreateBuffer(context, CL_MEM_WRITE_ONLY, width * height * depth * sizeof(cl_short), NULL, &status);
    if (status != CL_SUCCESS) {
        Bailout("clCreateBuffer failed");
    }

    for (i = 0; i < depth; i++) {
        status = clEnqueueWriteBuffer(
            command_queue,
            input_volume_mem,
            CL_FALSE,
            i * width * height * sizeof(cl_short),
            width * height * sizeof(cl_short),
            (input_volume->m_images + i)->m_data,
            0,
            NULL,
            NULL);
        if (status != CL_SUCCESS) {
            Bailout("clEnqueueWriteBuffer failed");
        }
    }
    for (i = 0; i < warp_depth; i++) {
        status = clEnqueueWriteBuffer(
            command_queue,
            warpfield_x_mem,
            CL_FALSE,
            i * warp_width * warp_height * sizeof(cl_float),
            warp_width * warp_height * sizeof(cl_float),
            (warpfield->m_warp_x->m_images + i)->m_data,
            0,
            NULL,
            NULL);
        if (status != CL_SUCCESS) {
            Bailout("clEnqueueWriteBuffer failed");
        }
        status = clEnqueueWriteBuffer(
            command_queue,
            warpfield_y_mem,
            CL_FALSE,
            i * warp_width * warp_height * sizeof(cl_float),
            warp_width * warp_height * sizeof(cl_float),
            (warpfield->m_warp_y->m_images + i)->m_data,
            0,
            NULL,
            NULL);
        if (status != CL_SUCCESS) {
            Bailout("clEnqueueWriteBuffer failed");
        }
        status = clEnqueueWriteBuffer(
            command_queue,
            warpfield_z_mem,
            CL_FALSE,
            i * warp_width * warp_height * sizeof(cl_float),
            warp_width * warp_height * sizeof(cl_float),
            (warpfield->m_warp_z->m_images + i)->m_data,
            0,
            NULL,
            NULL);
        if (status != CL_SUCCESS) {
            Bailout("clEnqueueWriteBuffer failed");
        }
    }

    status = clEnqueueBarrier(command_queue);
    if (status != CL_SUCCESS) {
        Bailout("clEnqueueBarrier failed");
    }

    program = clCreateProgramWithSource(
        context,
        1,
        program_sources,
        NULL,
        &status);
    if (status != CL_SUCCESS) {
        Bailout("clCreateProgramWithSource failed");
    }

    status = clBuildProgram(program, 0, NULL, "-w", NULL, NULL);
    if (status != CL_SUCCESS) {
        BailoutWithOpenClStatus("clBuildProgram failed", status);
    }

    status = clGetProgramBuildInfo(
        program,
        devices[0],
        CL_PROGRAM_BUILD_LOG,
        0,
        NULL,
        &build_log_size);
    if (status != CL_SUCCESS) {
        BailoutWithOpenClStatus("clGetProgramBuildInfo failed", status);
    }
    build_log = calloc(1, build_log_size);
    status = clGetProgramBuildInfo(
        program,
        devices[0],
        CL_PROGRAM_BUILD_LOG,
        build_log_size,
        build_log,
        NULL);
    if (status != CL_SUCCESS) {
        BailoutWithOpenClStatus("clGetProgramBuildInfo failed", status);
    }
    printf("%s\n", build_log);

    kernel = clCreateKernel(
        program,
        "warp",
        &status);
    if (status != CL_SUCCESS) {
        BailoutWithOpenClStatus("clCreateKernel failed", status);
    }

    clSetKernelArg(kernel, 0, sizeof(cl_int), &width);
    if (status != CL_SUCCESS) { Bailout("clSetKernelArg failed"); }
    clSetKernelArg(kernel, 1, sizeof(cl_int), &height);
    if (status != CL_SUCCESS) { Bailout("clSetKernelArg failed"); }
    clSetKernelArg(kernel, 2, sizeof(cl_int), &depth);
    if (status != CL_SUCCESS) { Bailout("clSetKernelArg failed"); }
    clSetKernelArg(kernel, 3, sizeof(cl_int), &warp_width);
    if (status != CL_SUCCESS) { Bailout("clSetKernelArg failed"); }
    clSetKernelArg(kernel, 4, sizeof(cl_int), &warp_height);
    if (status != CL_SUCCESS) { Bailout("clSetKernelArg failed"); }
    clSetKernelArg(kernel, 5, sizeof(cl_int), &warp_depth);
    if (status != CL_SUCCESS) { Bailout("clSetKernelArg failed"); }
    clSetKernelArg(kernel, 6, sizeof(cl_double), &warp_scale);
    if (status != CL_SUCCESS) { Bailout("clSetKernelArg failed"); }
    clSetKernelArg(kernel, 7, sizeof(cl_mem), &input_volume_mem);
    if (status != CL_SUCCESS) { Bailout("clSetKernelArg failed"); }
    clSetKernelArg(kernel, 8, sizeof(cl_mem), &warpfield_x_mem);
    if (status != CL_SUCCESS) { Bailout("clSetKernelArg failed"); }
    clSetKernelArg(kernel, 9, sizeof(cl_mem), &warpfield_y_mem);
    if (status != CL_SUCCESS) { Bailout("clSetKernelArg failed"); }
    clSetKernelArg(kernel, 10, sizeof(cl_mem), &warpfield_z_mem);
    if (status != CL_SUCCESS) { Bailout("clSetKernelArg failed"); }
    clSetKernelArg(kernel, 11, sizeof(cl_mem), &output_volume_mem);
    if (status != CL_SUCCESS) { Bailout("clSetKernelArg failed"); }

    status = clEnqueueNDRangeKernel(
        command_queue,
        kernel,
        3,
        NULL,
        global_work_size,
        NULL,
        0,
        NULL,
        NULL);
	if (status != CL_SUCCESS) {
	    Bailout("clEnqueueNDRangeKernel failed");
	}

    status = clEnqueueBarrier(command_queue);
    if (status != CL_SUCCESS) {
        Bailout("clEnqueueBarrier failed");
    }

    for (i = 0; i < depth; i++) {
        status = clEnqueueReadBuffer(
            command_queue,
            output_volume_mem,
            CL_FALSE,
            i * width * height * sizeof(cl_short),
            width * height * sizeof(cl_short),
            (output_volume->m_images + i)->m_data,
            0,
            NULL,
            NULL);
        if (status != CL_SUCCESS) {
            Bailout("clEnqueueReadBuffer failed");
        }
    }

    status = clFinish(command_queue);
	if (status != CL_SUCCESS) {
	    Bailout("clFinish failed");
	}

    status = clReleaseContext(context);
    if (status != CL_SUCCESS) {
        BailoutWithOpenClStatus("clReleaseContext failed", status);
    }
}

static void Benchmark(
    const char* const name,
    const ShortVolume* const input_volume,
    const Warpfield* const warpfield,
    const ShortVolume* const output_volume,
    const ShortVolume* const expected_volume,
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

    const double rmsError = MeasureRmsError(output_volume, expected_volume, 10);
    printf("%s: %f rms error.\n", name, rmsError);

    secondsPerIteration = ((double)(end - begin)) / CLOCKS_PER_SEC / iterations;
    printf("%s: %f seconds.\n", name, secondsPerIteration);
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

    Benchmark("Vanilla", &input_volume, &warpfield, &output_volume, &expected_volume, WarpVanilla);
    Benchmark("OpenCL", &input_volume, &warpfield, &output_volume, &expected_volume, WarpOpenCL);

    return 0;
}

