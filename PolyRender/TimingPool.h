#pragma once 

#include "Timer.h"
#include "AutoDeclarations.h"
#include "MaybeDeclarations.h"
#include <map>
#include <vector>

class LinkCount;

class TimingPool {
public:
	static std::string TimingSummary();
	static void ClearAllTimers();

	static int GiveIDFor(const std::string& name);
	static void Push(const int id);
	static void Pop();

	class PopOnDeletion {
	public:
		~PopOnDeletion();
	};

private:

	static std::vector<std::string> m_names;
	static Auto<LinkCount> m_timerStack;
	static LinkCount* m_currentTimerStack;
};

#define TIMINGON
#ifdef TIMINGON
#define TIMETHISBLOCK(msg) \
	static int timerId = TimingPool::GiveIDFor(msg); \
	TimingPool::Push(timerId); \
	const TimingPool::PopOnDeletion popOnDeletion();

#define TIMETHISFUNCTION TIMETHISBLOCK(__FUNCTION__)
#else 
#define TIMETHISBLOCK(msg) ;
#define TIMETHISFUNCTION ;
#endif


