#pragma once
// stdafx.h : include file for standard system include files,
// or project specific include files that are used frequently, but
// are changed infrequently
//

#pragma once


#define WIN32_LEAN_AND_MEAN		// Exclude rarely-used stuff from Windows headers
// C RunTime Header Files
#include <cstdlib>

// TODO: reference additional headers your program requires here
#include <cmath>
#include <vector>
#include <string>
#include <limits>

#ifndef WIN32
// Simple hack to get stuff compiling
#define HDC int
#define HWND int
#define RECT int
#define COLORREF int
#endif


#pragma warning(disable:4512)
#pragma warning(disable:4127)

#undef min
#undef max
