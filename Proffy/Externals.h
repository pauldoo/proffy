/*
    Copyright (C) 2008, 2009  Paul Richards.

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
#pragma once

/*
    I'm placing all includes of files external to this project here.
*/

// Microsoft includes
#define NOMINMAX
#define STRICT
#include <windows.h>
#include <dbgeng.h>
#pragma warning(push)
#pragma warning(disable: 4005) // macro redefinition
#include <ntstatus.h>
#pragma warning(pop)
#include <winerror.h>

// Xerces-C++ includes
#include <xercesc/dom/DOM.hpp>
#include <xercesc/framework/LocalFileFormatTarget.hpp>
#include <xercesc/util/PlatformUtils.hpp>
#include <xercesc/util/XMLString.hpp>

// Standard includes
#include <cmath>
#include <cstdio>
#include <cstdlib>
#include <ctime>
#include <fstream>
#include <iostream>
#include <list>
#include <map>
#include <memory>
#include <set>
#include <sstream>
#include <string>
#include <vector>

#pragma warning(disable: 4127) // conditional expression is constant
#pragma warning(disable: 4512) // assignment operator could not be generated
