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

namespace {
	const std::string lines = std::string(50, '-') + "\n";
}

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

class DebugEventCallbacks : public DebugBaseEventCallbacks
{
public:
	virtual ~DebugEventCallbacks()
	{
	}

	virtual ULONG __stdcall AddRef(void)
	{
		return 1;
	}

	virtual ULONG __stdcall Release(void)
	{
		return 1;
	}

	virtual HRESULT __stdcall GetInterestMask(ULONG* mask)
	{
		*mask =
			DEBUG_EVENT_BREAKPOINT |
			DEBUG_EVENT_EXCEPTION |
			DEBUG_EVENT_CREATE_THREAD |
			DEBUG_EVENT_EXIT_THREAD |
			DEBUG_EVENT_CREATE_PROCESS |
			DEBUG_EVENT_EXIT_PROCESS |
			DEBUG_EVENT_LOAD_MODULE |
			DEBUG_EVENT_UNLOAD_MODULE |
			DEBUG_EVENT_SYSTEM_ERROR |
			DEBUG_EVENT_SESSION_STATUS |
			DEBUG_EVENT_CHANGE_DEBUGGEE_STATE |
			DEBUG_EVENT_CHANGE_ENGINE_STATE |
			DEBUG_EVENT_CHANGE_SYMBOL_STATE;
		return S_OK;
	}

	virtual HRESULT __stdcall ChangeEngineState(
		ULONG flags,
		ULONG64 /*argument*/)
	{
		//if (flags & DEBUG_CES_CURRENT_THREAD) {
		//	std::cout << __FUNCTION__ << ": The current thread has changed, which implies that the current target and current process might also have changed.\n";
		//}
		if (flags & DEBUG_CES_EFFECTIVE_PROCESSOR) {
			std::cout << __FUNCTION__ << ": The effective processor has changed.\n";
		}
		if (flags & DEBUG_CES_BREAKPOINTS) {
			std::cout << __FUNCTION__ << ": One or more breakpoints have changed.\n";
		}
		if (flags & DEBUG_CES_CODE_LEVEL) {
			std::cout << __FUNCTION__ << ": The code interpretation level has changed.\n";
		}
		//if (flags & DEBUG_CES_EXECUTION_STATUS) {
		//	std::cout << __FUNCTION__ << ": The execution status has changed.\n";
		//}
		if (flags & DEBUG_CES_ENGINE_OPTIONS) {
			std::cout << __FUNCTION__ << ": The engine options have changed.\n";
		}
		if (flags & DEBUG_CES_LOG_FILE) {
			std::cout << __FUNCTION__ << ": The log file has been opened or closed.\n";
		}
		if (flags & DEBUG_CES_RADIX) {
			std::cout << __FUNCTION__ << ": The default radix has changed.\n";
		}
		if (flags & DEBUG_CES_EVENT_FILTERS) {
			std::cout << __FUNCTION__ << ": The event filters have changed.\n";
		}
		if (flags & DEBUG_CES_PROCESS_OPTIONS) {
			std::cout << __FUNCTION__ << ": The process options for the current process have changed.\n";
		}
		if (flags & DEBUG_CES_EXTENSIONS) {
			std::cout << __FUNCTION__ << ": Extension DLLs have been loaded or unloaded. (For more information, see Loading Debugger Extension DLLs.)\n";
		}
		if (flags & DEBUG_CES_SYSTEMS) {
			std::cout << __FUNCTION__ << ": A target has been added or removed.\n";
		}
		if (flags & DEBUG_CES_ASSEMBLY_OPTIONS) {
			std::cout << __FUNCTION__ << ": The assemble options have changed.\n";
		}
		if (flags & DEBUG_CES_EXPRESSION_SYNTAX) {
			std::cout << __FUNCTION__ << ": The default expression syntax has changed.\n";
		}
		if (flags & DEBUG_CES_TEXT_REPLACEMENTS) {
			std::cout << __FUNCTION__ << ": Text replacements have changed.\n";
		}

		return S_OK;
	}

	virtual HRESULT __stdcall Breakpoint(IDebugBreakpoint* /*breakpoint*/)
	{
		std::cout << __FUNCTION__ << "\n";
		return S_OK;
	}
};

class DebugOutputCallbacks : public IDebugOutputCallbacks
{
public:
	virtual ~DebugOutputCallbacks()
	{
	}

    virtual HRESULT __stdcall QueryInterface(REFIID interfaceId, PVOID* result)
    {
        *result = NULL;

        if (IsEqualIID(interfaceId, __uuidof(IUnknown)) ||
            IsEqualIID(interfaceId, __uuidof(IDebugEventCallbacks))) {
            *result = this;
            AddRef();
            return S_OK;
        } else {
            return E_NOINTERFACE;
        }
    }

