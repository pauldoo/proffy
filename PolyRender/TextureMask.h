#pragma once

#include "ColorDeclarations.h"
#include "AutoDeclarations.h"

class RenderInfo;
class TextureMap;
class Light;

Color TextureMask(const Auto<const RenderInfo>& info, const Auto<const TextureMap>& textureMap);