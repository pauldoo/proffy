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
#pragma once

namespace Proffy {
    /**
        Samples during the profile run are accumulated into an instance of this
        class.
    */
    class Results
    {
    public:
        Results();

        ~Results();

        void AccumulateSample(
            /**
                Path to source file.
            */
            const std::string& filename,
            /**
                Line number within source file.
            */
            const int lineNumber,
            /**
                Set to true iff this sample is at the end of the backtrace
                (ie is actually executing and not simply waiting on a call
                to return).
            */
            const bool isTerminalOnTrace);

    private:
        std::map<std::string, std::map<int, std::pair<int, int> > > fHits;
    };
}
