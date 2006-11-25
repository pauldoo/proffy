#include <iostream>

namespace {
    /**
        Invokable interface.
        
        This is the top level interface which ultimately our arbitary function
        calls should be packaged up as.
        
    */
    class Invokable {
    public:
        virtual ~Invokable()
        {
        }
    
        virtual void Invoke(void) = 0;
    };
    
    /**
        Empty type.
    */
    struct EmptyType { };
    
    /**
        Metafunctions to operate on EmptyType.
    */
    template<typename T>
    struct IsEmptyType {
        static const bool value = false;
    };
    
    template<>
    struct IsEmptyType<EmptyType> {
        static const bool value = true;
    };
    
    /**
        Caller.
        
        Template struct which has an operator() to call the actual function when
        passed all the arguments. This is where we specialise over the number of
        non-empty arguments.
        
        It would be nice if this struct were actually a function with partial
        specialisation, but C++ doesn't allow that.
        
    */
    template<
        bool Par1IsEmpty, bool Par2IsEmpty, bool Par3IsEmpty,
        typename T, typename Par1, typename Par2, typename Par3
    >
    struct Caller {
    };
    
    template<typename T, typename Par1, typename Par2, typename Par3>
    struct Caller<false, false, false, T, Par1, Par2, Par3> {
        void operator() (T& func, const Par1& par1, const Par2& par2, const Par3& par3)
        {
            func(par1, par2, par3);
        }
    };

    template<typename T, typename Par1, typename Par2, typename Par3>
    struct Caller<false, false, true, T, Par1, Par2, Par3> {
        void operator() (T& func, const Par1& par1, const Par2& par2, const Par3&)
        {
            func(par1, par2);
        }
    };

    template<typename T, typename Par1, typename Par2, typename Par3>
    struct Caller<false, true, true, T, Par1, Par2, Par3> {
        void operator() (T& func, const Par1& par1, const Par2&, const Par3&)
        {
            func(par1);
        }
    };

    template<typename T, typename Par1, typename Par2, typename Par3>
    struct Caller<true, true, true, T, Par1, Par2, Par3> {
        void operator() (T& func, const Par1&, const Par2&, const Par3&)
        {
            func();
        }
    };
    
    /**
        Function template class.
        
        This is the class which finally implements our Invokable interface.
        
    */
    template<
        typename T,
        typename Par1,
        typename Par2,
        typename Par3
    >
    class Function : public Invokable
    {
    public:
        Function(
            T& func,
            const Par1& par1,
            const Par2& par2,
            const Par3& par3
        ) :
            fFunc(func),
            fPar1(par1),
            fPar2(par2),
            fPar3(par3)
        {
        }
    
        void Invoke(void)
        {
            Caller<
                IsEmptyType<Par1>::value,
                IsEmptyType<Par2>::value,
                IsEmptyType<Par3>::value,
                T, Par1, Par2, Par3
            >()(fFunc, fPar1, fPar2, fPar3);
        }
        
    private:
        T& fFunc;
        const Par1& fPar1;
        const Par2& fPar2;
        const Par3& fPar3;
    };

    /**
        Single Threading concrete class.
    */
    class SingleThreader {
    public:
        template<
            typename T,
            typename Par1 = EmptyType,
            typename Par2 = EmptyType,
            typename Par3 = EmptyType>
        void Invoke(
            T& function,
            const Par1& par1 = EmptyType(),
            const Par2& par2 = EmptyType(),
            const Par3& par3 = EmptyType())
        {
            Function<T, Par1, Par2, Par3> call(function, par1, par2, par3);
            RealInvoke(&call);
        }
        
    private:
        void RealInvoke(Invokable* const i)
        {
            i->Invoke();
        }
    };
    
    /**
        Test functions.
    */
    void FuncA(void)
    {
        std::cout << __FUNCTION__ << "\n";
    }
    
    void FuncB(const int k)
    {
        std::cout << __FUNCTION__ << "(" << k << ")\n";
    }
    
    void FuncC(const std::string& h, const int k)
    {
        std::cout << __FUNCTION__ << "(" << h << ", " << k << ")\n";
    }
}

/**
    Main.
*/
int main(void)
{
    SingleThreader st;
    st.Invoke(FuncA);
    //st.Invoke(FuncB, 10);
    //st.Invoke(FuncC, "Foobar", 20);
    return 0;
}
