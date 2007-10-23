#include "stdafx.h"
#include "Timer.h"
#include "Exception.h"
#include <ctime>

namespace {
	double TimeInSeconds() {
		return static_cast<double>(clock()) / CLOCKS_PER_SEC;
	}
}

Timer::Timer() 
 :	m_totalTime(0),
	m_running(false)
{
}

double Timer::CurrentTime() const {
	return m_totalTime + (m_running ? TimeInSeconds() : 0);
}

Timer& Timer::Start() {
	if (m_running) throw Exception("Already Running");
	m_totalTime -= TimeInSeconds();
	m_running = true;
	return *this;
}

Timer& Timer::Stop() {
	if (!m_running) throw Exception("Not running");
	m_totalTime += TimeInSeconds();
	m_running = false;
	return *this;
}