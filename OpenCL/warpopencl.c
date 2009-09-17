#include "warpopencl.h"

#include <stdio.h>
#include <stdlib.h>
#include <CL/cl.h>

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
    case CL_INVALID_COMMAND_QUEUE:
        printf("CL_INVALID_COMMAND_QUEUE\n");
        break;
    case CL_INVALID_CONTEXT:
        printf("CL_INVALID_CONTEXT\n");
        break;
    case CL_INVALID_MEM_OBJECT:
        printf("CL_INVALID_MEM_OBJECT\n");
        break;
    case CL_INVALID_EVENT_WAIT_LIST:
        printf("CL_INVALID_EVENT_WAIT_LIST\n");
        break;
    case CL_MEM_OBJECT_ALLOCATION_FAILURE:
        printf("CL_MEM_OBJECT_ALLOCATION_FAILURE\n");
        break;
    case CL_OUT_OF_RESOURCES:
        printf("CL_OUT_OF_RESOURCES\n");
        break;
    default:
        printf("Unknown OpenCL error (%i)\n", status);
        break;
    }
    exit(EXIT_FAILURE);
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

void WarpOpenCL(
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
    const cl_float warp_scale = (cl_float)(warpfield->m_scale);
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

    status = clBuildProgram(program, 0, NULL, "-w -cl-fast-relaxed-math", NULL, NULL);
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
    clSetKernelArg(kernel, 6, sizeof(cl_float), &warp_scale);
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
            BailoutWithOpenClStatus("clEnqueueReadBuffer failed", status);
        }
    }

    status = clFinish(command_queue);
	if (status != CL_SUCCESS) {
	    BailoutWithOpenClStatus("clFinish failed", status);
	}

    status = clReleaseContext(context);
    if (status != CL_SUCCESS) {
        BailoutWithOpenClStatus("clReleaseContext failed", status);
    }
}

