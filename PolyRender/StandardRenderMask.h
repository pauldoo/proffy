#pragma once

#include "AutoDeclarations.h"
class RenderMask;
class TextureMap;

Auto<const RenderMask> MakeStandardRenderMask(const Auto<const TextureMap>& textureMap);