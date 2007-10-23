#include "stdafx.h"
#include "Utilities.h"
#include "TimingPool.h"
#include "Exception.h"
#include "Auto.h"
#include "Maybe.h"
#include <algorithm>
#include <set>

TimingPool::PopOnDeletion::~PopOnDeletion() {
	TimingPool::Pop();
}

namespace {
	class TimerTree : public LinkCount {
	public:
		explicit TimerTree(TimerTree* parent00) 
		:	m_count(0), 
			m_parent00(parent00)
		{
		}

		std::vector<Maybe<Auto<LinkCount> > > m_children;
		TimerTree* m_parent00;
		Timer m_timer;
		int m_count;
	};
}

std::vector<std::string> TimingPool::m_names;
Auto<LinkCount> TimingPool::m_timerStack = new TimerTree(NULL);
LinkCount* TimingPool::m_currentTimerStack = TimingPool::m_timerStack.Pointer();

namespace {
	// Returns a summary string and the estimated actual time
	std::pair<std::string, double> TimingSummary(
		const std::string& indent, 
		const TimerTree* timerTree, 
		const std::vector<std::string>& timerNames,
		const double averageTimerTime)
	{
		std::string result;
		double totaltimerTime = 0;
		for (unsigned int i = 0 ; i < timerTree->m_children.size() ; i++)
		{
			if (timerTree->m_children[i].IsValid()) {
				const TimerTree* timerChild = static_cast<TimerTree*>(timerTree->m_children[i].Get().Pointer());
				std::pair<std::string, double> timingSummary = 
					TimingSummary(indent + "\t", timerChild, timerNames, averageTimerTime);
				const double timerTime = timingSummary.second + averageTimerTime * timerChild->m_count;
				result += indent + timerNames[i] + ": " + ToString(std::max(timerChild->m_timer.CurrentTime() - timerTime, 0.0)) + "\n";
				result += timingSummary.first;
				totaltimerTime += timerTime;
			}
		}
		return std::make_pair(result, totaltimerTime);
	}
}

std::string TimingPool::TimingSummary() {
	const Timer internalTimer = Timer().Start();
	const int timingIterations = 1000000;
	for (int i = 0 ; i < timingIterations ; i++)
	{
		TIMETHISBLOCK("InternalTiming");
	}
	const double averageTimerTime = internalTimer.CurrentTime()/timingIterations;
	return ::TimingSummary("", static_cast<TimerTree*>(m_timerStack.Pointer()), m_names, averageTimerTime).first;
}



void TimingPool::ClearAllTimers()
{
	// TODO: add test (would be nicer if this was a proper singleton)
	/*m_timerStack = new TimerTree(NULL);
	m_numberOfLookups = 0;
	m_names.clear();
	m_currentTimerStack = m_timerStack.Pointer();*/
}

namespace {
	std::string ReplaceAnonymousNamespace(const std::string& str) {
		const std::string& removeThis = "`anonymous-namespace'::";
		if (std::search(str.begin(), str.end(), removeThis.begin(), removeThis.end()) != str.end()) {
			return std::string(str.begin() + removeThis.size(), str.end());
		}
		return str;
	}
}

int TimingPool::GiveIDFor(const std::string& name)
{	
	m_names.push_back(ReplaceAnonymousNamespace(name));
	return int(m_names.size() - 1);
}

void TimingPool::Push(const int id)
{
	if (static_cast<unsigned int>(id) >= m_names.size())
		throw Exception("Timer id out of range");
	
	TimerTree* timerTree = static_cast<TimerTree*>(m_currentTimerStack);
	if (timerTree->m_children.size() < size_t(id) + 1)
		timerTree->m_children.resize(id + 1);
	if (!timerTree->m_children[id].IsValid())
		timerTree->m_children[id] = new TimerTree(timerTree);
	m_currentTimerStack = timerTree = static_cast<TimerTree*>(timerTree->m_children[id].Get().Pointer());

	timerTree->m_timer.Start();
	timerTree->m_count++;
}

void TimingPool::Pop()
{
	TimerTree* timerTree = static_cast<TimerTree*>(m_currentTimerStack);
	m_currentTimerStack = timerTree->m_parent00;
	timerTree->m_timer.Stop();
}
