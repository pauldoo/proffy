#pragma once

#include "AutoDeclarations.h"
class Solid;
class RenderMask;


Auto<const Solid> MakeApplyTexture(const Auto<const Solid>& solid, const Auto<const RenderMask>& renderMask);