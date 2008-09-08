#include <iostream>

#include "../Foo/Code.h"

int main(void)
{
	std::cout << "Hello World\n";
	Foo::SomeFunction();
	Foo::SomeTemplateFunction<int>();
	Foo::SomeTemplateFunction<double>();
	return EXIT_SUCCESS;
}
