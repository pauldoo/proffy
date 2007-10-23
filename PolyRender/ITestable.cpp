#include "stdafx.h"
#include "ITestable.h"
#include "Exception.h"

bool ITestable::Assert(const std::string &errorMessage, const bool assertion) {
	if (!assertion) PersistentConsole::OutputString(errorMessage + "\n");
	m_testPasses = m_testPasses && assertion;
	return assertion;
}

void ITestable::ExecuteTest() {
	try {
		Execute();
	} catch (const Exception& e) {
		Assert("Exception Throw:\n" + e.Message(), false);
	}
	if (!TestPassing()) {
		PersistentConsole::OutputString(Name() + ": FAILED\n");
		//PersistentConsole::OutputString(TestPassing() ? "Passed!\n" : "");
	}
}

bool ITestable::TestPassing() const {
	return m_testPasses;
}
