#include "External.h"
#include "RollingChecksum.h"

#include "Types.h"

class RollingChecksum
{
    public:
        RollingChecksum();
	~RollingChecksum();

	void nextByte(const Byte value);
};

