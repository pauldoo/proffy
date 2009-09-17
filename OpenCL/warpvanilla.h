#ifndef WARPVANILLA_H
#define WARPVANILLA_H

#include "common.h"

void WarpVanilla(
    const ShortVolume* const input_volume,
    const Warpfield* const warpfield,
    const ShortVolume* const output_volume,
    const int iterations);

#endif
