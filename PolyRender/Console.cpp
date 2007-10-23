#include "stdafx.h"
#include "Console.h"
#include "GlobalMFC.h"
#include "GlobalWindowSize.h"

#include <iostream>

/////////////////////////////////////////////////////////////////////////////////

#ifdef WIN32
bool Console::s_firstCall = true;
RECT Console::s_rect;

void Console::OutputString(const std::string &output) {
    if (s_firstCall) {
        SetRect((LPRECT)&s_rect, drawing_pane_width + 50, 500, 900, 800);
        s_firstCall = false;
    }
	SetTextColor(hdc,RGB(0,0,255));
	FillRect(hdc, (LPRECT)&s_rect, (HBRUSH)RGB(255,255,255));
	DrawText(hdc, output.c_str(), -1, (LPRECT)&s_rect, DT_CALCRECT);
	DrawText(hdc, output.c_str(), -1, (LPRECT)&s_rect, DT_LEFT | DT_WORDBREAK);
}
#endif
/////////////////////////////////////////////////////////////////////////////////

#ifdef WIN32
bool PersistentConsole::s_firstCall = true;
RECT PersistentConsole::s_rect;
std::string PersistentConsole::s_log;

void PersistentConsole::OutputString(const std::string &output) {
    if (s_firstCall) {
        SetRect((LPRECT)&s_rect,drawing_pane_width + 50, 0, 1000, 800);
        s_firstCall = false;
    }
	s_log += output;
	SetTextColor(hdc,RGB(255,0,0));
	DrawText(hdc, s_log.c_str(), -1, (LPRECT)&s_rect, DT_LEFT | DT_WORDBREAK);
}
#endif
/////////////////////////////////////////////////////////////////////////////////

#ifndef WIN32
void Console::OutputString(const std::string &output) {
    std::cout << output;
}

void PersistentConsole::OutputString(const std::string &output) {
    std::cout << output;
}
#endif

