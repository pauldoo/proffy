/*
    Copyright (C) 2008  Paul Richards.

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

#include "../Proffy/Launcher.h"

#include <windows.h>

#include <algorithm>
#include <cstdlib>
#include <iostream>
#include <vector>

#pragma warning(disable: 4127) // conditional expression is constant

int main(void)
{
    std::cout << "Running dummy target.\n";

    const int count = 10000;
    std::vector<int> values;
    values.reserve(count);
    for (int i = 0; i < count; i++) {
        values.push_back(i);
    }

    {
        Proffy::Launcher profiler(L"../Debug", L"../test.xml");
        for (int i = 0; i < 10; i++) {
            std::random_shuffle(values.begin(), values.end());
            std::sort(values.begin(), values.end());
            std::cout << ".";
            std::cout.flush();
        }
    }
    return EXIT_SUCCESS;
}