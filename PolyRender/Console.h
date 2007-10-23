#pragma once
#include "NonCreatable.h"
///////////////////////////////////////////////////////////////////

class Console : public NonCreatable {
public:
	static void OutputString(const std::string &output);
private:
	static bool s_firstCall;
	static RECT s_rect;
};

///////////////////////////////////////////////////////////////////

class PersistentConsole : public NonCreatable {
public:
	static void OutputString(const std::string &output);
private:
	static bool s_firstCall;
	static RECT s_rect;
	static std::string s_log;
};

///////////////////////////////////////////////////////////////////