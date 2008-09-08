#pragma once

#include "Config.h"

namespace Foo {
	DLL_INTERFACE void SomeFunction();

	template<typename T> DLL_INTERFACE void SomeTemplateFunction();
}


