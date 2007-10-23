#pragma once
#include "NonCreatable.h"
///////////////////////////////////////////////////////////////////

class Console : public NonCreatable {
public:
	static void OutputString(const std::string &output);
private:
#ifdef WIN32
	static bool s_firstCall;
	static RECT s_rect;
#endif
};

///////////////////////////////////////////////////////////////////

class PersistentConsole : public NonCreatable {
public:
	static void OutputString(const std::string &output);
private:
#ifdef WIN32
	static bool s_firstCall;
	static RECT s_rect;
	static std::string s_log;
#endif
};

///////////////////////////////////////////////////////////////////
