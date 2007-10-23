#pragma once

class Timer {
public:
	Timer();
	
	Timer& Start();
	Timer& Stop();

	double CurrentTime() const;
private:
	double m_totalTime;
	bool m_running;
};