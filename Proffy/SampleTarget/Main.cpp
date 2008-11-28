#include <windows.h>

#include <iostream>
#include <cstdlib>

int main(void)
{
	std::cout << "Running dummy target.\n";
	::SetPriorityClass(::GetCurrentProcess(), BELOW_NORMAL_PRIORITY_CLASS);
	while(true) {
		for (int i = 50; i <= 100; i++) {
			void* p = malloc(i);
			free(p);
		}
	}
	return EXIT_SUCCESS;
}