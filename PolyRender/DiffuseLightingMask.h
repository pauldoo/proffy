#pragma once

#include "ColorDeclarations.h"
#include "AutoDeclarations.h"
class RenderInfo;
class Light;

Color DiffuseLightingMask(const Auto<const RenderInfo>& info, const Auto<const Light>& light);
