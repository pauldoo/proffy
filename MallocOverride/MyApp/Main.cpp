#include "../MyLib/Foobar.h"

#include <iostream>
#include <Windows.h>

extern "C" {
	void* malloc(size_t size)
	{
		printf("%s\n", __FUNCTION__);
		return LocalAlloc(LMEM_FIXED, size);
	}

	void free(void *ptr)
	{
		printf("%s\n", __FUNCTION__);
		LocalFree(ptr);
	}

	void* realloc(void *ptr, size_t size)
	{
		printf("%s\n", __FUNCTION__);
		return LocalReAlloc(ptr, size, 0);
	}
}

int main(void)
{
	std::cout << "Started\n";
	MyLib::Foobar();
	std::cout << "Finished\n";
	return 0;
}