	virtual ULONG __stdcall AddRef(void)
	{
		return 1;
	}

	virtual ULONG __stdcall Release(void)
	{
		return 1;
	}

	virtual HRESULT __stdcall Output(ULONG /*mask*/, PCSTR text)
	{
		std::cout << text;
		return S_OK;
	}
};

int main(void)
{
	try {
		STARTUPINFO startupInfo = {0};
		PROCESS_INFORMATION processInformation = {0};
		BOOL processStarted = ::CreateProcess(
			L"../Debug/SampleTarget.exe",
			NULL,
			NULL,
			NULL,
			FALSE,
			CREATE_NEW_CONSOLE,
			NULL,
			NULL,
			&startupInfo,
			&processInformation);
		if (processStarted != TRUE) {
			std::ostringstream message;
			message << ::GetLastError();
			throw ProffyException(message.str());
		}

		HRESULT result = S_OK;
		result = ::CoInitialize(0);
		assert(result == S_OK);

		IDebugClient* debugClient = NULL;
		result = ::DebugCreate(
			__uuidof(IDebugClient),
			reinterpret_cast<void**>(&debugClient));
		ASSERT(result == S_OK);
		ASSERT(debugClient->FlushCallbacks() == S_OK);

		DebugOutputCallbacks debugOutputCallbacks;
		result = debugClient->SetOutputCallbacks(&debugOutputCallbacks);
		ASSERT(result == S_OK);
		ASSERT(debugClient->FlushCallbacks() == S_OK);

		DebugEventCallbacks debugEventCallbacks;
		result = debugClient->SetEventCallbacks(&debugEventCallbacks);
		ASSERT(result == S_OK);
		ASSERT(debugClient->FlushCallbacks() == S_OK);

		IDebugControl* debugControl = NULL;
		result = debugClient->QueryInterface(
			__uuidof(IDebugControl),
			reinterpret_cast<void**>(&debugControl));
		ASSERT(result == S_OK);
		ASSERT(debugClient->FlushCallbacks() == S_OK);

		IDebugSystemObjects* debugSystemObjects = NULL;
		result = debugClient->QueryInterface(
			__uuidof(IDebugSystemObjects),
			reinterpret_cast<void**>(&debugSystemObjects));
		ASSERT(result == S_OK);
		ASSERT(debugClient->FlushCallbacks() == S_OK);

		result = debugClient->AttachProcess(
			0,
			processInformation.dwProcessId,
			0);
		ASSERT(result == S_OK);
		ASSERT(debugClient->FlushCallbacks() == S_OK);

		ULONG debugeeTypeClass;
		ULONG debugeeTypeQualifier;
		result = debugControl->GetDebuggeeType(&debugeeTypeClass, &debugeeTypeQualifier);
		ASSERT(result == S_OK);
		ASSERT(debugClient->FlushCallbacks() == S_OK);

		ASSERT(debugeeTypeClass == DEBUG_CLASS_USER_WINDOWS);
		ASSERT(debugeeTypeQualifier == DEBUG_USER_WINDOWS_PROCESS);
			
		while (true) {
			std::cout << lines << "Waiting..\n" << lines;
			result = debugControl->WaitForEvent(0, 1000);
			ASSERT(result == S_FALSE);
			std::cout << lines << "Done Waiting..\n" << lines;

			ULONG executionStatus;
			result = debugControl->GetExecutionStatus(&executionStatus);
			ASSERT(result == S_OK);
			ASSERT(debugClient->FlushCallbacks() == S_OK);
			std::cout << "ExecutionStatus: " << executionStatus << "\n";

			//ULONG numberThreads;
			//result = debugSystemObjects->GetNumberThreads(&numberThreads);
			//ASSERT(result == S_OK);
			//ASSERT(debugClient->FlushCallbacks() == S_OK);
			//std::cout << "NumberThreads: " << numberThreads << "\n";

			ASSERT(executionStatus == DEBUG_STATUS_BREAK);

            //result = debugControl->OutputStackTrace(
            //    DEBUG_OUTCTL_THIS_CLIENT,
            //    NULL,
            //    10,
            //    DEBUG_STACK_FRAME_NUMBERS);
            //ASSERT(result == S_OK);
            //ASSERT(debugClient->FlushCallbacks() == S_OK);

			std::cout << "Sleeping..\n";
			::Sleep(5000);
			std::cout << "Done sleeping..\n";
		}

		return EXIT_SUCCESS;
	} catch (const std::exception& ex) {
		std::cerr << typeid(ex).name() << ": " << ex.what() << "\n";
	}
	return EXIT_FAILURE;
}
