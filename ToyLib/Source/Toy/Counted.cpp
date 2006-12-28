#include "Toy/PrecompiledDeclarations.h"
#include "Toy/Counted.h"

#include "Toy/AssertException.h"

namespace Toy {
    Counted::Counted() : m_count(0)
    {
    }
    
    Counted::~Counted()
    {
        TOY_DEBUG_ASSERT(m_count == 0, "Reference count should be zero");
    }
    
    void Counted::AddReference() const
    {
        ++m_count;
    }
    
    void Counted::DeleteReference() const
    {
        if (--m_count == 0) {
            delete this;
        }
    }
    
    const std::string Counted::ClassName() const
    {
        return typeid(*this).name();
    }
    
    void intrusive_ptr_add_ref(const Counted* const object)
    {
        object->AddReference();
    }
    
    void intrusive_ptr_release(const Counted* const object)
    {
        object->DeleteReference();
    }
}

