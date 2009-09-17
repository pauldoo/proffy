#ifndef WARPVANILLA2_H
#define WARPVANILLA2_H

#include "common.h"

void WarpVanilla2(
    const ShortVolume* const input_volume,
    const Warpfield* const warpfield,
    const ShortVolume* const output_volume,
    const int iterations);

#endif
