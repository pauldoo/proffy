#pragma once

#ifdef DLL_INTERFACE
#error "DLL_INTERFACE already defined!"
#endif

#ifdef FOO_EXPORTS
#define DLL_INTERFACE __declspec( dllexport )
#else
#define DLL_INTERFACE __declspec( dllimport )
#endif
