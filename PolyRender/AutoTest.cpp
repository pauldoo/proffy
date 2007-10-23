#include "stdafx.h"
#include "AutoTest.h"

#include "Auto.h"
#include "DestructionAsserter.h"

std::string AutoTest::Name() const {
	return "Auto Test";
}

namespace {
	class DestructorParent : public LinkCount {
	public:
		DestructorParent(bool& destroyed) :	m_destructionAsserter(new DestructionAsserter(destroyed)) {}
		Auto<DestructionAsserter> m_destructionAsserter;
	};
}

void AutoTest::Execute() {
	bool destroyed1 = false;
	bool destroyed2 = false;
		
	// Simple case
	{
		Auto<DestructionAsserter> a(new DestructionAsserter(destroyed1));
		Assert("Heap object wasn't deleted: Simplest Case", !destroyed1);
	}
	Assert("Heap object was deleted: Simplest Case", destroyed1);

	// One Auto, multiple pointers case
	{
		Auto<DestructionAsserter> a = new DestructionAsserter(destroyed1);
		a = new DestructionAsserter(destroyed2);
		Assert("Old heap object was deleted : Assignment case", destroyed1);
		Assert("New heap object wasn't deleted : Assignment case", !destroyed2);
	}
	Assert("New heap object was deleted : Assignment case", destroyed2);
	
	// default constructor case
	{
		Auto<LinkCount> a;
		a = new DestructionAsserter(destroyed1);
	}
	Assert("New heap object was deleted : Default constructor case", destroyed1);
	
	// Multiple Auto, multiple pointers, out of order detruction case
	{
		DestructionAsserter* ptr = new DestructionAsserter(destroyed1);
		Auto<DestructionAsserter> a = new DestructionAsserter(destroyed2);
		{
			Auto<DestructionAsserter> b = ptr;
			Assert("First heap object wasn't deleted", !destroyed1);
			a = ptr;
			Assert("First heap object wasn't deleted", !destroyed1);
		}
		Assert("First heap object wasn't deleted", !destroyed1);
	}
	Assert("First heap object was deleted", destroyed1);
	
	
	// Copy constructor testing
	// Multiple Auto, multiple pointers, out of order destruction case
	{
		Auto<DestructionAsserter> a = new DestructionAsserter(destroyed1);
		{
			Auto<const DestructionAsserter> b = a;
		}
		Assert("Copy Constructor Case: First heap object wasn't deleted", !destroyed1);
	}
	Assert("Copy Constructor Case: First heap object was deleted", destroyed1);

	// Auto assignment testing
	// Multiple Auto, multiple pointers, out of order destruction case
	{
		Auto<DestructionAsserter> a = new DestructionAsserter(destroyed1);
		{
			Auto<const DestructionAsserter> b = new DestructionAsserter(destroyed2);
			b = a;
			Assert("Assignment Case: First heap object wasn't deleted", !destroyed1);
		}
		Assert("Assignment Case: First heap object wasn't deleted", !destroyed1);
		Assert("Assignment Case: Second heap object was deleted", destroyed2);
	}
	Assert("Assignment Case: First heap object was deleted", destroyed1);

	// Auto assignment to a pointer.
	{
		Auto<const DestructionAsserter> a = new DestructionAsserter(destroyed1);
		a = new DestructionAsserter(destroyed2);
		Assert("Assignment to Pointer Case: First heap object was deleted", destroyed1);	
	}
	Assert("Assignment Case: First heap object was deleted", destroyed2);

	// Test Self assignment
	{
		DestructionAsserter* ptr = new DestructionAsserter(destroyed1);
		Auto<const DestructionAsserter> a = ptr;
		a = ptr;
		Assert("Heap object wasn't deleted: Self assignment case", !destroyed1);
	}

	// Downcasting of pointer works (compilation check)
	{
		Auto<const DestructionAsserter> a = new DestructionAsserter(destroyed1);
		Auto<const LinkCount> b = new DestructionAsserter(destroyed2);
		b = a;
		
		// pointer access works;
		a->IsDestroyed();
	}
	
	// Putting into a vector case
	{
		std::vector<Auto<DestructionAsserter> > destructorList;
		destructorList.push_back(new DestructionAsserter(destroyed1));
		destructorList.push_back(new DestructionAsserter(destroyed2));
		Assert("Heap object wasn't deleted: vector case", !destroyed1);
		Assert("Heap object wasn't deleted: vector case", !destroyed2);
	}
	Assert("Heap object was deleted: vector case", destroyed1);
	Assert("Heap object was deleted: vector case", destroyed2);	
}

