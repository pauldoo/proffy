/*
    Copyright (C) 2008  Paul Richards.

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
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
