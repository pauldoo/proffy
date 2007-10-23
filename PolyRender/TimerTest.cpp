#include "stdafx.h"
#include "TimerTest.h"

#include "TimingPool.h"
#include "Exception.h"
#include "Auto.h"

std::string TimerTest::Name() const {
	return "Timer Test";
}

namespace {
	void Wait(const double seconds) {
		const Timer timer = Timer().Start();
		while(timer.CurrentTime() < seconds);
	}
}

void TimerTest::Execute() {

	Assert("Initial Time should be zero", Timer().CurrentTime() == 0);
	THROWCHECK("Stopping a timer that hasn't been started", Timer().Stop());
	THROWCHECK("Starting a timer that has already been started", Timer().Start().Start());
	
	Timer timer = Timer().Start();
	Wait(.1);
	Assert("Timer should now be greater than ,1", timer.CurrentTime() >= .1);
	timer.Stop();
	Assert("Timer should still be greater than ,1", timer.CurrentTime() >= .1);
	timer.Start();
	Wait(.1);
	Assert("Timer should now be greater than ,1", timer.CurrentTime() >= .2);
	timer.Stop();
	Assert("Timer should still be greater than ,1", timer.CurrentTime() >= .2);

	{
		TIMETHISBLOCK("1");
		{
			TIMETHISBLOCK("2");
		}
		{
			TIMETHISBLOCK("3");
		}
	}
	// TODO check output of this
	TimingPool::TimingSummary();
}


