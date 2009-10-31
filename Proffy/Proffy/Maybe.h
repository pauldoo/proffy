/*
    Copyright (C) 2009  Paul Richards.

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

#include "Assert.h"

namespace Proffy {
    template<typename T> class Maybe
    {
    public:
        Maybe()
        {
        }
        
        Maybe(const Maybe& other) :
            fValue(other.Ok() ? (new T(other.Get())) : (NULL))
        {
        }
        
        explicit Maybe(const T& val) :
            fValue(new T(val))
        {
        }
        
        const bool Ok() const
        {
            return fValue.get() != NULL;
        }
        
        const T& Get() const
        {
            ASSERT(Ok());
            return *(fValue.get());
        }
        
        ~Maybe()
        {
        }
        
    private:
        const std::auto_ptr<T> fValue;
    };
}

