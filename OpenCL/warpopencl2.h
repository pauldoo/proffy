#ifndef WARPOPENCL2_H
#define WARPOPENCL2_H

#include "common.h"

void WarpOpenCL2(
    const ShortVolume* const input_volume,
    const Warpfield* const warpfield,
    const ShortVolume* const output_volume,
    const int iterations);

#endif
