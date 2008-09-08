#include "Code.h"

#include <iostream>
#include <typeinfo>

namespace Foo {
	void SomeFunction()
	{
		std::cout << __FUNCTION__ << "\n";
		SomeTemplateFunction<short>();
	}

	template<typename T> void SomeTemplateFunction()
	{
		std::cout << __FUNCTION__ << ", " << typeid(T).name() << "\n";
	}

	template DLL_INTERFACE void SomeTemplateFunction<int>();
	template DLL_INTERFACE void SomeTemplateFunction<double>();
	template DLL_INTERFACE void SomeTemplateFunction<float>();
}