#include "Foobar.h"

#include <cassert>
#include <cstdlib>

namespace MyLib
{
	void Foobar()
	{
		void* p = malloc(10);
		assert(p);
		p = realloc(p, 20);
		assert(p);
		free(p);
	}
}
