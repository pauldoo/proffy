#include "Externals.h"



int main(void)
{
	HRESULT result = S_OK;
	result = ::CoInitialize(0);
	assert(result == S_OK);

	IDebugClient* client = NULL;
	result = ::DebugCreate(
		__uuidof(IDebugClient),
		reinterpret_cast<void**>(&client));
	assert(result == S_OK);

	ULONG processId = 0;
	result = client->GetRunningProcessSystemIdByExecutableName(
		0,
		"SampleTarget.exe",
		DEBUG_GET_PROC_ONLY_MATCH,
		&processId);
	assert(result == S_OK);

	std::cout << "Process ID: " << processId << "\n";

	result = client->AttachProcess(
		0,
		processId,
		DEBUG_ATTACH_NONINVASIVE);
	assert(result == S_OK);

	return EXIT_SUCCESS;
}
