#include "common.h"

#include <stdio.h>
#include <stdlib.h>

void Bailout(const char* const message)
{
    printf("ERROR: %s\n", message);
    exit(EXIT_FAILURE);
}
