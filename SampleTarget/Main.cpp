/*
    Copyright (c) 2008, 2009, 2010, 2012 Paul Richards <paul.richards@gmail.com>

    Permission to use, copy, modify, and distribute this software for any
    purpose with or without fee is hereby granted, provided that the above
    copyright notice and this permission notice appear in all copies.

    THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
    WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
    MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR
    ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
    WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
    ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF
    OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
*/

#include "../Proffy/Launcher.h"

#include <windows.h>

#include <algorithm>
#include <cstdlib>
#include <ctime>
#include <iostream>
#include <process.h>
#include <vector>

#pragma warning(disable: 4127) // conditional expression is constant

namespace {
    void SomeFunction(
        std::vector<int>* const values,
        int)
    {
        std::random_shuffle(values->begin(), values->end());
    }

    void SomeFunction(
        std::vector<int>* const values,
        float)
    {
        std::sort(values->begin(), values->end());
    }

    void ThreadFunction(void*)
    {
        while (true) {
            void* const pointer = calloc(20, 1000);
            free(pointer);
        }
    }
};

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
        std::wostringstream pathToProffy;
        pathToProffy << L"../dist/bin" << (sizeof(void*)*8) << L"/Proffy" << (sizeof(void*)*8) << L".exe";
        Proffy::Launcher profiler(pathToProffy.str(), L"../dist", 1.0 / 20);
        const clock_t begin = clock();
        _beginthread(ThreadFunction, 0, NULL);
        while ((clock() - begin) / CLOCKS_PER_SEC < 3) {
            SomeFunction(&values, 3);
            SomeFunction(&values, 3.14f);
        }
    }
    std::cout << "\nDone.\n";

    return EXIT_SUCCESS;
}
