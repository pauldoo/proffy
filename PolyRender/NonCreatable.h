#pragma once

class NonCreatable {
	NonCreatable();
	NonCreatable(const NonCreatable&);
	void operator=(const NonCreatable&);
};