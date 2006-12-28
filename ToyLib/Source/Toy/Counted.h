#pragma once

namespace Toy {
    class Counted
    {
    public:
        Counted();

        const std::string ClassName() const;

    protected:
        virtual ~Counted() = 0;
    
    private:
        friend void intrusive_ptr_add_ref(const Counted* const);
        friend void intrusive_ptr_release(const Counted* const);
        
        Counted(const Counted&);
        
        Counted& operator =(const Counted&);

        void AddReference() const;
        
        void DeleteReference() const;
                
        mutable unsigned int m_count;
    };
    
    void intrusive_ptr_add_ref(const Counted* const);
    void intrusive_ptr_release(const Counted* const);
}


