#include "common.h"

#include <stdio.h>
#include <stdlib.h>

void Bailout(const char* const message)
{
    printf("ERROR: %s\n", message);
    exit(EXIT_FAILURE);
}

void BailoutWithOpenClStatus(
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
    case CL_INVALID_DEVICE:
        printf("CL_INVALID_DEVICE\n");
        break;
    case CL_INVALID_QUEUE_PROPERTIES:
        printf("CL_INVALID_QUEUE_PROPERTIES\n");
        break;
    case CL_INVALID_KERNEL_ARGS:
        printf("CL_INVALID_KERNEL_ARGS\n");
        break;
    case CL_INVALID_WORK_DIMENSION:
        printf("CL_INVALID_WORK_DIMENSION\n");
        break;
    /*
    case CL_INVALID_GLOBAL_WORK_SIZE:
        printf("CL_INVALID_GLOBAL_WORK_SIZE\n");
        break;
    */
    case CL_INVALID_WORK_GROUP_SIZE:
        printf("CL_INVALID_WORK_GROUP_SIZE\n");
        break;
    case CL_INVALID_WORK_ITEM_SIZE:
        printf("CL_INVALID_WORK_ITEM_SIZE\n");
        break;
    case CL_INVALID_GLOBAL_OFFSET:
        printf("CL_INVALID_GLOBAL_OFFSET\n");
        break;
    case CL_INVALID_IMAGE_SIZE:
        printf("CL_INVALID_IMAGE_SIZE\n");
        break;
    case CL_INVALID_IMAGE_FORMAT_DESCRIPTOR:
        printf("CL_INVALID_IMAGE_FORMAT_DESCRIPTOR\n");
        break;
    case CL_INVALID_HOST_PTR:
        printf("CL_INVALID_HOST_PTR\n");
        break;
    case CL_IMAGE_FORMAT_NOT_SUPPORTED:
        printf("CL_IMAGE_FORMAT_NOT_SUPPORTED\n");
        break;
    case CL_INVALID_OPERATION:
        printf("CL_INVALID_OPERATION\n");
        break;
    default:
        printf("Unknown OpenCL error (%i)\n", status);
        break;
    }
    exit(EXIT_FAILURE);
}

const char* ReadFileIntoString(
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
