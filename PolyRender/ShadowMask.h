#pragma once
#include "AutoDeclarations.h"
class RenderInfo;
class Light;
class RenderMemory;

bool InShadow(const Auto<const RenderInfo>& info, const Auto<const Light>& light, RenderMemory& renderMemory);