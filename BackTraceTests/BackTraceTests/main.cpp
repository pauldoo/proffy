#include <iostream>
#include <vector>

#include <windows.h>
#include <winbase.h>
#include <dbghelp.h>

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
	std::cout << "\n\n" << &(SymFromAddr);

	DWORD  error;
	HANDLE hProcess;

	SymSetOptions(SYMOPT_UNDNAME | SYMOPT_DEFERRED_LOADS | SYMOPT_LOAD_LINES);

	hProcess = GetCurrentProcess();
	// hProcess = (HANDLE)processId;

	if (SymInitialize(hProcess, NULL, TRUE)) {
		// SymInitialize returned success
		for (std::vector<const void*>::const_iterator i = stack.begin(); i != stack.end(); ++i) {
			std::cout << *i << "\n";
			DWORD64  dwAddress = reinterpret_cast<DWORD64>(*i);
			DWORD64  dwDisplacement;

			ULONG64 buffer[(sizeof(SYMBOL_INFO) +
				MAX_SYM_NAME*sizeof(TCHAR) +
				sizeof(ULONG64) - 1) /
				sizeof(ULONG64)];
			PSYMBOL_INFO pSymbol = (PSYMBOL_INFO)buffer;

			pSymbol->SizeOfStruct = sizeof(SYMBOL_INFO);
			pSymbol->MaxNameLen = MAX_SYM_NAME;

			if (SymFromAddr(hProcess, dwAddress, &dwDisplacement, pSymbol)) {
				// SymFromAddr returned success
				std::cout << pSymbol->Name << "\n";
				//DWORD64  dwAddress;
				DWORD  dwDisplacement;
				IMAGEHLP_LINE64 line;

				SymSetOptions(SYMOPT_LOAD_LINES);

				line.SizeOfStruct = sizeof(IMAGEHLP_LINE64);

				if (SymGetLineFromAddr64(hProcess, dwAddress, &dwDisplacement, &line)) {
					// SymGetLineFromAddr64 returned success
					std::cout << line.FileName << ":" << line.LineNumber << "," << dwDisplacement << "\n";
				} else {
					// SymGetLineFromAddr64 failed
					error = GetLastError();
					printf("SymGetLineFromAddr64 returned error : %d\n", error);
				}
			} else {
				// SymFromAddr failed
				error = GetLastError();
				printf("SymFromAddr returned error : %d\n", error);
			}
		}
	} else {
		// SymInitialize failed
		error = GetLastError();
		printf("SymInitialize returned error : %d\n", error);
	}

	return 0;
}
