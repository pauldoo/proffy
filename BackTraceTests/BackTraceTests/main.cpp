#include <iostream>
#include <vector>

#include <windows.h>
#include <winbase.h>

const std::vector<const void*> Stack()
{
	std::vector<void*> stack(60);
	const int size = ::CaptureStackBackTrace(0, stack.size(), &(stack.front()), NULL);
	stack.resize(size);
	return std::vector<const void*>(stack.begin(), stack.end());
}

int main(void)
{
	const std::vector<const void*> stack = Stack();
	std::copy(stack.begin(), stack.end(), std::ostream_iterator<const void*>(std::cout, "\n"));
	return 0;
}
