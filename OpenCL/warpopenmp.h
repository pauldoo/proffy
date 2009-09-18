#ifndef WARPOPENMP_H
#define WARPOPENMP_H

#include "common.h"

void WarpOpenMP(
    const ShortVolume* const input_volume,
    const Warpfield* const warpfield,
    const ShortVolume* const output_volume,
    const int iterations);

#endif
