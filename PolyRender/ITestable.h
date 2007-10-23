#pragma once

#include "Console.h"
#include "Norms.h"
#include "Utilities.h"

class ITestable {
public:
    ITestable() : m_testPasses(true) {}
	void ExecuteTest();
protected:
	virtual std::string Name() const = 0;
	virtual void Execute() = 0;
	bool Assert(const std::string& errorMessage, const bool assertion);

	template <typename Type1, typename Type2> 
	bool AssertNearlyEqual(const std::string& errorMessage, const Type1& A, const Type2& B) {
		return Assert(
			errorMessage + "\nNorm(" + ToString(A) + " - " + ToString(B) + ") > " + ToString(maxAcceptableErrorSquared) + "\n", 
			NearlyZeroQ(A-Type2(B))
		);
	}

	template <typename Type1, typename Type2> 
	bool AssertEqual(const std::string& errorMessage, const Type1& A, const Type2& B) {
		return Assert(errorMessage + "\n" + ToString(A) + " != " + ToString(B) + "\n", A == Type2(B));
	}

	bool TestPassing() const;
private:
	bool m_testPasses;
};

#define THROWCHECK(message, operation) \
	{\
		try {\
			(operation);\
			Assert(std::string("Should have thrown:") + (message), false);\
		} catch (Exception&) {}\
	}