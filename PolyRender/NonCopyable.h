#pragma once

class NonCopyable {
	NonCopyable(const NonCopyable&);
	void operator=(const NonCopyable&);
};