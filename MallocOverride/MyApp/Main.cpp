#include "../MyLib/Foobar.h"

#include <iostream>
#include <Windows.h>

int main(void)
{
	std::cout << "Started\n";
	MyLib::Foobar();
	std::cout << "Finished\n";
	return 0;
}
