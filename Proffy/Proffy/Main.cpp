#include "stdafx.h"
#include "Externals.h"

class ProffyException : public std::exception
{
public:
	ProffyException(const std::string& reason) :
		fReason(reason)
	{
	}

	/// std::exception
	virtual const char* what () const throw ()
	{
		return fReason.c_str();
	}

private:
	const std::string fReason;
};

void Assert(
	const bool v,
	const std::string& function,
	const std::string& file,
	const int lineNumber)
{
	if (v == false) {
		std::ostringstream message;
		message << "Assertion failure: " << function << " (" << file << ", line " << lineNumber << ")";
		throw ProffyException(message.str());
	}
}

#define ASSERT(x) Assert((x), __FUNCTION__, __FILE__, __LINE__);

int main(void)
{
	try {
		HRESULT result = S_OK;
		result = ::CoInitialize(0);
		assert(result == S_OK);

		IDebugClient* debugClient = NULL;
		result = ::DebugCreate(
			__uuidof(IDebugClient),
			reinterpret_cast<void**>(&debugClient));
		ASSERT(result == S_OK);

		ULONG processId = 0;
		result = debugClient->GetRunningProcessSystemIdByExecutableName(
			0,
			"SampleTarget.exe",
			DEBUG_GET_PROC_ONLY_MATCH,
			&processId);
		ASSERT(result == S_OK);

		std::cout << "Process ID: " << processId << "\n";

		result = debugClient->AttachProcess(
			0,
			processId,
			DEBUG_ATTACH_NONINVASIVE);
		ASSERT(result == S_OK);

		IDebugControl* debugControl = NULL;
		result = debugClient->QueryInterface(
			__uuidof(IDebugControl),
			reinterpret_cast<void**>(&debugControl));
		ASSERT(result == S_OK);

		ULONG executionStatus;
		result = debugControl->GetExecutionStatus(&executionStatus);
		ASSERT(result == S_OK);
		
		std::cout << "ExecutionStatus: " << executionStatus << "\n";
		ASSERT(executionStatus == DEBUG_STATUS_GO);

		return EXIT_SUCCESS;
	} catch (const std::exception& ex) {
		std::cerr << typeid(ex).name() << ": " << ex.what() << "\n";
	}
	return EXIT_FAILURE;
}
