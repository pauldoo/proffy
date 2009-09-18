#ifndef WARPVANILLAF_H
#define WARPVANILLAF_H

#include "common.h"

void WarpVanillaF(
    const ShortVolume* const input_volume,
    const Warpfield* const warpfield,
    const ShortVolume* const output_volume,
    const int iterations);

#endif
