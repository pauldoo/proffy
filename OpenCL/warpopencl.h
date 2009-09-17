#ifndef WARPOPENCL_H
#define WARPOPENCL_H

#include "common.h"

void WarpOpenCL(
    const ShortVolume* const input_volume,
    const Warpfield* const warpfield,
    const ShortVolume* const output_volume,
    const int iterations);

#endif
