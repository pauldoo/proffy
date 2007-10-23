#pragma

#include "AutoDeclarations.h"
class Solid;
class BumpMap;

Auto<const Solid> MakeBumpMapAdapter(const Auto<const Solid>&, const Auto<const BumpMap>&);
